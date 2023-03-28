package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.track).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Home.this.startActivity(new Intent(Home.this, TrackOrder.class));
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Home.this.startActivity(new Intent(Home.this, AddProduct.class));
            }
        });
    }
}