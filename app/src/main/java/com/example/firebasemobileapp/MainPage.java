package com.example.firebasemobileapp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap map;
    LatLng turkey;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        TextView txtCountry,txtCity;

        txtCountry=(TextView) findViewById(R.id.txtCountry);
        txtCity=(TextView)findViewById(R.id.txtCity);
        String country =txtCountry.getText().toString();
        String city=txtCity.getText().toString();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Konum izni zaten varsa konumu al
            getLastKnownLocation();
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapClickListener((GoogleMap.OnMapClickListener) MainPage.this); // Harita tıklama olayını dinlemek için ayarla
        map.getUiSettings().setZoomControlsEnabled(true); // Yakınlaştırma kontrollerini etkinleştir

        AlertDialog.Builder builder= new AlertDialog.Builder(MainPage.this);

        builder.setTitle("Konum").setMessage("Konum izni ver").setPositiveButton("İzin ver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Kullanıcı izin verirse, konumu al
                getLastKnownLocation();
            }
        }).setNegativeButton("Reddet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Kullanıcı reddederse, varsayılan bir konum kullan
                turkey = new LatLng(39.925533, 32.866287); // Örnek bir konum (Ankara)
                map.addMarker(new MarkerOptions().position(turkey).title("Türkiye"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 10)); // 10x yakınlaştırma
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Harita üzerinde bir konum seçildiğinde burası çalışacak
        map.clear(); // Önceki marker'ları temizle
        map.addMarker(new MarkerOptions().position(latLng)); // Yeni marker ekle
        // Diğer işlemleri buraya ekleyebilirsiniz
    }
    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Kullanıcı konumu alındığında
                                turkey = new LatLng(location.getLatitude(), location.getLongitude());
                                map.addMarker(new MarkerOptions().position(turkey).title("Sizin Buradasınız"));
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 15)); // 15x yakınlaştırma
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Konum izni verilirse
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Konum izni reddedildi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
