package com.care.dragdropgrid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * This class is used with a GridView object. It provides a set of ImageCell objects
 * that support dragging and dropping. An View.OnDragListener object can be provided
 * when you create the adapter.
 */
public class ImageCellAdapter extends BaseAdapter {

    private Context mContext = null;
    private ViewGroup mParentView = null;
    private View.OnDragListener mDragListener = null;

    public ImageCellAdapter(Context c) {
        mContext = c;
        mDragListener = null;
    }

    public ImageCellAdapter(Context c, View.OnDragListener l) {
        mContext = c;
        mDragListener = l;
    }

    @Override
    public int getCount() {
        int numImages = mContext.getResources().getInteger(R.integer.num_images);
        return numImages;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mParentView = parent;

        int cellWidth = mContext.getResources().getDimensionPixelSize(R.dimen.grid_cell_width);
        int cellHeight = mContext.getResources().getDimensionPixelSize(R.dimen.grid_cell_height);
        int cellPad = mContext.getResources().getDimensionPixelSize(R.dimen.grid_cell_padding);

        ImageCell view = null;
        if(convertView == null) {
            // If it's not recycled, create a new ImageCell.
            view = new ImageCell(mContext);
            view.setLayoutParams(new GridView.LayoutParams(cellWidth, cellHeight));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);;
            view.setPadding(cellPad, cellPad, cellPad, cellPad);
        } else {
            view = (ImageCell) convertView;
            view.setImageDrawable(null);
        }

        view.setArguments((GridView) mParentView, position, true);
        view.setOnDragListener(mDragListener);
        view.setBackgroundResource(R.color.cell_empty);
        view.setOnClickListener((View.OnClickListener) mContext);
        view.setOnTouchListener((View.OnTouchListener) mContext);
        view.setOnLongClickListener((View.OnLongClickListener) mContext);

        return view;
    }
}
