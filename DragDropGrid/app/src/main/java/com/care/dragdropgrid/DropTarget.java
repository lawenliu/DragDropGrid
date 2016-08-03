package com.care.dragdropgrid;

import android.view.View;

/**
 * This interface defines a view that allows objects to be dropped on it in a drag-drop operation.
 */
public interface DropTarget {

    /**
     * Decide whether drop target allows items to be dropped on it.
     * @param source DragSource - source of DragSource
     * @return boolean - True if allow to drop
     */
    public boolean allowDrop(DragSource source);

    /**
     * Return the view that is the actual target of the item being dragged.
     * @return View - target view
     */
    public View dragDropView();

    /**
     * Handle an object being dropped on DropTarget.
     * @param source DragSource where the drag action started
     */
    public void onDrop(DragSource source);

    /**
     * React to a dragged object entering into the view of the DropTarget.
     * @param source DragSource where the drag action started
     */
    public void onDragEnter(DragSource source);

    /**
     * React to a dragged object leaving the view of the DropTarget.
     * @param source DragSource where the drag action started
     */
    public void onDragExit(DragSource source);
}
