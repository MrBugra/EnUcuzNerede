package com.example.mrbug.enucuznerede;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Share extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location myLoc = null;
    Long categoryid;
    Spinner spinner;
    Button buttonLocation,buttonShare;
    EditText textTitle,textPrice,textDiscounted;
    TextView labelTitle,labelPrice,labelDiscounted,labelLocation;
    final Context ctx=this;
    public void ObjInit(){
        buttonLocation = (Button)findViewById(R.id.btnLocation);
        buttonShare = (Button)findViewById(R.id.btnSendShare);
        textTitle = (EditText)findViewById(R.id.txtTitle);
        textPrice = (EditText)findViewById(R.id.txtPrice);
        textDiscounted = (EditText)findViewById(R.id.txtDiscounted);
        labelTitle=(TextView) findViewById(R.id.lblTitleError);
        labelDiscounted=(TextView) findViewById(R.id.lblDiscountedError);
        labelPrice=(TextView) findViewById(R.id.lblPriceError);
        labelLocation=(TextView)findViewById(R.id.lblLocation);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
            categoryid = parent.getItemIdAtPosition(position);
        Toast.makeText(getApplicationContext(), selectedItem+"  "+categoryid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ObjInit();

        //region Spinneroperation
        spinner= (Spinner) findViewById(R.id.spnCategory);
        // Spinner Drop down elements
        final List<String> categories = new ArrayList<String>();
        categories.add("Kategori Seçin");
        //region getCategories
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects){
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
    }

    public void sendShareClick(View v){
        String title,user;Double price,discounted;Boolean error=false;

        if(textTitle.getText().toString().matches("")) {labelTitle.setText("hata");labelTitle.setTextColor(Color.RED);error=true;}
        if(textPrice.getText().length()<1) {labelPrice.setText("hata");labelPrice.setTextColor(Color.RED);error=true;}
        if(textDiscounted.getText().length()<1) {labelDiscounted.setText("hata");labelDiscounted.setTextColor(Color.RED);error=true;}
        try {
            ParseGeoPoint parsegeo = new ParseGeoPoint(myLoc.getLatitude(), myLoc.getLongitude());
        }
        catch (Exception e){
            error=true;
        }
        if(!error) myAlertDialog(textTitle.getText().toString(),ParseUser.getCurrentUser().getUsername().toString(),
                Double.parseDouble(textPrice.getText().toString()),
                Double.parseDouble(textDiscounted.getText().toString()),
                error);
        else Toast.makeText(getApplicationContext(),"değerler boş yada tanımsız olamaz", Toast.LENGTH_SHORT).show();
        //region Garbage
      /*  if(!error){
        ParseObject Shares = new ParseObject("Shares");
        Shares.put("title",(textTitle.getText().toString()));
        Shares.put("price",(Double.parseDouble(textPrice.getText().toString())));
        Shares.put("discounted",(Double.parseDouble(textDiscounted.getText().toString())));
            try{
                Shares.put("user", ParseUser.getCurrentUser().getUsername().toString());
            }catch (Exception ue){
                Shares.put("user","tanimsiz");
            }
        //Shares.put("user", ParseUser.getCurrentUser().getUsername().toString());
        ParseGeoPoint parsegeo = new ParseGeoPoint(myLoc.getLatitude(), myLoc.getLongitude());
        Shares.put("location",parsegeo);
        Shares.put("id",categoryid);
        Shares.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) Toast.makeText(getApplicationContext(),"başarılı", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(),"başarısız  "+e.toString(), Toast.LENGTH_LONG).show();
            }
        });
        }
        else {
            Toast.makeText(getApplicationContext(),"işlem başarısız girilen değerleri kontrol edin",Toast.LENGTH_LONG);
        }*/


        /*Toast.makeText(getApplicationContext(),
        textTitle.getText().toString()+
        textPrice.getText().toString()+
        textDiscounted.getText().toString(), Toast.LENGTH_LONG).show();*/
       /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
      /*  Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);*/
      //endregion
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }






    //region LocationOperations
    public  void CheckLocation(View v){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (Build.VERSION.SDK_INT < 23) {
            //konumçek
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
                labelLocation.setText("konumunuz Longitude="+myLoc.getLongitude()+" Latitude="+myLoc.getLatitude());
            }
            catch (Exception e){
                labelLocation.setText("bir kaç saniye sonra deneyin");
            }

        } else {
            //yukardaki if olmadan konum istenemez derleyici hata veriyor
            //konum servisleri kullanılsınmı diye sor
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //else we have permission
            else {
                //konumçek
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
                    labelLocation.setText("Longitude="+myLoc.getLongitude()+"\nLatitude="+myLoc.getLatitude());
                }
                catch (Exception e){
                    labelLocation.setText("bir kaç saniye sonra deneyin");
                }
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                //konum çek
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
                    labelLocation.setText("konumunuz Longitude="+myLoc.getLongitude()+" Latitude="+myLoc.getLatitude());
                }
                catch (Exception e){
                    labelLocation.setText("bir kaç saniye\n sonra deneyin");
                }
            }
        }
    }
    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location loc)
        {
            myLoc = loc;
        }
        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Kapatıldı", Toast.LENGTH_SHORT ).show();
        }
        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Açıldı", Toast.LENGTH_SHORT).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras)
        {}
    }

    //endregion

    public boolean myAlertDialog(String title,String user,Double price,Double discounted,Boolean e){
        final Boolean error = e;
             new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setIcon(R.drawable.mapmini)
                .setMessage(price+" iken "+discounted+" oldu")
                .setPositiveButton("paylaş", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!error){
                            ParseObject Shares = new ParseObject("Shares");
                            Shares.put("title",(textTitle.getText().toString()));
                            Shares.put("price",(Double.parseDouble(textPrice.getText().toString())));
                            Shares.put("discounted",(Double.parseDouble(textDiscounted.getText().toString())));
                            try{
                                Shares.put("user", ParseUser.getCurrentUser().getUsername().toString());
                            }catch (Exception ue){
                                Shares.put("user","tanimsiz");
                            }
                            //Shares.put("user", ParseUser.getCurrentUser().getUsername().toString());
                            ParseGeoPoint parsegeo = new ParseGeoPoint(myLoc.getLatitude(), myLoc.getLongitude());
                            Shares.put("location",parsegeo);
                            Shares.put("id",categoryid);
                            Shares.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null) Toast.makeText(getApplicationContext(),"başarılı", Toast.LENGTH_LONG).show();
                                    else Toast.makeText(getApplicationContext(),"başarısız  "+e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"işlem başarısız girilen değerleri kontrol edin",Toast.LENGTH_LONG);
                        }

                    }
                })
                .setNegativeButton("vazgeçtim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"paylaşılmadı",Toast.LENGTH_SHORT).show();

                    }
                })
                .show();
            return error;
    };
}



     /*   ParseObject catogories = new ParseObject("Categories");
        catogories.put("id", 2);
        catogories.put("name", "technology");

        catogories.saveInBackground();*/