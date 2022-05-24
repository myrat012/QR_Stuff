package com.example.new_qr_stuff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.new_qr_stuff.Pages.GuratActivity;
import com.example.new_qr_stuff.Pages.GuwanamaActivity;
import com.example.new_qr_stuff.Pages.ShahadatnamaActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue rQueue;
    private String SHADATNAMA;
    private Button button, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        scan();

    }

    public void scan(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning Code");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            SHADATNAMA = result.getContents();

            getdata();

            button.setOnClickListener(view -> {
                Intent intent = new Intent(this, ShahadatnamaActivity.class);
                intent.putExtra("nomer", SHADATNAMA);
                startActivity(intent);
            });
            button2.setOnClickListener(view -> {
                Intent intent = new Intent(this, GuwanamaActivity.class);
                intent.putExtra("nomer", SHADATNAMA);
                startActivity(intent);
            });
            button3.setOnClickListener(view -> {
                Intent intent = new Intent(this, GuratActivity.class);
                intent.putExtra("nomer", SHADATNAMA);
                startActivity(intent);
            });

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(result.getContents());
//            builder.setTitle("Scanning Result");
//            builder.setPositiveButton("scan Again", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    scan();
//                }
//            }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
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
                        if (!jsonObject.getString("ayratyn_bellikler").equals("yok"))
                            button.setBackgroundColor(getResources().getColor(R.color.success));
                        else
                            button.setEnabled(false);
                    }
                    if (i == 1){
                        if (!jsonObject.getString("guwa_kiminki").equals("yok"))
                            button2.setBackgroundColor(getResources().getColor(R.color.success));
                        else
                            button2.setEnabled(false);
                    }
                    if (i == 2){
                        if (!jsonObject.getString("gurat_sahadatnama").equals("yok"))
                            button3.setBackgroundColor(getResources().getColor(R.color.success));
                        else
                            button3.setEnabled(false);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.d("VolleyError", error.toString())){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("sahadatnama", SHADATNAMA);

                return params;
            }
        };

        rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(stringRequest);

    }

}