package com.saptra.sieron.myapplication.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.saptra.sieron.myapplication.R;

public class ErrorActivity extends AppCompatActivity {

    TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Intent intent = getIntent();
        String msjErrores = intent.getStringExtra("error");

        errors = (TextView) findViewById(R.id.Errors);
        errors.setText(msjErrores);
    }
}
