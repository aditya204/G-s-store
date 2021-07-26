package com.gaurishankarprashad.gsstore;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBquaries {


    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance( );
    ////grocery
    public static int TOTAL_DISCOUNT_GROCERY = 0;
    public static List<Integer> itemCount_grocery = new ArrayList<>( );
    public static List<String> itemName_grocery = new ArrayList<>( );
    public static List<String> itemCategory_grocery = new ArrayList<>( );
    public static int ITEMS_IN_CART_GROCERY = 0;
    public static List<String> product_list = new ArrayList<>( );
    public static List<String> product_id_list = new ArrayList<>( );
    public static List<String> product_tag_list = new ArrayList<>( );
    public static List<String> previous_list = new ArrayList<>( );

    public static List<String> grocery_wishList = new ArrayList<>( );
    public static List<String> grocery_CartList_product_id = new ArrayList<>( );
    public static List<String> grocery_CartList_product_count = new ArrayList<>( );
    public static List<String> grocery_CartList_product_OutOfStock = new ArrayList<>( );
    public static List<String> grocery_OrderList = new ArrayList<>( );
    public static List<Integer> tax_condition = new ArrayList<>( );
    public static List<Integer> tax_price = new ArrayList<>( );

    public static int PRICE_IN_CART_GROCERY = 0;
    public static int TOTAL_SAVE = 0;
    public static int DELIVERY_CHARGES = 0;
    public static boolean IS_ADMIN = false;
    public static int MIN_ORDER_AMOUNT = 0;
    public static String ADMIN_NO;
    public static String ADMIN_MAIL;




    public static void loadProductList() {
        product_list.clear( );
        previous_list.clear( );
        product_id_list.clear( );
        product_tag_list.clear( );
        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).orderBy( "name" ).get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful( )) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {

                        product_list.add( documentSnapshot.get( "name" ).toString( ) );
                        product_id_list.add( documentSnapshot.getId( ).toString( ) );
                        product_tag_list.add( documentSnapshot.get( "relevant_tag" ).toString( ) );

                    }

                }
            }
        } );

       /* FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).collection( "USER_DATA" )
                .document( "PREVIOUS_SEARCH" ).collection( "SEARCHES" ).document( "1" ).get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                           long list_size=(long)task.getResult().get( "list_size" );

                           for(long x=list_size-1;x>=0;x--){
                                previous_list.add( task.getResult().get( "search_"+x ).toString() );
                           }


                        }
                    }
                } );*/


    }

   public static void loadGroceryOrders() {
        grocery_OrderList.clear( );
        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                .collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            long size = (long) task.getResult( ).get( "list_size" );
                            for (long x = 0; x < size; x++) {
                                grocery_OrderList.add( task.getResult( ).get( "order_id_" + x ).toString( ) );
                            }


                        }

                    }
                } );

    }

    public static void loadGroceryWishList(final Context context) {

        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" )
                .get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful( )) {
                    for (long x = 0; x < (long) task.getResult( ).get( "list_size" ); x++) {

                        String id = task.getResult( ).get( "id_" + x ).toString( );
                        grocery_wishList.add( id );
                    }
                } else {
                    String error = task.getException( ).getMessage( );
                    Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );
                }
            }
        } );


    }

    public static void loadGroceryCartList(final Context context) {
        grocery_CartList_product_id.clear( );
        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                .get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful( )) {
                    for (long x = 0; x < (long) task.getResult( ).get( "list_size" ); x++) {

                        String id = task.getResult( ).get( "id_" + x ).toString( );
                        grocery_CartList_product_id.add( id );
                    }
                } else {
                    String error = task.getException( ).getMessage( );
                    Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );
                }
            }
        } );

    }

    public static void calcualtePriceGrocery(String todo, String amount) {
        if (todo.equals( "+" )) {
            DBquaries.PRICE_IN_CART_GROCERY = DBquaries.PRICE_IN_CART_GROCERY + Integer.parseInt( amount );
            GroceryCart.priceIncart.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );

        } else {
            DBquaries.PRICE_IN_CART_GROCERY = DBquaries.PRICE_IN_CART_GROCERY - Integer.parseInt( amount );
            GroceryCart.priceIncart.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );

        }


    }

    public static void calculateTotalSave(String todo, String price, String cutPrice) {

        if (todo.equals( "+" )) {
            TOTAL_SAVE = TOTAL_SAVE + Integer.parseInt( cutPrice ) - Integer.parseInt( price );
            GroceryCart.totalSave.setText( "₹" + TOTAL_SAVE );
        } else {
            TOTAL_SAVE = TOTAL_SAVE - Integer.parseInt( cutPrice ) + Integer.parseInt( price );
            GroceryCart.totalSave.setText( "₹" + String.valueOf( TOTAL_SAVE ) );

        }
    }

    public static void chechAdmin() {
        FirebaseFirestore.getInstance( ).collection( "ADMIN" ).document( "dDlPGvTZ7MvX4BegYbi3" ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {

                            MIN_ORDER_AMOUNT = Integer.parseInt( task.getResult( ).get( "min_order_amount" ).toString( ) );

                            ADMIN_NO = task.getResult( ).get( "phone_no" ).toString( );
                            ADMIN_MAIL=task.getResult().get( "mail" ).toString();
                            String id = task.getResult( ).get( "id" ).toString( );

                            if (id.equals( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )) {
                                Home.OutOfStock.setVisibility( View.VISIBLE );
                                IS_ADMIN = true;
                                Home.Orders.setVisibility( View.VISIBLE );

                            } else {
                                IS_ADMIN = false;
                            }
                        }
                    }
                } );
    }

    public static void loadTax() {

        FirebaseFirestore.getInstance( ).collection( "ADMIN" ).document( "dDlPGvTZ7MvX4BegYbi3" ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            tax_condition.clear( );
                            long no_of_condition = (long) task.getResult( ).get( "no_of_condition" );

                            for (long x = 1; x < no_of_condition + 1; x++) {
                                tax_condition.add( Integer.parseInt( task.getResult( ).get( "tax_condition_" + x ).toString( ) ) );
                            }
                            for (long x = 1; x < no_of_condition + 2; x++) {
                                tax_price.add( Integer.parseInt( task.getResult( ).get( "tax_price_" + x ).toString( ) ) );
                            }
                        }

                    }
                } );


    }

    public static void setTax() {
        int size = tax_condition.size( );
        DELIVERY_CHARGES = 0;
        if (size == 0) {
            GroceryCart.tax.setText( "0" );
            DELIVERY_CHARGES = 0;
            GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );
            GroceryCart.payAmount.setText( "₹" + PRICE_IN_CART_GROCERY );
        }

        if (size == 1) {

            if (GroceryCart.pickUpCheck.isChecked( )) {
                GroceryCart.tax.setText( "0" );
                DELIVERY_CHARGES = 0;
                GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );
                GroceryCart.payAmount.setText( "₹" + PRICE_IN_CART_GROCERY );
            } else {


                if (PRICE_IN_CART_GROCERY < tax_condition.get( 0 )) {
                    DELIVERY_CHARGES = tax_price.get( 0 );
                    GroceryCart.tax.setText( String.valueOf( tax_price.get( 0 ) ) );
                    GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 0 ) ) );
                    GroceryCart.payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 0 ) ) );

                } else {
                    DELIVERY_CHARGES = tax_price.get( 1 );
                    GroceryCart.tax.setText( String.valueOf( tax_price.get( 1 ) ) );
                    GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 1 ) ) );
                    GroceryCart.payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 1 ) ) );


                }
            }

        }
        if (size == 2) {

            if (GroceryCart.pickUpCheck.isChecked( )) {
                GroceryCart.tax.setText( "0" );
                DELIVERY_CHARGES = 0;
                GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );
                GroceryCart.payAmount.setText( "₹" + PRICE_IN_CART_GROCERY );
            } else {


                if (PRICE_IN_CART_GROCERY <= tax_condition.get( 0 )) {
                    DELIVERY_CHARGES = tax_price.get( 0 );
                    GroceryCart.tax.setText( String.valueOf( tax_price.get( 0 ) ) );
                    GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 0 ) ) );
                    GroceryCart.payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 0 ) ) );

                }

                if (tax_condition.get( 0 ) < PRICE_IN_CART_GROCERY) {
                    if (PRICE_IN_CART_GROCERY <= tax_condition.get( 1 )) {
                        DELIVERY_CHARGES = tax_price.get( 1 );
                        GroceryCart.tax.setText( String.valueOf( tax_price.get( 1 ) ) );
                        GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 1 ) ) );
                        GroceryCart.payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 1 ) ) );


                    }
                }
                if (PRICE_IN_CART_GROCERY > tax_condition.get( 1 )) {
                    DELIVERY_CHARGES = tax_price.get( 2 );
                    GroceryCart.tax.setText( String.valueOf( tax_price.get( 2 ) ) );
                    GroceryCart.payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 2 ) ) );

                    GroceryCart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + tax_price.get( 2 ) ) );
                }

            }
        }


    }

    public static void removeFromGroceryWishList(String id, final Context context) {


        grocery_wishList.remove( id );


        final int size = grocery_wishList.size( );
        Map<String, Object> updateWishList = new HashMap<>( );
        if (size == 0) {

            Map<String, Object> Size = new HashMap<>( );
            Size.put( "list_size", 0 );

            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" )
                    .set( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {

                    }

                }
            } );


        }

        for (int x = 0; x < size; x++) {

            updateWishList.put( "id_" + x, grocery_wishList.get( x ) );

            final int finalX = x;
            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" )
                    .set( updateWishList ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {
                        Map<String, Object> Size = new HashMap<>( );
                        Size.put( "list_size", size );

                        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_WISHLIST" )
                                .update( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText( context, "removed", Toast.LENGTH_SHORT ).show( );
                                 }
                        } );

                    } else {
                        String error = task.getException( ).getMessage( );
                        Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );


                    }
                }
            } );

        }

    }

    public static void removeFromGroceryCartList(final String id, final Context context) {
        grocery_CartList_product_id.remove( id );

        final int size = grocery_CartList_product_id.size( );

        if (size == 0) {

            Map<String, Object> Size = new HashMap<>( );
            Size.put( "list_size", 0 );


            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                    .set( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {
                        DBquaries.grocery_CartList_product_id.clear( );
                        DBquaries.grocery_CartList_product_count.clear( );
                        Toast.makeText( context, "removed from cart", Toast.LENGTH_SHORT ).show( );
                        DBquaries.grocery_CartList_product_OutOfStock.remove( id );

                        GroceryProductDetails.gotoCart.setVisibility( View.GONE );
                        GroceryProductDetails.addtoCart.setVisibility( View.VISIBLE );


                    }


                }
            } );

        }
        for (int x = 0; x < size; x++) {
            Map<String, Object> updateWishList = new HashMap<>( );
            updateWishList.put( "id_" + x, grocery_CartList_product_id.get( x ) );

            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                    .set( updateWishList ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {
                        Map<String, Object> Size = new HashMap<>( );
                        Size.put( "list_size", size );

                        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                                .update( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful( )) {


                                    Toast.makeText( context, "removed from cart", Toast.LENGTH_SHORT ).show( );
                                    DBquaries.grocery_CartList_product_OutOfStock.remove( id );
                                    GroceryProductDetails.gotoCart.setVisibility( View.GONE );
                                    GroceryProductDetails.addtoCart.setVisibility( View.VISIBLE );


                                }


                            }
                        } );

                    } else {
                        String error = task.getException( ).getMessage( );
                        Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );


                    }
                }
            } );


        }


    }

    public static void setCartCount() {

        if (DBquaries.grocery_CartList_product_id.size( ) == 0) {
            Product.groceryProduct_cartItemCount.setVisibility( View.GONE );

        } else {
            Product.groceryProduct_cartItemCount.setVisibility( View.VISIBLE );
            Product.groceryProduct_cartItemCount.setText( String.valueOf( DBquaries.grocery_CartList_product_id.size( ) ) );

        }

    }
}
