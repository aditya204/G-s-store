package com.gaurishankarprashad.gsstore;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gaurishankarprashad.gsstore.Adapters.GroceryProductAdapter;
import com.gaurishankarprashad.gsstore.Adapters.ReviewAdapter;
import com.gaurishankarprashad.gsstore.Adapters.grocery_product_details_descrioption_Adapter;
import com.gaurishankarprashad.gsstore.Adapters.productDetailsViewPagerAdapter;
import com.gaurishankarprashad.gsstore.Models.GroceryProductModel;
import com.gaurishankarprashad.gsstore.Models.ReviewModel;
import com.gaurishankarprashad.gsstore.Models.grocery_product_details_descrioption_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroceryProductDetails extends AppCompatActivity {
    private ViewPager imageViewPager;
    private TabLayout viewPagerIndicator;
    private FloatingActionButton addtoWishlist,cartFAB;
    public static boolean ADDED_towishList = false;
    private RecyclerView descriptionRecycler;
    private List<String> productImages;
    private int count[] ={0};
    private List<grocery_product_details_descrioption_Model> grocery_product_details_descrioption_modelList;

    private TextView out_of_stockText;
    private Button buyBtn;
    public static LinearLayout addtoCart,allReviews;
    public static LinearLayout gotoCart;

    public static LinearLayout viewAllDescription;
    public static ConstraintLayout descLayout,review_layout;
    public static String Pname="";
    private RecyclerView relevantProductRecycler;
    private LinearLayout rating_LL;
    private ConstraintLayout product_details_CL;


    /////productImage/nmae/price

    private TextView name, price, cutprice, offer, rating, reviewCount;



    /////productImage/nmae/price

    /////productRating
    private RecyclerView reviewRecycler;
    private List<ReviewModel> reviewModelList;
    private ReviewAdapter reviewAdapter;

    private TextView star_1_count;
    private TextView star_2_count;
    private TextView star_3_count;
    private TextView star_4_count;
    private TextView star_5_count;
    private TextView avg_rating;
    private TextView totalstarCount;
    private TextView totalreviewCount;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;
    final int[] one_star_count = {0};
    final int[] two_star_count = {0};
    final int[] five_star_count = {0};
    final int[] four_star_count = {0};
    final int[] three_star_count = {0};
    final int[] total_rating_count = {0};
    final int[] total_review_count = {0};
    final double[] avg_star = {0.00000};
    private Dialog loadingDialog;

    String in_stock="";






    /////productRating



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_grocery_product_details );

        buyBtn=findViewById( R.id.buy_now_groceryBtn );
        addtoWishlist = findViewById( R.id.addtoWishlist );
        imageViewPager = findViewById( R.id.grocery_product_image_VP );
        viewPagerIndicator = findViewById( R.id.grocery_product_image_VP_indicator );
        descriptionRecycler = findViewById( R.id.grocery_product_details_descrioption_Recycler );
        addtoCart=findViewById( R.id.addtoGroceryCary );
        gotoCart=findViewById( R.id.gotoGroceryCary );
        relevantProductRecycler=findViewById( R.id.recycler_view111 );
        product_details_CL=findViewById( R.id.product_details_CL );

        rating_LL=findViewById( R.id.linearLayout17 );

        review_layout=findViewById( R.id.review_layout );
        viewAllDescription=findViewById( R.id.description_ViewAll_LL );
        descLayout=findViewById( R.id.descriptionConstraintLayout );

        loadingDialog= new Dialog( GroceryProductDetails.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show();

        /////productImage/nmae/price

        out_of_stockText=findViewById( R.id.grocery_product_details_OutstockText );
        name = findViewById( R.id.grocery_product_details_Name );
        price = findViewById( R.id.grocery_product_details_Price );
        cutprice = findViewById( R.id.grocery_product_details_CutPrice );
        rating = findViewById( R.id.grocery_product_details_Rating );
        reviewCount = findViewById( R.id.grocery_product_details_ReviewCount );
        offer = findViewById( R.id.grocery_product_details_Offer );
        cartFAB=findViewById( R.id.cartList );


        /////productImage/nmae/price


        /////productRating

        reviewRecycler=findViewById( R.id.review_recycler );
        star_1_count = findViewById( R.id.review_1_star_count );
        star_2_count = findViewById( R.id.review_2_star_count );
        star_3_count = findViewById( R.id.review_3_star_count );
        star_4_count = findViewById( R.id.review_4_star_count );
        star_5_count = findViewById( R.id.review_5_star_count );
        progressBar1 = findViewById( R.id.progressBar1star );
        progressBar2 = findViewById( R.id.progressBar2star );
        progressBar3 = findViewById( R.id.progressBar3star );
        progressBar4 = findViewById( R.id.progressBar4star );
        progressBar5 = findViewById( R.id.progressBar5star );
        avg_rating = findViewById( R.id.review_avg_star );
        totalstarCount = findViewById( R.id.review_total_rating );
        totalreviewCount = findViewById( R.id.review_total_review_count );
        allReviews=findViewById( R.id.view_all_review );





        /////productRating



        final String product_id = getIntent( ).getStringExtra( "product_id" );




        productImages = new ArrayList<>( );
        grocery_product_details_descrioption_modelList = new ArrayList<>( );


        ////description

        final grocery_product_details_descrioption_Adapter grocery_product_details_descrioption_adapter = new grocery_product_details_descrioption_Adapter( grocery_product_details_descrioption_modelList );

        final LinearLayoutManager layoutManager = new LinearLayoutManager( this );

        layoutManager.setOrientation( RecyclerView.VERTICAL );
        descriptionRecycler.setAdapter( grocery_product_details_descrioption_adapter );
        descriptionRecycler.setLayoutManager( layoutManager );
        descriptionRecycler.setHasFixedSize( true );
        descriptionRecycler.setNestedScrollingEnabled( false );


        viewAllDescription.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( GroceryProductDetails.this,AllDescription.class );
                intent.putExtra( "product_id",product_id );
                startActivity( intent );



            }
        } );

        allReviews.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( GroceryProductDetails.this,AllReview.class );
                intent.putExtra( "product_id",product_id );
                startActivity( intent );
            }
        } );

        ////description

        /////releventProduct
        Pname=getIntent().getStringExtra( "tag_string" );


        /////releventProduct











        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).document( product_id ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            in_stock= String.valueOf((long) task.getResult( ).get( "in_stock" ));
                            long no_of_images = (long) task.getResult( ).get( "no_of_image" );
                            long no_of_description = (long) task.getResult( ).get( "no_of_description" );
                            if(no_of_description>4){
                                viewAllDescription.setVisibility( View.VISIBLE );
                            }else {
                                viewAllDescription.setVisibility( View.GONE );
                            }
                            for (long x = 1; x < no_of_images + 1; x++) {

                                productImages.add( task.getResult( ).get( "image_0" + x ).toString( ) );
                            }
                            productDetailsViewPagerAdapter productDetailsViewPagerAdapter = new productDetailsViewPagerAdapter( productImages );
                            imageViewPager.setAdapter( productDetailsViewPagerAdapter );

                            for (long x = 1; x < no_of_description + 1; x++) {

                                grocery_product_details_descrioption_modelList.add( new grocery_product_details_descrioption_Model( task.getResult( ).get( "description_0" + x ).toString( ) ) );
                            }

                            name.setText( task.getResult( ).get( "name" ).toString( ) );
                            price.setText( "₹" + task.getResult( ).get( "price" ).toString( ) + "/-" );

                            if(in_stock.equals( "0" )){
                                out_of_stockText.setVisibility( View.VISIBLE );
                            }else {
                                out_of_stockText.setVisibility( View.GONE );

                            }
                            if(task.getResult( ).get( "offer" ).toString( ).equals( "0" )){

                                cutprice.setVisibility( View.GONE );
                                offer.setVisibility( View.GONE );
                            }
                            if(task.getResult( ).get( "rating" ).toString( ) .length()==1){
                                rating_LL.setVisibility( View.GONE );
                                review_layout.setVisibility( View.GONE );
                                reviewCount.setVisibility( View.GONE );
                            }
                            cutprice.setText( "₹" + task.getResult( ).get( "cut_price" ).toString( ) + "/-" );
                            offer.setText( task.getResult( ).get( "offer" ).toString( ) + " off " );
                            reviewCount.setText( "(" + task.getResult( ).get( "review_count" ).toString( ) + ")" );
                            rating.setText( task.getResult( ).get( "rating" ).toString( ) );
                            grocery_product_details_descrioption_adapter.notifyDataSetChanged( );
                            product_details_CL.setVisibility( View.VISIBLE );
                            loadingDialog.dismiss();
                        }
                    }
                } );


        viewPagerIndicator.setupWithViewPager( imageViewPager, true );


        if (DBquaries.grocery_wishList.contains( product_id )) {

            ADDED_towishList = true;
            addtoWishlist.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
        } else {
            ADDED_towishList = false;
        }

        if(DBquaries.grocery_CartList_product_id.contains( product_id )){
            addtoCart.setVisibility( View.GONE );
            gotoCart.setVisibility( View.VISIBLE );
            cartFAB.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#39559e" ) ) );

        }else {
            cartFAB.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#696969" ) ) );

        }
        gotoCart.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                DBquaries.removeFromGroceryCartList( product_id,GroceryProductDetails.this );
                cartFAB.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#696969" ) ) );


            }
        } );

        cartFAB.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( GroceryProductDetails.this,GroceryCart.class );
                startActivity( intent );
            }
        } );




        buyBtn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {


                if(in_stock.equals( "0" )) {
                    Toast.makeText( GroceryProductDetails.this, "Product is Out of Stock", Toast.LENGTH_SHORT ).show( );

                }else {
                    Intent intent = new Intent( GroceryProductDetails.this, BuyNow.class );
                    intent.putExtra( "product_id", product_id );
                    intent.putExtra( "stock", in_stock );
                    startActivity( intent );
                }
            }
        } );

        addtoCart.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                loadingDialog.show();
                addtoCart.setClickable( false );
                final Map<String, Object> updateListSize = new HashMap<>( );
                updateListSize.put( "list_size", (long) DBquaries.grocery_CartList_product_id.size( ) + 1 );

                Map<String, Object> product_Id = new HashMap<>( );
                product_Id.put( "id_" + (long) DBquaries.grocery_CartList_product_id.size( ), product_id );

                FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                        .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).
                        update( product_Id ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).
                                    update( updateListSize ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        DBquaries.grocery_CartList_product_id.add( product_id );
                                        addtoCart.setVisibility( View.GONE );
                                        addtoCart.setClickable( true );
                                        cartFAB.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#39559e" ) ) );

                                        gotoCart.setVisibility( View.VISIBLE );
                                        Toast.makeText( GroceryProductDetails.this, "Added to cart", Toast.LENGTH_SHORT ).show( );



                                        loadingDialog.dismiss();


                                        Map<String, Object> Count = new HashMap<>( );
                                        Count.put( product_id,1 );

                                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTITEMCOUNT" ).
                                                update( Count ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        } );

                                    }

                                }
                            } );

                        }
                    }
                } );
            }
        } );

        addtoWishlist.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                addtoWishlist.setClickable( false );
                if (ADDED_towishList) {
                    ADDED_towishList = false;
                    addtoWishlist.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#9e9e9e" ) ) );
                    ////remove from wish List

                    DBquaries.removeFromGroceryWishList( product_id, GroceryProductDetails.this );
                    addtoWishlist.setClickable( true );

                } else {
                    ADDED_towishList = true;
                    addtoWishlist.setSupportImageTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                    ////add to wish List
                    final Map<String, Object> updateListSize = new HashMap<>( );
                    updateListSize.put( "list_size", (long) DBquaries.grocery_wishList.size( ) + 1 );
                    Map<String, Object> product_Id = new HashMap<>( );
                    product_Id.put( "id_" + (long) DBquaries.grocery_wishList.size( ), product_id );
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
                                            Toast.makeText( GroceryProductDetails.this, "Added to Wishlist", Toast.LENGTH_SHORT ).show( );

                                            addtoWishlist.setClickable( true );
                                           // Product.groceryProductAdapter.notifyDataSetChanged();
                                            DBquaries.grocery_wishList.add( product_id );

                                        }

                                    }
                                } );


                            }

                        }
                    } );

                }

            }
        } );



