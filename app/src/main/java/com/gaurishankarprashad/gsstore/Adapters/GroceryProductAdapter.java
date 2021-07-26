package com.gaurishankarprashad.gsstore.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.AddProduct;
import com.gaurishankarprashad.gsstore.DBquaries;
import com.gaurishankarprashad.gsstore.Models.GroceryProductModel;
import com.gaurishankarprashad.gsstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroceryProductAdapter extends RecyclerView.Adapter<GroceryProductAdapter.ViewHolder> {

    private List<GroceryProductModel> groceryProductModelList;



    public GroceryProductAdapter(List<GroceryProductModel> groceryProductModelList) {
        this.groceryProductModelList = groceryProductModelList;
    }

    public List<GroceryProductModel> getGroceryProductModelList() {
        return groceryProductModelList;
    }

    public void setGroceryProductModelList(List<GroceryProductModel> groceryProductModelList) {
        this.groceryProductModelList = groceryProductModelList;
    }

    @NonNull
    @Override
    public GroceryProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.grocery_home_product_item, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryProductAdapter.ViewHolder holder, int position) {
        String name = groceryProductModelList.get( position ).getName( );

        String offerType = groceryProductModelList.get( position ).getOffertype( );
        String offerAmount = groceryProductModelList.get( position ).getOfferAmount( );
        String price = groceryProductModelList.get( position ).getPrice( );
        String rating = groceryProductModelList.get( position ).getRating( );
        String reviewCount = groceryProductModelList.get( position ).getReviewCount( );
        String id = groceryProductModelList.get( position ).getId( );
        String tag=groceryProductModelList.get( position ).getTag_list();


        long inStock = groceryProductModelList.get( position ).getStock( );
        String image = groceryProductModelList.get( position ).getImage( );


        //   holder.checkAdmin( product_code );

        holder.setData( name, offerType, price, offerAmount, inStock, rating, reviewCount, image, id,tag );


    }

    @Override
    public int getItemCount() {
        return groceryProductModelList.size( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, offer, cutprice, price, rating, reviewCount,no_of_stock;
        private ImageView image;
        private FloatingActionButton favouriteButton;
        private LinearLayout rating_ll;
        private ConstraintLayout constraintLayout;
        private boolean ADDED_towishList = false;

        public ViewHolder(@NonNull final View itemView) {
            super( itemView );

            name = itemView.findViewById( R.id.grocery_home_product_Name );

            offer = itemView.findViewById( R.id.grocery_home_product_Offer );
            cutprice = itemView.findViewById( R.id.grocery_home_product_CutPrice );
            price = itemView.findViewById( R.id.grocery_home_product_Price );
            image = itemView.findViewById( R.id.grocery_home_productImage );
            favouriteButton = itemView.findViewById( R.id.grocery_home_product_floating_action_button );
            no_of_stock=itemView.findViewById( R.id.no_of_stock );

            constraintLayout = itemView.findViewById( R.id.grocery_home_product_layout );
            rating = itemView.findViewById( R.id.grocery_home_product_Rating );
            reviewCount = itemView.findViewById( R.id.grocery_home_product_ReviewCount );
            rating_ll=itemView.findViewById( R.id.grocery_home_product_Rating_LL );


        }





      /*  private void checkAdmin(final String ProductCode){

            String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(currentUser.equals( DBquaries.GROCERY_ADMIN_ID )){
                add.setVisibility( View.GONE );
                leftInstock.setVisibility( View.VISIBLE );
                constraintLayout.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent( itemView.getContext(),GroceryAddProduct.class );
                        intent.putExtra( "code",ProductCode );
                        itemView.getContext().startActivity( intent );
                    }
                } );


            }

        }*/

        @SuppressLint("RestrictedApi")
        private void setData(final String Name, String cutPrice, String Price, String OfferAmount, long instock, String Rating, String ReviewCount, String resource, final String Id, final String Tag) {
            if (DBquaries.IS_ADMIN) {
                favouriteButton.setVisibility( View.GONE );

                no_of_stock.setText( String.valueOf( instock ) );
                no_of_stock.setVisibility( View.VISIBLE );
                constraintLayout.setOnClickListener( new View.OnClickListener( ) {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( itemView.getContext( ), AddProduct.class );
                        intent.putExtra( "product_id", Id );
                        intent.putExtra( "layout_code", 1 );
                        itemView.getContext( ).startActivity( intent );
                    }
                } );
            } else {
                constraintLayout.setOnClickListener( new View.OnClickListener( ) {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( itemView.getContext( ), com.gaurishankarprashad.gsstore.GroceryProductDetails.class );
                        intent.putExtra( "product_id", Id );
                        intent.putExtra( "tag_string" ,Tag);
                        itemView.getContext( ).startActivity( intent );
                    }
                } );


            }

            price.setText( "₹"+Price+"/-" );
            cutprice.setText( "₹"+cutPrice+"/-" );


            name.setText( Name );
            offer.setText( OfferAmount + " off" );

            if (DBquaries.grocery_wishList.contains( Id )) {

                ADDED_towishList = true;
                favouriteButton.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );

            } else {
                ADDED_towishList = false;
                favouriteButton.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#9e9e9e" ) ) );
            }




            if (OfferAmount.equals( "0" )) {
                offer.setVisibility( View.GONE );
                cutprice.setVisibility( View.GONE );
            }


            if(Rating.length()==1){

                rating_ll.setVisibility( View.GONE );
                reviewCount.setVisibility( View.GONE );

            }
            rating.setText( Rating );
            reviewCount.setText( "(" + ReviewCount + ")" );

            Glide.with( itemView.getContext( ) ).load( resource ).into( image );


            favouriteButton.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {

                    favouriteButton.setClickable( false );
                    if (ADDED_towishList) {
                        ADDED_towishList = false;
                        favouriteButton.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#9e9e9e" ) ) );
                        ////remove from wish List

                        DBquaries.removeFromGroceryWishList( Id, itemView.getContext( ) );

                        favouriteButton.setClickable( true );

                    } else {
                        ADDED_towishList = true;
                        favouriteButton.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                        ////add to wish List
                        final Map<String, Object> updateListSize = new HashMap<>( );
                        updateListSize.put( "list_size", (long) DBquaries.grocery_wishList.size( ) + 1 );
                        Map<String, Object> product_Id = new HashMap<>( );
                        product_Id.put( "id_" + (long) DBquaries.grocery_wishList.size( ), Id );
                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" ).
                                update( product_Id ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful( )) {

                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                            .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" ).
                                            update( updateListSize ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful( )) {
                                                Toast.makeText( itemView.getContext( ), "Added to Wishlist", Toast.LENGTH_SHORT ).show( );
                                                DBquaries.grocery_wishList.add( Id );
                                                favouriteButton.setClickable( true );
                                            }

                                        }
                                    } );


                                }

                            }
                        } );

                    }


                }
            } );

        }


    }
}
