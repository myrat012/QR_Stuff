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

public class GuratActivity extends AppCompatActivity {
    private RequestQueue rQueue;
    private TextView pyyg_text, kysymy_text, san_belgisi_text, ondurilen_yly_text, berilen_sene_text, bellige_alnan_text;

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
        setContentView(R.layout.activity_gurat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pyyg_text = findViewById(R.id.san_belgisi_guwa);
        kysymy_text = findViewById(R.id.yly_guwa);
        san_belgisi_text = findViewById(R.id.faa_guwa);
        ondurilen_yly_text = findViewById(R.id.address_guwa);
        berilen_sene_text = findViewById(R.id.marka_guwa);
        bellige_alnan_text = findViewById(R.id.renk_guwa);

        getdata();
    }

    private void getdata() {
        Connection connection = new Connection();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, connection.getGurat(), response -> {
            rQueue.getCache().clear();
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0){
                        pyyg_text.setText(jsonObject.getString("pyyyg_birligi"));
                        kysymy_text.setText(jsonObject.getString("kysymy"));
                        san_belgisi_text.setText(jsonObject.getString("nomeri"));
                        ondurilen_yly_text.setText(jsonObject.getString("ondurulen_yyly"));
                        berilen_sene_text.setText(jsonObject.getString("bellige_alnan_sene"));
                        bellige_alnan_text.setText(jsonObject.getString("gutaryan_yyly"));

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