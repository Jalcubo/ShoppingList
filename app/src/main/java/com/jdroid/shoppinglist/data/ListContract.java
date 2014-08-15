package com.jdroid.shoppinglist.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JoséDaniel on 15/08/2014.
 */
public class ListContract {

    public static final String CONTENT_AUTHORITY = "com.jdroid.shoppinglist";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_LIST = "list";
    public static final String PATH_ITEM = "item";

    public static final String DATE_FORMAT = "yyyyMMdd";


    public static final class ListEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LIST;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LIST;

        public static final String TABLE_NAME = "list";

        public static final String COLUMN_NAME = "list_name";

        public static final String COLUMN_DATETEXT = "date";

        public  static Uri buildListUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }




    }

    public static final class ItemEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        public static final String TABLE_NAME = "item";

        public static final String COLUMN_NAME = "item_name";

        public static final String COLUMN_QUANTITY = "quantity";

        public static final String COLUMN_MEASURE = "measure";

        public static final String COLUMN_LIST_KEY = "list_key";


        public  static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }






    }

}
