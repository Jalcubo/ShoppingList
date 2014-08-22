package com.jdroid.shoppinglist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


/**
 * Created by JoséDaniel on 15/08/2014.
 */
public class ListProvider extends ContentProvider{
    private final String LOG_TAG = getClass().getSimpleName();

    private static final int LIST = 100;
    private static final int ITEM = 200;
    private static final int ITEM_BY_LIST = 300;

    private ListDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ListContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ListContract.PATH_LIST, LIST);

        matcher.addURI(authority, ListContract.PATH_ITEM + "/*", ITEM_BY_LIST);
        matcher.addURI(authority, ListContract.PATH_ITEM, ITEM);




        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ListDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        Log.v(LOG_TAG, sUriMatcher.match(uri) + "");
        switch (sUriMatcher.match(uri)){
            case LIST: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ListContract.ListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ITEM_BY_LIST: {
                retCursor = getItemsByList(uri,projection,sortOrder);
                break;
            }
            case ITEM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ListContract.ItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getItemsByList(Uri uri, String[] projection, String sortOrder) {
        String selection = ListContract.ItemEntry.COLUMN_LIST_KEY + " = ? ";
        String list_id =  ListContract.ItemEntry.getListIdFromUriItem(uri);

        String[] selectionArgs = new String[] {list_id};

        return mOpenHelper.getReadableDatabase().query(
                ListContract.ItemEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case LIST:
                return ListContract.ListEntry.CONTENT_TYPE;
            case ITEM:
                return ListContract.ItemEntry.CONTENT_TYPE;
            case ITEM_BY_LIST:
                return ListContract.ItemEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch (match){
            case LIST:
                _id = db.insert(ListContract.ListEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ListContract.ListEntry.buildListUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case ITEM:
                _id = db.insert(ListContract.ItemEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ListContract.ItemEntry.buildItemUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;


        switch (match){
            case LIST:
                rowsDeleted = db.delete(ListContract.ListEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case ITEM:
                rowsDeleted = db.delete(ListContract.ItemEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;


        switch (match){
            case LIST:
                rowsUpdated = db.update(ListContract.ListEntry.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case ITEM:
                rowsUpdated = db.update(ListContract.ItemEntry.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return rowsUpdated;
    }
}
