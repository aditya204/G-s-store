package com.gaurishankarprashad.gsstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.Models.AddProductImageModel;
import com.gaurishankarprashad.gsstore.R;

import java.util.List;

public class AddproductImageAdapter extends RecyclerView.Adapter<AddproductImageAdapter.ViewHolder> {

    private List<AddProductImageModel> productImageModelList;

    public AddproductImageAdapter(List<AddProductImageModel> productImageModelList) {
        this.productImageModelList = productImageModelList;
    }

    @NonNull
    @Override
    public AddproductImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.add_product_image_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AddproductImageAdapter.ViewHolder holder, int position) {

        String url= productImageModelList.get( position ).getImage();

        holder.setData(url);
    }

    @Override
    public int getItemCount() {
        return productImageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imaeg;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            imaeg=itemView.findViewById( R.id.add_product_image );
        }

        public void setData(String url) {

            Glide.with( itemView.getContext() ).load( url ).into( imaeg );


        }
    }
}
