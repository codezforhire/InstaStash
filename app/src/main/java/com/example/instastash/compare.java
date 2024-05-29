package com.example.instastash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class compare extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 100;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private String scannedCode1;
    private String scannedCode2;
    private TextView productInfo1;
    private TextView productInfo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        productInfo1 = findViewById(R.id.productInfo1);
        productInfo2 = findViewById(R.id.productInfo2);

        // Check for camera permission
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        // Set up barcode detector
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        // Set up camera source
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        // Start scanning for the first barcode
        startScanning();
    }

    private void startScanning() {
        // Associate camera with SurfaceView
        // Add your code for camera surface view setup...

        // Set up barcode detector processor
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    // Handle barcode / QR code detection
                    String scannedCode = barcodes.valueAt(0).displayValue;
                    if (scannedCode1 == null) {
                        scannedCode1 = scannedCode;
                        // Start scanning for the second barcode
                        // Additional code for starting scanning...
                    } else if (scannedCode2 == null) {
                        scannedCode2 = scannedCode;
                        // Both barcodes are scanned, now compare
                        compareProducts(scannedCode1, scannedCode2);
                    }
                }
            }
        });
    }

    private void compareProducts(String scannedCode1, String scannedCode2) {
        // Logic to compare products using the scanned codes
        // Retrieve product data from database based on the scanned codes
        // Example:
        String productInfoText1 = "Product 1 Information"; // Replace with actual product information
        String productInfoText2 = "Product 2 Information"; // Replace with actual product information
        productInfo1.setText(productInfoText1);
        productInfo2.setText(productInfoText2);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
