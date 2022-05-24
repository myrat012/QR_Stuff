package com.example.new_qr_stuff.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.new_qr_stuff.Connection;
import com.example.new_qr_stuff.MainActivity;
import com.example.new_qr_stuff.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShahadatnamaActivity extends AppCompatActivity {

    private RequestQueue rQueue;
    private ImageView profile;
    private TextView familyasy, ady, doglan_yeri, berlen_yeri, berlen_senesi, mohleti, derejesi, belgisi;
    private ImageLoadTask imageLoadTask;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shahadatnama);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        profile = findViewById(R.id.imageView);
        familyasy = findViewById(R.id.familyasy);
        ady = findViewById(R.id.ady);
        doglan_yeri = findViewById(R.id.doglan_yeri);
        berlen_yeri = findViewById(R.id.berlen_yeri);
        berlen_senesi = findViewById(R.id.berlen_senesi);
        mohleti = findViewById(R.id.mohleti);
        derejesi = findViewById(R.id.derejesi);
        belgisi = findViewById(R.id.belgisi);

        getdata();
    }

    private void getdata() {
        Connection connection = new Connection();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, connection.getShadatnama(), response -> {
            rQueue.getCache().clear();
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0){
                        imageLoadTask = (ImageLoadTask) new ImageLoadTask(connection.getIP()+"qr/"+ getIntent().getStringExtra("nomer") + ".jpg", profile).execute();
                        familyasy.setText(jsonObject.getString("familyasy"));
                        ady.setText(jsonObject.getString("ady"));
                        doglan_yeri.setText(jsonObject.getString("doglan_guni") + "\n" + jsonObject.getString("doglan_yeri"));
                        berlen_yeri.setText(jsonObject.getString("berlen_yeri"));
                        berlen_senesi.setText(jsonObject.getString("berlen_senesi"));
                        mohleti.setText(jsonObject.getString("mohleti"));
                        derejesi.setText(jsonObject.getString("derejesi"));
                        belgisi.setText("TM № "+jsonObject.getString("ayratyn_bellikler"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.d("VolleyError", error.toString())){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("sahadatnama", getIntent().getStringExtra("nomer"));

                return params;
            }
        };

        rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }
}
}