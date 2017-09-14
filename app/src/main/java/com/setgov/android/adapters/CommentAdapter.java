package com.setgov.android.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.setgov.android.R;
import com.setgov.android.models.Comment;
import com.setgov.android.models.Event;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;
import com.setgov.android.util.MyEditText;
import com.setgov.android.viewholders.CommentViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ross on 9/10/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private static final String TAG = "CommentAdapter";
    private Context mContext;
    private List<Comment> mComments;
    private MyEditText mEditText;
    private User mUser;
    private ApiGraphRequestTask activeApiCall;
    private Handler handler;
    private Runnable deleteCommentToast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(mContext, "Deleted comment", Toast.LENGTH_SHORT).show();
        }
    };
    private int voteRecord;

    public CommentAdapter(User user, MyEditText editText, Context context, ArrayList<Comment> comments){
        this.mUser = user;
        this.mEditText = editText;
        this.mComments = comments;
        this.mContext = context;
        handler = new Handler();
        voteRecord =0;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final Comment comment = mComments.get(position);
        holder.commentText.setText(comment.getText());
        holder.commentUserName.setText(comment.getUser().getName());
        holder.commentVoteScore.setText(""+comment.getKarma());

        for(int i = 0; i < comment.getVotes().size();i++){
            if(comment.getVotes().get(i).getUser() == mUser.getID()){
                holder.setKarma(comment.getVotes().get(i).getVoteValue());
            }
        }
        holder.commentTimePosted.setText(""+comment.getTimestamp());
        Picasso.with(mContext).load(mUser.getProfileImageUrl()).into(holder.commentUserProfile);
        holder.commentUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if user voted on comment already
                //if user already voted cancel the vote
                //if user hasnt voted, kickoffUpvoteComment

                if(holder.karma == -1){
                    kickoffVoteOnComment(comment,1);
                    holder.setKarma(1);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num++;
                    num++;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma == 0) {
                    kickoffVoteOnComment(comment,1);
                    holder.setKarma(1);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num++;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma == 1){
                    kickoffVoteOnComment(comment,0);
                    holder.setKarma(0);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num--;
                    holder.commentVoteScore.setText(""+num);
                }
                //update karma textview
            }
        });
        holder.commentDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.karma  == 1){
                    kickoffVoteOnComment(comment,-1);
                    holder.setKarma(-1);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num--;
                    num--;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma  == 0) {
                    kickoffVoteOnComment(comment,-1);
                    holder.setKarma(-1);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num--;
                    holder.commentVoteScore.setText(""+num);
                } else if(holder.karma  == -1){
                    kickoffVoteOnComment(comment,0);
                    holder.setKarma(0);
                    String currentNum = holder.commentVoteScore.getText().toString();
                    int num = Integer.parseInt(currentNum);
                    num++;
                    holder.commentVoteScore.setText(""+num);
                }
            }
        });
        holder.commentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(mContext);
                }
                builder.setTitle("Delete comment")
                        .setMessage("Are you sure you want to delete this comment?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                removeComment(mComments.indexOf(comment));
                                //refresh event details
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.commentReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.commentReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.requestFocus();
                mEditText.setText("@"+comment.getUser().getName());
                mEditText.setSelection(mEditText.getText().length());
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                //Shar

            }
        });
    }


    @Override
    public int getItemCount() {
        return mComments.size();
    }

    private void removeComment(int position) {
        kickoffDeleteComment(mComments.get(position));
        mComments.remove(position);
        notifyItemRemoved(position);
    }

    private void addComment(Comment comment){
        mComments.add(comment);
        notifyItemInserted(mComments.size() - 1);
    }

    private void kickoffVoteOnComment(final Comment comment, int vote_value) {
        activeApiCall = new ApiGraphRequestTask(mContext);

        String jsonQuery="mutation{voteOnComment(comment_id: "+comment.getId()+",vote_value:"+vote_value+"){id}}";

        activeApiCall.run(jsonQuery,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ApiGraphRequestTask: onFailure ");
                activeApiCall = null;
                //     handler.post(apiFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activeApiCall = null;
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    Log.d(TAG, "vote on Comment response: " + jsonResponse.toString());
                    JSONObject data = jsonResponse.getJSONObject("data");
             //       JSONObject vote = data.getJSONObject("voteOnComment");
                   // int vote_value = vote.getInt("vote_value");
                    kickoffGetEvents(comment.getEventCity());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void kickoffGetEvents(final String city){
        activeApiCall = new ApiGraphRequestTask(mContext);

        String jsonQuery="query{upcomingEvents(city: \""+city+"\"){id,name,city,address,date,time,description,type,attendingUsers" +
                "{id,profileImage{url}},comments{id,event{id,city},user{id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}},text,karma,timestamp," +
                "votes{id,user{id},comment{id},vote_value},replies{id},parentComment{id}},agendaItems{id,name,description,type,event{id}}}}";

        activeApiCall.run(jsonQuery,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ApiGraphRequestTask: onFailure ");
                activeApiCall = null;
                //     handler.post(apiFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                activeApiCall = null;
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONObject data = jsonResponse.getJSONObject("data");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(city+"Events",data.toString());
                    editor.apply();
                    Log.d(TAG, "upcoming events str: " + data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void kickoffDeleteComment(final Comment comment){

        activeApiCall = new ApiGraphRequestTask(mContext);

        String jsonQuery="mutation{deleteComment(comment_id: "+comment.getId()+"){id}}";

        activeApiCall.run(jsonQuery,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ApiGraphRequestTask: onFailure ");
                activeApiCall = null;
                //     handler.post(apiFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activeApiCall = null;
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    Log.d(TAG, "delete comment response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");
                   handler.post(deleteCommentToast);
                    kickoffGetEvents(comment.getEventCity());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
