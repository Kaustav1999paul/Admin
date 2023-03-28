package com.example.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder{

    public TextView cancelOrder;
    public TextView changeOrder;
    public ImageView itemImage;
    public TextView itemName;
    public TextView itemPrice;
    public TextView noOfItem;
    public LinearLayout parent;
    public ProgressBar progressBar;
    public LinearLayout shiftPage;
    public LinearLayout temp;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        this.temp = (LinearLayout) itemView.findViewById(R.id.temp);
        this.changeOrder = (TextView) itemView.findViewById(R.id.changeOrder);
        this.parent = (LinearLayout) itemView.findViewById(R.id.parent);
        this.cancelOrder = (TextView) itemView.findViewById(R.id.cancelOrder);
        this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        this.itemName = (TextView) itemView.findViewById(R.id.itemName);
        this.itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
        this.noOfItem = (TextView) itemView.findViewById(R.id.noOfItem);
        this.shiftPage = (LinearLayout) itemView.findViewById(R.id.shiftPage);
        this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}
