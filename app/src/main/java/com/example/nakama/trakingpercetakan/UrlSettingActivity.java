package com.example.nakama.trakingpercetakan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class UrlSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);

        Button setUrl = (Button) findViewById(R.id.setUrl);
        EditText url  =  findViewById(R.id.url);
        url.setText(getURL());

        setUrl.setOnClickListener(click);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View v) {
            EditText url  =  findViewById(R.id.url);
            SharedPreferences sharedPreferences =  getSharedPreferences("url",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("baseUrl", url.getText().toString());
            editor.commit();
            editor.apply();

            Toast.makeText(getApplicationContext(), "Berhasil Set Base URL",
                    Toast.LENGTH_SHORT).show();
        }
    };

    String getURL(){
        SharedPreferences sharedPreferences = getSharedPreferences("url",MODE_PRIVATE);
        return  (sharedPreferences.getString("baseUrl", ""));
    }

}
