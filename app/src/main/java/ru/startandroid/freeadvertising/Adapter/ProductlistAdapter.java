package ru.startandroid.freeadvertising.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import java.security.PublicKey;
import java.util.List;

import ru.startandroid.freeadvertising.Activity.ViewReklamActivity;
import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;
import ru.startandroid.freeadvertising.model.Product;



public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.MyViewHolder>{

    private List<Product> productList;
    private Context context;
    public ProductlistAdapter(Context context, List<Product> list) {
        this.context = context;
        this.productList = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reklam, parent, false);
        return new MyViewHolder(itemview);
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname, description;
        public ImageView imageproduct;
        public View homeview;

        public MyViewHolder(View itemView) {
            super(itemView);
            fullname = (TextView) itemView.findViewById(R.id.reklam_full_name);
            description = (TextView) itemView.findViewById(R.id.reaklam_description);
            imageproduct = (ImageView) itemView.findViewById(R.id.reklam_thumbnail);
            homeview = itemView.findViewById(R.id.home_view);
        }
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.fullname.setText(product.getName());
        holder.description.setText(product.getComment());
        String firstimagepath = "" + Config.getUrlserverjusthtttp();
        for(int i = 0; i < product.getImagepath().length(); i ++) {
            if(product.getImagepath().charAt(i) == '|')
                break;
            firstimagepath += product.getImagepath().charAt(i);
        }
        Log.d("mylogs", "" + firstimagepath);
        if(!product.getImagepath().equals("")) Glide.with(context).load(firstimagepath).into(holder.imageproduct);
        holder.homeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewReklamActivity.class);
                Log.d("mylogs", "" + product.getId());
                intent.putExtra("id", product.getId() + "");
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }
}
