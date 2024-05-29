package com.example.instastash;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class carttable extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART_ITEMS = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CART_NAME = "name";
    public static final String COLUMN_CART_PRICE = "price";
    public static final String COLUMN_CART_IMAGE = "image";

    public carttable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCartTableQuery = "CREATE TABLE " + TABLE_CART_ITEMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_NAME + " TEXT, " +
                COLUMN_CART_IMAGE + " BLOB, " +
                COLUMN_CART_PRICE + " REAL)";
        db.execSQL(createCartTableQuery);

        Log.d("carttable", "Table created: " + TABLE_CART_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEMS);

        // Create tables again
        onCreate(db);
    }

    public long addToCart(String name, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_NAME, name);
        values.put(COLUMN_CART_PRICE, price);
        values.put(COLUMN_CART_IMAGE, image);
        long newRowId = db.insert(TABLE_CART_ITEMS, null, values);
        db.close();
        return newRowId;
    }
}
