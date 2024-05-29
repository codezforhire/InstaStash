package com.example.instastash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class additem extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, barcodeEditText;
    private ImageView imageView;
    private Uri selectedImageUri;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        nameEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        barcodeEditText = findViewById(R.id.barcode);
        imageView = findViewById(R.id.imageView4);

        dbHelper = new DatabaseHelper(this);

        Button addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (data != null) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void addProduct() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String barcode = barcodeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price) || TextUtils.isEmpty(barcode)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert selected image URI to byte array
        byte[] imageData = null;
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                imageData = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long result = dbHelper.addProduct(name, description, Double.parseDouble(price), barcode, imageData);
        if (result != -1) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        nameEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        barcodeEditText.setText("");
        imageView.setImageURI(null);
        selectedImageUri = null;
    }

    public void navigateToman(View view) {
        Intent intent = new Intent(this, manageproducts.class);
        startActivity(intent);
        finish();
    }
}



/*public class additem extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, barcodeEditText;
    private ImageView imageView;
    private Uri selectedImageUri;

    private FirebaseFirestore db;
    private CollectionReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        nameEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        barcodeEditText = findViewById(R.id.barcode);
        imageView = findViewById(R.id.imageView4);

        db = FirebaseFirestore.getInstance();
        productsRef = db.collection("products");

        Button addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (data != null) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void addProduct() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String barcode = barcodeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price) || TextUtils.isEmpty(barcode)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        product.put("barcode", barcode);
        // Add selected image URI to product details
        if (selectedImageUri != null) {
            product.put("imageUri", selectedImageUri.toString());
        }

        productsRef.add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(additem.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(additem.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearFields() {
        nameEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        barcodeEditText.setText("");
        imageView.setImageURI(null);
        selectedImageUri = null;
    }

    public void navigateToAdmin(View view) {
        Intent intent = new Intent(this, adminhome.class);
        startActivity(intent);
        finish();
    }
}
*/