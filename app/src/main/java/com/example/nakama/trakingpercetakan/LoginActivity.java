package com.example.nakama.trakingpercetakan;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.login);
        TextView gantiUrl = findViewById(R.id.gantiURl);

        gantiUrl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent main = new Intent(LoginActivity.this, UrlSettingActivity.class);
                startActivity(main);
            }
        });

        login.setOnClickListener(loginClick);

        Log.d("local", getLocalLogin());
        if (getLocalLogin().equals("true")){
            goToMain();
        }

    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        public void onClick(View v) {
            EditText no_telp = (EditText) findViewById(R.id.nomor_telp);
            EditText password = (EditText)findViewById(R.id.password);
            try{
                run(no_telp.getText().toString(), password.getText().toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    void run(String noTelp, String password) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("nomor_telp", noTelp)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .post(body)
                .url(getURL()+"/api/login")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("run", e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject j = new JSONObject(myResponse);
                            String status ;
                            status = j.getString("error");
                            if( status.equals("false")){
                                Log.d("go to antother activity", myResponse);
                                setLocalPref("true");
                                goToMain();
                            }else {
                                Toast.makeText(getApplicationContext(), "Akun tidak ditemukan",
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

    void goToMain(){
        Intent main = new Intent(this, MenuActivity.class);
        startActivity(main);
    }

    void setLocalPref(String value){
        SharedPreferences sharedPreferences =  getSharedPreferences("loginPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isLogin", value);
        editor.commit();
        editor.apply();
    }

    String getLocalLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        return  (sharedPreferences.getString("isLogin", ""));
    }

    String getURL(){
        SharedPreferences sharedPreferences = getSharedPreferences("url",MODE_PRIVATE);
        return  (sharedPreferences.getString("baseUrl", ""));
    }
}
