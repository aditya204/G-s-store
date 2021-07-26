package com.gaurishankarprashad.gsstore;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.Adapters.HomeAdapter;
import com.gaurishankarprashad.gsstore.Adapters.HomeCategoryAdapter;
import com.gaurishankarprashad.gsstore.Models.HomeCategoryModels;
import com.gaurishankarprashad.gsstore.Models.HomeModel;
import com.gaurishankarprashad.gsstore.Models.dealsofthedayModel;
import com.gaurishankarprashad.gsstore.Models.sliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private RecyclerView homecategoryRecycler;
    private RecyclerView homecycler;

    private Toolbar toolbar;
    private HomeCategoryAdapter homeCategoryAdapter;
    private List<HomeCategoryModels> homeCategoryModelsList;
    private List<HomeModel> homeModelList;
    private NestedScrollView scrollView;
    private AppBarLayout appBarLayout;
    private FloatingActionButton search_Fab;
    public static CircleImageView imageProfileHome;

    public static List<dealsofthedayModel> dealsofthedayModelList=new ArrayList<>(  );
    public static List<dealsofthedayModel> gridList=new ArrayList<>(  );
    private List<sliderModel> sliderModelList;
    private List<HomeCategoryModels> circularHorizontallist;
    private LinearLayout search_bar_LL,profileLL;
    private LinearLayout adminLayout;
    public static TextView OutOfStock,Orders;
    String image="",name="",mail="";
    private Dialog loadingDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );




        toolbar=findViewById( R.id.toolbar );
        DBquaries.loadTax();






        toolbar.setTitle( "G.S Store" );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        toolbar.setSubtitleTextColor( Color.parseColor( "#FFFFFF" ) );
        setSupportActionBar( toolbar );
        appBarLayout=findViewById( R.id.appBarLayout );
        adminLayout=findViewById( R.id.admin_layout );
        OutOfStock=findViewById( R.id.admin_layout_outOfStock );
        Orders=findViewById( R.id.admin_layout_Orders );
        search_Fab=findViewById( R.id. home_search_product_FAB);

        imageProfileHome=findViewById( R.id.profile_image_home );

        homecategoryRecycler=findViewById( R.id.home_category_recycler );

        homecycler=findViewById( R.id.home_recycler );
        scrollView=findViewById( R.id.home_scroll );
        search_bar_LL=findViewById( R.id.search_bar_LL );
        profileLL=findViewById( R.id.profileImageHomeLL);
        loadingDialog= new Dialog( Home.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show();



        DBquaries.loadProductList();

        DBquaries.chechAdmin();

        FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                .get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                     image=task.getResult().get( "image" ).toString();
                     if(image.length()==0 ){
                         imageProfileHome.setImageResource( R.drawable.ic_account_circle_white_24dp );
                     }else {
                         Glide.with( Home.this ).load( image ).into( imageProfileHome );
                     }

                     if(!(boolean)task.getResult().get( "is_valid" )){
                         FirebaseAuth.getInstance().signOut();
                     }
                     name =task.getResult().get( "fullname" ).toString();
                     mail= task.getResult().get( "permanent_phone" ).toString();

                }
            }
        } );

        profileLL.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Home.this,ProfileActivity.class );

                intent.putExtra( "image",image);
                intent.putExtra( "name",name);
                intent.putExtra( "mail",mail);
                startActivity( intent );
            }
        } );



       scrollView.setOnScrollChangeListener( new NestedScrollView.OnScrollChangeListener( ) {
           @SuppressLint("RestrictedApi")
           @Override
           public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

               int movement = v.getScrollY();
               int maxDistance=toolbar.getHeight();
               float alphaFactor = ((movement * 1.0f) / (maxDistance));
               int height= appBarLayout.getScrollY();
               if ( height>= 0 &&
               height<= maxDistance) {
                   /*for image parallax with scroll */
//                   /* set visibility */
                   search_bar_LL.setAlpha(alphaFactor);
                   search_Fab.setAlpha( 1-alphaFactor );
                   if(alphaFactor>0.8){
                       appBarLayout.setBackgroundColor( Color.parseColor( "#39559e" ) );

                   }else {
                       appBarLayout.setBackgroundResource( R.drawable.store_back );

                   }
               }

               if(oldScrollY>scrollY){
                   search_Fab.setAlpha( 1f );
                   search_Fab.setVisibility( View.VISIBLE );
                   appBarLayout.setBackgroundResource( R.drawable.store_back );
                   search_bar_LL.setAlpha(0);
               }else {
                   search_Fab.setVisibility( View.GONE );
               }


           }
       } );

       search_Fab.setOnClickListener( new View.OnClickListener( ) {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent( Home.this,Search.class );
               startActivity( intent );
           }
       } );
       search_bar_LL.setOnClickListener( new View.OnClickListener( ) {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent( Home.this,Search.class );
               startActivity( intent );
           }
       } );


        OutOfStock.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Home.this,OutOfStock.class );
                startActivity( intent );
            }
        } );



       Orders.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Home.this,PendingOrders.class );
                startActivity( intent );
            }
        } );



        toolbar.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Home.this,ProfileActivity.class );
                startActivity( intent );
            }
        } );


        if (DBquaries.grocery_wishList.size( ) == 0) {
            DBquaries.loadGroceryWishList( this );
        }


        if (DBquaries.grocery_OrderList.size( ) == 0) {
            DBquaries.loadGroceryOrders();
        }



        if(DBquaries.grocery_CartList_product_id.size()==0){
            DBquaries.loadGroceryCartList(this  );
        }



        homeCategoryModelsList=new ArrayList<>(  );



        homeCategoryAdapter=new HomeCategoryAdapter( homeCategoryModelsList );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        homecategoryRecycler.setLayoutManager( linearLayoutManager );
        homecategoryRecycler.setAdapter( homeCategoryAdapter );
        homeCategoryAdapter.notifyDataSetChanged();



      FirebaseFirestore.getInstance().collection( "GROCERYCATEGORIES" ).orderBy( "index" ).get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull( task.getResult( ) )){

                                homeCategoryModelsList.add( new HomeCategoryModels( documentSnapshot.get( "image" ).toString(),
                                        documentSnapshot.get( "title" ).toString(),documentSnapshot.get( "tag" ).toString()  ) );
                            }
                            homeCategoryAdapter.notifyDataSetChanged();
                        }

                    }
                } );



        homeModelList=new ArrayList<>(  );
        LinearLayoutManager grocerymain=new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        homecycler.setLayoutManager( grocerymain );
        final HomeAdapter homeAdapter=new HomeAdapter(homeModelList  );
        homecycler.setAdapter( homeAdapter );

        FirebaseFirestore.getInstance().collection( "GROCERYHOME" ).orderBy( "index" ).get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :task.getResult()){

                                if ((long) documentSnapshot.get( "view_type" ) == 1) {
                                    sliderModelList = new ArrayList<>( );

                                    long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );

                                    for (long x = no_of_banners; x > no_of_banners - 2; x--) {
                                        sliderModelList.add( new sliderModel( documentSnapshot.get( "banner_"+x+"_image"  ).toString( ),documentSnapshot.get( "banner_"+x+"_tag"  ).toString( ),documentSnapshot.get( "banner_"+x+"_background"  ).toString( ) ) );
                                    }
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add( new sliderModel( documentSnapshot.get(  "banner_"+x+"_image" ).toString( ),documentSnapshot.get( "banner_"+x+"_tag"  ).toString( ),documentSnapshot.get( "banner_"+x+"_background"  ).toString( ) ) );
                                    }

                                    for (long x = 1; x < 3; x++) {
                                        sliderModelList.add( new sliderModel( documentSnapshot.get(  "banner_"+x+"_image" ).toString( ),documentSnapshot.get( "banner_"+x+"_tag"  ).toString( ),documentSnapshot.get( "banner_"+x+"_background"  ).toString( ) ) );
                                    }

                                    homeModelList.add( new HomeModel( 1,sliderModelList ) );

                                }
                                if((long) documentSnapshot.get( "view_type" ) == 2){
                                    ArrayList<String> ids=new ArrayList<>(  );
                                    dealsofthedayModelList =new ArrayList<>(  );
                                    long no_of_products=(long) documentSnapshot.get( "no_of_products" );
                                    String background= documentSnapshot.get( "background_color" ).toString();
                                    String title= documentSnapshot.get( "title" ).toString();
                                    for (long x = 1; x < no_of_products + 1; x++) {


                                        dealsofthedayModelList.add( new dealsofthedayModel( documentSnapshot.get( "image_" + x ).toString( )
                                                , documentSnapshot.get( "name_" + x ).toString( )
                                                , documentSnapshot.get( "description_" + x ).toString( ),
                                                documentSnapshot.get( "price_" + x ).toString( ),
                                                documentSnapshot.get( "id_" + x ).toString( ),
                                                documentSnapshot.get( "tag_" + x ).toString( )) );

                                        ids.add( documentSnapshot.get( "id_" + x ).toString( ) );


                                    }


                                    homeModelList.add( new HomeModel( 2,title,dealsofthedayModelList,ids,background ) );


                                }

                                if((long) documentSnapshot.get( "view_type" ) == 3){
                                    ArrayList<String> ids=new ArrayList<>(  );
                                    gridList =new ArrayList<>(  );
                                    String grid_title= documentSnapshot.get( "title" ).toString();
                                    long no_of_products=(long) documentSnapshot.get( "no_of_products" );
                                    String background= documentSnapshot.get( "background_color" ).toString();

                                    for (long x = 1; x < no_of_products + 1; x++) {

                                        gridList.add( new dealsofthedayModel( documentSnapshot.get( "image_" + x ).toString( )
                                                , documentSnapshot.get( "name_" + x ).toString( )
                                                , documentSnapshot.get( "description_" + x ).toString( )
                                                , documentSnapshot.get( "price_" + x ).toString( ),
                                                documentSnapshot.get( "id_" + x ).toString( ),
                                                documentSnapshot.get( "tag_" + x ).toString( )) );


                                        ids.add( documentSnapshot.get( "id_" + x ).toString( ) );

                                    }
                                    homeModelList.add( new HomeModel( 3,grid_title,gridList,ids,background ) );
                                }
                                if((long)documentSnapshot.get( "view_type" ) == 4){

                                    circularHorizontallist=new ArrayList<>(  );
                                    long no_of_products=(long) documentSnapshot.get( "no_of_products" );

                                    for(long x=1;x<no_of_products+1;x++){

                                        circularHorizontallist.add( new HomeCategoryModels( documentSnapshot.get("image_"+x).toString()
                                                ,documentSnapshot.get("name_"+x).toString(),documentSnapshot.get("tag_"+x).toString()
                                        ));
                                    }
                                    homeModelList.add( new HomeModel( 4,0,"Top Brands",circularHorizontallist ) );
                                }
                                if((long)documentSnapshot.get( "view_type" ) == 5){

                                    homeModelList.add( new HomeModel( 5,
                                            documentSnapshot.get("name_1").toString(),
                                            documentSnapshot.get("name_2").toString(),
                                            documentSnapshot.get("name_3").toString(),
                                            documentSnapshot.get("name_4").toString(),
                                            documentSnapshot.get("image_1").toString(),
                                            documentSnapshot.get("image_2").toString(),
                                            documentSnapshot.get("image_3").toString(),
                                            documentSnapshot.get("image_4").toString(),
                                            documentSnapshot.get("title").toString(),
                                            documentSnapshot.get("tag_1").toString(),
                                            documentSnapshot.get("tag_2").toString(),
                                            documentSnapshot.get("tag_3").toString(),
                                            documentSnapshot.get("tag_4").toString()
                                    ) );


                                }

                            }
                            homeAdapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }else {
                            String error = task.getException( ).getMessage( );
                            Toast.makeText( Home.this, error, Toast.LENGTH_SHORT ).show( );


                        }
                    }
                } );



    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater( );
        inflater.inflate( R.menu.home, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId( );

        if (item.getItemId( ) == android.R.id.home) {

            finish();        }


        if (id == R.id.productcartMenu) {

            Intent intent = new Intent( Home.this, GroceryCart.class );
            startActivity( intent );

        }
        if (id == R.id.productsearchMenu) {

            // Intent intent = new Intent( GroceryProductDetails.this, GrocerySearch.class );
            //startActivity( intent );

        }


        return super.onOptionsItemSelected( item );


    }
*/

}
