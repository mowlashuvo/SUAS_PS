package com.example.suas_ps;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder> {
    private final Activity activity;
    private final List<ServiceRequestModel> list;

    int number = 1;

    FirebaseAuth mAuth;

    private static RecyclerViewClickListener itemListener;

    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }
    public ServiceRequestAdapter(Activity activity, List<ServiceRequestModel> list) {
        this.activity = activity;
        this.list = list;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ServiceRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.request_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRequestAdapter.ViewHolder holder, int position) {
        final ServiceRequestModel model = list.get(position);
        if (model.getStatus().equals(Constants.PENDING_STATUS)){
            holder.card_root.setCardBackgroundColor(activity.getResources().getColor(R.color.yellow_500));
        } else if (model.getStatus().equals(Constants.WORKING_STATUS)){
            holder.card_root.setCardBackgroundColor(activity.getResources().getColor(R.color.cyan_500));
        } else {
            holder.card_root.setCardBackgroundColor(activity.getResources().getColor(R.color.green_500));
        }
        holder.idRoom.setText("Room No. "+model.getRoom_no());
        holder.descriptionTV.setText(model.getDescription());
        String STATUS ="0";
        switch (model.getStatus()){
            case "0":
                STATUS = "Pending";
                break;
            case "2":
                STATUS = "Completed";
                break;
            case "1":
                STATUS = "In Progress";
        }
        holder.statusTv.setText(STATUS);
        holder.parent.setOnClickListener(view -> {
//            Intent intent = new Intent(activity, RequestDetailActivity.class);
//            intent.putExtra("model", model);
//            intent.putExtra("status", status);
//            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idRoom, descriptionTV, statusTv;
        LinearLayout parent;
        CardView card_root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idRoom = itemView.findViewById(R.id.idRoom);
            statusTv = itemView.findViewById(R.id.statusTv);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            parent = itemView.findViewById(R.id.parent);
            card_root = itemView.findViewById(R.id.card_root);
        }
    }
}
