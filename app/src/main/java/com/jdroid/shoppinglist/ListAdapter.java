package com.jdroid.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by JoséDaniel on 15/08/2014.
 */
public class ListAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final TextView nameView;
        public final TextView datetextView;


        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.list_item_list_text);
            datetextView = (TextView) view.findViewById(R.id.list_item_list_datetext);
        }
    }


    public ListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = R.layout.list_item_list;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.nameView.setText(cursor.getString(ListFragment.COL_LIST_NAME));
        viewHolder.datetextView.setText(cursor.getString(ListFragment.COL_LIST_DATE));

        Log.e("AUX", viewHolder.toString());



    }
}
