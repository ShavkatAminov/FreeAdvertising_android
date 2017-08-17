package ru.startandroid.freeadvertising.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class UpdateActivity extends AppCompatActivity {

    TextView textname;
    Button btnsave;
    EditText editdes, editscore;
    String des, score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        textname = (TextView) findViewById(R.id.updrekname);
        btnsave = (Button) findViewById(R.id.updbtnsave);
        editdes = (EditText) findViewById(R.id.updrekdes);
        editscore = (EditText) findViewById(R.id.updrekscore);
        editscore.setText(getIntent().getStringExtra("score"));
        editdes.setText(getIntent().getStringExtra("des"));
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des = editdes.getText().toString();
                score = editscore.getText().toString();
                MyTask myTask = new MyTask();
                myTask.execute();
            }
        });
    }
    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                entityBuilder.addTextBody("id", getIntent().getStringExtra("id"));
                entityBuilder.addTextBody("des",des);
                entityBuilder.addTextBody("score", score);

                HttpPost post = new HttpPost(Config.getUpd());
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);

                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                boolean ujiyest = (response.getStatusLine().getStatusCode() != 200);
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
                Toast.makeText(UpdateActivity.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
            else {
                    Intent intent = new Intent(UpdateActivity.this, ViewReklamActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    startActivity(intent);
                    finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateActivity.this, ViewReklamActivity.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
        finish();
    }
}
