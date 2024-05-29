package com.example.instastash;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instastash.DatabaseHelper;
import com.example.instastash.Product;
import com.example.instastash.ProductAdapter; // Import ProductAdapter

import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> cartProducts;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load cart products
        loadCartProducts();
    }

    private void loadCartProducts() {
        // Fetch cart products from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        cartProducts = new ArrayList<>();
        Cursor cursor = databaseHelper.getReadableDatabase().query(DatabaseHelper.TABLE_CART_ITEMS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CART_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_CART_PRICE));
                // Assuming there's a constructor in the Product class that takes name, price, and image
                Product product = new Product(name, price, null);
                cartProducts.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set up RecyclerView adapter
        productAdapter = new ProductAdapter(this, cartProducts);
        cartRecyclerView.setAdapter(productAdapter);
    }*/
}
