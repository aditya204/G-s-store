package com.gaurishankarprashad.gsstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchedProduct extends AppCompatActivity {

    private RecyclerView seachedProductRecycler;
    private TextView noProductTxt;
    private static String name = "";
    private static List<GroceryProductModel> list = new ArrayList<>( );
    private Toolbar toolbar;
    private FloatingActionButton addProductFAB;


    public static TextView searchedProduct_cartItemCount;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_searched_product );
        seachedProductRecycler = findViewById( R.id.searched_product_recycler );


        noProductTxt = findViewById( R.id.search_No_product_txt );
        addProductFAB = findViewById( R.id.grocery_search_add_product_FAB );

        toolbar = findViewById( R.id.toolbar_searched_product );
        toolbar.setTitle( "Products" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        searchedProduct_cartItemCount = findViewById( R.id.searched_product_cartItemCount );

        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        name = getIntent( ).getStringExtra( "tag_string" );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext( ), 2 );
        seachedProductRecycler.setLayoutManager( gridLayoutManager );


        if (DBquaries.IS_ADMIN) {
            addProductFAB.setVisibility( View.VISIBLE );
            addProductFAB.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( SearchedProduct.this,AddProduct.class );
                    startActivity( intent );
                }
            } );

        }

        if (DBquaries.grocery_CartList_product_id.size( ) == 0) {
            searchedProduct_cartItemCount.setVisibility( View.GONE );

        } else {
            searchedProduct_cartItemCount.setVisibility( View.VISIBLE );
            searchedProduct_cartItemCount.setText( String.valueOf( DBquaries.grocery_CartList_product_id.size( ) ) );

        }



        final List<String> ids = new ArrayList<>( );

        final Adapter adapter = new Adapter( list );
        seachedProductRecycler.setAdapter( adapter );


        final String[] tags = name.split( " " );
        for (final String tag : tags) {

            ids.clear( );
            list.clear( );
            FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).whereArrayContains( "tags", tag )
                    .get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful( )) {
                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull( task.getResult( ) )) {

                            GroceryProductModel model = new GroceryProductModel( Objects.requireNonNull( documentSnapshot.get( "image_01" ) ).toString( )
                                    , Objects.requireNonNull( documentSnapshot.get( "name" ) ).toString( )
                                    , documentSnapshot.get( "cut_price" ).toString( )
                                    , documentSnapshot.get( "offer" ).toString( )
                                    , documentSnapshot.get( "price" ).toString( )
                                    , documentSnapshot.get( "rating" ).toString( )
                                    , documentSnapshot.get( "review_count" ).toString( )
                                    , (long) documentSnapshot.get( "in_stock" )
                                    , documentSnapshot.getId( )
                            ,documentSnapshot.get( "relevant_tag" ).toString( ));

                            model.setTags( (ArrayList<String>) documentSnapshot.get( "tags" ) );

                            if (!ids.contains( model.getId( ) )) {
                                list.add( model );
                                ids.add( model.getId( ) );
                            }
                        }

                        if (tag.equals( tags[tags.length - 1] )) {
                            if (list.size( ) == 0) {
                                seachedProductRecycler.setVisibility( View.GONE );
                                noProductTxt.setVisibility( View.VISIBLE );
                            } else {
                                seachedProductRecycler.setVisibility( View.VISIBLE );
                                noProductTxt.setVisibility( View.GONE );
                                adapter.getFilter( ).filter( name );

                            }
                        }


                    } else {
                        String e = task.getException( ).getMessage( );
                        Toast.makeText( SearchedProduct.this, e, Toast.LENGTH_SHORT ).show( );
                    }
                }
            } );
        }


    }

    public class Adapter extends GroceryProductAdapter implements Filterable {
        private List<GroceryProductModel> originalList;

        public Adapter(List<GroceryProductModel> groceryProductModelList) {
            super( groceryProductModelList );
            originalList = groceryProductModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter( ) {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults( );
                    List<GroceryProductModel> filterList = new ArrayList<>( );

                    final String[] tags = name.toLowerCase( ).split( " " );

                    ArrayList<String> presentTags = new ArrayList<>( );


                    for (GroceryProductModel model : originalList) {


                        for (String tag : tags) {

                            if (model.getTags( ).contains( tag )) {

                                presentTags.add( tag );


                            }

                            model.setTags( presentTags );


                        }
                    }
                    for (int i = tags.length; i > 0; i--) {
                        for (GroceryProductModel model : originalList) {
                            if (model.getTags( ).size( ) == i) {
                                filterList.add( model );
                            }
                        }
                    }
                    filterResults.values = filterList;
                    filterResults.count = filterList.size( );
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults.count > 0) {
                        setGroceryProductModelList( (List<GroceryProductModel>) filterResults.values );
                    }

                    notifyDataSetChanged( );
                }
            };
        }
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

            finish( );
        }


        if (id == R.id.productcartMenu) {

            Intent intent = new Intent( SearchedProduct.this, GroceryCart.class );
            startActivity( intent );

        }
        if (id == R.id.productsearchMenu) {

            Intent intent = new Intent( SearchedProduct.this, Search.class );
            startActivity( intent );
            finish( );

        }


        return super.onOptionsItemSelected( item );


    }

}
