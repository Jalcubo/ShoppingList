package com.jdroid.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by JoséDaniel on 20/08/2014.
 */
public class DetailListAdapter  extends CursorAdapter{



    public DetailListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView name_tv;
        public final TextView quantity_tv;
        public final TextView measure_tv;


        public ViewHolder(View view) {
            name_tv = (TextView) view.findViewById(R.id.detail_item_list_name);
            quantity_tv = (TextView) view.findViewById(R.id.detail_item_list_quantity);
            measure_tv = (TextView) view.findViewById(R.id.detail_item_list_measure);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String name = cursor.getString(DetailList.COL_ITEM_NAME);
        String quantity = cursor.getString(DetailList.COL_ITEM_QUANTITY);
        String measure = cursor.getString(DetailList.COL_ITEM_MEASURE);





        viewHolder.name_tv.setText(name);
        viewHolder.quantity_tv.setText(quantity);
        viewHolder.measure_tv.setText(measure);

    }

}
