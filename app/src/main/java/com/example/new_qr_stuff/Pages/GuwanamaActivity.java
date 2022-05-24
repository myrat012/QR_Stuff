package com.example.new_qr_stuff.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.new_qr_stuff.Connection;
import com.example.new_qr_stuff.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuwanamaActivity extends AppCompatActivity {

    private TextView san_belgisi_guwa, yly_guwa, faa_guwa, address_guwa, marka_guwa, renk_guwa, mator_guwa, agram_guwa;
    private RequestQueue rQueue;

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
        setContentView(R.layout.activity_guwanama);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        san_belgisi_guwa = findViewById(R.id.san_belgisi_guwa);
        yly_guwa = findViewById(R.id.yly_guwa);
        faa_guwa = findViewById(R.id.faa_guwa);
        address_guwa = findViewById(R.id.address_guwa);
        marka_guwa = findViewById(R.id.marka_guwa);
        renk_guwa = findViewById(R.id.renk_guwa);
        mator_guwa = findViewById(R.id.renk_guwa);
        agram_guwa = findViewById(R.id.agram_guwa);

        getdata();
    }
    private void getdata() {
        Connection connection = new Connection();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, connection.getGuwa(), response -> {
            rQueue.getCache().clear();
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0){
                        san_belgisi_guwa.setText(jsonObject.getString("nomer"));
                        yly_guwa.setText(jsonObject.getString("yyly"));
                        faa_guwa.setText(jsonObject.getString("faa"));
                        address_guwa.setText(jsonObject.getString("address"));
                        marka_guwa.setText(jsonObject.getString("marka"));
                        renk_guwa.setText(jsonObject.getString("renk"));
                        mator_guwa.setText(jsonObject.getString("mator"));
                        agram_guwa.setText(jsonObject.getString("agram"));
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
}