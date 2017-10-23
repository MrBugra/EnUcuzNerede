package com.example.mrbug.enucuznerede;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Search extends FragmentActivity implements OnMapReadyCallback , AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Button seachButton;
    EditText textSearch;
    List<String> categories;
    ArrayList<AppShares> getShares;
    private GoogleMap mMap;
    long categoryid;
    LatLng corlu= new LatLng(41.1476594,27.8263061);
    List<Marker> markers= new ArrayList<Marker>();
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selectedItem = parent.getItemAtPosition(position).toString();
        categoryid = parent.getItemIdAtPosition(position);
        if (selectedItem.matches("Hepsi"))
            Toast.makeText(getApplicationContext(), "Kategori seçin", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), selectedItem + "  " + categoryid, Toast.LENGTH_SHORT).show();

        //region AllMap

        if (categoryid != 0) {
            markers.clear();
            mMap.clear();
            getShares = new ArrayList<AppShares>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
            query.whereEqualTo("id", categoryid);
            //query.whereContains("title","usta");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {


                    for (ParseObject object : objects) {
                        AppShares item = new AppShares();
                        item.setTitle(object.get("title").toString());
                        item.setDiscounted(object.getDouble("discounted"));
                        item.setPrice(object.getDouble("price"));
                        ParseGeoPoint locc = object.getParseGeoPoint("location");
                        item.setLocationLatitude(locc.getLatitude());
                        item.setLocationLongitude(locc.getLongitude());
                        getShares.add(item);
                        LatLng loccc = new LatLng(item.getLocationLatitude(), item.getLocationLongitude());
                        try {

                         Marker marker =   mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(whichPng(object.getInt("id"))))
                                    .position(loccc)
                                    .title(item.getTitle())
                                    .snippet("ilk fiyat =" + item.getPrice() + "  iken indirimli fiyatı" + item.getDiscounted())

                            );
                            markers.add(marker);
                        } catch (Exception ee) {


                            Toast.makeText(getApplicationContext(), ee.toString(), Toast.LENGTH_SHORT).show();

                        }


                    }

                    Toast.makeText(getApplicationContext(), "dönen paylaşım sayısı" + markers.size(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        seachButton = (Button) findViewById(R.id.btnSearch);
        textSearch = (EditText) findViewById(R.id.txtSearch);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //region Spinneroperation
        spinner = (Spinner) findViewById(R.id.srchCategory);
        // Spinner Drop down elements
        categories = new ArrayList<String>();
        categories.add("Hepsi");
        //region getCategories
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    categories.add(object.getString("name").toString());
                }
            }
        });
        //endregion
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        //endregion


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(corlu));
    }

    public Integer whichPng(Integer categoryid) {
        if (categoryid == 1) return R.drawable.market;
        if (categoryid == 2) return R.drawable.yiyecek;
        if (categoryid == 3) return R.drawable.icecek;
        if (categoryid == 4) return R.drawable.giyim;
        if (categoryid == 5) return R.drawable.teknoloji;
        if (categoryid == 6) return R.drawable.konaklama;
        if (categoryid == 7) return R.drawable.ulasim;
        else return R.drawable.market;
    };
    public Integer whichPng(Long categoryid) {
        if (categoryid == 1) return R.drawable.market;
        if (categoryid == 2) return R.drawable.yiyecek;
        if (categoryid == 3) return R.drawable.icecek;
        if (categoryid == 4) return R.drawable.giyim;
        if (categoryid == 5) return R.drawable.teknoloji;
        if (categoryid == 6) return R.drawable.konaklama;
        if (categoryid == 7) return R.drawable.ulasim;
        else return R.drawable.market;
    };

    public void SearchButton(View v) {

      /*  if (categoryid != 0) {
            mMap.clear();
            getShares = new ArrayList<AppShares>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
            query.whereEqualTo("id", categoryid);
            query.whereContains("title", textSearch.getText().toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {


                    for (ParseObject object : objects) {
                        AppShares item = new AppShares();
                        item.setTitle(object.get("title").toString());
                        item.setDiscounted(object.getDouble("discounted"));
                        item.setPrice(object.getDouble("price"));
                        ParseGeoPoint locc = object.getParseGeoPoint("location");
                        item.setLocationLatitude(locc.getLatitude());
                        item.setLocationLongitude(locc.getLongitude());
                        getShares.add(item);
                        LatLng loccc = new LatLng(item.getLocationLatitude(), item.getLocationLongitude());
                        try {

                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(whichPng(object.getInt("id"))))
                                    .position(loccc)
                                    .title(item.getTitle())
                                    .snippet("ilk fiyat =" + item.getPrice() + "  iken indirimli fiyatı" + item.getDiscounted())

                            );

                        } catch (Exception ee) {


                            Toast.makeText(getApplicationContext(), ee.toString(), Toast.LENGTH_SHORT).show();

                        }


                    }

                    Toast.makeText(getApplicationContext(), "dönen paylaşım sayısı" + getShares.size(), Toast.LENGTH_SHORT).show();
                }
            });*/
      mMap.clear();
        Integer count=0;
        for(Marker m : markers) {
            if(m.getTitle().toString().toLowerCase().contains(textSearch.getText().toString().toLowerCase())) {
               mMap.addMarker(new MarkerOptions().title(m.getTitle().toString())
                       .snippet(m.getSnippet().toString())
                       .position(m.getPosition())
                       .icon(BitmapDescriptorFactory.fromResource(whichPng(categoryid)))
               );count++;
            }
        }
        Toast.makeText(getApplicationContext(), "dönen paylaşım sayısı" + count, Toast.LENGTH_SHORT).show();








        }
    }