//////review

        reviewModelList = new ArrayList<>( );

        reviewAdapter = new ReviewAdapter( reviewModelList );
        LinearLayoutManager reviewlayoutManager = new LinearLayoutManager( this );
        reviewlayoutManager.setOrientation( RecyclerView.VERTICAL );
        reviewRecycler.setAdapter( reviewAdapter );
        reviewRecycler.setLayoutManager( reviewlayoutManager );
        reviewRecycler.setHasFixedSize( true);
        reviewRecycler.setNestedScrollingEnabled( false );




        FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).collection( "REVIEWS" ).orderBy( "order_id", Query.Direction.DESCENDING )
                .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        String rating=documentSnapshot.get( "rating" ).toString();
                        String review=documentSnapshot.get( "review" ).toString();

                        if(!(review.length()==0)){
                            total_review_count[0]= total_review_count[0]+1;
                            totalreviewCount.setText( String.valueOf(  total_review_count[0] ) );
                            reviewModelList.add( new ReviewModel(
                                    documentSnapshot.get("user_name").toString(),
                                    rating,
                                    documentSnapshot.get("date").toString(),
                                    review,
                                    documentSnapshot.get("image").toString()
                            ) );
                            reviewAdapter.notifyDataSetChanged();
                        }
                        totalreviewCount.setText( String.valueOf(  total_review_count[0] ) );
                        totalstarCount.setText( String.valueOf( total_rating_count[0] ) );




                        if (rating.equals( "5" )) {
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                            five_star_count[0] = five_star_count[0] + 1;
                            star_5_count.setText( String.valueOf( five_star_count[0] ) );
                        } else {
                            star_5_count.setText( String.valueOf( five_star_count[0] ) );
                        }
                        if (rating.equals( "4" )) {
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                            four_star_count[0] = four_star_count[0] + 1;
                            star_4_count.setText( String.valueOf( four_star_count[0] ) );

                        } else {
                            star_4_count.setText( String.valueOf( four_star_count[0] ) );

                        }
                        if (rating.equals( "3" )) {
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                            three_star_count[0] = three_star_count[0] + 1;
                            star_3_count.setText( String.valueOf( three_star_count[0] ) );

                        } else {
                            star_3_count.setText( String.valueOf( three_star_count[0] ) );
                        }
                        if (rating.equals( "2" )) {
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                            two_star_count[0] = two_star_count[0] + 1;
                            star_2_count.setText( String.valueOf( two_star_count[0] ) );

                        } else {
                            star_2_count.setText( String.valueOf( two_star_count[0] ) );

                        }
                        if (rating.equals( "1" )) {
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                            one_star_count[0] = one_star_count[0] + 1;
                            star_1_count.setText( String.valueOf( one_star_count[0] ) );
                        } else {
                            star_1_count.setText( String.valueOf( one_star_count[0] ) );

                        }


                        if(total_rating_count[0]!=0) {
                            review_layout.setVisibility( View.VISIBLE );
                            avg_star[0] = (5.0 * five_star_count[0] + 4.0 * four_star_count[0] + 3.0 * three_star_count[0] + 2.0 * two_star_count[0] + one_star_count[0]) / total_rating_count[0];
                            avg_rating.setText( String.format( "%.1f", avg_star[0] ) );

                            Map< String,Object> Update_ratings= new HashMap< >( ) ;
                            Update_ratings.put( "rating",String.format( "%.1f", avg_star[0] ) );
                            Update_ratings.put( "review_count",String.valueOf( total_rating_count[0] ) );

                            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( product_id ).update( Update_ratings )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                        }
                                    } );


                            progressBar1.setProgress( (one_star_count[0] * 100) / total_rating_count[0] );
                            progressBar2.setProgress( (two_star_count[0] * 100) / total_rating_count[0] );
                            progressBar3.setProgress( (three_star_count[0] * 100) / total_rating_count[0] );
                            progressBar4.setProgress( (four_star_count[0] * 100) / total_rating_count[0] );
                            progressBar5.setProgress( (five_star_count[0] * 100) / total_rating_count[0] );
                        }else {
                            review_layout.setVisibility( View.GONE );
                        }

                        if(total_review_count[0]>1){
                            allReviews.setVisibility( View.VISIBLE );
                        }

                    }
                    reviewAdapter.notifyDataSetChanged();
                }else {
                }

            }
        } );


