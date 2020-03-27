package pmdm.qexame15.elprado;

import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.RecyclerView;

abstract class CursorAdapterPMDM<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Context context;
    protected Cursor c;

    public CursorAdapterPMDM(Context context, Cursor c) {
        this.context = context;
        this.c = c;
    }

    @Override
    public int getItemCount() {
        if(c != null) {
            return c.getCount();
        }
        return 0;
    }

    public long getItemIdAtPosition(int position) {
        if(position >=0 && position < c.getCount()) {
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex("_id"));
        }
        return -1;
    }

    public Cursor swapCursor(Cursor novoCursor) {
        Cursor antigoCursor = this.c;

        this.c = novoCursor;
        //Para que o CursorAdapter se enteire de que os datos cambiaron
        this.notifyDataSetChanged();

        return antigoCursor;
    }

    public Cursor getCursor() {
        return c;
    }
}
