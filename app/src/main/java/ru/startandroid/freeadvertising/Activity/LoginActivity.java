package ru.startandroid.freeadvertising.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;

public class LoginActivity extends AppCompatActivity {

    private EditText edtlogin, edtparol;
    private Button btnlogin, btnregister;
    private String login, parol;
    private boolean ujiyest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtlogin = (EditText) findViewById(R.id.edtlogin);
        edtparol = (EditText) findViewById(R.id.edtparol);
        btnlogin = (Button) findViewById(R.id.loginbtn);
        btnregister = (Button) findViewById(R.id.registerbtn);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = edtlogin.getText().toString();
                parol = edtparol.getText().toString();
                MyTask myTask = new MyTask();
                myTask.execute();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("login", login);
                entityBuilder.addTextBody("parol", parol);

                HttpPost post = new HttpPost(Config.getAuth());
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
                Toast.makeText(LoginActivity.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
            else {
                if(ujiyest) {
                    Toast.makeText(LoginActivity.this, "Login yoki parolni xato kiritdingiz", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor ed =  sp.edit();
                    ed.putString("login", login);
                    ed.putString("parol", parol);
                    ed.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
