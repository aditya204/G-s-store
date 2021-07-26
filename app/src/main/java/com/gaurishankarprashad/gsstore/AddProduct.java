package com.gaurishankarprashad.gsstore;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.AddProductDescriptionAdapter;
import com.gaurishankarprashad.gsstore.Adapters.AddproductImageAdapter;
import com.gaurishankarprashad.gsstore.Models.AddProductDescriptionModel;
import com.gaurishankarprashad.gsstore.Models.AddProductImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private Button choose,upload,addProduct_btn,addDesc_btn,showImage_btb;
    private ProgressBar uploadProgress;
    private static EditText productname,productPrice,productcutPrice,productoffer,productStock,description_et,description_home_et,relevant_tags_et,deccroption_content_et;
    private ImageView image;
    private ProgressDialog progressDialog;
    private TextView prviousStock;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDetabaseRef;
    private static  String product_id="";



    private AddProductDescriptionAdapter addProductDescriptionAdapter;
    private AddproductImageAdapter addproductImageAdapter;
    private List<AddProductImageModel> addProductImageModelList;
    private List<AddProductDescriptionModel> addproductDescriptionModel;
    private RecyclerView imageRecycler,descriptionRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_product );

        choose=findViewById( R.id.chooseFile_btn );
        upload=findViewById( R.id.UploadFile );
        productname=findViewById( R.id.add_product_name_et );
        productPrice=findViewById( R.id.add_product_price_et );
        productcutPrice=findViewById( R.id.add_product_cutPrice_et );
        productoffer=findViewById( R.id.add_product_offer_et );
        productStock=findViewById( R.id.add_product_stock_et );
        addProduct_btn=findViewById( R.id.add_product_btn );
        image=findViewById( R.id.imageView3 );
        uploadProgress=findViewById( R.id.progressBar );
        mStorageRef= FirebaseStorage.getInstance().getReference("PRODUCTS");
        mDetabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        imageRecycler=findViewById( R.id.addProductImageRecycler );
        descriptionRecycler=findViewById( R.id.addProductDescriptionRecycler );
        addDesc_btn=findViewById( R.id.add_description_btn );
        description_et=findViewById( R.id.add_product_description_et );
        deccroption_content_et=findViewById( R.id.add_product_description_content_et );
        showImage_btb=findViewById( R.id.ShowUpload );
        prviousStock=findViewById( R.id.  add_product_previous_stock );
        description_home_et=findViewById( R.id.  add_product_home_desc_et );
        relevant_tags_et=findViewById( R.id.add_product_relevant_tag_et );

       progressDialog=new ProgressDialog( this );

       int code=getIntent().getIntExtra( "layout_code",-1 );
       if(code==1){
           final int[] PS = {0};
           product_id=getIntent().getStringExtra( "product_id" );
           FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if(task.isSuccessful()){
                       PS[0] =Integer.parseInt( String.valueOf( (long) task.getResult().get( "in_stock" ) ));
                       productname.setText( task.getResult().get( "name" ).toString() );
                       productPrice.setText( task.getResult().get( "price" ).toString() );
                       productcutPrice.setText( task.getResult().get( "cut_price" ).toString() );
                       productoffer.setText( task.getResult().get( "offer" ).toString() );
                       description_home_et.setText( task.getResult().get( "description" ).toString() );
                       relevant_tags_et.setText( task.getResult().get( "relevant_tag" ).toString() );
                       prviousStock.setText( String.valueOf( (long) task.getResult().get( "in_stock" ) ) );

                   }
               }
           } );

           addProduct_btn.setOnClickListener( new View.OnClickListener( ) {
               @Override
               public void onClick(View view) {
                   progressDialog.show();
                   updateProduct(productname.getText().toString(),productPrice.getText().toString(),productcutPrice.getText().toString(),productoffer.getText().toString(),productStock.getText().toString(),PS[0],description_home_et.getText().toString(),relevant_tags_et.getText().toString());
               }
           } );
       }else{
           DocumentReference ref = FirebaseFirestore.getInstance().collection("PRODUCTS").document();
           product_id = ref.getId();
           addProduct_btn.setOnClickListener( new View.OnClickListener( ) {
               @Override
               public void onClick(View view) {
                   progressDialog.show();
                   addProduct(productname.getText().toString(),productPrice.getText().toString(),productcutPrice.getText().toString(),productoffer.getText().toString(),productStock.getText().toString(),description_home_et.getText().toString(),relevant_tags_et.getText().toString());
               }
           } );

           showImage_btb.setVisibility( View.GONE );
       }





        addDesc_btn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                addDesc(description_et.getText().toString()+","+deccroption_content_et.getText().toString());
            }
        } );

        showImage_btb.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                addProductImageModelList = new ArrayList<>( );
                addproductDescriptionModel = new ArrayList<>( );

                addproductImageAdapter = new AddproductImageAdapter( addProductImageModelList );
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager( AddProduct.this );
                linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
                imageRecycler.setLayoutManager( linearLayoutManager );
                imageRecycler.setAdapter( addproductImageAdapter );


                addProductDescriptionAdapter = new AddProductDescriptionAdapter( addproductDescriptionModel );
                LinearLayoutManager linearLayout= new LinearLayoutManager( AddProduct.this );
                linearLayout.setOrientation( RecyclerView.VERTICAL );
                descriptionRecycler.setLayoutManager( linearLayout );
                descriptionRecycler.setAdapter( addProductDescriptionAdapter );

                FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            long no_of_image=(long) task.getResult().get( "no_of_image" );
                            long no_of_description=(long) task.getResult().get( "no_of_description" );

                            for(long x=1;x<no_of_image+1;x++){
                                addProductImageModelList.add( new AddProductImageModel( task.getResult().get( "image_0"+x ).toString() ) );
                            }
                            for(long x=1;x<no_of_description+1;x++){
                                addproductDescriptionModel.add( new AddProductDescriptionModel( task.getResult().get( "description_0"+x ).toString() ) );
                            }


                            addProductDescriptionAdapter.notifyDataSetChanged();
                            addproductImageAdapter.notifyDataSetChanged();
                        }
                    }
                } );
            }
        } );













        choose.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
             openFileChooser();
            }
        } );

        upload.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                uploadFile();

            }
        } );


    }

    private void addDesc(final String description) {
        if(description.isEmpty()){
            Toast.makeText( AddProduct.this,"add all fields",Toast.LENGTH_SHORT ).show();
        }else {
            FirebaseFirestore.getInstance().collection( "PRODUCTS"  ).document(product_id).get()
                    .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                long no_of_description= (long) task.getResult().get( "no_of_description" );

                                Map<String,Object> Ima=new HashMap<>(  );
                                Ima.put("description_0"+(no_of_description+1),description);
                                Ima.put( "no_of_description",no_of_description+1 );

                                FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).update( Ima )
                                        .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    description_et.setText("");
                                                    deccroption_content_et.setText("");
                                                }
                                            }
                                        } );
                            }
                        }
                    } );

        }
    }


    private void addProduct(@org.jetbrains.annotations.NotNull String name, String price, String cutPrice, String offer, String stock,String home_desc,String relevanr_Tags){

        if(name.isEmpty() || price.isEmpty() || cutPrice.isEmpty() || offer.isEmpty() || stock.isEmpty()||relevanr_Tags.isEmpty()||home_desc.isEmpty() ){

            Toast.makeText( AddProduct.this,"add all fields",Toast.LENGTH_SHORT ).show();

        }else {
            String[] tag =relevanr_Tags.toLowerCase().trim().split( "," );


            ArrayList<String> tags =new ArrayList<>(  );
            tags.add( name );
            tags.add( price );
            tags.add( offer );
            tags.add( home_desc );
            tags.addAll( Arrays.asList( tag ) );






            Map<String,Object> AddProductDetails=new HashMap<>(  );
            AddProductDetails.put( "name",name );
            AddProductDetails.put( "price",price );
            AddProductDetails.put( "rating","-" );
            AddProductDetails.put( "review_count","0" );
            AddProductDetails.put( "in_stock",Integer.parseInt( stock ) );
            AddProductDetails.put( "cut_price",cutPrice );
            AddProductDetails.put( "no_of_description",0 );
            AddProductDetails.put( "no_of_image",0 );
            AddProductDetails.put( "offer",offer );
            AddProductDetails.put( "image_01","" );
            AddProductDetails.put( "description_01","" );
            AddProductDetails.put( "description",home_desc );
            AddProductDetails.put( "relevant_tag",relevanr_Tags );



            AddProductDetails.put( "tags",tags );

            FirebaseFirestore.getInstance().collection("PRODUCTS").document(product_id).set( AddProductDetails )
                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                choose.setVisibility( View.VISIBLE );
                                upload.setVisibility( View.VISIBLE );
                                image.setVisibility( View.VISIBLE );
                                showImage_btb.setVisibility( View.VISIBLE );


                            }

                        }
                    } );
        }



    }

    private void updateProduct( String name, String price, String cutPrice, String offer, String stock, int previousStock,String home_desc,String relevanr_Tags){

        if(name.isEmpty() || price.isEmpty() || cutPrice.isEmpty() || offer.isEmpty()||relevanr_Tags.isEmpty()||home_desc.isEmpty() ){

            Toast.makeText( AddProduct.this,"add all fields",Toast.LENGTH_SHORT ).show();

        }else {


            String[] tag =relevanr_Tags.toLowerCase().split( "," );


            ArrayList<String> tags =new ArrayList<>(  );
            tags.add( name );
            tags.add( price );
            tags.add( offer );
            tags.add( home_desc );
            tags.addAll( Arrays.asList( tag ) );



            Map<String,Object> AddProductDetails=new HashMap<>(  );
            AddProductDetails.put( "name",name );
            AddProductDetails.put( "price",price );
            if(stock.length()==0){
                AddProductDetails.put( "in_stock",previousStock );

            }else {
                AddProductDetails.put( "in_stock",Integer.parseInt( stock ) );

            }
            AddProductDetails.put( "cut_price",cutPrice );
            AddProductDetails.put( "offer",offer );
            AddProductDetails.put( "description",home_desc );
            AddProductDetails.put( "relevant_tag",relevanr_Tags );

            AddProductDetails.put( "tags",tags );

            FirebaseFirestore.getInstance().collection("PRODUCTS").document(product_id).update( AddProductDetails )
                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressDialog.dismiss();



                            }

                        }
                    } );
        }


    }

    private void openFileChooser(){

        Intent intent=new Intent(  );
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent,PICK_IMAGE_REQUEST );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK
                && data!= null && data.getData() != null   ){


            mImageUri= data.getData();

            image.setImageURI( mImageUri );

        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( cR.getType( uri ) );
    }

    public void uploadFile(){

        if(mImageUri!=null){
            final StorageReference fileref= mStorageRef.child( productname.getText().toString()+System.currentTimeMillis()+"."+getFileExtention( mImageUri ) );

            fileref.putFile( mImageUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler =new Handler(  );
                    handler.postDelayed( new Runnable( ) {
                        @Override
                        public void run() {
                            uploadProgress.setProgress( 0 );
                        }
                    },500 );
                    Toast.makeText( AddProduct.this,"Upload Sucessful",Toast.LENGTH_SHORT ).show();

             /*       Upload upload= new Upload( productname.getText().toString().trim(),
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                    String UploadId=mDetabaseRef.push().getKey();
                    mDetabaseRef.child( UploadId ).setValue( upload );*/

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>( ) {
                        @Override
                        public void onSuccess(final Uri uri) {

                            FirebaseFirestore.getInstance().collection( "PRODUCTS"  ).document(product_id).get()
                                    .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                long no_of_image= (long) task.getResult().get( "no_of_image" );

                                                Map<String,Object> Ima=new HashMap<>(  );
                                                Ima.put("image_0"+(no_of_image+1),uri.toString());
                                                Ima.put( "no_of_image",no_of_image+1 );

                                                FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).update( Ima )
                                                        .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        } );
                                            }
                                        }
                                    } );







                        }
                    } );




                }
            } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT ).show( );
                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress= (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getBytesTransferred());
                    uploadProgress.setProgress((int) progress );


                }
            } );
        }else {
            Toast.makeText( this, "no item selected", Toast.LENGTH_SHORT ).show( );

        }



    }


}