//////review



        /////releventProduct


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager( GroceryProductDetails.this);
        gridLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        relevantProductRecycler.setLayoutManager( gridLayoutManager );

        final List<GroceryProductModel> list= new ArrayList<>(  );
        final List<String> ids=new ArrayList<>(  );

        final Adapter adapter=new Adapter( list );
        relevantProductRecycler.setAdapter( adapter );


        final String[] tags=Pname.toLowerCase().split( "," );
        for (final String tag: tags)
        {

            ids.clear();
            list.clear();
            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).whereArrayContains( "tags",tag )
                    .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull( task.getResult( ) )){

                            GroceryProductModel model=   new GroceryProductModel( Objects.requireNonNull( documentSnapshot.get( "image_01" ) ).toString( )
                                    , Objects.requireNonNull( documentSnapshot.get( "name" ) ).toString( )
                                    , documentSnapshot.get( "cut_price" ).toString( )
                                    , documentSnapshot.get( "offer" ).toString( )
                                    , documentSnapshot.get( "price" ).toString( )
                                    , documentSnapshot.get( "rating" ).toString( )
                                    , documentSnapshot.get( "review_count" ).toString( )
                                    , (long) documentSnapshot.get( "in_stock" )
                                    , documentSnapshot.getId( )
                            ,documentSnapshot.get( "relevant_tag" ).toString( )) ;

                            model.setTags((ArrayList<String>) documentSnapshot.get("tags") );

                            if(!ids.contains( model.getId() )){
                                list.add( model );
                                ids.add( model.getId() );
                            }
                        }

                        if(tag.equals( tags[tags.length-1] )){
                            if(list.size()==0){
                                relevantProductRecycler.setVisibility( View.GONE );
                            }else {
                                relevantProductRecycler.setVisibility( View.VISIBLE );
                                adapter.getFilter().filter( Pname );

                            }
                        }


                    }else {
                        String e=task.getException().getMessage();
                        Toast.makeText( GroceryProductDetails.this, e, Toast.LENGTH_SHORT ).show( );
                    }
                }
            } );
        }




    }

    class Adapter extends GroceryProductAdapter implements Filterable {
        private List<GroceryProductModel> originalList;

        public Adapter(List<GroceryProductModel> groceryProductModelList) {
            super( groceryProductModelList );
            originalList=groceryProductModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter( ) {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults=new FilterResults();
                    List<GroceryProductModel> filterList=new ArrayList<>(  );

                    final String[] tags=Pname.toLowerCase().split( " " );
                    ArrayList<String> presentTags=new ArrayList<>(  );
                    for(GroceryProductModel model:originalList){



                        for(String tag :tags){

                            if(model.getTags().contains( tag )){

                                presentTags.add( tag );
                            }

                            model.setTags( presentTags );


                        }
                    }
                    for(int i= tags.length;i> 0;i--){
                        for(GroceryProductModel model: originalList){
                            if(model.getTags().size()==i){
                                filterList.add( model );
                            }
                        }
                    }
                    filterResults.values=filterList;
                    filterResults.count=filterList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if(filterResults.count>0){

                        setGroceryProductModelList( (List<GroceryProductModel>)filterResults.values );
                    }

                    notifyDataSetChanged();
                }
            };
        }










        /////releventProduct



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater( );
        inflater.inflate( R.menu.search_and_cart_icon, menu );
        return true;
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId( );

        if (item.getItemId( ) == android.R.id.home) {

         finish();        }


        if (id == R.id.productcartMenu) {

           Intent intent = new Intent( GroceryProductDetails.this, GroceryCart.class );
           startActivity( intent );

        }
        if (id == R.id.productsearchMenu) {

           // Intent intent = new Intent( GroceryProductDetails.this, GrocerySearch.class );
            //startActivity( intent );

        }


        return super.onOptionsItemSelected( item );


    }
}
