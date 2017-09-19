package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ross on 9/10/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
    @InjectView(R.id.commentReplyFrame)
    public RecyclerView commentReplyFrame;
    @InjectView(R.id.commentBackground)
    public LinearLayout commentBackground;

    public int karma;
    private boolean repliesAreVisiable;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
       // commentBackground.setOnClickListener(this);
        karma = 0;
        repliesAreVisiable = true;
    }


    public int getKarma(){return karma;}
    public void setKarma(int karma){
        this.karma = karma;
    }

    @Override
    public void onClick(View v) {
        if(repliesAreVisiable){
            commentReplyFrame.setVisibility(View.GONE);
            repliesAreVisiable = false;
        } else {
            commentReplyFrame.setVisibility(View.VISIBLE);
            repliesAreVisiable = true;
        }

    }
}
