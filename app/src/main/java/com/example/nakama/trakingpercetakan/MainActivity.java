package com.example.nakama.trakingpercetakan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button car = (Button) findViewById(R.id.cari);


        car.setOnClickListener(cariOrder);
    }

    private View.OnClickListener cariOrder = new View.OnClickListener() {
        public void onClick(View v) {
            EditText no = (EditText) findViewById(R.id.no_order);

            try{
                run(no.getText().toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    void run(String noOrder) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("id", noOrder)
                .build();

        Request request = new Request.Builder()
                .post(body)
                .url(getURL()+"/api/getOrder")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject j = new JSONObject(myResponse);
                            String status ;
                            status = j.getString("error");
                            if( status.equals("false")){
                                Log.d("go to antother activity", myResponse);
                                updateView(
                                        j.getString("no_order"),
                                        j.getString("total_bayar"),
                                        j.getString("uang_muka"),
                                        j.getString("sisa_bayar"),
                                        j.getString("bar_persen"));
                            }else {
                                Toast.makeText(getApplicationContext(), "Orderan tidak ditemukan",
                                        Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Log.d("ERROR", e.getMessage());
                            Toast.makeText(getApplicationContext(),  e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    void updateView(String no_order,String  total_bayar,String  uang_muka,String  sisa_bayar,String  status){
        TextView noOrder = findViewById(R.id.nomor_order);
        TextView totalBayar = findViewById(R.id.total_bayar);
        TextView uangMuka =findViewById(R.id.uang_muka);
        TextView sisaBayar = findViewById(R.id.sisa_bayar);
        TextView statusPersen = findViewById(R.id.status_persen);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        noOrder.setText(no_order.toUpperCase());
        totalBayar.setText("Rp "+total_bayar);
        uangMuka.setText("Rp "+uang_muka);
        sisaBayar.setText("Rp "+sisa_bayar);
        statusPersen.setText(status+"%");

        int p = Integer.parseInt(status);
        progressBar.setProgress(p);
    }

    String getURL(){
        SharedPreferences sharedPreferences = getSharedPreferences("url",MODE_PRIVATE);
        return  (sharedPreferences.getString("baseUrl", ""));
    }
}
