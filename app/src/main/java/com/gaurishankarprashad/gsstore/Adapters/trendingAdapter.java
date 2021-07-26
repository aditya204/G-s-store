package com.gaurishankarprashad.gsstore.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.GroceryProductDetails;
import com.gaurishankarprashad.gsstore.Models.dealsofthedayModel;
import com.gaurishankarprashad.gsstore.R;

import java.util.List;

public class trendingAdapter  extends BaseAdapter {

    List<dealsofthedayModel> gridmodelList;

    public trendingAdapter(List<dealsofthedayModel> gridmodelList) {
        this.gridmodelList = gridmodelList;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from( viewGroup.getContext( ) ).inflate( R.layout.dealsoftheday_recycler_item, null );
            view.setElevation( 2 );
            view.setBackgroundColor( Color.parseColor( "#ffffff" ) );
            ImageView imageView = view.findViewById( R.id.dealsofthedayitemimage );

            TextView title = view.findViewById( R.id.dealsofthedayitemname );
            TextView descriptonwe = view.findViewById( R.id.dealsofthedayitedescription );
            TextView price = view.findViewById( R.id.dealsofthedayitemprice );

           final String id = gridmodelList.get( i ).getId( );
            Glide.with( view.getContext( ) ).load( gridmodelList.get( i ).getImage( ) ).into( imageView );
            title.setText( gridmodelList.get( i ).getTitle( ) );

            imageView.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( view.getContext(), GroceryProductDetails.class );
                    intent.putExtra("product_id", id );
                    intent.putExtra("tag_string", gridmodelList.get( i ).getTag( )  );
                    view.getContext().startActivity( intent );
                }
            } );
            descriptonwe.setText( gridmodelList.get( i ).getDescription( ) );

            price.setText( gridmodelList.get( i ).getPrice( ));


        } else {
            view = convertView;
        }
        return view;
    }
}
