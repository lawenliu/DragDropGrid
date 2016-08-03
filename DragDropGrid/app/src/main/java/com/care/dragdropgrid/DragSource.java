package com.care.dragdropgrid;

import android.content.ClipData;
import android.view.View;

/**
 * This interface defines drag-drop operations.
 */
public interface DragSource {

    /**
     * Decide whether there are any items for drag-drop.
     * @return boolean - Any items for drag-drop return true
     */
    public boolean allowDrag();

    /**
     * Return the ClipData associated with the drag operation.
     * @return ClipData - clip data of source
     */
    public ClipData clipDataForDragDrop();

    /**
     * Return the view that is the actual source of the item being dragged.
     * @return Dragged view
     */
    public View dragDropView();

    /**
     * This method is called at the start of a drag-drop operation so that the object being dragged
     * knows that it is being dragged.
     */
    public void onDragStarted();

    /**
     * This method is called on the completion of the drag operation so the DragSource knows
     * whether it succeeded or failed.
     * @param target DropTarget - the object that accepted the dragged object
     * @param success boolean - true means that the object was dropped successfully
     */
    public void onDropCompleted(DropTarget target, boolean success);
}
