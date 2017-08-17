package ru.startandroid.freeadvertising.Activity;

import android.Manifest;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;

public class RegisterActivity extends AppCompatActivity {

    public EditText firstname;
    public EditText lastname;
    private EditText telefonnumber;
    private EditText login;
    private EditText parol;
    private Button btnregister;
    private String first_name;
    private String last_name;
    private String telefon;
    private String slogin;
    public boolean ujiyest;
    private LocationManager locationManager = null;
    private String sparol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);
        telefonnumber = (EditText) findViewById(R.id.telefon_number);
        login = (EditText) findViewById(R.id.edtloginreg);
        parol = (EditText) findViewById(R.id.edtparolreg);
        btnregister = (Button) findViewById(R.id.registerbtnreg);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name = firstname.getText().toString();
                last_name = lastname.getText().toString();
                telefon = telefonnumber.getText().toString();
                slogin = login.getText().toString();
                sparol = parol.getText().toString();
                if (first_name.equals("") || lastname.equals("") || telefon.equals("") || slogin.equals("") || sparol.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Ma'lumotlarni to'liq kiriting", Toast.LENGTH_LONG).show();
                } else {
                    MyTask myTask = new MyTask();
                    myTask.execute();
                }

            }
        });
    }

    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                entityBuilder.addTextBody("firstname", first_name);
                entityBuilder.addTextBody("lastname", last_name);
                entityBuilder.addTextBody("telefon", telefon);
                entityBuilder.addTextBody("login", slogin);
                entityBuilder.addTextBody("parol", sparol);

                HttpPost post = new HttpPost(Config.getRegister());
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);

                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                ujiyest = (response.getStatusLine().getStatusCode() != 200);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean) {
                Toast.makeText(RegisterActivity.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
            else {
                if(ujiyest) {
                    Toast.makeText(RegisterActivity.this, "Bunday login bilan foydalanuvchi ro'yhatdan o'tgan. Iltimos boshqa login tanlang", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor ed =  sp.edit();
                    ed.putString("login", login.getText().toString());
                    ed.putString("parol", parol.getText().toString());
                    ed.commit();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
