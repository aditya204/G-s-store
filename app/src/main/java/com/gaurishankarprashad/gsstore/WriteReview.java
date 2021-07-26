package com.gaurishankarprashad.gsstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteReview extends AppCompatActivity {
    private EditText review;
    private Button post;
    private  String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_write_review );
        review=findViewById( R.id.reviewEditText );
        post=findViewById( R.id.postReview );

        int layout_code =  getIntent().getIntExtra( "layout_code",-1 );
        final String OID =getIntent().getStringExtra( "order_id" );
        final String PID =getIntent().getStringExtra( "product_id" );


        final String[] user_name = {""};
        final String[] image = {""};

        FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            user_name[0] =task.getResult().get( "fullname" ).toString();
                            image[0]=task.getResult().get( "image" ).toString();
                        }

                    }
                } );

        if(layout_code==1){
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat( "dd MMM yyyy" );
            final String datetime = ft.format( dNow );

            post.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    text= review.getText().toString();
                    if(text.isEmpty()){
                        Toast.makeText( WriteReview.this, "Write your review", Toast.LENGTH_SHORT ).show( );
                    }
                    else {

                        final Map< String,Object> Review= new HashMap< >( ) ;
                        Review.put( "review",text );
                        Review.put( "date" ,datetime);
                        Review.put( "user_name" ,user_name[0]);

                        FirebaseFirestore.getInstance().collection( "ORDERS" ).document(OID).collection( "ORDER_LIST" ).document(PID)
                                .update( Review ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Review.put( "review",text );
                                    Review.put( "date" ,datetime);
                                    Review.put( "user_name" ,user_name[0]);
                                    Review.put( "image" ,image[0]);
                                    Toast.makeText( WriteReview.this, "Thank You for your review", Toast.LENGTH_SHORT ).show( );
                                    FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(PID).collection( "REVIEWS" ).document(OID)
                                            .update( Review ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent=new Intent( WriteReview.this,MyOrderGrocery.class );
                                            startActivity( intent );
                                        }
                                    } );


                                }

                            }
                        } );

                    }

                }
            } );



        }else {

            post.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                text= review.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText( WriteReview.this, "Write your review", Toast.LENGTH_SHORT ).show( );
                }
                else {

                 int position= getIntent().getIntExtra( "index",-1 );

                    Map< String,Object> Review= new HashMap< >( ) ;
                    Review.put( "review",text );

                    FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                            .collection( "USER_DATA" ).document( "MY_ORDERS" ).collection( "ORDER_LIST" ).document("ORDER_"+position)
                            .update( Review ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( WriteReview.this, "Review Uploaded", Toast.LENGTH_SHORT ).show( );
                                review.setClickable( false );
                            }

                        }
                    } );

                }

            }
        } );

        }


    }
}
