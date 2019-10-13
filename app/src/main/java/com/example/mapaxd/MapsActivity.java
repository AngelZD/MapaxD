package com.example.mapaxd;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.Stack;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    //private GoogleMap mMap;
    private Stack<String> datos;
    private LocationManager locationManager = null;
    private LatLng currentLocation = new LatLng(19.432608, -99.133208);
    private boolean moveCameraCurrentLocation = true;
    private static final int DEFAULT_ZOOM_LEVEL = 19;
    private int timeUpdateLocation = 2000;
    private float distanceUpateLocation = (float) 0.05;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        datos = new Stack<String>();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marcador en Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        this.initLocationService();
    }
    @Override
    public void onLocationChanged(Location location) {
        // This method is called when the location changes.
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = new LatLng(latitude, longitude);
        Log.d("onLocationChanged", "Latitud:" + latitude + " Longitud:" + longitude);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Posición Actual")
                .icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360))));
        if (moveCameraCurrentLocation) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)
                    .zoom(DEFAULT_ZOOM_LEVEL)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void ChangeToD1(View view){
        Button boton = (Button) view;
        String texto = boton.getText().toString();
        String token="";
        elegirUb(texto);
        //double latitude = 19.4352;
        //double longitude = -99.1412;
        token = datos.pop();
        double longitude = Double.parseDouble(token);
        token = datos.pop();
        double latitude = Double.parseDouble(token);
        currentLocation = new LatLng(latitude, longitude);
        token = datos.pop();
        Log.d("onLocationChanged", "Latitud:" + latitude + " Longitud:" + longitude);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title(token)
                .icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360))));
        if (moveCameraCurrentLocation) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)
                    .zoom(DEFAULT_ZOOM_LEVEL)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void elegirUb(String texto){
        if(texto.compareTo("Bellas Artes")==0){
            datos.push("Palacio de Bellas Artes");
            datos.push(19.4352+"");
            datos.push(-99.1412+"");
        }
        else if (texto.compareTo("Zocalo CDMX")==0){
            datos.push("Zócalo de la Ciudad de México");
            datos.push(19.432778+"");
            datos.push(-99.133333+"");
        }
        else if (texto.compareTo("Templo Mayor")==0){
            datos.push("Templo Mayor en la Ciudad de México");
            datos.push(19.43500+"");
            datos.push(-99.13139+"");
        }
        else if (texto.compareTo("Kiosco Morisco")==0){
            datos.push("Kiosco Morisco en la Ciudad de México");
            datos.push(19.44967+"");
            datos.push(-99.156853+"");
        }
        else if (texto.compareTo("Plaza Garibaldi")==0){
            datos.push("Plaza Garibaldi en la Ciudad de México");
            datos.push(19.44056+"");
            datos.push(-99.13889+"");
        }
        else if (texto.compareTo("Monumento Revolucion")==0){
            datos.push("Monumento a la Revolucion");
            datos.push(19.43639+"");
            datos.push(-99.15389+"");
        }
        else if (texto.compareTo("Monumento Independencia")==0){
            datos.push("Ángel de la Independencia");
            datos.push(19.427+"");
            datos.push(-99.16771+"");
        }
        else if (texto.compareTo("Castillo Chapultepec")==0){
            datos.push("Castillo de Chapultepec en la Ciudad de México");
            datos.push(19.420556+"");
            datos.push(-99.181667+"");
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void initLocationService() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("initLocationService", "Registrando Servicio....");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    timeUpdateLocation, distanceUpateLocation, this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    timeUpdateLocation, distanceUpateLocation, this);
        }
    }

}
