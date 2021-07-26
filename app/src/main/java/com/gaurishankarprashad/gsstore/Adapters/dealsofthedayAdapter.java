package com.gaurishankarprashad.gsstore.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.GroceryProductDetails;
import com.gaurishankarprashad.gsstore.Models.dealsofthedayModel;
import com.gaurishankarprashad.gsstore.R;

import java.util.List;

public class dealsofthedayAdapter extends RecyclerView.Adapter<dealsofthedayAdapter.ViewHolder> {


    private List<dealsofthedayModel> dealsofthedayModelList;

    public dealsofthedayAdapter(List<dealsofthedayModel> dealsofthedayModelList) {
        this.dealsofthedayModelList = dealsofthedayModelList;
    }

    @NonNull
    @Override
    public dealsofthedayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.dealsoftheday_recycler_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull dealsofthedayAdapter.ViewHolder holder, int position) {
        String title = dealsofthedayModelList.get( position ).getTitle( );
        String descri = dealsofthedayModelList.get( position ).getDescription( );
        String price = dealsofthedayModelList.get( position ).getPrice( );
        String image = dealsofthedayModelList.get( position ).getImage( );
        String id = dealsofthedayModelList.get( position ).getId( );
        String tag= dealsofthedayModelList.get( position ).getTag();

        holder.setData( image, descri, title, price, id,tag );

    }

    @Override
    public int getItemCount() {

        if (dealsofthedayModelList.size( ) >= 8) {
            return 8;
        } else {
            return dealsofthedayModelList.size( );
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title, description, price;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            image = itemView.findViewById( R.id.dealsofthedayitemimage );
            title = itemView.findViewById( R.id.dealsofthedayitemname );
            description = itemView.findViewById( R.id.dealsofthedayitedescription );
            price = itemView.findViewById( R.id.dealsofthedayitemprice );
        }


        private void setData(String resource, String Description, final String Title, String Price, final String id, final String tag) {

            Glide.with( itemView.getContext( ) ).load( resource ).into( image );
            title.setText( Title );
            price.setText( "₹" + Price + "/-" );
            description.setText( Description );
            image.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( view.getContext(), GroceryProductDetails.class );
                    intent.putExtra("product_id", id );
                    intent.putExtra("tag_string", tag );

                    view.getContext().startActivity( intent );
                }
            } );
        }


    }
}
