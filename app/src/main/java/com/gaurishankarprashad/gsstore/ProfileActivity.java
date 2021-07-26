package com.gaurishankarprashad.gsstore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout myAccountchild;
    private TextView editProfile_txt;
    public static TextView userMail, username;
    private ImageView myAccountArrowDown;
    private ImageView myAccountArrowUp;
    private LinearLayout myOrderLayout, myAccountbottomLayout, myCart, myOffers, myAccoutnFav,ordrOnDemand,help_ll;
    private LinearLayout ChangeAddressLayout;
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    public static CircleImageView profileImage;
    private String phone, name;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDetabaseRef;
    private Dialog loadingDialog;
    private Dialog orderOnDemandDialog;






    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );


        myAccountchild = findViewById( R.id.myAccountChildLayout );
        editProfile_txt = findViewById( R.id.editProfile_txt );
        username = findViewById( R.id.profile_user_name );
        userMail = findViewById( R.id.profile_user_mail );
        myAccountArrowDown = findViewById( R.id.myAccountArrowDown );
        myAccountArrowUp = findViewById( R.id.myAccountArrowUp );
        myOrderLayout = findViewById( R.id.myOrderLayout );
        ordrOnDemand=findViewById( R.id.linearLayout55 );

        help_ll=findViewById( R.id.linearLayout8 );
        toolbar = findViewById( R.id.toolbar_profile );
        ChangeAddressLayout = findViewById( R.id.myAccountChangeAddressLayout );
        myCart = findViewById( R.id.myAccountCartLL );
        myOffers = findViewById( R.id.myAccountOfferLayout );
        myAccoutnFav = findViewById( R.id.myAccountFavouriteLayout );
        firebaseFirestore = FirebaseFirestore.getInstance( );
        profileImage = findViewById( R.id.profile_image );

        mStorageRef= FirebaseStorage.getInstance().getReference("USER_PROFILE_PIC");
        mDetabaseRef= FirebaseDatabase.getInstance().getReference("uploads");





        loadingDialog= new Dialog( ProfileActivity.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );


        ordrOnDemand.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                final Dialog ondemand_dialog=new Dialog( ProfileActivity.this );
                ondemand_dialog.setContentView( R.layout.order_on_demand );
                ondemand_dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );
                ondemand_dialog.setCancelable( true );
                ondemand_dialog.show();
                final EditText product_details=ondemand_dialog.findViewById( R.id.on_demand_product_details );
                final Button submit=ondemand_dialog.findViewById( R.id.on_demand_submit );

                submit.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        if(product_details.getText().toString().isEmpty()){
                            Toast.makeText( ProfileActivity.this, "fill the details", Toast.LENGTH_SHORT ).show( );

                        }else {
                            Map<String,Object> Details=new HashMap<>(  );
                            Details.put( "product_details",product_details.getText().toString() );
                            Details.put( "id",FirebaseAuth.getInstance().getCurrentUser().getUid() );
                            FirebaseFirestore.getInstance().collection( "ORDER_ON_DEMAND" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).set(Details  )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                ondemand_dialog.dismiss();
                                            }
                                        }
                                    } );

                        }
                    }
                } );
            }
        } );

        help_ll.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Intent.ACTION_SEND );
                intent.putExtra( Intent.EXTRA_EMAIL,new String[]{DBquaries.ADMIN_MAIL} );
                intent.putExtra( Intent.EXTRA_SUBJECT,"Report/help" );
                intent.setType( "message/rfc822" );
                startActivity( Intent.createChooser( intent,"send mail" ) );

            }
        } );


        profileImage.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                openFileChooser( );
            }
        } );


        myAccoutnFav.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, Grocery_wish_list.class );
                intent.putExtra( "name", "Wish List" );
                startActivity( intent );
            }
        } );
        myOffers.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, Grocery_wish_list.class );
                intent.putExtra( "layout_code", 1 );
                intent.putExtra( "name", "Offers" );

                startActivity( intent );
            }
        } );

        myCart.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                 Intent intent= new Intent( ProfileActivity.this,GroceryCart.class );
                 startActivity( intent );
            }
        } );

        ChangeAddressLayout.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, MyAddress.class );
                startActivity( intent );
            }
        } );


        toolbar.setTitle( "My Profile" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );

        final String imaeg=getIntent().getStringExtra( "image" );
        final String Name=getIntent().getStringExtra( "name" );
        final String mail=getIntent().getStringExtra( "mail" );


        username.setText( Name );
        userMail.setText( mail );
        if(imaeg.length()==0 ){
            profileImage.setImageResource( R.drawable.ic_account_circle_black_24dp );

        }else {
            Glide.with( ProfileActivity.this ).load( imaeg ).into( profileImage );
        }





        myAccountArrowDown.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                myAccountchild.setVisibility( View.VISIBLE );
                myAccountArrowDown.setVisibility( View.GONE );
                myAccountArrowUp.setVisibility( View.VISIBLE );

            }
        } );

        myAccountArrowUp.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                myAccountchild.setVisibility( View.GONE );
                myAccountArrowUp.setVisibility( View.GONE );
                myAccountArrowDown.setVisibility( View.VISIBLE );

            }
        } );

        editProfile_txt.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, Edit_pofile.class );
                intent.putExtra( "name", name );
                intent.putExtra( "phone", phone );
                startActivity( intent );
            }
        } );


        myOrderLayout.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ProfileActivity.this, MyOrderGrocery.class );
                startActivity( intent );

            }
        } );


    }


    private void openFileChooser() {

        Intent intent = new Intent( );
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent, PICK_IMAGE_REQUEST );


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK
                && data!= null && data.getData() != null   ){


            mImageUri= data.getData();

            loadingDialog.show();
            uploadFile();

        }
    } private String getFileExtention(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( cR.getType( uri ) );
    }

    public void uploadFile(){

        if(mImageUri!=null){
            final StorageReference fileref= mStorageRef.child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( System.currentTimeMillis()+"."+getFileExtention( mImageUri ) );

            fileref.putFile( mImageUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>( ) {
                        @Override
                        public void onSuccess(final Uri uri) {
                            Map<String,Object> Ima=new HashMap<>(  );
                            Ima.put("image",uri.toString());
                            FirebaseFirestore.getInstance().collection( "USERS"  ).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update( Ima )
                                   .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){

                                               loadingDialog.dismiss();
                                               Toast.makeText( ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT ).show( );
                                               Glide.with( ProfileActivity.this ).load( uri.toString() ).into( profileImage );

                                           }
                                       }
                                   } );







                        }
                    } );




                }
            } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show( );
                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {



                }
            } );
        }else {
            Toast.makeText( this, "no item selected", Toast.LENGTH_SHORT ).show( );

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
