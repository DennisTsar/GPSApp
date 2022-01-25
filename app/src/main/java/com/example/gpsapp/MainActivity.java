package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.os.SystemClock.elapsedRealtime;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private LocationManager l;
    private TextView t;
    private ArrayList<Tracker> tracker;
    private Geocoder g;
    private TextView address;
    private double d;
    private TextView distance;
    private Location loc;
    private RecyclerView recycler;
    private Spinner spinner;
    private long time;
    private MyAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this link https://developer.android.com/training/permissions/requesting.html
        l = (LocationManager)getSystemService(LOCATION_SERVICE);
        t = findViewById(R.id.a_t);
        address = findViewById(R.id.a_address);
        distance = findViewById(R.id.a_distance);
        recycler = findViewById(R.id.a_recycler);
        spinner = findViewById(R.id.a_spinner);
        String[] units = {"meters","feet","miles","kilometers","steps"};
        ArrayAdapter<String> a = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,units);
        spinner.setAdapter(a);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                display(d);
                Log.d("tag","qwert");
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        d = 0;///fdasfds
        tracker = new ArrayList<>();
        adapter = new MyAdapter(tracker,this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        g = new Geocoder(getApplicationContext(), Locale.US);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        Log.d("tag","36"+(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));
        l.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10,this);
        l.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,10,this);
    }

    public void onLocationChanged(Location location) {
        t.setText(location.getLatitude()+" "+location.getLongitude());
        if(elapsedRealtime()>5000)
            try {
                Log.d("tag","apple");
                //if(g.getFromLocation(location.getLatitude(), location.getLongitude(), 2).get(0)!=addresses.get(addresses.size()-1))
                tracker.add(new Tracker(g.getFromLocation(location.getLatitude(), location.getLongitude(), 2).get(0),elapsedRealtime()-time));

                address.setText(tracker.get(tracker.size()-1).getAddress().getAddressLine(0)+"");
                adapter.notifyDataSetChanged();
                Log.d("tag","5"+tracker.size());
                if(tracker.size()>1) {
                    d += location.distanceTo(loc);
                    display(d);
                }///
                loc = location;
                time = elapsedRealtime();
            }
            catch(IOException | IndexOutOfBoundsException e){
                Log.d("tag","hi");
                Log.d("tag","error1 "+ e);
            }
    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void display(double d){
        double z = d;
        Log.d("tag","zxcv");
        switch(spinner.getSelectedItem().toString()){
            case "meters": z = Math.round(d*100)/100.0;
                break;
            case "feet": z = Math.round(d*328.084)/100.0;
                break;
            case "miles": z = d*.000621371;
                break;
            case "kilometers": z = Math.round(d*100)/100000.0;
                break;
            case "steps": z = Math.round(d*131.2335958)/100.0;
                break;
        }
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(5);
        distance.setText(df.format(z)+"");
    }
}
class  Tracker{
    private Address address;
    private double time;
    public Tracker(Address a, double t){
        address = a;
        time = t;
    }

    public Address getAddress() {
        return address;
    }

    public double getTime() {
        return time;
    }
}

