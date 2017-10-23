package com.example.mrbug.enucuznerede;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrbug.enucuznerede.Adapters.customShares;
import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListedShares extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    List<AppShares> returnedShares= new ArrayList<>();
    List<ParseObject> results;
    final Context ctx = this;
    final Activity acc= (Activity) this ;
    Spinner listedspinner;
    Button searchlistedButton;
    EditText textlistedSearch;
    List<String> listedcategories;
    ListView listedAllShares;
    ArrayList<AppShares> getlistedShares;
    long listedcategoryid;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        listedcategoryid = parent.getItemIdAtPosition(position);
        returnedShares.clear();
        if (listedcategoryid!=0){
        for (ParseObject object : results){
            AppShares item = new AppShares();
            item.setTitle(object.getString("title").toString());
            item.setDiscounted(object.getDouble("discounted"));
            item.setPrice(object.getDouble("price"));
            ParseGeoPoint locc = object.getParseGeoPoint("location");
            item.setLocationLatitude(locc.getLatitude());
            item.setLocationLongitude(locc.getLongitude());
            item.setUser(ParseUser.getCurrentUser().getUsername().toString());
            item.setId(object.getInt("id"));
            item.setObjectID(object.getObjectId());
            LatLng location = new LatLng(locc.getLatitude(),locc.getLongitude());
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    if (true) {

                        Log.i("PlaceInfo", listAddresses.get(0).toString());

                        String address = "";

                        if (listAddresses.get(0).getSubThoroughfare() != null) {

                            address += listAddresses.get(0).getSubThoroughfare() + " ";

                        }

                        if (listAddresses.get(0).getThoroughfare() != null) {

                            address += listAddresses.get(0).getThoroughfare() + ", ";

                        }

                        if (listAddresses.get(0).getLocality() != null) {

                            address += listAddresses.get(0).getLocality() + ", ";

                        }

                        if (listAddresses.get(0).getPostalCode() != null) {

                            address += listAddresses.get(0).getPostalCode() + ", ";

                        }

                        if (listAddresses.get(0).getCountryName() != null) {

                            address += listAddresses.get(0).getCountryName();

                        }
                        item.setLongAdress(address);
                        Toast.makeText(getApplicationContext(),address, Toast.LENGTH_SHORT);
                    }
                } catch (Exception eeee) {
                    item.setLongAdress("adres bulunamadı");
                }


                if (item.getId() == listedcategoryid) returnedShares.add(item);}


        listedAllShares.setAdapter(null);
        listedAllShares.setAdapter(new customShares(acc, returnedShares));
        Toast.makeText(getApplicationContext(), "dönen paylaşım sayısı" + returnedShares.size(), Toast.LENGTH_SHORT).show();}
        else {
            Toast.makeText(getApplicationContext(), "lütfen bir kategori seçin", Toast.LENGTH_SHORT).show();
            getAllShares();
            listedAllShares.setAdapter(null);
            listedAllShares.setAdapter(new customShares(acc, returnedShares));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_shares);
        searchlistedButton = (Button) findViewById(R.id.btnlistedSearch);
        textlistedSearch = (EditText) findViewById(R.id.txtlistedSearch);

        //region Spinneroperation
        listedspinner = (Spinner) findViewById(R.id.srchlistedCategory);
        // Spinner Drop down elements
        listedcategories = new ArrayList<String>();
        listedcategories.add("Hepsi");
        //region getCategories
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    listedcategories.add(object.getString("name").toString());
                }
            }
        });
        //endregion
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listedcategories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        listedspinner.setAdapter(dataAdapter);
        listedspinner.setOnItemSelectedListener(this);
        //endregion
        //region listViewInitialize
        listedAllShares=(ListView)findViewById(R.id.listedShares);
        getAllShares();
        try{
            //customShares myadaptor=new customShares(this, returnedShares);
            Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+returnedShares.size(),Toast.LENGTH_SHORT).show();
            listedAllShares.setAdapter(new customShares(this, returnedShares));
        }catch (Exception eee ){
            Log.e("hataa",eee.toString());
        }

        listedAllShares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // myAlertDialog((int)id);
            }
        });
        //endregion

    }
    public void getAllShares(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
        returnedShares.clear();
        try {
            results=query.find();
            for (ParseObject object : results){

                AppShares item = new AppShares();
                item.setTitle(object.getString("title").toString());
                item.setDiscounted(object.getDouble("discounted"));
                item.setPrice(object.getDouble("price"));
                ParseGeoPoint locc = object.getParseGeoPoint("location");
                item.setLocationLatitude(locc.getLatitude());
                item.setLocationLongitude(locc.getLongitude());
                item.setUser(object.getString("user").toString());
                item.setId(object.getInt("id"));
                item.setObjectID(object.getObjectId());
                LatLng location = new LatLng(locc.getLatitude(),locc.getLongitude());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    if (true) {

                        Log.i("PlaceInfo", listAddresses.get(0).toString());

                        String address = "";

                        if (listAddresses.get(0).getSubThoroughfare() != null) {

                            address += listAddresses.get(0).getSubThoroughfare() + " ";

                        }

                        if (listAddresses.get(0).getThoroughfare() != null) {

                            address += listAddresses.get(0).getThoroughfare() + ", ";

                        }

                        if (listAddresses.get(0).getLocality() != null) {

                            address += listAddresses.get(0).getLocality() + ", ";

                        }

                        if (listAddresses.get(0).getPostalCode() != null) {

                            address += listAddresses.get(0).getPostalCode() + ", ";

                        }

                        if (listAddresses.get(0).getCountryName() != null) {

                            address += listAddresses.get(0).getCountryName();

                        }
                        item.setLongAdress(address);
                        Toast.makeText(getApplicationContext(),address, Toast.LENGTH_SHORT);
                    }
                } catch (Exception eeee) {
                    item.setLongAdress("adres bulunamadı");
                }
                returnedShares.add(item);

            }


        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }


    public void SearchListedButton(View v) {

        try{
        List<AppShares> tempreturnedShares= new ArrayList<>();
        tempreturnedShares.addAll(returnedShares);
        returnedShares.clear();
        for (AppShares object : tempreturnedShares){
            AppShares item = new AppShares();
            item.setTitle(object.getTitle());
            item.setDiscounted(object.getDiscounted());
            item.setPrice(object.getPrice());
            item.setLocationLatitude(object.getLocationLatitude());
            item.setLocationLongitude(object.getLocationLongitude());
            item.setUser(object.getUser());
            item.setId(object.getId());
            item.setObjectID(object.getObjectID());
            if(item.getTitle().toString().toLowerCase().contains(textlistedSearch.getText().toString().toLowerCase())) returnedShares.add(item);
        }
            listedAllShares.setAdapter(null);
            listedAllShares.setAdapter(new customShares(acc, returnedShares));
            Toast.makeText(getApplicationContext(), "dönen paylaşım sayısı" + returnedShares.size(), Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"bir hata ile karşılaşıldı",Toast.LENGTH_SHORT).show();
        }


    }

}
