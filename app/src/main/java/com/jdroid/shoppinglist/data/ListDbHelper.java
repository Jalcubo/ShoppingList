package com.jdroid.shoppinglist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jdroid.shoppinglist.data.ListContract.ItemEntry;
import com.jdroid.shoppinglist.data.ListContract.ListEntry;

/**
 * Created by JoséDaniel on 15/08/2014.
 */
public class ListDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "list.db";


    public ListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_LIST_TABLE =
                "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +

                        ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                        ListEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        ListEntry.COLUMN_DATETEXT + " TEXT NOT NULL, );";

        final String SQL_CREATE_ITEM_TABLE =
                "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +

                        ItemEntry._ID + " INTEGER PRIMARY KEY," +

                        ItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        ItemEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                        ItemEntry.COLUMN_MEASURE + "TEXT NOT NULL," +

                        " FOREIGN KEY (" + ItemEntry.COLUMN_LIST_KEY + ") REFERENCES " +
                        ListEntry.TABLE_NAME + " (" + ListEntry._ID +"));";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ListContract.ListEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ListContract.ItemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
