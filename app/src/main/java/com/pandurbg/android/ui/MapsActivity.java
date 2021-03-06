package com.pandurbg.android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pandurbg.android.R;
import com.pandurbg.android.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Post> mPosts;
    List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPosts = getIntent().getParcelableArrayListExtra("data");
        markers = new ArrayList<>(mPosts.size());
    }

    public static void startActivity(Context context, ArrayList<Post> posts) {
        Intent i = new Intent(context, MapsActivity.class);
        i.putParcelableArrayListExtra("data", posts);
        context.startActivity(i);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showMapMarkers();
    }

    private void showMapMarkers() {
        mMap.clear();

        if (mPosts.size() == 0) {
            return;
        }

        for (Post post :
                mPosts) {
            LatLng latLng = new LatLng(post.getLocation().getLatitude(), post.getLocation().getLongitude());
            MarkerOptions mo = new MarkerOptions();
            mo.position(latLng);
            Marker m = mMap.addMarker(mo);
            markers.add(m);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        int width = 1080;
        int height = 1920;
        int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.moveCamera(cu);
    }

}
