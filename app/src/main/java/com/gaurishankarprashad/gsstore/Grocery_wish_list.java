package com.gaurishankarprashad.gsstore;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.GroceryProductAdapter;
import com.gaurishankarprashad.gsstore.Models.GroceryProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Grocery_wish_list extends AppCompatActivity {

    private RecyclerView wishListRecycler;
    private Toolbar toolbar;
    public static GroceryProductAdapter groceryProductAdapter;
    public static List<GroceryProductModel> groceryProductWishListModel;
    private TextView no_item_txt;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_grocery_wish_list );


        toolbar = findViewById( R.id.grocery_wishList );
        int layout_code=getIntent().getIntExtra( "layout_code",-1 );
        String name=getIntent().getStringExtra( "name" );
        toolbar.setTitle( name );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        setSupportActionBar( toolbar );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        wishListRecycler = findViewById( R.id.wishListRecycler );
        no_item_txt=findViewById( R.id.no_item_txt );

        loadingDialog= new Dialog( Grocery_wish_list.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show();






        if(layout_code==1){


            groceryProductWishListModel = new ArrayList<>( );

            groceryProductAdapter = new GroceryProductAdapter( groceryProductWishListModel );
            GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext( ), 2 );
            wishListRecycler.setLayoutManager( gridLayoutManager );

            wishListRecycler.setAdapter( groceryProductAdapter );

            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).orderBy( "offer", Query.Direction.DESCENDING ).get()
                    .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful( )) {


                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    if(!documentSnapshot.get( "offer" ).toString( ).equals( "0" )) {
                                        groceryProductWishListModel.add( new GroceryProductModel( documentSnapshot.get( "image_01" ).toString( )
                                                , documentSnapshot.get( "name" ).toString( )
                                                , documentSnapshot.get( "cut_price" ).toString( )
                                                , documentSnapshot.get( "offer" ).toString( )
                                                , documentSnapshot.get( "price" ).toString( )
                                                , documentSnapshot.get( "rating" ).toString( )
                                                , documentSnapshot.get( "review_count" ).toString( )
                                                , (long) documentSnapshot.get( "in_stock" )
                                                , documentSnapshot.getId( )
                                                , documentSnapshot.get( "relevant_tag" ).toString( ) ) );
                                    }
                                }

                                loadingDialog.dismiss();
                                groceryProductAdapter.notifyDataSetChanged( );


                            }

                        }
                    } );







        }else {

            int size = DBquaries.grocery_wishList.size( );

            groceryProductWishListModel = new ArrayList<>( );


            groceryProductAdapter = new GroceryProductAdapter( groceryProductWishListModel );
            GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext( ), 2 );
            wishListRecycler.setLayoutManager( gridLayoutManager );
            wishListRecycler.setAdapter( groceryProductAdapter );

            if(size==0){
                no_item_txt.setVisibility( View.VISIBLE );
            }else {
                no_item_txt.setVisibility( View.GONE );

            }

            for (int x = 0; x < size; x++) {
                String id = DBquaries.grocery_wishList.get( x );

                FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( id ).get( )
                        .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful( )) {
                                    groceryProductWishListModel.add( new GroceryProductModel( task.getResult( ).get( "image_01" ).toString( )
                                            , task.getResult( ).get( "name" ).toString( )
                                            , task.getResult( ).get( "cut_price" ).toString( )
                                            , task.getResult( ).get( "offer" ).toString( )
                                            , task.getResult( ).get( "price" ).toString( )
                                            , task.getResult( ).get( "rating" ).toString( )
                                            , task.getResult( ).get( "review_count" ).toString( )
                                            , (long) task.getResult( ).get( "in_stock" )
                                            , task.getResult( ).getId( )
                                            ,task.getResult().get( "relevant_tag" ).toString( )) );



                                }
                                groceryProductAdapter.notifyDataSetChanged( );

                            }
                        } );


            }

            loadingDialog.dismiss();
            groceryProductAdapter.notifyDataSetChanged( );

        }




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId( ) == android.R.id.home) {
            finish( );
        }

        return super.onOptionsItemSelected( item );
    }


}
