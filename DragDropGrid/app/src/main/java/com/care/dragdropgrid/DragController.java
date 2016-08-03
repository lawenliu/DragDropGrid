package com.care.dragdropgrid;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.View;

import java.util.PriorityQueue;

/**
 * This object is used in conjunction with a DragDropPresenter.
 * It handles all drag events generated during a drag-drop operation.
 * When a drag starts, it creates a special view (a DragView) that moves around the screen
 * until the user ends the drag. As feedback to the user, this object causes the device to
 * vibrate as the drag begins. It interacts with other objects through methods defined
 * in the DropTarget and DragSource interfaces.
 */
public class DragController
        implements View.OnDragListener {

    private DragDropPresenter mPresenter;
    private boolean mDragging;
    private boolean mDropSuccess;
    private DragSource mDragSource;
    private DropTarget mDropTarget;

    /**
     * Create a new instance of DragController.
     * @param p DragDropPresenter - presenter for drag-drop
     */
    public DragController(DragDropPresenter p) {
        mPresenter = p;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        // Check whether presenter is available
        if(mPresenter != null && !mPresenter.isDragDropEnabled()) {
            return false;
        }

        // Determine if the view is a DragSource, DropTarget, or both.
        // That information is used below when the event is handled.
        boolean isDragSource = false, isDropTarget = false;
        DragSource source = null;
        DropTarget target = null;
        if(v instanceof DragSource) {
            source = (DragSource) v;
            isDragSource = true;
        }

        if(v instanceof DropTarget) {
            target = (DropTarget) v;
            isDropTarget = true;
        }

        boolean eventResult = false;
        final int action = event.getAction();
        // Handles each of expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // We want a call to mPresenter.onDragStarted once.
                // So check to see if we are already dragging.
                if(!mDragging) {
                    mDragging = true;
                    mDropSuccess = false;
                    mPresenter.onDragStarted(mDragSource);
                }

                // At the start of a drag, all drop targets must say they are interested in the rest
                // of the drag events of this drag-drop operation.
                // Allow for the case where a view is both a source and a target.
                if(isDragSource) {
                    // The view continues to see drag events if it is the source of the current drag
                    // or if it is a target itself.
                    if(source == mDragSource) {
                        if(source.allowDrag()) {
                            eventResult = true;
                            source.onDragStarted();
                        }
                    } else {
                        eventResult = isDropTarget && target.allowDrop(mDragSource);
                    }
                } else if(isDropTarget) {
                    eventResult = target.allowDrop(mDragSource);
                } else {
                    eventResult = false;
                }

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(isDropTarget) {
                    target.onDragEnter(mDragSource);
                    mDropTarget = target;
                    eventResult = true;
                } else {
                    eventResult = false;
                }

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if(isDropTarget) {
                    mDropTarget = null;
                    target.onDragExit(mDragSource);
                    eventResult = true;
                } else {
                    eventResult = false;
                }

                break;
            case DragEvent.ACTION_DROP:
                if(isDropTarget) {
                    if(target.allowDrop(mDragSource)) {
                        target.onDrop(mDragSource);
                        mDropTarget = target;
                        mDropSuccess = true;
                    }

                    eventResult = true;
                } else {
                    eventResult = false;
                }

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if(mDragging) {
                    if(mDragSource != null) {
                        mDragSource.onDropCompleted(mDropTarget, mDropSuccess);
                    }

                    if(mPresenter != null) {
                        mPresenter.onDropCompleted(mDropTarget, mDropSuccess);
                    }

                    eventResult = true;
                }

                mDragging = false;
                mDragSource = null;
                mDropTarget = null;
                break;
        }

        return eventResult;
    }

    /**
     * If the view is a DragSource that allows a drag, start a drag-drop operation.
     * @param v View - item for drag-drop
     * @return boolean - True if allow to drag
     */
    public boolean startDrag(View v) {
        try {
            DragSource ds = (DragSource) v;
            if(ds.allowDrag()) {
                mDragging = false;
                mDropSuccess = false;
                mDragSource = ds;
                mDropTarget = null;

                ClipData dragData = ds.clipDataForDragDrop();
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(dragData, shadowBuilder, null, 0);

                return true;
            }

            return false;

        } catch (ClassCastException ex) {
            return false;
        }
    }
}
