package com.setgov.android.adapters;

/**
 * Created by Ross on 9/17/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.setgov.android.R;
import com.setgov.android.SimpleDividerItemDecoration;
import com.setgov.android.models.Comment;
import com.setgov.android.models.CommentReply;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;
import com.setgov.android.util.MyEditText;
import com.setgov.android.viewholders.CommentViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ross on 9/10/2017.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private static final String TAG = "CommentAdapter";
    private Context mContext;
    private List<CommentReply> mComments;
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

    public CommentReplyAdapter(User user, MyEditText editText, Context context, ArrayList<CommentReply> comments){
        this.mUser = user;
        this.mEditText = editText;
        this.mComments = comments;
        this.mContext = context;
        handler = new Handler();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final CommentReply comment = mComments.get(position);
        holder.commentText.setText(comment.getText());
        holder.commentUserName.setText(comment.getUser().getName());
        holder.commentVoteScore.setText(""+comment.getKarma());
        if(comment.getUser().getID()==mUser.getID()){
            holder.commentDeleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.commentDeleteButton.setVisibility(View.GONE);
        }
        for(int i = 0; i < comment.getVotes().size();i++){
            if(comment.getVotes().get(i).getUser() == mUser.getID()){
                holder.setKarma(comment.getVotes().get(i).getVoteValue());
            }
        }
        holder.commentTimePosted.setText(formatCommentDate(comment.getTimestamp()));
        Picasso.with(mContext).load(mUser.getProfileImageUrl()).into(holder.commentUserProfile);
        holder.commentUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if user voted on comment already
                //if user already voted cancel the vote
                //if user hasnt voted, kickoffUpvoteComment
                String currentNum = holder.commentVoteScore.getText().toString();
                int num = Integer.parseInt(currentNum);
                if(holder.karma == -1){
                    kickoffVoteOnComment(comment,1);
                    holder.setKarma(1);
                    num++;
                    num++;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma == 0) {
                    kickoffVoteOnComment(comment,1);
                    holder.setKarma(1);
                    num++;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma == 1){
                    kickoffVoteOnComment(comment,0);
                    holder.setKarma(0);
                    num--;
                    holder.commentVoteScore.setText(""+num);
                }
                //update karma textview
            }
        });
        holder.commentDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNum = holder.commentVoteScore.getText().toString();
                int num = Integer.parseInt(currentNum);
                if(holder.karma  == 1){
                    kickoffVoteOnComment(comment,-1);
                    holder.setKarma(-1);

                    num--;
                    num--;
                    holder.commentVoteScore.setText(""+num);
                } else if (holder.karma  == 0) {
                    kickoffVoteOnComment(comment,-1);
                    holder.setKarma(-1);
                    num--;
                    holder.commentVoteScore.setText(""+num);
                } else if(holder.karma  == -1){
                    kickoffVoteOnComment(comment,0);
                    holder.setKarma(0);
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
                SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences
                        ("auth", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sp.edit();
                editor.putBoolean("isReply",true);
                editor.putInt("replyTo",comment.getId());
                editor.apply();

            }
        });
        if(comment.getReplies().size()>0){
            holder.commentReplyFrame.setAdapter(new CommentAdapter(mUser,mEditText,mContext,comment.getReplies()));
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
            holder.commentReplyFrame.setLayoutManager(layoutManager);
            holder.commentReplyFrame.addItemDecoration(new SimpleDividerItemDecoration(mContext,false));
        }
    }

    public String formatCommentDate(String timestamp){
        String[] timeParts = timestamp.split(" ");
        String[] str = {"January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};
        String formattedTimestamp, day, month, year, time;
        int monthInt=0;
        int dayInt;
        month = timeParts[1];
        day = timeParts[2];
        year = timeParts[3];
        time = timeParts[4];
        for(int i = 0;i<str.length;i++){
            if(str[i].contains(month)){
                monthInt = i+1;
            }
        }
        if(monthInt<10&&monthInt>0){
            month = "0" +monthInt;
        } else {
            month = ""+ monthInt;
        }
        dayInt = Integer.parseInt(day);
        if(dayInt<10 &&dayInt>0){
            day = "0" + dayInt;
        } else{
            day = ""+ dayInt;
        }
        formattedTimestamp = year+"-"+month+"-"+day +" "+time;
        Timestamp mTimestamp = Timestamp.valueOf(formattedTimestamp);
        long currentTime = System.currentTimeMillis();
        long commentTime =mTimestamp.getTime();
        long twentyFourHours = 86400000;
        if(currentTime-commentTime<=twentyFourHours){
            Log.d(TAG, "within 24 hours");
            long numMillisAgo = currentTime-commentTime;

            //less than an hour
            if(numMillisAgo<3600000){
                long minutes = (numMillisAgo/60000);
                if(minutes == 1){
                    timestamp = minutes + " min ago";
                } else {
                    timestamp = minutes + " mins ago";
                }

                //less than 24 hours
            } else {
                long hours = (numMillisAgo / 3600000);
                if(hours == 1){
                    timestamp = hours + " hour ago";
                } else {
                    timestamp = hours + " hours ago";
                }
            }

        }else{
            timestamp = str[monthInt-1]+" "+ dayInt + ", " +year;
        }
        return timestamp;
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

    private void addComment(CommentReply comment){
        mComments.add(comment);
        notifyItemInserted(mComments.size() - 1);
    }

    private void kickoffVoteOnComment(final CommentReply comment, int vote_value) {
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
                "votes{id,user{id},comment{id},vote_value},replies{id,event{id,city},user{id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}},text,karma,timestamp," +
                "votes{id,user{id},comment{id},vote_value},replies{id},parentComment{id}},parentComment{id}},agendaItems{id,name,description,type,event{id}}}}";
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

    private void kickoffDeleteComment(final CommentReply comment){

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

