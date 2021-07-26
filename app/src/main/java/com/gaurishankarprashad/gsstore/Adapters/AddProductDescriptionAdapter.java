package com.gaurishankarprashad.gsstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Models.AddProductDescriptionModel;
import com.gaurishankarprashad.gsstore.R;

import java.util.List;

public class AddProductDescriptionAdapter extends RecyclerView.Adapter<AddProductDescriptionAdapter.ViewHolder> {


    private List<AddProductDescriptionModel> addProductDescriptionModelList;

    public AddProductDescriptionAdapter(List<AddProductDescriptionModel> addProductDescriptionModelList) {
        this.addProductDescriptionModelList = addProductDescriptionModelList;
    }

    @NonNull
    @Override
    public AddProductDescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.add_product_description_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductDescriptionAdapter.ViewHolder holder, int position) {

        String content=addProductDescriptionModelList.get( position ).getDescContent();

        holder.setData(content);

    }

    @Override
    public int getItemCount() {
        return addProductDescriptionModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView content;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            content=itemView.findViewById( R.id.add_product_desc_content );


        }

        public void setData( String Content) {


            content.setText( Content );

        }
    }
}
