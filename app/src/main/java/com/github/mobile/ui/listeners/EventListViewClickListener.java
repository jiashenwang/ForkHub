package com.github.mobile.ui.listeners;

import android.content.Context;
import android.view.View;

import com.github.mobile.api.model.TimelineEvent;
import com.github.mobile.ui.issue.IssueFragment;

public class EventListViewClickListener implements View.OnClickListener {

    final String EDIT = "edit";
    final String DELETE = "delete";

    private final IssueFragment issueFragment;
    private final Context context;
    private final String action;
    private final TimelineEvent comment;

    public EventListViewClickListener(Context context,
                                      TimelineEvent comment,
                                      IssueFragment issueFragment,
                                      String action) {
        this.context = context;
        this.issueFragment = issueFragment;
        this.action = action;
        this.comment = comment;
    }

    @Override
    public void onClick(View view) {
        if(action.equalsIgnoreCase(EDIT)) {
            issueFragment.editComment(comment.getOldCommentModel());
        }else if(action.equalsIgnoreCase(DELETE)) {
            issueFragment.deleteComment(comment.getOldCommentModel());
        }
    }
}