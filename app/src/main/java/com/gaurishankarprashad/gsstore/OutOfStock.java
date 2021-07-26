package com.gaurishankarprashad.gsstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.GroceryProductAdapter;
import com.gaurishankarprashad.gsstore.Models.GroceryProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OutOfStock extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    public static GroceryProductAdapter groceryProductAdapter;
    public static List<GroceryProductModel> groceryProductWishListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_out_of_stock );

        toolbar=findViewById( R.id.grocery_outfStock );


        toolbar.findViewById( R.id.grocery_outfStock );
        toolbar.setTitle( "Out Of Stock" );
        setSupportActionBar( toolbar );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        recyclerView = findViewById( R.id.outfStock_Recycler);


        groceryProductWishListModel = new ArrayList<>( );

        groceryProductAdapter = new GroceryProductAdapter( groceryProductWishListModel );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext( ), 2 );
        recyclerView.setLayoutManager( gridLayoutManager );

        recyclerView.setAdapter( groceryProductAdapter );

        FirebaseFirestore.getInstance().collection( "PRODUCTS" ).orderBy( "in_stock", Query.Direction.ASCENDING ).get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful( )) {

                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                groceryProductWishListModel.add( new GroceryProductModel( documentSnapshot.get( "image_01" ).toString( )
                                        , documentSnapshot.get( "name" ).toString( )
                                        , documentSnapshot.get( "cut_price" ).toString( )
                                        , documentSnapshot.get( "offer" ).toString( )
                                        , documentSnapshot.get( "price" ).toString( )
                                        , documentSnapshot.get( "rating" ).toString( )
                                        , documentSnapshot.get( "review_count" ).toString( )
                                        , (long) documentSnapshot.get( "in_stock" )
                                        , documentSnapshot.getId( )
                                ,documentSnapshot.get( "relevant_tag" ).toString( )) );

                            }

                            groceryProductAdapter.notifyDataSetChanged( );


                        }

                    }
                } );
    }
}
