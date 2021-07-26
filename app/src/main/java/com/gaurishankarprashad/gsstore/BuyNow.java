package com.gaurishankarprashad.gsstore;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BuyNow extends AppCompatActivity {


    private TextView name, description, price, offer, cutPrice;
    private ImageView image;
    private TextView addproduct, subproduct, product_count;
    private Toolbar toolbar;
    private static String otp = new DecimalFormat( "000000" ).format( new Random( ).nextInt( 999999 ) );

    private ConstraintLayout buynow;
    private TextView priceIncart;
    private static TextView tax;
    private static TextView grandTotal;
    private static TextView totalSave, payAmount;
    private final String CHANNEL_ID = "ai";

    private Button order_Btn;

    private long order_no[] = {0};

    private int count[] = {1};
    private int calculaePrice = 0, calculateSave = 0;

    private static int DELIVERY_CHARGES = 0;
    private int Pprice = 0, Pcutprice = 0;
    private static String product_id = "";
    private CheckBox pickUpCheck;
    private Dialog loadingDialog;
    private ConstraintLayout address_layout;

    private static String mName = "", mPrice = "", mcutPrice = "", mdescription = "", moffer = "", mimage = "";


    ///address

    private TextView addresss_name, address_details, address_phn, edit_address, adderss_type;
    private String user_address_details = "";
    private String user_name = "";
    private long previous_position = 0;
    private String user_phn = "";
    private String user_address_type = "";

    ///address
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_buy_now );


        buynow = findViewById( R.id.buy_now_activity );

        addresss_name = findViewById( R.id.grocery_cart_address_name );
        address_details = findViewById( R.id.grocery_cart_address_hostel_room );
        address_phn = findViewById( R.id.grocery_cart_address_phone );
        edit_address = findViewById( R.id.grocery_cart_address_editTxt );
        adderss_type = findViewById( R.id.grocery_cart_address_type );
        address_layout = findViewById( R.id.address_layout );


        priceIncart = findViewById( R.id.itemTotalPrice );
        tax = findViewById( R.id.taxChargesPrice );
        grandTotal = findViewById( R.id.grandTotalPrice );

        name = findViewById( R.id.grocery_cart_product_title );
        description = findViewById( R.id.grocery_cart_productDescription );
        price = findViewById( R.id.grocery_cart_productPrice );
        offer = findViewById( R.id.grocery_cart_product_offer );
        cutPrice = findViewById( R.id.grocery_cart_productcutprice );
        image = findViewById( R.id.grocery_cart_productImage );
        addproduct = findViewById( R.id.grocery_cart_noCountAdd );
        subproduct = findViewById( R.id.grocery_cart_noCountSubtract );
        product_count = findViewById( R.id.grocery_cart_noCount );
        totalSave = findViewById( R.id.grocery_cart_totalSave );
        payAmount = findViewById( R.id.grocery_cart_payAmount );
        order_Btn = findViewById( R.id.myCartGroceryPayinCashPayment );
        pickUpCheck = findViewById( R.id.pickUpCheck );
        toolbar = findViewById( R.id.toolbar_buy_now );
        loadingDialog = new Dialog( BuyNow.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show( );


        buynow.setVisibility( View.INVISIBLE );

        toolbar.setTitle( "Buy Now" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );

        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );

        product_id = getIntent( ).getStringExtra( "product_id" );
        final int stock = Integer.parseInt( getIntent( ).getStringExtra( "stock" ) );

        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful( )) {
                            user_address_details = task.getResult( ).get( "address_details" ).toString( );
                            user_name = task.getResult( ).get( "fullname" ).toString( );
                            previous_position = (long) task.getResult( ).get( "previous_position" );
                            user_phn = task.getResult( ).get( "phone" ).toString( );
                            user_address_type = task.getResult( ).get( "address_type" ).toString( );


                            if (user_address_details.isEmpty( )) {
                                address_layout.setVisibility( View.GONE );
                            }
                            addresss_name.setText( user_name );
                            address_phn.setText( user_phn );
                            address_details.setText( user_address_details );
                            adderss_type.setText( user_address_type );


                        }


                    }
                } );


        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( product_id ).get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful( )) {

                    String p = task.getResult( ).get( "price" ).toString( );
                    String cp = task.getResult( ).get( "cut_price" ).toString( );

                    mPrice = task.getResult( ).get( "price" ).toString( );
                    mcutPrice = task.getResult( ).get( "cut_price" ).toString( );
                    mdescription = task.getResult( ).get( "description_01" ).toString( );
                    mimage = task.getResult( ).get( "image_01" ).toString( );
                    mName = task.getResult( ).get( "name" ).toString( );
                    moffer = task.getResult( ).get( "offer" ).toString( );


                    name.setText( task.getResult( ).get( "name" ).toString( ) );
                    description.setText( task.getResult( ).get( "description" ).toString( ) );

                    price.setText( "₹" + p + "/-" );
                    Pprice = Integer.parseInt( p );
                    Pcutprice = Integer.parseInt( cp );
                    cutPrice.setText( "₹" + cp + "/-" );
                    offer.setText( task.getResult( ).get( "offer" ).toString( ) + " off" );
                    if (task.getResult( ).get( "offer" ).toString( ).equals( "0" )) {
                        cutPrice.setVisibility( View.GONE );
                        offer.setVisibility( View.GONE );
                    }
                    Glide.with( BuyNow.this ).load( task.getResult( ).get( "image_01" ).toString( ) ).into( image );
                    priceIncart.setText( p );
                    calculaePrice = calculaePrice + Integer.parseInt( p );
                    calculateSave = calculateSave + Integer.parseInt( cp ) - Integer.parseInt( p );
                    totalSave.setText( "₹" + String.valueOf( calculateSave ) );
                    setTax( calculaePrice );


                    buynow.setVisibility( View.VISIBLE );
                    loadingDialog.dismiss( );


                }
            }
        } );


        edit_address.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( BuyNow.this, MyAddress.class );
                intent.putExtra( "previous_position", previous_position );
                startActivity( intent );


            }
        } );

        product_count.setText( String.valueOf( count[0] ) );


        addproduct.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if (count[0] < stock) {
                    loadingDialog.show( );

                    count[0] = count[0] + 1;
                    product_count.setText( String.valueOf( count[0] ) );
                    subproduct.setVisibility( View.VISIBLE );
                    calculaePrice = calculaePrice + Pprice;
                    setTax( calculaePrice );
                    calculateSave = calculateSave + Pcutprice - Pprice;
                    totalSave.setText( "₹" + String.valueOf( calculateSave ) );
                    priceIncart.setText( String.valueOf( calculaePrice ) );
                    loadingDialog.dismiss( );

                } else {
                    Toast.makeText( BuyNow.this, "Max in stock", Toast.LENGTH_SHORT ).show( );
                }

            }
        } );


        subproduct.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                loadingDialog.show( );
                if (count[0] > 1) {

                    count[0] = count[0] - 1;
                    product_count.setText( String.valueOf( count[0] ) );
                    calculaePrice = calculaePrice - Pprice;
                    setTax( calculaePrice );
                    calculateSave = calculateSave - Pcutprice + Pprice;
                    priceIncart.setText( String.valueOf( calculaePrice ) );
                    totalSave.setText( "₹" + String.valueOf( calculateSave ) );
                    loadingDialog.dismiss( );
                } else {
                    subproduct.setVisibility( View.GONE );
                    loadingDialog.dismiss( );

                }
            }
        } );

        pickUpCheck.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setTax( calculaePrice );
            }
        } );


        order_Btn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                if (address_layout.getVisibility( ) == View.GONE) {

                    Intent intent = new Intent( BuyNow.this, address.class );
                    intent.putExtra( "position", 0 );
                    startActivity( intent );

                } else {
                    if (DBquaries.MIN_ORDER_AMOUNT > calculaePrice + Integer.parseInt( tax.getText( ).toString( ) )) {
                        Toast.makeText( BuyNow.this, "Min Order Amount is ₹" + String.valueOf( DBquaries.MIN_ORDER_AMOUNT ), Toast.LENGTH_SHORT ).show( );
                    } else {

                        loadingDialog.show( );
                        order_no[0] = order_no[0] + 1;
                        Date dNow = new Date( );
                        SimpleDateFormat ft = new SimpleDateFormat( "yyMMddhhmmssMs" );
                        String datetime = ft.format( dNow );
                        String Order_Id = datetime + order_no[0];

                        addOrderDetails( Order_Id, "POD", false, stock );

                    }


                }


            }
        } );


    }

    private void addOrderDetails(final String order_id, String Payment_Mode, boolean Payment_Status, final int Stock) {

        Map<String, Object> OrderDetails = new HashMap<>( );
        OrderDetails.put( "name", mName );
        OrderDetails.put( "product_id", product_id );
        OrderDetails.put( "price", mPrice );
        OrderDetails.put( "cut_price", mcutPrice );
        OrderDetails.put( "offer", moffer );
        OrderDetails.put( "description", mdescription );
        OrderDetails.put( "image", mimage );
        OrderDetails.put( "item_count", String.valueOf( count[0] ) );
        OrderDetails.put( "user_name", user_name );
        OrderDetails.put( "user_phn", user_phn );
        OrderDetails.put( "user_address_details", user_address_details );
        OrderDetails.put( "user_address_type", user_address_type );
        OrderDetails.put( "user_id", FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) );
        OrderDetails.put( "payment_mode", Payment_Mode );
        OrderDetails.put( "delivery_status", false );
        OrderDetails.put( "is_cancled", false );
        OrderDetails.put( "delivery_time", "1" );
        OrderDetails.put( "payment_status", Payment_Status );
        OrderDetails.put( "review", "" );
        OrderDetails.put( "rating", "0" );
        OrderDetails.put( "otp", otp );
        OrderDetails.put( "is_pickUp", pickUpCheck.isChecked( ) );


        FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).collection( "ORDER_LIST" )
                .document( product_id ).set( OrderDetails ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful( )) {


                    Date dNow = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat( "yy/MM/dd hh:mm" );
                    String datetime = ft.format( dNow );
                    final Map<String, Object> details = new HashMap<>( );
                    details.put( "grand_total", String.valueOf( calculaePrice ) );
                    details.put( "id", order_id );
                    details.put( "otp", otp );
                    details.put( "tax", String.valueOf( DELIVERY_CHARGES ) );
                    details.put( "time", datetime );
                    details.put( "is_pickUp", pickUpCheck.isChecked( ) );
                    details.put( "is_paid", false );

                    FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).set( details ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful( )) {

                            }
                        }
                    } );

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder( BuyNow.this, CHANNEL_ID )
                                    .setSmallIcon( R.mipmap.ic_launcher )
                                    .setContentTitle( "Thank You" )
                                    .setContentText( "for placing order. Tap here for more details." );

                    Intent notificationIntent = new Intent( BuyNow.this, MyOrderGrocery.class );
                    notificationIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

                    PendingIntent contentIntent = PendingIntent.getActivity( BuyNow.this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT );
                    builder.setContentIntent( contentIntent );

                    NotificationManager manager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                    manager.notify( 0, builder.build( ) );


                    final Map<String, Object> orderId = new HashMap<>( );
                    orderId.put( "order_id_" + DBquaries.grocery_OrderList.size( ), order_id );
                    orderId.put( "list_size", DBquaries.grocery_OrderList.size( ) + 1 );

                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                            .collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).update( orderId )
                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful( )) {
                                        DBquaries.grocery_OrderList.add( order_id );
                                        Map<String, Object> Size = new HashMap<>( );
                                        Size.put( "list_size", 0 );
                                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                                                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).set( Size )
                                                .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful( )) {
                                                            Map<String, Object> id = new HashMap<>( );
                                                            id.put( "user_name", FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) );
                                                            id.put( "order_id", order_id );
                                                            id.put( "review", "" );
                                                            id.put( "rating", "0" );
                                                            FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( product_id ).collection( "REVIEWS" ).document( order_id )
                                                                    .set( id ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful( )) {
                                                                        Map<String, Object> stockUpdate = new HashMap<>( );
                                                                        stockUpdate.put( "in_stock", Stock - count[0] );

                                                                        Toast.makeText( BuyNow.this, "Your Order Is Placed", Toast.LENGTH_SHORT ).show( );

                                                                        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( product_id ).update( stockUpdate ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful( )) {

                                                                                    PhoneAuthProvider.getInstance( ).verifyPhoneNumber(
                                                                                            "+91" + DBquaries.ADMIN_NO,        // Phone number to verify
                                                                                            60,                 // Timeout duration
                                                                                            TimeUnit.SECONDS,   // Unit of timeout
                                                                                            TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                                                                                            mCallbacks );

                                                                                    loadingDialog.dismiss( );
                                                                                    Intent intent = new Intent( BuyNow.this, Home.class );
                                                                                    startActivity( intent );
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
                            } );


                }
            }
        } );


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks( ) {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent( s, forceResendingToken );
            // verificarionCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String Code = phoneAuthCredential.getSmsCode( );
            if (Code != null) {


            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText( BuyNow.this, e.getMessage( ), Toast.LENGTH_SHORT ).show( );
        }
    };


    public void setTax(final int PRICE_IN_CART_GROCERY) {
        final int size = DBquaries.tax_condition.size( );

        DELIVERY_CHARGES = 0;

        if (size == 0) {

            DELIVERY_CHARGES = 0;
            tax.setText( "0" );
            grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
            payAmount.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
        }

        if (size == 1) {
            if (pickUpCheck.isChecked( )) {
                DELIVERY_CHARGES = 0;
                tax.setText( "0" );
                grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
            } else {
                if (PRICE_IN_CART_GROCERY < DBquaries.tax_condition.get( 0 )) {
                    DELIVERY_CHARGES = DBquaries.tax_price.get( 0 );
                    tax.setText( String.valueOf( DELIVERY_CHARGES ) );
                    grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                    payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );


                } else {
                    DELIVERY_CHARGES = DBquaries.tax_price.get( 1 );

                    tax.setText( String.valueOf( DELIVERY_CHARGES ) );
                    grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                    payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                }
            }


        }
        if (size == 2) {

            if (pickUpCheck.isChecked( )) {
                DELIVERY_CHARGES = 0;
                tax.setText( "0" );
                grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
            } else {

                if (PRICE_IN_CART_GROCERY <= DBquaries.tax_condition.get( 0 )) {
                    DELIVERY_CHARGES = DBquaries.tax_price.get( 0 );
                    grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                    tax.setText( String.valueOf( DELIVERY_CHARGES ) );
                    payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                }

                if (DBquaries.tax_condition.get( 0 ) < PRICE_IN_CART_GROCERY) {
                    if (PRICE_IN_CART_GROCERY <= DBquaries.tax_condition.get( 1 )) {
                        DELIVERY_CHARGES = DBquaries.tax_price.get( 1 );
                        tax.setText( String.valueOf( DELIVERY_CHARGES ) );
                        grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                        payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );

                    }
                }
                if (PRICE_IN_CART_GROCERY > DBquaries.tax_condition.get( 1 )) {
                    DELIVERY_CHARGES = DBquaries.tax_price.get( 2 );
                    tax.setText( String.valueOf( DELIVERY_CHARGES ) );
                    grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                    payAmount.setText( "₹" + String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );

                }
            }

        }


    }

    @Override
    public void onRestart() {
        super.onRestart( );
        finish( );
        startActivity( getIntent( ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId( ) == android.R.id.home) {

            finish( );
        }


        return super.onOptionsItemSelected( item );


    }

}
