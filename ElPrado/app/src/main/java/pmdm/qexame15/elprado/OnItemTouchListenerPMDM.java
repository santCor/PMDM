package pmdm.qexame15.elprado;


import android.content.Context;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OnItemTouchListenerPMDM implements RecyclerView.OnItemTouchListener {

    private final Context context;
    private final RecyclerView recyclerView;
    private final OnItemTouchActionListener onItemTouchActionListener;
    private final GestureDetectorCompat gestureDetector;

    public OnItemTouchListenerPMDM(Context context, final RecyclerView recyclerView, final OnItemTouchActionListener onItemTouchActionListener) {

        this.context = context;
        this.recyclerView = recyclerView;
        this.onItemTouchActionListener = onItemTouchActionListener;

        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);


                View item = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(item != null) {
                    int itemPosition = recyclerView.getChildAdapterPosition(item);
                    long id = itemPosition;

                    if(CursorAdapterPMDM.class.isAssignableFrom(recyclerView.getAdapter().getClass())) {
                        id = ((CursorAdapterPMDM)recyclerView.getAdapter()).getItemIdAtPosition(itemPosition);
                    }

                    onItemTouchActionListener.onItemLongClick(recyclerView, item, itemPosition, id);
                }
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                super.onSingleTapConfirmed(e);

                View item = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(item != null) {
                    int itemPosition = recyclerView.getChildAdapterPosition(item);
                    long id = itemPosition;

                    if(CursorAdapterPMDM.class.isAssignableFrom(recyclerView.getAdapter().getClass())) {
                        id = ((CursorAdapterPMDM)recyclerView.getAdapter()).getItemIdAtPosition(itemPosition);
                    }

                    onItemTouchActionListener.onItemClick(recyclerView, item, itemPosition, id);
                }
                return false;
            }
        }
        );



    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }


    public interface OnItemTouchActionListener {
        //TODO ISTO NON ESTÁ COMPLETO!!!!!!!

        public void onItemClick(RecyclerView recyclerView, View item, int itemPosition, long id);
        public void onItemLongClick(RecyclerView recyclerView, View item, int itemPosition, long id);


    }
}
