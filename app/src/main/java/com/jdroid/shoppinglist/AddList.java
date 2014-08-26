package com.jdroid.shoppinglist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jdroid.shoppinglist.data.ListContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddList extends ActionBarActivity{

    private AddListAdapter mAddListAdapter;

    public static ArrayList<Map<String, String>> data_temp = new ArrayList<Map<String, String>>();

    public static ListView lv;

    public String measure_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);



        lv = (ListView) findViewById(R.id.temp_listview);
        lv.setEmptyView(findViewById(R.id.temp_empty_list_view));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Toast.makeText(getApplicationContext(),"Deleted!",Toast.LENGTH_LONG).show();

                data_temp.remove(position);
                mAddListAdapter.notifyDataSetChanged();

            }
        });

        mAddListAdapter = new AddListAdapter(this,data_temp);
        lv.setAdapter(mAddListAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_list) {

            SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();


            EditText et = (EditText) findViewById(R.id.et_listTitle);


            ContentValues listValues = new ContentValues();
            listValues.put(ListContract.ListEntry.COLUMN_NAME, et.getText().toString());
            listValues.put(ListContract.ListEntry.COLUMN_DATETEXT, sdt.format(now).toString());

            Uri listInsertUri = this.getContentResolver().insert(ListContract.ListEntry.CONTENT_URI, listValues);

            for (int x = 0; x<data_temp.size();x++){
                ContentValues itemValues = new ContentValues();
                itemValues.put(ListContract.ItemEntry.COLUMN_LIST_KEY, Integer.valueOf(listInsertUri.getLastPathSegment()));
                itemValues.put(ListContract.ItemEntry.COLUMN_NAME, data_temp.get(x).get("name"));
                itemValues.put(ListContract.ItemEntry.COLUMN_QUANTITY, data_temp.get(x).get("quantity"));
                itemValues.put(ListContract.ItemEntry.COLUMN_MEASURE, data_temp.get(x).get("measure"));
                itemValues.put(ListContract.ItemEntry.COLUMN_CHECK,0);
                this.getContentResolver().insert(ListContract.ItemEntry.CONTENT_URI, itemValues);

            }

            Toast.makeText(this,"Added!", Toast.LENGTH_SHORT).show();

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.addElement_btn:
                final Map<String, String> map = new HashMap<String, String>();

                /*map.put("measure", "kg");
                data_temp.add(map);
                mAddListAdapter = new AddListAdapter(this,data_temp);
                lv.setAdapter(mAddListAdapter);*/
                // custom dialog
                final DialogFragment dialog1 = new DialogFragment();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();

                final View view = inflater.inflate(R.layout.insert_element_dialog, null);


                Spinner spinner = (Spinner) view.findViewById(R.id.spinner_measure);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.measures_types, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] measures = getResources().getStringArray(R.array.measures_types);
                        measure_selected = measures[i];
                        Toast.makeText(getApplicationContext(),measure_selected + "", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                dialog.setView(view);
                dialog.setTitle(R.string.insert_element_dialog_title);
                dialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        EditText et_name = (EditText) view.findViewById(R.id.et_name_element);
                        map.put("name", et_name.getText().toString() );
                        EditText et_quantity = (EditText) view.findViewById(R.id.et_quantity_element);
                        map.put("quantity", et_quantity.getText().toString() );
                        map.put("measure", measure_selected);
                        data_temp.add(map);
                        //mAddListAdapter = new AddListAdapter(getApplicationContext(),data_temp);
                        //lv.setAdapter(mAddListAdapter);
                        mAddListAdapter.notifyDataSetChanged();


                    }
                });

                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

                dialog.create();


                dialog.show();
                break;
            default:
                break;
        }
    }



}
