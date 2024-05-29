package com.example.instastash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // 2 columns in the grid

        productList = new ArrayList<>();
        loadProductsFromDatabase();

        productAdapter = new ProductAdapter(this, productList, this); // Pass 'this' as the listener
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProductsFromDatabase() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BARCODE));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
                Product product = new Product(id, name, description, price, barcode, image);
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onItemClick(Product product) {
        // Handle item click here
        // For example, you can open a new activity to show detailed information about the clicked product
        Intent intent = new Intent(this, productview.class);
        intent.putExtra("name", product.getName());
        intent.putExtra("description", product.getDescription());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("image", product.getImage());
        startActivity(intent);
    }

    public void navtocart (View view){
        Intent intent = new Intent(this, cart.class);
        startActivity(intent);
        finish();
    }
}
