package com.care.dragdropgrid;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * This subclass of ImageView is used to display an image on an GridView.
 * An ImageCell knows which cell on the grid it is showing and which grid it is attached to
 * Cell numbers are from 0 to NumCells-1.
 * It also knows if it is empty.
 *
 * Image cells are places where images can be dragged from and dropped onto.
 * Therefore, this class implements both the DragSourceOLD and DropTargetOLD interfaces.
 */
public class ImageCell extends ImageView
        implements DragSource, DropTarget {

    private boolean mEmpty = true;
    private int mCellNumber = -1;
    private GridView mGridView;

    public ImageCell(Context context) {
        super(context);
    }

    public ImageCell (Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public ImageCell (Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    /**
     * This method is called to determine if the DragSourceOLD has something to drag.
     * @return boolean - True if this cell has nothing to drag
     */
    @Override
    public boolean allowDrag() {
        return !mEmpty;
    }

    @Override
    public ClipData clipDataForDragDrop() {
        return null;
    }

    /**
     * Return true if the DropTarget allows objects to be dropped on it.
     * Disallow drop if the source object is the same ImageCell.
     * Allow a drop of the ImageCell is empty.
     * @param source DragSource - source of DragSource
     * @return boolean - True if the drop will be accepted, false otherwise.
     */
    @Override
    public boolean allowDrop(DragSource source) {
        // Do not allow a drop if the DragSource is the same cell.
        if(source == this) {
            return false;
        }

        // An ImageCell accepts a drop if it is empty and if it is part of a grid.
        // A free-standing ImageCell does not accept drops.
        return mEmpty && (mCellNumber >= 0);
    }

    @Override
    public View dragDropView() {
        return this;
    }

    @Override
    public void onDrop(DragSource source) {
        // Mark the cell so it is no longer empty.
        mEmpty = false;
        int bg = mEmpty ? R.color.cell_empty : R.color.cell_filled;
        setBackgroundResource(bg);

        // The view being dragged does not actually change its parent and switch over to the ImageCell.
        // What we do is copy the drawable from the source view.
        ImageView sourceView = (ImageView) source.dragDropView();
        Drawable d = sourceView.getDrawable();
        if(d != null) {
            this.setImageDrawable(d);
            this.invalidate();
        }
    }

    @Override
    public void onDragEnter(DragSource source) {
        if(mCellNumber >= 0) {
            int bg = mEmpty ? R.color.cell_empty_hover : R.color.cell_filled_hover;
            setBackgroundResource(bg);
        }
    }

    @Override
    public void onDragExit(DragSource source) {
        if(mCellNumber >= 0) {
            int bg = mEmpty ? R.color.cell_empty : R.color.cell_filled;
            setBackgroundResource(bg);
        }
    }

    @Override
    public void onDragStarted() {
        if(mCellNumber >= 0) {
            setColorFilter(R.color.cell_nearly_empty);
            invalidate();
        }
    }

    @Override
    public void onDropCompleted(DropTarget target, boolean success) {
        // Undo what we did in onDragStarted
        if(mCellNumber >= 0) {
            clearColorFilter();;
            invalidate();
        }

        // If the drop succeeds, the image has moved elsewhere.
        // So clear the image cell.
        if(success) {
            mEmpty = true;
            if(mCellNumber >= 0) {
                int bg = mEmpty ? R.color.cell_empty : R.color.cell_filled;
                setBackgroundResource(bg);
                setImageDrawable(null);
            } else {
                // If the cell number is negative, it means we are interacting with a free-standing
                // image cell. There is one of those. It is the place where an image is added when
                // the user clicks "add image".
                // At the conclusion of a drop, clear it.
                setImageResource(0);
            }
        } else {
            // On the failure case, reset the background color in case it is still set to the hover color.
            if(mCellNumber >= 0) {
                int bg = mEmpty ? R.color.cell_empty : R.color.cell_filled;
                setBackgroundResource(bg);
            }
        }
    }

    /**
     * Set arguments for ImageCell
     * @param gv GridView - grid view as the parent of this cell
     * @param cn integer - cell number of the grid
     * @param e boolean - cell empty status
     */
    public void setArguments(GridView gv, int cn, boolean e) {
        mGridView = gv;
        mCellNumber = cn;
        mEmpty = e;
    }
    /**
     * Return true if this cell is empty.
     * If it is, it means that it will accept dropped views.
     * It also means that there is nothing to drag.
     * @return boolean - True if this cell is empty
     */
    public boolean isEmpty() {
        return mEmpty;
    }

    /**
     * Call this view's onClick listener. Return true if it was called.
     * Clicks are ignored if the cell is empty.
     * @return boolean - performClick
     */
    public boolean performClick() {
        if(!mEmpty) {
            return super.performClick();
        }

        return false;
    }

    /**
     * Call this view's onLongClick listener. Return true if it was called.
     * Clicks are ignored if the cell is empty.
     * @return boolean - performLongClick
     */
    public boolean performLongClick() {
        if(!mEmpty) {
            return super.performLongClick();
        }

        return false;
    }
}
