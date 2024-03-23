package com.example.firebasemobileapp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                LatLng ipLocation = getIpBasedLocation();
                if (ipLocation != null) {
                    map.addMarker(new MarkerOptions().position(ipLocation).title("IP Bazlı Konum"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ipLocation, 10)); // 10x yakınlaştırma
                    Toast.makeText(MainPage.this, "IP bazlı konum kullanılıyor.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainPage.this, "Konum bilgisi alınamadı.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        TextView txtCountry,txtCity;

        txtCountry=(TextView) findViewById(R.id.txtCountry);
        txtCity=(TextView)findViewById(R.id.txtCity);
        map.clear(); // Önceki imleci temizle
        map.addMarker(new MarkerOptions().position(latLng)); // Yeni imleç ekle
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String country = address.getCountryName();
                String city = address.getAdminArea(); // Şehir bilgisini alabilirsiniz, ancak bazı durumlarda null dönebilir
                // Ülke ve şehir bilgilerini TextView'lere atayabilirsiniz
                txtCountry.setText(country);
                if (city != null) {
                    txtCity.setText(city);
                } else {
                    txtCity.setText("Belirtilmemiş"); // Şehir bilgisi null ise bir mesaj gösterebilirsiniz
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Adres alınamadı.", Toast.LENGTH_SHORT).show();
        }
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
    private LatLng getIpBasedLocation() {
        // Wi-Fi bağlantısı üzerinden IP bazlı konum almak için
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0 ve üzeri sürümler için gereken izin kontrolü
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                // IP adresinden konum tahmini yapılabilir
                double latitude = ((ipAddress >> 24) & 0xFF);
                double longitude = ((ipAddress >> 16) & 0xFF);
                return new LatLng(latitude, longitude);
            } else {
                // İzin verilmemişse null döndür
                return null;
            }
        } else {
            // Android 6.0'dan önceki sürümlerde izin gerekmez, ancak bu yaklaşımın doğruluğu daha düşüktür
            // Dikkat: Bu yöntem, cihazın gerçek konumunu sağlamaz, yalnızca bir tahmin sunar
            // Bu nedenle, bu bilgiye tamamen güvenmemelisiniz
            // Daha iyi bir doğruluk için, Android 6.0 ve üstü sürümler için ACCESS_WIFI_STATE izni gereklidir
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            // IP adresinden konum tahmini yapılabilir
            double latitude = ((ipAddress >> 24) & 0xFF);
            double longitude = ((ipAddress >> 16) & 0xFF);
            return new LatLng(latitude, longitude);
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
