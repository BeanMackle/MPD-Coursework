package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Model.Colours;
import Model.Description;
import Model.EarthQuake;
import Model.Location;

//Gavin Mackle S1513818
public class DetailedEarthQuake extends AppCompatActivity implements OnMapReadyCallback {


    TextView locationView;
    TextView titleView;
    TextView magnitudeView;
    TextView depthView;
    private GoogleMap mMap;

    private EarthQuake earthQuake;

    private LatLng position;

    private Boolean LastActivitySearch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_earth_quake);
        locationView = findViewById(R.id.Location);
        titleView = findViewById(R.id.Title);
        magnitudeView = findViewById(R.id.Magnitude);
        depthView = findViewById(R.id.Depth);
        setEarthQuake();
    }

    private void setEarthQuake()
    {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        GetEarthquake(intent);
        GetLastActivity(intent);

        SetTitle(earthQuake.getPublishDate().toString());
        SetLocation(earthQuake.getLocation());
        SetDescription(earthQuake.getDescription());

    }

    private void GetLastActivity(Intent intent)
    {

        LastActivitySearch = intent.getBooleanExtra("search", false);

    }

    private void GetEarthquake(Intent intent)
    {
       earthQuake = (EarthQuake) intent.getSerializableExtra("earthquake");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        AddMarkers();
        CentreCamera();
    }


    private void AddMarkers()
    {
        position = new LatLng(earthQuake.getLocation().GetLatitude(), earthQuake.getLocation().GetLongtitude());
        mMap.addMarker(new MarkerOptions().position(position).title(earthQuake.getLocation().getName())
                .icon(BitmapDescriptorFactory.defaultMarker(Colours.GetPinColour(earthQuake.getDescription().getMagnitude()))));
    }

    private void CentreCamera()
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    private void SetTitle(String title)
    {
        titleView.setText(title);
    }

    private void SetLocation(Location location)
    {
        String locationString = "Location:" + " " + location.getName() + "<br>" +
            "Latitude:" + " " + location.GetLatitude() + "<br>" +
            "Longitude:" + " "  + location.GetLongtitude();

        locationView.setText(Html.fromHtml(locationString));
    }

    private void SetDescription(Description description)
    {
        SetMagnitude(description.getMagnitude());
        SetDepth(description.getDepth());
    }

    private void SetMagnitude(double magnitude)
    {
        String magnitudeText = "Magnitude: " +  magnitude;

        magnitudeView.setText(Html.fromHtml(magnitudeText));
    }

    private void SetDepth(double depth)
    {
        String depthText = "Depth: "  + depth + "km";
        depthView.setText(Html.fromHtml(depthText));
    }

    public void Back(View view)
    {
        Intent intent;
        if(LastActivitySearch)
        {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
        }
        else
            {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}