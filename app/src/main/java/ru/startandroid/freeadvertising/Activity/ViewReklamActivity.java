package ru.startandroid.freeadvertising.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import ru.startandroid.freeadvertising.Adapter.ProductlistAdapter;
import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;
import ru.startandroid.freeadvertising.http.HttpHandler;
import ru.startandroid.freeadvertising.model.Product;
import ru.startandroid.freeadvertising.model.User;

public class ViewReklamActivity extends AppCompatActivity {

    private Product product;
    private User user = new User();
    private User productuser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reklam);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        user.setLogin(sp.getString("login", ""));
        GetReklam gt = new GetReklam();
        gt.execute();
    }
    class GetReklam extends AsyncTask<Void,Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String jsonstr = null;
            Log.d("mylogs", "" + Config.getProductwithid());
            Log.d("mylogs", "" + getIntent().getStringExtra("id"));
            jsonstr = handler.ReadHttpResponsewithHeader(Config.getProductwithid(), "id", "" + getIntent().getStringExtra("id"));
            if (jsonstr != null) {
                try {
                    JSONArray productarray = new JSONArray(jsonstr);
                    for (int i = 0; i < productarray.length(); i++) {
                        product = new Product(
                                productarray.getJSONObject(i).getString("name"),
                                productarray.getJSONObject(i).getString("score"),
                                productarray.getJSONObject(i).getString("comment"),
                                productarray.getJSONObject(i).getString("imagepath"),
                                productarray.getJSONObject(i).getLong("type"),
                                productarray.getJSONObject(i).getString("userlogin")
                        );
                        product.setId(productarray.getJSONObject(i).getInt("id"));
                    }
                    productuser.setLogin(product.getUser_login());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonstr = handler.ReadHttpResponsewithHeader(Config.getAccount(), "login", productuser.getLogin());
            if (jsonstr != null) {
                try {
                    JSONArray productarray = new JSONArray(jsonstr);
                    for (int i = 0; i < productarray.length(); i++) {
                        productuser.setFirstname(productarray.getJSONObject(i).getString("firstname"));
                        productuser.setLastname(productarray.getJSONObject(i).getString("lastname"));
                        productuser.setTelefon(productarray.getJSONObject(i).getString("telefon"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        public void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            if(ok) {
                String imagepath = "";
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout linLayout = (LinearLayout) findViewById(R.id.viewrekitemlinlay);
                for(int i = 0; i < product.getImagepath().length(); i ++) {
                    if(product.getImagepath().charAt(i) == '|') {
                        imagepath = Config.getUrlserverjusthtttp() + imagepath;
                        final View childview =  inflater.inflate(R.layout.imageitem, linLayout, false);
                        final ImageView imageView = (ImageView) childview.findViewById(R.id.imageViewitem);
                        Glide.with(ViewReklamActivity.this).load(imagepath).into(imageView);
                        linLayout.addView(childview);
                        Log.d("mylogs", "imagepath   " + imagepath);
                        imagepath = "";
                    }
                    else {
                        imagepath += product.getImagepath().charAt(i);
                    }
                }
                TextView view_reklam_score = (TextView) findViewById(R.id.view_reklam_score);
                TextView view_reklam_name = (TextView) findViewById(R.id.view_reklam_name);
                TextView view_reklam_description = (TextView) findViewById(R.id.view_reklam_description);
                TextView reklam_user_name = (TextView) findViewById(R.id.reklam_user_name);
                TextView reklam_tel_user = (TextView) findViewById(R.id.reklam_tel_user);
                view_reklam_score.setText(product.getScore());
                view_reklam_name.setText(product.getName());
                view_reklam_description.setText(product.getComment());
                Button btndel = (Button) findViewById(R.id.btndeleterek);
                Button btnupd = (Button) findViewById(R.id.btnupdaterek);
                if(productuser.getLogin().equals(user.getLogin())) {
                    reklam_user_name.setText("Shaxsiy e'lon");
                    btndel.setVisibility(View.VISIBLE);
                    btndel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelReklam dt = new DelReklam();
                            dt.execute();
                        }
                    });
                    btnupd.setVisibility(View.VISIBLE);
                    btnupd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ViewReklamActivity.this, UpdateActivity.class);
                            intent.putExtra("id", getIntent().getStringExtra("id"));
                            intent.putExtra("name", product.getName());
                            intent.putExtra("des", product.getComment());
                            intent.putExtra("score", product.getScore());
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else {
                    reklam_tel_user.setVisibility(View.VISIBLE);
                    reklam_tel_user.setText(productuser.getTelefon());
                    reklam_user_name.setText(productuser.getLastname() + " " + productuser.getFirstname());
                }
            }
            else {
                Toast.makeText(ViewReklamActivity.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
        }
    }
    class DelReklam extends AsyncTask<Void,Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            handler.ReadHttpResponsewithHeader(Config.getDelet(), "id", product.getId() + "");
            return true;
        }
        public void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            if(ok) {
                finish();
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewReklamActivity.this, MainActivity.class));
    }
}
