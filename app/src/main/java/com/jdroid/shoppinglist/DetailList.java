package com.jdroid.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jdroid.shoppinglist.data.ListContract;

;

public class DetailList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    DetailListAdapter mDetailListAdapter;

    private ShareActionProvider mShareActionProvider;


    private static final int DETAIL_LOADER = 1;

    private final String LOG_TAG = getClass().getSimpleName();
    private int mPosition = ListView.INVALID_POSITION;

    private static String list_id = null;

    private static int count = 0;

    public interface Callback {

        public void onItemSelected(String date);
    }


    private static final String[] ITEM_COLUMNS = {
            ListContract.ItemEntry.TABLE_NAME + "." + ListContract.ItemEntry._ID,
            ListContract.ItemEntry.COLUMN_NAME,
            ListContract.ItemEntry.COLUMN_QUANTITY,
            ListContract.ItemEntry.COLUMN_MEASURE

    };

    public static final int COL_ITEM_ID = 0;
    public static final int COL_ITEM_NAME = 1;
    public static final int COL_ITEM_QUANTITY = 2;
    public static final int COL_ITEM_MEASURE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail_list);

        list_id = getIntent().getStringExtra("LIST_ID");

        setTitle(getIntent().getStringExtra("LIST_NAME"));

        mDetailListAdapter =new DetailListAdapter(this,null,0);

        ListView lv = (ListView) findViewById(R.id.lv_items);
        lv.setAdapter(mDetailListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mDetailListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {



                    if (view.getBackground() == null ){
                        view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        count++;

                    }else{
                        view.setBackground(null);
                        count--;
                    }
                    Toast.makeText(getApplicationContext(),count +"",Toast.LENGTH_SHORT).show();


                    /*int itemDeleteUri = getContentResolver().delete(
                            ListContract.ItemEntry.CONTENT_URI,
                            ListContract.ItemEntry._ID + "= ?",
                            new String [] {cursor.getString(COL_ITEM_ID)});*/

                }



            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_list, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareListIntent("holaaaaaa"));
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        list_id =  getIntent().getStringExtra("LIST_ID");
        Uri itemUriByListId = ListContract.ItemEntry.buildItemByListId(list_id);

        return new CursorLoader(
                this,
                itemUriByListId,
                ITEM_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDetailListAdapter.swapCursor(data);



    }

    private Intent createShareListIntent(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        return shareIntent;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailListAdapter.swapCursor(null);

    }
}
