package com.care.dragdropgrid;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener,
        View.OnTouchListener, DragDropPresenter {

    private static final int VIBRATE_DURATION = 35;

    private DragController mDragController;
    private GridView mGridView;
    private DeleteZone mDeleteZone;
    private int mImageCount = 0;
    private ImageCell mLastNewCell = null;
    private boolean mLongClickStartDrag = false;
    private BaseAdapter mImageCellAdapter = null;

    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initialize();
    }

    private void initialize() {
        // When a drag starts, we vibrate so the user gets some feedback.
        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        // This activity will listen for drag-drop events.
        // The listener used is a DragController. Set it up.
        mDragController = new DragController(this);

        // Set up the grid view with an ImageCellAdapter and have it use the DragController.
        mGridView = (GridView) findViewById(R.id.image_grid_view);
        if(mGridView != null) {
            mImageCellAdapter = new ImageCellAdapter(this, mDragController);
            mGridView.setAdapter(mImageCellAdapter);
        }

        // Always add the delete_zone so there is a place to get rid of views.
        // Find the delete_zone and add it as a drop target.
        // That gives the user a place to drag views to get them off the screen.
        mDeleteZone = (DeleteZone) findViewById(R.id.delete_zone_view);
        if(mDeleteZone != null) {
            mDeleteZone.setOnDragListener(mDragController);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isDragDropEnabled() {
        return true;
    }

    @Override
    public void onDragStarted(DragSource source) {
        mVibrator.vibrate(VIBRATE_DURATION);
    }

    @Override
    public void onDropCompleted(DropTarget target, boolean success) {

    }

    @Override
    public boolean onLongClick(View v) {
        if(mLongClickStartDrag) {
            if(!v.isInTouchMode()) {
                return false;
            }

            return startDrag(v);
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!mLongClickStartDrag) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                return startDrag(v);
            }
        }

        return false;
    }

    /**
     * Handle a click of the Add Image button
     * @param v View - clicked view
     */
    public void onClickAddImage (View v)
    {
        addNewImageToScreen ();
    }

    /**
     * Start dragging a view.
     * @param v View - view to drag and drop
     * @return boolean - starting drag and drop
     */
    public boolean startDrag(View v) {
        v.setOnDragListener(mDragController);
        mDragController.startDrag(v);
        return true;
    }

    /**
     * Add a new image so the user can move it around. It shows up in the image_source_frame
     * part of the screen.
     * @param resourceId int - the resource id of the image to be added
     */
    public void addNewImageToScreen(int resourceId) {
        if(mLastNewCell != null) {
            mLastNewCell.setVisibility(View.GONE);
        }

        FrameLayout imageHolder = (FrameLayout) findViewById(R.id.image_source_frame);
        if(imageHolder != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            ImageCell newView = new ImageCell(this);
            newView.setImageResource(resourceId);
            imageHolder.addView(newView, lp);
            newView.setArguments(null, -1, false);
            mLastNewCell = newView;
            mImageCount++;

            // Have this activity listen to touch and click events for the view.
            newView.setOnClickListener(this);
            newView.setOnLongClickListener(this);
            newView.setOnTouchListener (this);
        }
    }

    /**
     * Add one of the images to the screen so the user has a new image to move around.
     * See addImageToScreen.
     */
    public void addNewImageToScreen() {
        int resourceId = R.drawable.trashcan;

        addNewImageToScreen(resourceId);
    }

    @Override
    public void onClick(View v) {

    }
}
