package com.care.dragdropgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Delete zone to define a zone for drag-drop item to delete
 */
public class DeleteZone extends ImageView
        implements  DropTarget {

    private boolean mEnabled = true;

    public DeleteZone(Context context) {
        super(context);
    }

    public DeleteZone (Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public DeleteZone (Context context, AttributeSet attrs, int style)
    {
        super (context, attrs, style);
    }

    @Override
    public boolean allowDrop(DragSource source) {
        return isEnabled();
    }

    @Override
    public View dragDropView() {
        return this;
    }

    @Override
    public void onDrop(DragSource source) {
        if(isEnabled()) {
            setImageLevel(1);
            invalidate();
        }
    }

    @Override
    public void onDragEnter(DragSource source) {
        if(isEnabled()) {
            // Set the image level to 2 so that the image is highlighted.
            setImageLevel(2);
        }
    }

    @Override
    public void onDragExit(DragSource source) {
        if(isEnabled()) {
            // Set the image level to 1 so that the image show normally.
            setImageLevel(1);
        }
    }

    /**
     * Check whether this target is enable to drop on this view.
     * @return boolean - True if allow to drop on this view
     */
    public boolean isEnabled() {
        return mEnabled && (getVisibility() == View.VISIBLE);
    }
}
