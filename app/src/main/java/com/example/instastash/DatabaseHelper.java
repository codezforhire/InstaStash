package com.example.instastash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_IMAGE = "image";

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID  = "id";
    public static final String COLUMN_CLIENT_NAME = "name";
    public static final String COLUMN_CLIENT_EMAIL = "email";
    public static final String COLUMN_ORDER_DETAILS = "details";
    public static final String COLUMN_ORDER_TOTAL = "total";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private void createOrdersTable(SQLiteDatabase db) {
        String createOrdersTableQuery = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLIENT_NAME + " TEXT, " +
                COLUMN_CLIENT_EMAIL + " TEXT, " +
                COLUMN_ORDER_DETAILS + " TEXT, " +
                COLUMN_ORDER_TOTAL + " REAL)";
        db.execSQL(createOrdersTableQuery);

    }


    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_BARCODE + " TEXT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTableQuery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public long addProduct(String name, String description, double price, String barcode, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BARCODE, barcode);
        values.put(COLUMN_IMAGE, image);
        long newRowId = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return newRowId;
    }

    public void deleteProduct(long productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public void updateProduct(long productId, String name, String description, double price, String barcode, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BARCODE, barcode);
        values.put(COLUMN_IMAGE, image);
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
        return cursor;
    }
}
