package com.jdroid.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by JoséDaniel on 18/08/2014.
 */
public class AddListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Map<String, String>> values;

    public AddListAdapter(Context context,  List<Map<String, String>> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_add, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);

        viewHolder.name_tv.setText(values.get(position).get("name"));
        viewHolder.quantity_tv.setText(values.get(position).get("quantity"));
        viewHolder.measure_tv.setText(values.get(position).get("measure"));

        return rowView;
    }

    public static class ViewHolder {
        public final TextView name_tv;
        public final TextView quantity_tv;
        public final TextView measure_tv;


        public ViewHolder(View view) {
            name_tv = (TextView) view.findViewById(R.id.add_item_list_name);
            quantity_tv = (TextView) view.findViewById(R.id.add_item_list_quantity);
            measure_tv = (TextView) view.findViewById(R.id.add_item_list_measure);
        }
    }


}
