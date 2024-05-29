package com.example.instastash;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class deleteitem extends AppCompatActivity {
    private EditText idEditText;
    private EditText nameEditText;
    private RecyclerView productRecyclerView;
    private Button confirmDeleteButton;

    private DatabaseHelper dbHelper;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteitem);

        dbHelper = new DatabaseHelper(this);

        idEditText = findViewById(R.id.id);
        nameEditText = findViewById(R.id.name);
        productRecyclerView = findViewById(R.id.product_list);
        confirmDeleteButton = findViewById(R.id.confirm_delete_btn);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                selectedProduct = product;
                confirmDeleteButton.setVisibility(View.VISIBLE);
                Toast.makeText(deleteitem.this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);
    }

    public void deleteProductById(View view) {
        String idInput = idEditText.getText().toString().trim();
        if (TextUtils.isEmpty(idInput)) {
            Toast.makeText(this, "Please enter a product ID", Toast.LENGTH_SHORT).show();
            return;
        }

        long productId;
        try {
            productId = Long.parseLong(idInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show();
            return;
        }

        showDeleteConfirmationDialog(productId);
    }

    public void searchProductByName(View view) {
        String nameInput = nameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nameInput)) {
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
            return;
        }

        searchByName(nameInput);
    }

    private void showDeleteConfirmationDialog(final long productId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProductByIdConfirmed(productId);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteProductByIdConfirmed(long productId) {
        dbHelper.deleteProduct(productId);
        Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        selectedProduct = null;
        productList.clear();
        productAdapter.notifyDataSetChanged();
        confirmDeleteButton.setVisibility(View.GONE);
    }

    private void searchByName(String nameInput) {
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

        String selection = DatabaseHelper.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { "%" + nameInput + "%" };

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

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmDeleteProduct(View view) {
        if (selectedProduct != null) {
            showDeleteConfirmationDialog(selectedProduct.getId());
        } else {
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToman(View view) {
        Intent intent = new Intent(this, manageproducts.class);
        startActivity(intent);
        finish();
    }
}
