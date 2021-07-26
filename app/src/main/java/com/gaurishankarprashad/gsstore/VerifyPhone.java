package com.gaurishankarprashad.gsstore;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    String verificarionCodeBySystem;
    EditText code;
    Button verify;
    private static String phoneNo="",mail="",password="",fullname="";
    private Dialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_verify_phone );


        loadingDialog= new Dialog( VerifyPhone.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );

         phoneNo=getIntent().getStringExtra( "phoneNo" );
        // mail=getIntent().getStringExtra( "mail" );
       //  password=getIntent().getStringExtra( "password" );
       //  fullname=getIntent().getStringExtra( "name" );

         verify=findViewById( R.id.button2 );
        code=findViewById( R.id. enter_otp);

        verify.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                verifyCode( code.getText().toString() );
            }
        } );
        sendVerificationCode( phoneNo );


    }



    private void sendVerificationCode(String PhoneNo){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+PhoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);


    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks( ) {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent( s, forceResendingToken );


            verificarionCodeBySystem=s;

        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String Code=phoneAuthCredential.getSmsCode();
            if(Code!=null){
                verifyCode(Code);
                code.setText( Code );

            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText( VerifyPhone.this, e.getMessage(), Toast.LENGTH_SHORT ).show( );
        }
    };

    private void verifyCode(String verificationCode){

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificarionCodeBySystem,verificationCode);
        SigninUserByCredential(credential);

    }

    private void SigninUserByCredential(PhoneAuthCredential credential) {

        loadingDialog.show();

        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential( credential ).addOnCompleteListener( VerifyPhone.this, new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                  //  firebaseAuth.signInWithEmailAndPassword( mail,password  );
                    Map<String,Object> userData =new HashMap<>(  );
                    userData.put( "is_valid",true );
                    userData.put( "fullname","" );
                    userData.put( "image","" );
                    userData.put( "mail","" );
                    userData.put( "password","" );
                    userData.put( "permanent_phone",phoneNo );
                    userData.put( "phone",phoneNo );
                    userData.put( "previous_position",0 );
                    userData.put( "address_details","" );
                    userData.put( "address_type","" );



                    FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).set( userData ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Map<String,Object> listSize =new HashMap<>(  );
                            listSize.put( "list_size",0 );
                            FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).collection( "USER_DATA" ).document("MY_ADDRESS").set( listSize )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    } );
                            FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).collection( "USER_DATA" ).document("MY_GROCERY_CARTITEMCOUNT").set( listSize )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    } );
                            FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).collection( "USER_DATA" ).document("MY_GROCERY_CARTLIST").set( listSize )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    } );
                            FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).collection( "USER_DATA" ).document("MY_GROCERY_ORDERS").set( listSize )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    } );

                            FirebaseFirestore.getInstance().collection( "USERS" ).document( firebaseAuth.getCurrentUser().getUid() ).collection( "USER_DATA" ).document("MY_GROCERY_WISHLIST").set( listSize )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {



                                        }
                                    } );



                        }
                    } );


                 /*   firebaseAuth.createUserWithEmailAndPassword(mail,password  ).addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){




                            }else {
                                loadingDialog.dismiss();

                                firebaseAuth.signOut();
                                Toast.makeText( VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show( );
                            }
                        }
                    } );*/






                }else {
                    loadingDialog.dismiss();
                    firebaseAuth.signOut();
                    Toast.makeText( VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show( );
                }
            }
        } );
    }


}
