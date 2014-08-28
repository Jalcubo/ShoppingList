package com.jdroid.shoppinglist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jdroid.shoppinglist.data.ListContract;

import java.util.Timer;
import java.util.TimerTask;

;

public class DetailList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    DetailListAdapter mDetailListAdapter;

    private ShareActionProvider mShareActionProvider;


    private static final int DETAIL_LOADER = 1;

    private final String LOG_TAG = getClass().getSimpleName();
    private int mPosition = ListView.INVALID_POSITION;

    private static String list_id = null;

    String shareList;

    private CustomProgressView mCustomProgressView;

    public static int count = 0;
    private static int list_size = 0;

    public interface Callback {

        public void onItemSelected(String date);
    }


    private static final String[] ITEM_COLUMNS = {
            ListContract.ItemEntry.TABLE_NAME + "." + ListContract.ItemEntry._ID,
            ListContract.ItemEntry.COLUMN_NAME,
            ListContract.ItemEntry.COLUMN_QUANTITY,
            ListContract.ItemEntry.COLUMN_MEASURE,
            ListContract.ItemEntry.COLUMN_LIST_KEY,
            ListContract.ItemEntry.COLUMN_CHECK

    };

    public static final int COL_ITEM_ID = 0;
    public static final int COL_ITEM_NAME = 1;
    public static final int COL_ITEM_QUANTITY = 2;
    public static final int COL_ITEM_MEASURE = 3;
    public static final int COL_ITEM_LIST_KEY = 4;
    public static final int COL_ITEM_CHECK = 5;

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

                    CheckBox check_box = (CheckBox) view.findViewById(R.id.checkbox_element);


                    ContentValues itemValues = new ContentValues();
                    itemValues.put(ListContract.ItemEntry.COLUMN_LIST_KEY, cursor.getString(COL_ITEM_LIST_KEY));
                    itemValues.put(ListContract.ItemEntry.COLUMN_NAME, cursor.getString(COL_ITEM_NAME));
                    itemValues.put(ListContract.ItemEntry.COLUMN_QUANTITY, cursor.getString(COL_ITEM_QUANTITY));
                    itemValues.put(ListContract.ItemEntry.COLUMN_MEASURE, cursor.getString(COL_ITEM_MEASURE));

                    if (!check_box.isChecked()) {
                        itemValues.put(ListContract.ItemEntry.COLUMN_CHECK, 1);
                        check_box.setChecked(true);
                        count++;

                    } else {
                        itemValues.put(ListContract.ItemEntry.COLUMN_CHECK, 0);
                        check_box.setChecked(false);
                        count--;
                    }
                    getContentResolver().update(
                            ListContract.ItemEntry.CONTENT_URI, itemValues,
                            ListContract.ItemEntry._ID + "= ?",
                            new String[]{cursor.getString(COL_ITEM_ID)});
                    Toast.makeText(getApplicationContext(), cursor.getInt(COL_ITEM_CHECK) + "", Toast.LENGTH_SHORT).show();
                    mCustomProgressView.setPercentage(getPercentage(count, list_size));
                    view.destroyDrawingCache();
                    view.setVisibility(ListView.INVISIBLE);
                    view.setVisibility(ListView.VISIBLE);




                    /*int itemDeleteUri = getContentResolver().delete(
                            ListContract.ItemEntry.CONTENT_URI,
                            ListContract.ItemEntry._ID + "= ?",
                            new String [] {cursor.getString(COL_ITEM_ID)});*/

                }

            }
        });



        mCustomProgressView = (CustomProgressView) findViewById(R.id.custom_progress_bar);
        mCustomProgressView.setPercentage(getPercentage(count,list_size));


    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }





    public void  updateData(){
        mCustomProgressView.setPercentage(getPercentage(count,list_size));
        mDetailListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_list, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareListIntent(shareList));
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

        ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar1);

        int id = item.getItemId();
        switch (id){
            case R.id.action_reset:
                spinner.setVisibility(View.VISIBLE);
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                new ResetCheckTask().execute();

                            }
                        },
                        1000
                );
                break;
            case R.id.action_delete:

                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                spinner.setVisibility(View.VISIBLE);
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                new DeleteListTask().execute();

                            }
                        },
                        1000
                );
                break;
        }

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
        list_size = data.getCount();
        count = 0;
        shareList= "   " + getIntent().getStringExtra("LIST_NAME") + "\r\n------------------ \r\n";
        for (int i = 0; i < list_size; i++){
            data.moveToPosition(i);
            String name = data.getString(COL_ITEM_NAME);
            String quantity = data.getString(COL_ITEM_QUANTITY);
            String measure = data.getString(COL_ITEM_MEASURE);
            if (data.getInt(COL_ITEM_CHECK) == 1){
                count ++;
            }
            shareList = shareList + name + " x" + quantity + " " + measure +"\r\n";

        }
        mCustomProgressView.setPercentage(getPercentage(count,list_size));



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

    public int getPercentage(int c, int total){
        if (total != 0){
            return (c*100)/total;

        }else {
            return 0;
        }


    }

    private class ResetCheckTask extends AsyncTask<Void, Void, Cursor> {
        protected void onPostExecute(Cursor c) {


            count = 0;
            mCustomProgressView.setPercentage(getPercentage(count, list_size));
            mDetailListAdapter.notifyDataSetChanged();
            ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);
            onResume();

            Toast.makeText(getApplicationContext(),"Reset!", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor c = mDetailListAdapter.getCursor();



            int c_size = c.getCount();

            for (int i = 0; i < c_size; i++){
                c.moveToPosition(i);
                ContentValues itemValues = new ContentValues();
                itemValues.put(ListContract.ItemEntry.COLUMN_LIST_KEY, c.getString(COL_ITEM_LIST_KEY));
                itemValues.put(ListContract.ItemEntry.COLUMN_NAME, c.getString(COL_ITEM_NAME));
                itemValues.put(ListContract.ItemEntry.COLUMN_QUANTITY, c.getString(COL_ITEM_QUANTITY));
                itemValues.put(ListContract.ItemEntry.COLUMN_MEASURE, c.getString(COL_ITEM_MEASURE));
                itemValues.put(ListContract.ItemEntry.COLUMN_CHECK, 0);

                getContentResolver().update(
                        ListContract.ItemEntry.CONTENT_URI, itemValues,
                        ListContract.ItemEntry._ID + "= ?",
                        new String[]{c.getString(COL_ITEM_ID)});





            }

            return c;
        }
    }

    private class DeleteListTask extends  AsyncTask<Void,Void,Integer>{


        protected void onPostExecute(Integer i) {
            Toast.makeText(getApplicationContext(),"Delete!", Toast.LENGTH_SHORT).show();
            finish();
        }


        @Override
        protected Integer doInBackground(Void... voids) {

            Cursor c = mDetailListAdapter.getCursor();
            int c_size = c.getCount();

            for (int i = 0; i < c_size; i++){
                c.moveToPosition(i);
                ContentValues itemValues = new ContentValues();
                itemValues.put(ListContract.ItemEntry.COLUMN_LIST_KEY, c.getString(COL_ITEM_LIST_KEY));
                itemValues.put(ListContract.ItemEntry.COLUMN_NAME, c.getString(COL_ITEM_NAME));
                itemValues.put(ListContract.ItemEntry.COLUMN_QUANTITY, c.getString(COL_ITEM_QUANTITY));
                itemValues.put(ListContract.ItemEntry.COLUMN_MEASURE, c.getString(COL_ITEM_MEASURE));
                itemValues.put(ListContract.ItemEntry.COLUMN_CHECK, 0);

                getContentResolver().delete(
                        ListContract.ItemEntry.CONTENT_URI,
                        ListContract.ItemEntry._ID + "= ?",
                        new String [] {c.getString(COL_ITEM_ID)});


            }



            return getContentResolver().delete(
                    ListContract.ListEntry.CONTENT_URI,
                    ListContract.ListEntry._ID + "= ?",
                    new String [] {list_id});
        }




    }
}
