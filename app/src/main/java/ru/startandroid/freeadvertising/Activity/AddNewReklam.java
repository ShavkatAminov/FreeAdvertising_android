package ru.startandroid.freeadvertising.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;
import ru.startandroid.freeadvertising.http.HttpHandler;
import ru.startandroid.freeadvertising.http.MultipartEntity;
import ru.startandroid.freeadvertising.model.ImageList;
import ru.startandroid.freeadvertising.model.ReklamType;

public class AddNewReklam extends AppCompatActivity {

    private String rekname, rektypestr, rekscore, rekcomment, userlogin;
    private ReklamType one = new ReklamType("", 0);;
    public static final int TAKE_PICTURE_REQUEST_B = 100;
    private List<ImageList> imageLists = new ArrayList<>();
    MyTask myTask = new MyTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reklam);
        myTask.execute();
        initaddpphoto();
    }
    public void initaddpphoto() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnPhotoReklam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddNewReklam.this, CameraActivity.class), TAKE_PICTURE_REQUEST_B);
            }
        });
        Button btnsaverek = (Button) findViewById(R.id.btn_save_Reklam);
        btnsaverek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText alll = (EditText) findViewById(R.id.name_reklam);
                rekname = alll.getText().toString();
                SaveTask saveTask = new SaveTask();
                alll = (EditText) findViewById(R.id.reklam_score);
                rekscore = alll.getText().toString();
                alll = (EditText) findViewById(R.id.comment_reklam);
                rekcomment = alll.getText().toString();
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                userlogin = sp.getString("login", "");
                saveTask.execute();
            }
        });
    }
    class SaveTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("name", rekname);
                entityBuilder.addTextBody("type", rektypestr);
                entityBuilder.addTextBody("score", rekscore);
                entityBuilder.addTextBody("comment", rekcomment);
                entityBuilder.addTextBody("login", userlogin);
                entityBuilder.addTextBody("imagesize", imageLists.size() + "");
                if (imageLists != null && imageLists.size() > 0) {
                    for (int i = 0; i < imageLists.size(); i++) {
                        entityBuilder.addBinaryBody("file" + i, new File(imageLists.get(i).getImagepath()));
                    }
                }
                HttpPost post = new HttpPost(Config.getAddnewrek());
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200)
                    return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            if(!ok) {
                Toast.makeText(AddNewReklam.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(AddNewReklam.this, "E'lon muvaffaqiyatli qo'shildi", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddNewReklam.this, MainActivity.class));
                finish();
            }
        }
    }
    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return justgo(one);
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            if(!ok) {
                Toast.makeText(AddNewReklam.this, "Internet bilan bog'lanish yo'q", Toast.LENGTH_LONG).show();
            }
            else {
                LinearLayout lvmain = (LinearLayout) findViewById(R.id.lvMain);
                one.setLinearLayout(lvmain);
                dfs(one);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST_B) {
            if (resultCode == RESULT_OK) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Bundle extras = data.getExtras();

                String imagePath = extras.getString(CameraActivity.EXTRA_CAMERA_DATA);
                if (imagePath != null && !imagePath.equals("")) {

                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imagePath, bmOptions);

                    int targetW = 400;//mCameraImageView.getWidth();
                    int targetH = 1;

                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    if (photoH > photoW) {
                        targetW = 1;//mCameraImageView.getWidth();
                        targetH = 400;
                    }
                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                    final LayoutInflater inflater = getLayoutInflater();
                    final LinearLayout linlayout = (LinearLayout) findViewById(R.id.image_linlayout);
                    final View childview =  inflater.inflate(R.layout.image_capture, linlayout, false);
                    final ImageList image = new ImageList();
                    image.setImagepath(imagePath);
                    image.setImageView((ImageView) childview.findViewById(R.id.image_capture_w));
                    image.getImageView().setImageBitmap(bitmap);
                    image.setBtndelete((Button) childview.findViewById(R.id.btndelete_imge));
                    linlayout.addView(childview);
                    imageLists.add(image);
                    image.getBtndelete().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageLists.remove(image);
                            linlayout.removeView(childview);
                            findViewById(R.id.btnPhotoReklam).setVisibility(View.VISIBLE);
                            if(imageLists.size() == 0) {
                                findViewById(R.id.btn_save_Reklam).setVisibility(View.GONE);
                            }
                        }
                    });
                    if(imageLists.size() == 5) {
                        findViewById(R.id.btnPhotoReklam).setVisibility(View.GONE);
                    }
                    findViewById(R.id.btn_save_Reklam).setVisibility(View.VISIBLE);
                }
            }
        }

    }
    public boolean justgo(ReklamType on) {
        HttpHandler handler = new HttpHandler();
        try {
            String json = handler.ReadHttpResponsewithHeader(Config.getType(), "id", "" + on.getId());
            if(json == null)
                return true;
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i ++) {
                ReklamType reklamType = new ReklamType(
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getInt("id")
                );
                justgo(reklamType);
                on.getChildren().add(reklamType);
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
    public void dfs(final ReklamType type) {
        final LayoutInflater inflater = getLayoutInflater();
        for(final ReklamType child : type.getChildren()) {

            final View childview =  inflater.inflate(R.layout.typereklam, type.getLinearLayout(), false);

            final TextView text = (TextView) childview.findViewById(R.id.partItemName);
            text.setText(child.getReklamtypeName());
            type.getLinearLayout().addView(childview);

            final LinearLayout linearLayoutChild = new LinearLayout(this);
            child.setLinearLayout(linearLayoutChild);

            LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leftMarginParams.leftMargin = 20;
            child.getLinearLayout().setVisibility(View.GONE);

            final ImageView arrowimage = (ImageView) childview.findViewById(R.id.partItemArrow);
            if(child.haschildren()) {
                arrowimage.setImageResource(R.drawable.ic_action_arrow_not_opened);
            }

            childview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.click();
                    if(!child.haschildren()) {
                        final TextView reklam_type = (TextView) findViewById(R.id.reklam_type);
                        reklam_type.setText(child.getReklamtypeName());
                        rektypestr = child.getId() + "";
                        one.getLinearLayout().setVisibility(View.GONE);
                        final Button btnothertype = (Button) findViewById(R.id.other_type);
                        btnothertype.setVisibility(View.VISIBLE);
                        final EditText name_reklam = (EditText) findViewById(R.id.name_reklam);
                        final EditText commentrek = (EditText) findViewById(R.id.comment_reklam);
                        final EditText score = (EditText) findViewById(R.id.reklam_score);
                        name_reklam.setVisibility(View.VISIBLE);
                        score.setVisibility(View.VISIBLE);
                        commentrek.setVisibility(View.VISIBLE);
                        findViewById(R.id.btnPhotoReklam).setVisibility(View.VISIBLE);
                        btnothertype.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                one.getLinearLayout().setVisibility(View.VISIBLE);
                                name_reklam.setText("");
                                commentrek.setText("");
                                score.setText("");
                                reklam_type.setText("E'lon turini tanlang");
                                name_reklam.setVisibility(View.GONE);
                                commentrek.setVisibility(View.GONE);
                                score.setVisibility(View.GONE);
                                btnothertype.setVisibility(View.GONE);
                                imageLists.clear();
                                findViewById(R.id.btnPhotoReklam).setVisibility(View.GONE);
                                findViewById(R.id.btn_save_Reklam).setVisibility(View.GONE);
                            }
                        });
                    }
                    if(child.isFirstclick()) {
                        child.getLinearLayout().setVisibility(View.VISIBLE);
                        if(child.haschildren()) {
                            arrowimage.setImageResource(R.drawable.ic_action_arrow_opened);
                            text.setTypeface(null, Typeface.BOLD);
                        }
                    }
                    else {
                        child.getLinearLayout().setVisibility(View.GONE);
                        if(child.haschildren()) {
                            arrowimage.setImageResource(R.drawable.ic_action_arrow_not_opened);
                            text.setTypeface(null, Typeface.NORMAL);
                        }
                    }
                }
            });
            dfs(child);
            child.getLinearLayout().setOrientation(LinearLayout.VERTICAL);
            type.getLinearLayout().addView(child.getLinearLayout(), leftMarginParams);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddNewReklam.this, MainActivity.class));
    }
}
