package com.example.ex1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private List<Restaurant> restaurants;
    private MyRestaurantListener listener;

    interface MyRestaurantListener {
        void onRestaurantClicked(int position, View view);
        void onRestaurantLongClicked(int position, View view);
    }

    public void setListener(MyRestaurantListener listener) {
    this.listener=listener;
    }

    public RestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantIv;
        TextView restaurantTv;
        TextView addressTv;
        TextView foodTypeTv;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            restaurantIv = itemView.findViewById(R.id.restaurant_pic_list);
            restaurantTv = itemView.findViewById(R.id.name_content);
            addressTv = itemView.findViewById(R.id.address_content);
            foodTypeTv = itemView.findViewById(R.id.food_type_content);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onRestaurantClicked(getAdapterPosition(), v);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    if(listener!=null)
                        listener.onRestaurantLongClicked(getAdapterPosition(), v);
                    return false;
                }
            });
        }
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_layout, parent, false);
        RestaurantViewHolder restaurantViewHolder = new RestaurantViewHolder(view);
        return restaurantViewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.restaurantTv.setText(restaurant.getName());
        holder.addressTv.setText(restaurant.getAddress());
        holder.foodTypeTv.setText(restaurant.getPhoneNumber());
        holder.restaurantIv.setImageBitmap(restaurant.getPhoto());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}