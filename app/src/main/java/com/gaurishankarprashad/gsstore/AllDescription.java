package com.gaurishankarprashad.gsstore;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.grocery_product_details_descrioption_Adapter;
import com.gaurishankarprashad.gsstore.Models.grocery_product_details_descrioption_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AllDescription extends AppCompatActivity {


    private RecyclerView descriptionRecycler;
    private List<grocery_product_details_descrioption_Model> grocery_product_details_descrioption_modelList;
    public static LinearLayout viewAllDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_all_description );


        final String product_id = getIntent( ).getStringExtra( "product_id" );



        descriptionRecycler = findViewById( R.id.grocery_product_details_descrioption_Recycler );
        viewAllDescription=findViewById( R.id.description_ViewAll_LL );


        viewAllDescription.setVisibility( View.GONE );



        grocery_product_details_descrioption_modelList = new ArrayList<>( );


        final grocery_product_details_descrioption_Adapter grocery_product_details_descrioption_adapter = new grocery_product_details_descrioption_Adapter( grocery_product_details_descrioption_modelList );
        final LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        descriptionRecycler.setAdapter( grocery_product_details_descrioption_adapter );
        descriptionRecycler.setLayoutManager( layoutManager );

        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( product_id ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            long no_of_description = (long) task.getResult( ).get( "no_of_description" );

                            for (long x = 1; x < no_of_description + 1; x++) {

                                grocery_product_details_descrioption_modelList.add( new grocery_product_details_descrioption_Model( task.getResult( ).get( "description_0" + x ).toString( ) ) );
                            }

                        }

                        grocery_product_details_descrioption_adapter.notifyDataSetChanged();
                    }
                } );



    }
}
