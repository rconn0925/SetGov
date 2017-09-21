package com.setgov.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setgov.android.R;
import com.setgov.android.models.Event;
import com.setgov.android.models.Representative;
import com.setgov.android.models.User;
import com.setgov.android.viewholders.EventViewHolder;
import com.setgov.android.viewholders.RepresentativeViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 9/18/2017.
 */

public class RepresentativeAdapter extends RecyclerView.Adapter<RepresentativeViewHolder> implements View.OnClickListener {

    private static final String TAG = "RepresentativeAdapter";
    private Context mContext;
    private List<Representative> mReps;
    private RecyclerView mRecyclerView;
    private User mUser;

    public RepresentativeAdapter(RecyclerView view, Context context, ArrayList<Representative> mReps){

            this.mContext = context;
            this.mReps = mReps;
            this.mRecyclerView = view;
            SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
            String userJson = sp.getString("loggedInUserJson","");
            try {
                this.mUser = new User(new JSONObject(userJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
    @Override
    public RepresentativeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.representative_item, parent, false);
        view.setOnClickListener(this);
        return new RepresentativeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepresentativeViewHolder holder, int position) {
        final Representative representative = mReps.get(position);
        holder.repName.setText(representative.getName());
        holder.repOfficeName.setText(representative.getOffice().getName());

        if(representative.getUrl()!=null && representative.getUrl().length()>0){
         //   holder.repMoreInfo.setOnClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return mReps.size();
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        Representative rep = mReps.get(itemPosition);
      // mContext.startActivity(
       //         new Intent(Intent.ACTION_VIEW, Uri.parse(rep.getUrl())));

    }
}
