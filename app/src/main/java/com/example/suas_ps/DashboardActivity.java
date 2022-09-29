package com.example.suas_ps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suas_ps.complaints.ComplaintsActivity;
import com.example.suas_ps.service_request.ServiceRequestActivity;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout createService, complains, eventRequest, productListing, giveaways;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initComponent();

        onClickMethod(createService, ServiceRequestActivity.class);
        onClickMethod(complains, ComplaintsActivity.class);
        onClickMethod(eventRequest, EventRequestActivity.class);
        onClickMethod(productListing, ProductListingActivity.class);
        onClickMethod(giveaways, GiveawaysActivity.class);

    }

    private void onClickMethod(View view, Class<?> destination) {
        view.setOnClickListener(view1 -> {
            startActivity(new Intent(this, destination));
        });
    }

    private void initComponent() {
        createService = findViewById(R.id.createService);
        complains = findViewById(R.id.complains);
        eventRequest = findViewById(R.id.eventRequest);
        productListing = findViewById(R.id.productListing);
        giveaways = findViewById(R.id.giveaways);
    }

}