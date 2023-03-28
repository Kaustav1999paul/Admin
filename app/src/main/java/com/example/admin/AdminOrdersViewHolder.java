package com.example.admin;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView userPhoneNumber;

    public AdminOrdersViewHolder(View itemView) {
        super(itemView);
        this.userName = (TextView) itemView.findViewById(R.id.name);
        this.userPhoneNumber = (TextView) itemView.findViewById(R.id.phone);
    }
}