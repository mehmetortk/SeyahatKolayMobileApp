package com.example.seyahatkolaymobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutUs extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng ankaraAnitkabir = new LatLng(39.925533, 32.836944); // Ankara Anıtkabir koordinatları
        googleMap.addMarker(new MarkerOptions().position(ankaraAnitkabir).title("Ankara Anıtkabir"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankaraAnitkabir, 15)); // Zoom seviyesi: 15 (Genişletilmiş görüntü)
        googleMap.setOnMapClickListener(this); // Haritaya tıklama olayını dinle
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Tıklanan konumu al
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        // Google Haritalar uygulamasını açmak için adres bilgisini oluştur
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=Ankara Anıtkabir");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Google Haritalar uygulamasını zorunlu kıl
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent); // Google Haritalar uygulamasını aç
        }
    }
}
