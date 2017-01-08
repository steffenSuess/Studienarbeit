package com.example.steffensuess.price48.DatabaseHandling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.steffensuess.price48.Models.SearchQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steffensuess on 07.01.17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "searchQueriesManager";

    // Contacts table name
    private static final String TABLE_SEARCHQUERIES = "searchqueries";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SHOP_NAME = "shop";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUERIES_TABLE = "CREATE TABLE " + TABLE_SEARCHQUERIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT," + KEY_TEXT + " TEXT," + KEY_DATE + " TEXT,"
                + KEY_PRICE + " TEXT," + KEY_SHOP_NAME + " TEXT" + ")";
        db.execSQL(CREATE_QUERIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHQUERIES);

        // Create tables again
        onCreate(db);
    }


    // Adding new Query
    public void addQuery(SearchQuery query) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, query.getProductName());
        values.put(KEY_IMAGE, query.getImageURL());
        values.put(KEY_TEXT, query.getSearchText());
        values.put(KEY_DATE, query.getDate());
        values.put(KEY_PRICE, query.getPrice());
        values.put(KEY_SHOP_NAME, query.getShopName());

        // Inserting Row
        db.insert(TABLE_SEARCHQUERIES, null, values);
    }

    // Getting single Query
    public SearchQuery getQuery(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SEARCHQUERIES, new String[] { KEY_ID,
                        KEY_NAME, KEY_IMAGE, KEY_TEXT, KEY_DATE, KEY_PRICE, KEY_SHOP_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setId(Integer.parseInt(cursor.getString(0)));
        searchQuery.setProductName(cursor.getString(1));
        searchQuery.setImageURL(cursor.getString(2));
        searchQuery.setSearchText(cursor.getString(3));
        searchQuery.setDate(cursor.getString(4));
        searchQuery.setPrice(cursor.getString(5));
        searchQuery.setShopName(cursor.getString(6));
        // return contact
        return searchQuery;
    }

    // Getting All Query
    public List<SearchQuery> getAllQueries() {
        List<SearchQuery> searchQueries = new ArrayList<SearchQuery>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCHQUERIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SearchQuery searchQuery = new SearchQuery();
                searchQuery.setId(Integer.parseInt(cursor.getString(0)));
                searchQuery.setProductName(cursor.getString(1));
                searchQuery.setImageURL(cursor.getString(2));
                searchQuery.setSearchText(cursor.getString(3));
                searchQuery.setDate(cursor.getString(4));
                searchQuery.setPrice(cursor.getString(5));
                searchQuery.setShopName(cursor.getString(6));
                // Adding contact to list
                searchQueries.add(searchQuery);
            } while (cursor.moveToNext());
        }

        // return contact list
        return searchQueries;
    }

    // Getting Query Count
    public int getQueriesCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_SEARCHQUERIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        // return count
        return count;
    }
    // Updating single Query
    public int updateQuery(SearchQuery query) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, query.getProductName());
        values.put(KEY_IMAGE, query.getImageURL());
        values.put(KEY_TEXT, query.getSearchText());
        values.put(KEY_DATE, query.getDate());
        values.put(KEY_PRICE, query.getPrice());
        values.put(KEY_SHOP_NAME, query.getShopName());

        // updating row
        return db.update(TABLE_SEARCHQUERIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(query.getId()) });
    }

    // Deleting single Query
    public void deleteQuery(SearchQuery query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEARCHQUERIES, KEY_ID + " = ?",
                new String[] { String.valueOf(query.getId()) });
    }
}
