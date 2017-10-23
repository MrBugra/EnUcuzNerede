package com.example.mrbug.enucuznerede;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Sign extends AppCompatActivity {

    TextView username,password,email;
    Button btnSign,btnMode;
    public void ObjInit(){
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        email = (TextView) findViewById(R.id.rectext);
        btnSign = (Button) findViewById(R.id.btnSign);
        btnMode=(Button) findViewById(R.id.changeButton);
    }

    public void openMainAct(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    public void changemode(View view){
        if(email.getVisibility()==View.VISIBLE){
            email.setVisibility(View.GONE);
            btnSign.setText("Giriş Yap");
        }
        else {
            email.setVisibility(View.VISIBLE);
            btnSign.setText("Kayıt Ol");
        }
    }

    public void signop(View view){

    if (email.getVisibility()==View.VISIBLE){
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
               if(e==null) openMainAct(); //Toast.makeText(getApplicationContext(),"kayıt başarılı",1).show();
                else Toast.makeText(getApplicationContext(),"başarısız",1).show();

            }
        });
    }
    else{
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
            if(user!=null)  openMainAct(); //Toast.makeText(getApplicationContext(),"giris başarılı",1).show();
            else Toast.makeText(getApplicationContext(),"giriş başarısız",1).show();
            }
        });

    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ObjInit();

        //if(ParseUser.getCurrentUser()!=null)  openMainAct();


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

}
