package com.android.anmol.githubapi.user_feeds.ui_handling;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.anmol.githubapi.utility.ResUtils;

/**
 * To add spacing between 2 items in the list/grid etc.
 */
public class RecyclerViewSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int mSpaceSize;

    public RecyclerViewSpaceItemDecorator(@NonNull final Context context,
                                          @DimenRes final int dimenRes) {
        mSpaceSize = ResUtils.getDimen(context, dimenRes);
    }


    @Override
    public void getItemOffsets(final Rect outRect
            , final View view
            , final RecyclerView parent
            , final RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mSpaceSize;
        outRect.left = mSpaceSize;
        outRect.right = mSpaceSize;
    }
}
