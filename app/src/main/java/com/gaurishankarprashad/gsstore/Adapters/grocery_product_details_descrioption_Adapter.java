package com.gaurishankarprashad.gsstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Models.grocery_product_details_descrioption_Model;
import com.gaurishankarprashad.gsstore.R;

import java.util.List;

public class grocery_product_details_descrioption_Adapter extends RecyclerView.Adapter<grocery_product_details_descrioption_Adapter.ViewHolder> {

    private List<grocery_product_details_descrioption_Model> grocery_product_details_descrioption_modelList;

    public grocery_product_details_descrioption_Adapter(List<grocery_product_details_descrioption_Model> grocery_product_details_descrioption_modelList) {
        this.grocery_product_details_descrioption_modelList = grocery_product_details_descrioption_modelList;
    }

    @NonNull
    @Override
    public grocery_product_details_descrioption_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( parent.getContext() ).inflate( R.layout.grocery_product_details_description_item,parent,false);
        return new ViewHolder( view );    }

    @Override
    public void onBindViewHolder(@NonNull grocery_product_details_descrioption_Adapter.ViewHolder holder, int position) {

        String txt=grocery_product_details_descrioption_modelList.get( position ).getDescription();

        holder.setDescription( txt );
    }

    @Override
    public int getItemCount() {
        return grocery_product_details_descrioption_modelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView description_head,description_content;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            description_head=itemView.findViewById( R.id.grocery_product_details_descrioption__head );
            description_content=itemView.findViewById( R.id.grocery_product_details_descrioption__content );

        }

        private void setDescription(String Description ){
           String des[]=Description.split( "," );
           description_head.setText( des[0] );
           description_content.setText( des[1] );

        }
    }
}
