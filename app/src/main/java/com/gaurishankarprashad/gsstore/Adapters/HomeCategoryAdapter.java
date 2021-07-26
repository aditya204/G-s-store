package com.gaurishankarprashad.gsstore.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.Models.HomeCategoryModels;
import com.gaurishankarprashad.gsstore.R;
import com.gaurishankarprashad.gsstore.SearchedProduct;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private List<HomeCategoryModels> homeCategoryModelsList;

    public HomeCategoryAdapter(List<HomeCategoryModels> homeCategoryModelsList) {
        this.homeCategoryModelsList = homeCategoryModelsList;
    }


    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.grocery_home_category_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, int position) {

        String image = homeCategoryModelsList.get( position ).getImageResource( );
        String title = homeCategoryModelsList.get( position ).getTitle( );
        String tag=homeCategoryModelsList.get( position ).getTag();
        holder.setData( image,title,tag );


    }

    @Override
    public int getItemCount() {
        return homeCategoryModelsList.size( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        private LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            image = itemView.findViewById( R.id.grocery_home_category_icon );
            title = itemView.findViewById( R.id.grocery_home_category_title );
            linearLayout=itemView.findViewById( R.id.grocery_home_category_LinearLayout );


        }

        private void setData(String url, final String Title, final String TagList){


            Glide.with( itemView.getContext() ).load( url ).into( image );
            title.setText( Title );




            image.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    if(!TagList.isEmpty()) {
                        Intent intent = new Intent( itemView.getContext( ), SearchedProduct.class );
                        intent.putExtra( "tag_string", TagList );
                        itemView.getContext( ).startActivity( intent );
                    }
                }
            } );








        }

    }
}
