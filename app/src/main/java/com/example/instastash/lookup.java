package com.example.instastash;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class lookup extends AppCompatActivity {

    private EditText keywordEditText;
    private RecyclerView productsRecyclerView;
    private ProgressBar progressBar;
    private TextView textViewNoResults;

    private DatabaseHelper dbHelper;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);

        dbHelper = new DatabaseHelper(this);

        keywordEditText = findViewById(R.id.keyword);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        textViewNoResults = findViewById(R.id.textViewNoResults);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productAdapter);

        keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    searchProducts(keyword);
                } else {
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                    textViewNoResults.setVisibility(View.GONE);
                }
            }
        });
    }

    private void searchProducts(String keyword) {

        progressBar.setVisibility(View.VISIBLE);

        productList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_PRICE,
                DatabaseHelper.COLUMN_BARCODE,
                DatabaseHelper.COLUMN_IMAGE
        };

        String selection = DatabaseHelper.COLUMN_NAME + " LIKE ? OR " +
                DatabaseHelper.COLUMN_DESCRIPTION + " LIKE ?";
        String[] selectionArgs = { "%" + keyword + "%", "%" + keyword + "%" };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PRODUCTS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
            String barcode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BARCODE));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));

            Product product = new Product(name, description, price, barcode, image);
            product.setId(id);
            productList.add(product);
        }
        cursor.close();

        productAdapter.notifyDataSetChanged();

        // Show or hide "No results" message based on productList size
        if (productList.isEmpty()) {
            textViewNoResults.setVisibility(View.VISIBLE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }

    public void navigateToStaff(View view) {
        Intent intent = new Intent(this, staffhome.class);
        startActivity(intent);
        finish();
    }
    public void search(View view) {
        String keyword = keywordEditText.getText().toString().trim();
        searchProducts(keyword);
    }
}


/*public class lookup extends AppCompatActivity {

    private static final String TAG = "LookupActivity";

    private EditText keywordEditText;
    private RecyclerView productsRecyclerView;
    private ProgressBar progressBar;
    private TextView textViewNoResults;

    private FirebaseFirestore db;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        keywordEditText = findViewById(R.id.keyword);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        textViewNoResults = findViewById(R.id.textViewNoResults);

        // Initialize productList and set up RecyclerView
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productAdapter);

        // Add text change listener to the keyword EditText
        keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Perform search when text changes
                searchProducts(s.toString());
            }
        });
    }

    private void searchProducts(String keyword) {
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Clear previous search results
        productList.clear();
        productAdapter.notifyDataSetChanged();

        // Perform Firestore query to search for products
        db.collection("products")
                .whereEqualTo("name", keyword)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Convert each document to a Product object and add to productList
                                Product product = document.toObject(Product.class);
                                productList.add(product);
                            }
                            // Notify adapter of data change
                            productAdapter.notifyDataSetChanged();

                            // Show or hide "No results" message based on productList size
                            if (productList.isEmpty()) {
                                textViewNoResults.setVisibility(View.VISIBLE);
                            } else {
                                textViewNoResults.setVisibility(View.GONE);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }

                        // Hide progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void navigateToStaff(View view) {
        Intent intent = new Intent(this, staffhome.class);
        startActivity(intent);
        finish();
    }
}*/
