package com.example.mrbug.enucuznerede;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.example.mrbug.enucuznerede.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<AppShares> getShares ;
    LatLng corlu= new LatLng(41.1476594,27.8263061);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
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
      //  if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
         //   mMap.setMyLocationEnabled(true);

        //region AllMap
        if(getIntent().getStringExtra("mode").matches("all")){
        getShares = new ArrayList<AppShares>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                for (ParseObject object : objects){
                    AppShares item = new AppShares();
                    item.setTitle(object.get("title").toString());
                    item.setDiscounted(object.getDouble("discounted"));
                    item.setPrice(object.getDouble("price"));
                    ParseGeoPoint locc = object.getParseGeoPoint("location");
                    item.setLocationLatitude(locc.getLatitude());
                    item.setLocationLongitude(locc.getLongitude());
                    getShares.add(item);
                    LatLng loccc= new LatLng(item.getLocationLatitude(),item.getLocationLongitude());
                    try{

                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(whichPng(object.getInt("id"))))
                                .position(loccc)
                                .title(item.getTitle())
                                .snippet("ilk fiyat ="+item.getPrice()+"  iken indirimli fiyatı"+item.getDiscounted())

                        );

                    }catch (Exception ee){



                        Toast.makeText(getApplicationContext(),ee.toString(),Toast.LENGTH_SHORT).show();

                    }


                }

               // Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+getShares.size(),Toast.LENGTH_SHORT).show();
            }
        });
        }
          /* LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title(""+getShares.size()));*/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(corlu));
            //endregion


    }

    public Integer whichPng(Integer categoryid){
        if(categoryid==1) return R.drawable.market;
        if(categoryid==2) return R.drawable.yiyecek;
        if(categoryid==3) return R.drawable.icecek;
        if(categoryid==4) return R.drawable.giyim;
        if(categoryid==5) return R.drawable.teknoloji;
        if(categoryid==6) return R.drawable.konaklama;
        if(categoryid==7) return R.drawable.ulasim;
        else return R.drawable.market;
    };
}
