package com.example.suas_ps.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suas_ps.R;

public class ComplaintsActivity extends AppCompatActivity {
    LinearLayout actionLL, complains;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        initComponent();

//        onClickMethod(actionLL, CreateComplaintActivity.class);
        onClickMethod(complains, CreateComplaintActivity.class);

    }

    private void onClickMethod(View view, Class<?> destination) {
        view.setOnClickListener(view1 -> {
            startActivity(new Intent(this, destination));
        });
    }

    private void initComponent() {
        actionLL = findViewById(R.id.actionLL);
        complains = findViewById(R.id.complains);
    }

}