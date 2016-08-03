package com.care.dragdropgrid;

/**
 * This is the interface that defines an object that implements a drag-drop operation.
 * Typically, this interface is implemented by an Activity or Fragment.
 * It is called a "Presenter" because it presents the drag-drop operation to the user.
 */
public interface DragDropPresenter {

    /**
     * This method is called to determine if drag-drop is enabled.
     * @return boolean - True if drag-drop is enabled
     */
    public boolean isDragDropEnabled ();

    /**
     * React to the starting of a drag-drop operation.
     * @param source DragSource - source of item for drag-drop
     */
    public void onDragStarted (DragSource source);

    /**
     * This method is called on the completion of a drag operation.
     * If the drop was not successful, the target is null.
     * @param target DropTarget - drop target view
     * @param success boolean - drop successfully
     */
    public void onDropCompleted (DropTarget target, boolean success);
}
