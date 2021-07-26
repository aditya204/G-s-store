package com.gaurishankarprashad.gsstore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView product_recycler;
    public static GroceryProductAdapter groceryProductAdapter;
    private List<GroceryProductModel> groceryProductModel;
    public static TextView groceryProduct_cartItemCount;
    private FloatingActionButton addProductFAB;
    private Dialog loadingDialog;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product );


        product_recycler = findViewById( R.id.grocery_product_recycler );
        toolbar = findViewById( R.id.toolbar_grocery_product );
        groceryProduct_cartItemCount = findViewById( R.id.grocery_product_cartItemCount );
        addProductFAB = findViewById( R.id.grocery_add_product_FAB );
        loadingDialog= new Dialog( Product.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show();

        toolbar.setTitle( "Products" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );


        int from_grocery=getIntent().getIntExtra( "from_grocery_category",-1 );
        final String category=getIntent().getStringExtra( "category" );



        DBquaries.setCartCount();






//
       if (DBquaries.IS_ADMIN) {
            addProductFAB.setVisibility( View.VISIBLE );
            addProductFAB.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( Product.this,AddProduct.class );
                    intent.putExtra( "category",category );
                    startActivity( intent );
                }
            } );

       }




        groceryProductModel = new ArrayList<>( );


        groceryProductAdapter = new GroceryProductAdapter( groceryProductModel );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext( ), 2 );
        product_recycler.setLayoutManager( gridLayoutManager );

        product_recycler.setAdapter( groceryProductAdapter );






        if(from_grocery==1){
            FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).orderBy( "name" ).get( )
                    .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful( )) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {
                                    if(documentSnapshot.get( "category" ).toString().toLowerCase().equals( category )) {


                                        groceryProductModel.add( new GroceryProductModel( documentSnapshot.get( "image_01" ).toString( )
                                                , documentSnapshot.get( "name" ).toString( )
                                                , documentSnapshot.get( "cut_price" ).toString( )
                                                , documentSnapshot.get( "offer" ).toString( )
                                                , documentSnapshot.get( "price" ).toString( )
                                                , documentSnapshot.get( "rating" ).toString( )
                                                , documentSnapshot.get( "review_count" ).toString( )
                                                , (long) documentSnapshot.get( "in_stock" )
                                                , documentSnapshot.getId( )
                                               , documentSnapshot.get( "relevant_tag" ).toString( )) );
                                        loadingDialog.dismiss( );

                                    }
                                    groceryProductAdapter.notifyDataSetChanged( );
                                }


                                groceryProductAdapter.notifyDataSetChanged( );

                            }

                        }
                    } );




        }
        if(from_grocery==2){

            ArrayList<String>  index= (ArrayList<String>) getIntent().getSerializableExtra( "ids" );

            for(int x = 0; x<index.size(); x++){
                String id=index.get( x )   ;
                FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( id ).get()
                        .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){

                                    groceryProductModel.add( new GroceryProductModel( task.getResult().get( "image_01" ).toString( )
                                            ,  task.getResult().get( "name" ).toString( )
                                            ,  task.getResult().get( "cut_price" ).toString( )
                                            ,  task.getResult().get( "offer" ).toString( )
                                            ,  task.getResult().get( "price" ).toString( )
                                            ,  task.getResult().get( "rating" ).toString( )
                                            ,  task.getResult().get( "review_count" ).toString( )
                                            , (long)  task.getResult().get( "in_stock" )
                                            ,  task.getResult().getId( )
                                            ,task.getResult().get( "relevant_tag" ).toString( )) );
                                    loadingDialog.dismiss();
                                }
                                groceryProductAdapter.notifyDataSetChanged( );

                            }
                        } );



            }
            groceryProductAdapter.notifyDataSetChanged( );






        }
        if(from_grocery== -1) {
            FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).orderBy( "name" ).get( )
                    .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful( )) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {


                                    groceryProductModel.add( new GroceryProductModel( documentSnapshot.get( "image_01" ).toString( )
                                            , documentSnapshot.get( "name" ).toString( )
                                            , documentSnapshot.get( "cut_price" ).toString( )
                                            , documentSnapshot.get( "offer" ).toString( )
                                            , documentSnapshot.get( "price" ).toString( )
                                            , documentSnapshot.get( "rating" ).toString( )
                                            , documentSnapshot.get( "review_count" ).toString( )
                                            , (long) documentSnapshot.get( "in_stock" )
                                            , documentSnapshot.getId( )
                                           , documentSnapshot.get( "relevant_tag" ).toString( )) );
                                    loadingDialog.dismiss();


                                }
                                groceryProductAdapter.notifyDataSetChanged( );

                            }

                        }
                    } );



        }


    }


    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater( );
        inflater.inflate( R.menu.search_and_cart_icon, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId( );

        if (item.getItemId( ) == android.R.id.home) {

            finish();
        }


        if (id == R.id.productcartMenu) {

            Intent intent = new Intent( Product.this, GroceryCart.class );
            startActivity( intent );

        }
        if (id == R.id.productsearchMenu) {

             Intent intent = new Intent( Product.this, Search.class );
             startActivity( intent );
             finish();

        }


        return super.onOptionsItemSelected( item );


    }
}
