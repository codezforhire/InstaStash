package com.example.instastash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class manageproducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manageproducts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void navtodelete(View view) {
        Intent intent = new Intent(this, deleteitem.class);
        startActivity(intent);
        finish();
    }

    public void navtomodify(View view) {
        Intent intent = new Intent(this, modifyitem.class);
        startActivity(intent);
        finish();
    }

    public void navtoadd(View view) {
            Intent intent = new Intent(this, additem.class);
        startActivity(intent);
        finish();
    }

    public void navigateToAdmin(View view) {
        Intent intent = new Intent(this, adminhome.class);
        startActivity(intent);
        finish();
    }
}