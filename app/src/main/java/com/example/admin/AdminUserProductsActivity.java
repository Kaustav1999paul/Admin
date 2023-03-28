package com.example.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.Model.AdminOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class AdminUserProductsActivity extends AppCompatActivity {

    public DatabaseReference cartListRef;
    RecyclerView.LayoutManager layoutManager;
    private String proID;
    private RecyclerView productsList;
//    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        String userID = getIntent().getStringExtra("uid");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.products_list);
        this.productsList = recyclerView;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.layoutManager = linearLayoutManager;
        this.productsList.setLayoutManager(linearLayoutManager);
        this.cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");

        FirebaseRecyclerAdapter<AdminOrder, CartViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrder, CartViewHolder>(new FirebaseRecyclerOptions.Builder().setQuery((Query) this.cartListRef, AdminOrder.class).build()) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(CartViewHolder holder, int position, AdminOrder model) {
                CartViewHolder cartViewHolder = holder;
                final AdminOrder adminOrder = model;
                cartViewHolder.itemName.setText(model.getName());
                String state = model.getState();
                String isCancelled = model.getIs_Cancelled();
                if (isCancelled.equals("cancelled_by_Me")) {
                    cartViewHolder.parent.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = cartViewHolder.parent.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    cartViewHolder.parent.setLayoutParams(params);
                }
                if (isCancelled.equals("cancelled_by_Admin")) {
                    cartViewHolder.progressBar.setVisibility(View.GONE);
                    cartViewHolder.changeOrder.setVisibility(View.GONE);
                    cartViewHolder.temp.setVisibility(View.GONE);
                    cartViewHolder.cancelOrder.setText("You Cancelled the order");
                    cartViewHolder.cancelOrder.setEnabled(false);
                }
                if (state.equals("Ordered") || state.equals("Shipped") || state.equals("Out for Delivery") || state.equals("Delivered")) {
                    int qty = Integer.parseInt(model.getQty());
                    Glide.with((View) cartViewHolder.itemImage).load(model.getImage()).into(cartViewHolder.itemImage);
                    cartViewHolder.itemName.setText(model.getName());
                    cartViewHolder.noOfItem.setText(String.valueOf(qty));
                    cartViewHolder.itemPrice.setText("₹" + String.valueOf(Integer.parseInt(model.getPrice()) * qty));
                    cartViewHolder.cancelOrder.setVisibility(View.GONE);
                    if (state.equals("Ordered")) {
                        cartViewHolder.progressBar.setProgress(5);
                        cartViewHolder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals("Shipped")) {
                        cartViewHolder.progressBar.setProgress(35);
                        cartViewHolder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals("Out for Delivery")) {
                        cartViewHolder.progressBar.setProgress(60);
                        cartViewHolder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals("Delivered")) {
                        cartViewHolder.progressBar.setProgress(100);
                        cartViewHolder.cancelOrder.setVisibility(View.GONE);
                    }
                    cartViewHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            new AlertDialog.Builder(AdminUserProductsActivity.this).setTitle("Cancel Order").setMessage("Are you sure you want to cancel the order?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, Object> mapa = new HashMap<>();
                                    mapa.put("is_Cancelled", "cancelled_by_Admin");
                                    AdminUserProductsActivity.this.cartListRef.child(adminOrder.getID()).updateChildren(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(Task<Void> task) {
                                            Toast.makeText(AdminUserProductsActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setIcon(R.mipmap.ic_launcher_round).show();
                        }
                    });
                    cartViewHolder.changeOrder.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserProductsActivity.this);
                            builder.setTitle("Update order status");
                            builder.setIcon(R.mipmap.ic_launcher_round);
                            builder.setItems(new CharSequence[]{"Shipped", "Out for Delivery", "Delivered"}, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    if (i == 0) {
                                        AdminUserProductsActivity.this.ChangeStatus(adminOrder.getID());
                                    } else if (i == 1) {
                                        AdminUserProductsActivity.this.OutforDelivery(adminOrder.getID());
                                    } else if (i == 2) {
                                        AdminUserProductsActivity.this.Delivered(adminOrder.getID());
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                    return;
                }
                cartViewHolder.parent.setVisibility(View.GONE);
                ViewGroup.LayoutParams params2 = cartViewHolder.parent.getLayoutParams();
                params2.height = 0;
                params2.width = 0;
                cartViewHolder.parent.setLayoutParams(params2);
            }

            public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
            }
        };
        this.productsList.setAdapter(adapter);
        adapter.startListening();
    }

    /* access modifiers changed from: private */
    public void Delivered(String usID) {
        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("state", "Delivered");
        this.cartListRef.child(usID).updateChildren(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void OutforDelivery(String usID) {
        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("state", "Out for Delivery");
        this.cartListRef.child(usID).updateChildren(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void ChangeStatus(String uID) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "Shipped");
        this.cartListRef.child(uID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Toast.makeText(AdminUserProductsActivity.this, "Status Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}