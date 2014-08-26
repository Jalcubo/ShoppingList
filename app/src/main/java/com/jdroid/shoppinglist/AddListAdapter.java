package com.jdroid.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_add, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);

        viewHolder.name_tv.setText(values.get(position).get("name"));
        viewHolder.quantity_tv.setText(values.get(position).get("quantity"));
        viewHolder.measure_tv.setText(values.get(position).get("measure"));

        viewHolder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });





        return rowView;
    }

    public static class ViewHolder {
        public final TextView name_tv;
        public final TextView quantity_tv;
        public final TextView measure_tv;
        public final ImageButton del_btn;


        public ViewHolder(View view) {
            name_tv = (TextView) view.findViewById(R.id.add_item_list_name);
            quantity_tv = (TextView) view.findViewById(R.id.add_item_list_quantity);
            measure_tv = (TextView) view.findViewById(R.id.add_item_list_measure);
            del_btn = (ImageButton) view.findViewById(R.id.del_btn);
        }
    }


}
