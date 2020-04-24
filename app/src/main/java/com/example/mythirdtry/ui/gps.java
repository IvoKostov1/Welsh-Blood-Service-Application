package com.example.mythirdtry.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mythirdtry.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class gps extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //Setting up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Brecon
        LatLng BreconHospital = new LatLng(51.948770, -3.385660);
        LatLng BreconWarHospital = new LatLng(51.9491, -3.3848 );
        LatLng PowysHealthCare = new LatLng( 51.9449, -3.3822  );
        LatLng BreconSurgery = new LatLng(51.9472,  -3.3956);
        //Cardiff
        LatLng ParkInnCardiff = new LatLng(51.478830, -3.172700);
        LatLng CityHall = new LatLng(51.4736197722, -3.17621429514);
        LatLng UniHospital = new LatLng(51.50680, -3.19083);
        LatLng LlanishenLeisureCenter = new LatLng(51.52949, -3.19360);

        //Brecon markers
        map.addMarker(new MarkerOptions().position(BreconHospital).title("Brecon Hospital"));
        map.addMarker(new MarkerOptions().position(PowysHealthCare).title("Powys Health Care"));
        map.addMarker(new MarkerOptions().position(BreconWarHospital).title("Brecon War Hospital"));
        map.addMarker(new MarkerOptions().position(BreconSurgery).title("Brecon Surgery"));
        //Cardiff markers
        map.addMarker(new MarkerOptions().position(ParkInnCardiff).title("Park inn"));
        map.addMarker(new MarkerOptions().position(CityHall).title("City Hall"));
        map.addMarker(new MarkerOptions().position(UniHospital).title("University Hospital of Wales"));
        map.addMarker(new MarkerOptions().position(LlanishenLeisureCenter).title("Llanishen Leisure Center"));

        //Fixing the camera position when the activity is started
        CameraPosition position = new CameraPosition.Builder().target(ParkInnCardiff)
                .zoom(9).build();

        //Zooming in slightly
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
