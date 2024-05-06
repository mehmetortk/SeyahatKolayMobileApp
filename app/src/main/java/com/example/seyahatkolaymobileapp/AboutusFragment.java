package com.example.seyahatkolaymobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutusFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public AboutusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aboutus, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent); // Google Haritalar uygulamasını aç
        }
    }
}
