package com.example.instastash;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class productview extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productview);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        priceTextView = findViewById(R.id.priceTextView);

        // Get data from intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0);
        byte[] image = getIntent().getByteArrayExtra("image");

        // Set data to views
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(String.valueOf(price));
        if (image != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
    }

    /*public void addToCart(View view) {
        // Get the product details from intent extras
        Intent intent = getIntent();
        String productName = intent.getStringExtra("name");
        double productPrice = intent.getDoubleExtra("price", 0);
        byte[] productImage = intent.getByteArrayExtra("image");

        // Add the product to the cart table
        carttable cartTableHelper = new carttable(productview.this);
        long newRowId = cartTableHelper.addToCart(productName, productPrice, productImage);

        if (newRowId != -1) {
            Toast.makeText(productview.this, "Product added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(productview.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
        }
    }*/

}
