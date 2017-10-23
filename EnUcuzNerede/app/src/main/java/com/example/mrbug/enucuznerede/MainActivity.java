package com.example.mrbug.enucuznerede;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    public Button buttonShare;
    public Button buttonSearch;
    public Button buttonShowmap;
    public  Button buttonAccount;
    ArrayList<AppShares> getShares ;
    public void ObjInit(){
        buttonShare=(Button)findViewById(R.id.btnShare);
        buttonSearch=(Button) findViewById(R.id.btnSearch);
        buttonShowmap=(Button) findViewById(R.id.btnShowmap);
        buttonAccount = (Button)findViewById(R.id.btnAccount);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjInit();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void ShareClick(View v){
        Intent i = new Intent(getApplicationContext(),Share.class);
        startActivity(i);

    }
    public void SearchClick(View v){
        Intent i = new Intent(getApplicationContext(),Search.class);
        i.putExtra("mode","all");
        startActivity(i);
    }

    public void ShowClick(View v){

        /*
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
                }


                Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+getShares.size(),Toast.LENGTH_SHORT).show();
            }
        });*/

        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        i.putExtra("mode","all");
        startActivity(i);
    }
    public void AccountClick(View v){

        if(ParseUser.getCurrentUser()!=null){
            Intent i = new Intent(getApplicationContext(),Account.class);
        startActivity(i);
        } else Toast.makeText(getApplicationContext(),"kullanıcı tanımlı değll", Toast.LENGTH_SHORT).show();
    }
    public void ListedClick(View v){

        if(ParseUser.getCurrentUser()!=null){
            Intent i = new Intent(getApplicationContext(),ListedShares.class);
            startActivity(i);
        } else Toast.makeText(getApplicationContext(),"kullanıcı tanımlı değll", Toast.LENGTH_SHORT).show();
    }
}
