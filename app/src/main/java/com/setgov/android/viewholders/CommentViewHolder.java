package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ross on 9/10/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.commentText)
    public TextView commentText;
    @InjectView(R.id.commentReplyButton)
    public TextView commentReplyButton;
    @InjectView(R.id.commentVoteScore)
    public TextView commentVoteScore;
    @InjectView(R.id.commentUserName)
    public TextView commentUserName;
    @InjectView(R.id.commentTimePosted)
    public TextView commentTimePosted;
    @InjectView(R.id.commentUpvote)
    public ImageView commentUpvote;
    @InjectView(R.id.commentDownvote)
    public ImageView commentDownvote;
    @InjectView(R.id.commentDeleteButton)
    public ImageView commentDeleteButton;
    @InjectView(R.id.commentReportButton)
    public ImageView commentReportButton;
    @InjectView(R.id.commentUserProfile)
    public CircleImageView commentUserProfile;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
