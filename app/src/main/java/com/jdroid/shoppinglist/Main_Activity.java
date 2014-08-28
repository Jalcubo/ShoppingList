package com.jdroid.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jdroid.shoppinglist.sync.ShoppingListSyncAdapter;


public class Main_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_, menu);

        ShoppingListSyncAdapter.initializeSyncAdapter(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_list) {
            /*
            Toast.makeText(this,"Holaaaaaaa!", Toast.LENGTH_SHORT).show();
            ContentValues listValues = new ContentValues();
            listValues.put(ListContract.ListEntry.COLUMN_NAME, "lista de la compra2");
            listValues.put(ListContract.ListEntry.COLUMN_DATETEXT, "20140816");

            Uri listInsertUri = this.getContentResolver().insert(ListContract.ListEntry.CONTENT_URI, listValues);*/

            Intent i = new Intent(this,AddList.class);
            startActivity(i);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
