package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.Model.AdminOrderHomeModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TrackOrder extends AppCompatActivity {

    RecyclerView orderList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        orderList = (RecyclerView) findViewById(R.id.orderList);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList.setLayoutManager(new GridLayoutManager(this, 2));

        FirebaseRecyclerAdapter<AdminOrderHomeModel, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrderHomeModel, AdminOrdersViewHolder>(new FirebaseRecyclerOptions.Builder().setQuery((Query) this.ordersRef, AdminOrderHomeModel.class).build()) {

            public void onBindViewHolder(AdminOrdersViewHolder holder, int position, final AdminOrderHomeModel model) {
//                holder.userName.setText(model.getName());
                holder.userPhoneNumber.setText(model.getPhone());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String key = model.getId();
                        Intent intent = new Intent(TrackOrder.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", key);
                        startActivity(intent);
                    }
                });
            }

            public AdminOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new AdminOrdersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false));
            }
        };
        this.orderList.setAdapter(adapter);
        adapter.startListening();
    }
}