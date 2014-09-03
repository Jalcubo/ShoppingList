package com.jdroid.shoppinglist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

                if ( view.getId() == R.id.del_btn){
                    Toast.makeText(getApplicationContext(),"Deleted!",Toast.LENGTH_LONG).show();

                    data_temp.remove(position);
                    mAddListAdapter.notifyDataSetChanged();

                }


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


            if(!et.getText().toString().matches("")){
                if(data_temp.size() > 0){
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

                    data_temp = new ArrayList<Map<String, String>>();

                    Toast.makeText(this,R.string.added, Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    Toast.makeText(this,R.string.empty_items, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, R.string.empty_list_name, Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.addElement_btn:
                final Map<String, String> map = new HashMap<String, String>();

                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                dialog.setView(view);
                dialog.setTitle(R.string.insert_element_dialog_title);

                dialog.setPositiveButton(R.string.add,null);
                dialog.setNegativeButton(R.string.cancel, null);



                final AlertDialog d = dialog.create();
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);



                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // FIRE ZE MISSILES!

                                EditText et_name = (EditText) view.findViewById(R.id.et_name_element);
                                map.put("name", et_name.getText().toString() );
                                EditText et_quantity = (EditText) view.findViewById(R.id.et_quantity_element);
                                map.put("quantity", et_quantity.getText().toString() );
                                map.put("measure", measure_selected);

                                if ((!et_name.getText().toString().matches("")) && (!et_quantity.getText().toString().matches(""))){
                                    data_temp.add(map);
                                    mAddListAdapter.notifyDataSetChanged();
                                    d.dismiss();

                                }else{
                                    Toast.makeText(getApplicationContext(), R.string.add_empty_fields, Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });

                d.show();
                break;
            default:
                break;
        }
    }



}
