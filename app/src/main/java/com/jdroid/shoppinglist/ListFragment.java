package com.jdroid.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jdroid.shoppinglist.data.ListContract.ListEntry;

/**
 * Created by JoséDaniel on 15/08/2014.
 */

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LOADER = 0;
    private ListAdapter mListAdapter;

    private final String LOG_TAG = getClass().getSimpleName();
    private int mPosition = ListView.INVALID_POSITION;


    private static final String[] LIST_COLUMNS = {
            ListEntry.TABLE_NAME + "." + ListEntry._ID,
            ListEntry.COLUMN_NAME,
            ListEntry.COLUMN_DATETEXT

    };

    public static final int COL_LIST_ID = 0;
    public static final int COL_LIST_NAME = 1;
    public static final int COL_LIST_DATE = 2;


    public ListFragment() {
    }

    public interface Callback {

        public void onItemSelected(String date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mListAdapter = new ListAdapter(getActivity(), null, 0);


        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_list);
        listView.setEmptyView( rootView.findViewById(R.id.empty_list_view) );
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {

                    Intent i = new Intent(getActivity(), DetailList.class);
                    i.putExtra("LIST_ID",cursor.getString(COL_LIST_ID));
                    i.putExtra("LIST_NAME",cursor.getString(COL_LIST_NAME));
                    startActivity(i);

                    onResume();
                }
            }
        });




        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LIST_LOADER, null, this);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri listUri = ListEntry.ListUri();
        Log.v(LOG_TAG, listUri.toString());
        return new CursorLoader(
                getActivity(),
                listUri,
                LIST_COLUMNS,
                null,
                null,
                ListEntry.COLUMN_DATETEXT + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
    }
}
