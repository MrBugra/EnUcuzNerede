package com.example.mrbug.enucuznerede;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mrbug.enucuznerede.Adapters.customShares;
import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Account extends AppCompatActivity {
    ListView listMyShare;
    List<AppShares> returnedShares= new ArrayList<>();
    List<ParseObject> results;
    final Context ctx = this;
    final Activity acc= (Activity) this ;
    //String titless[]=new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //region listViewInitialize
        listMyShare=(ListView)findViewById(R.id.listShare);
        getSharestoUser();
    try{
        //customShares myadaptor=new customShares(this, returnedShares);
        Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+returnedShares.size(),Toast.LENGTH_SHORT).show();
        listMyShare.setAdapter(new customShares(this, returnedShares));
    }catch (Exception eee ){
        Log.e("hataa",eee.toString());
    }

        listMyShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAlertDialog((int)id);




                /*Toast.makeText(getApplicationContext(),""+returnedShares.get((int)id).getObjectID(),1).show();
                returnedShares.get((int)id).getTitle();*/
            }
        });
        //endregion
    }

 public void getSharestoUser(){
     //region garbage
       /* try {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
            query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername().toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    //Integer count=0;
                    for (ParseObject object : objects){
                    AppShares returnedShare = new AppShares();
                        AppShares item = new AppShares();
                        item.setTitle(object.get("title").toString());
                        item.setDiscounted(object.getDouble("discounted"));
                        item.setPrice(object.getDouble("price"));
                        ParseGeoPoint locc = object.getParseGeoPoint("location");
                        item.setLocationLatitude(locc.getLatitude());
                        item.setLocationLongitude(locc.getLongitude());
                        item.setUser(ParseUser.getCurrentUser().getUsername().toString());
                        item.setId(object.getInt("id"));
                        returnedShares.add(returnedShare);
                    }
        Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+returnedShares.size(),Toast.LENGTH_SHORT).show();  //buraya bak
                }
            });
        }
        catch (Exception e){
            //Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+e.toString(),Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(),"dönen paylaşım sayısı"+returnedShares.size(),Toast.LENGTH_SHORT).show();  //buraya bak
        return 1;*/
//endregion
     ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
     query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername().toString());
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
             item.setUser(ParseUser.getCurrentUser().getUsername().toString());
             item.setId(object.getInt("id"));
             item.setObjectID(object.getObjectId());
             returnedShares.add(item);

         }


     } catch (ParseException e) {
         Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
     }

    }
public void myAlertDialog(final int id){
    new AlertDialog.Builder(ctx)
            .setTitle("paylaşım silinecek")
            .setIcon(R.drawable.deleteicon)
            .setMessage("silmek istediğinize emin misiniz¿")
            .setPositiveButton("sil gitsin", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(),""+returnedShares.get(id).getObjectID(),Toast.LENGTH_SHORT).show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Shares");
                    query.getInBackground(returnedShares.get(id).getObjectID(), new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.deleteInBackground();
                                Toast.makeText(getApplicationContext(),"silindi  "+returnedShares.get(id).getObjectID(),Toast.LENGTH_SHORT).show();
                                listMyShare.setAdapter(null);
                                getSharestoUser();
                                listMyShare.setAdapter(new customShares(acc, returnedShares));

                               /* Intent i = new Intent(getApplicationContext(),Account.class);
                                startActivity(i);
                                finish();*/
                            } else {
                                Toast.makeText(getApplicationContext(),"hata:"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }   
            })
            .setNegativeButton("vazgeçtim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"umarım pişman olmazsın",Toast.LENGTH_SHORT).show();

                }
            })
            .show();
}
}
