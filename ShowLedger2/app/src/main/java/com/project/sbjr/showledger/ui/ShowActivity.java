package com.project.sbjr.showledger.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Utill;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        TextView t = (TextView ) findViewById(R.id.text1);
        t.setText(Utill.usernameFromSharedPreference(this));
    }
}
