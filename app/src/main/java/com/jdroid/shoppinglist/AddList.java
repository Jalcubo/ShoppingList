package com.jdroid.shoppinglist;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jdroid.shoppinglist.data.ListContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddList extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.save_btn:
                SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMdd");
                Date now = new Date();


                EditText et = (EditText) findViewById(R.id.et_listTitle);


                ContentValues listValues = new ContentValues();
                listValues.put(ListContract.ListEntry.COLUMN_NAME, et.getText().toString());
                listValues.put(ListContract.ListEntry.COLUMN_DATETEXT, "20140816");

                Uri listInsertUri = this.getContentResolver().insert(ListContract.ListEntry.CONTENT_URI, listValues);

                Toast.makeText(this,"Added!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }
    }


}
