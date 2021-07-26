package com.gaurishankarprashad.gsstore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    public TextInputLayout name;
    public TextInputLayout password;
    private ImageView login;
    public TextView info, etforgotPassword;
    private TextView newreg,forgotPassword;
    public FirebaseAuth firebaseAuth;
    private Dialog loadingDialog;
    private NetworkInfo networkInfo;
    //  private RequestQueue requestQueue;
    private String user = "";
    private TextView tvGS, tvGauri;

    private Button userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        tvGauri = findViewById( R.id.textViewGauri );
        tvGS = findViewById( R.id.textViewGS );
        name = findViewById( R.id.name );
        password = findViewById( R.id.password );
        login = findViewById( R.id.button );
        newreg = findViewById( R.id.newRegister );
        forgotPassword =findViewById( R.id.forgotPassword );
        etforgotPassword = findViewById( R.id.forgotPassword );
        // skipbtn=findViewById( R.id.button2 );
        firebaseAuth = FirebaseAuth.getInstance( );
        userRegister = findViewById( R.id.regUserRegister );

        loadingDialog = new Dialog( MainActivity.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.dismiss( );
        // current_work_page=findViewById( R.id.goto_current_WORKING_PAGE );


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        networkInfo = connectivityManager.getActiveNetworkInfo( );



        forgotPassword.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( MainActivity.this,ForgotPassword.class );
                startActivity( intent );
            }
        } );




        setAlphaAnimation( tvGS,tvGauri );

        userRegister.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if(validatePhone()){
                    Intent intent = new Intent( MainActivity.this, VerifyPhone.class );
                    intent.putExtra( "phoneNo", name.getEditText( ).getText( ).toString() );

                    startActivity( intent );
                }


              /*  if (validateEmail( ) && validateName( ) && validatePhone( ) && validatePassword( )) {
                    Toast.makeText( Registration.this, "Sending OTP", Toast.LENGTH_SHORT ).show( );
                    Intent intent = new Intent( Registration.this, VerifyPhone.class );
                    intent.putExtra( "phoneNo", userPhone.getEditText( ).getText( ).toString() );
                    intent.putExtra( "name", name.getEditText( ).getText( ).toString() );
                    intent.putExtra( "mail", userMail.getEditText( ).getText( ).toString());
                    intent.putExtra( "password", userPassword.getEditText( ).getText( ).toString() );
                    startActivity( intent );
                }else {
                    Toast.makeText( Registration.this, "Something went Wrong", Toast.LENGTH_SHORT ).show( );

                }*/


            }
        } );
       /* login.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                if (networkInfo != null && networkInfo.isConnected( )) {

                    if (name.getEditText().getText( ).toString( ).isEmpty( ) || password.getEditText().getText( ).toString( ).isEmpty( )) {
                        Toast.makeText( MainActivity.this, "Please enter Email or password", Toast.LENGTH_SHORT ).show( );
                    } else {

                        loadingDialog.show( );
                        validate( name.getEditText().getText( ).toString( ), password.getEditText().getText( ).toString( ) );
                        String token_id = FirebaseInstanceId.getInstance( ).getToken( );

                        Map<String, Object> TokenId = new HashMap<>( );
                        TokenId.put( "token_id", token_id );


                    }

                } else {
                    Toast.makeText( MainActivity.this, "No Internet", Toast.LENGTH_SHORT ).show( );


                }

            }
        } );

        newreg.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, Registration.class );
                startActivity( intent );
            }
        } );*/

    }

    private boolean validatePhone() {

        String val = name.getEditText( ).getText( ).toString( );

        if (val.isEmpty( )) {

            name.setError( "Field cannot be empty" );
            return false;
        } else if (val.length( ) < 10) {
            name.setError( "Enter 10 digit no" );
            return false;

        } else {
            name.setError( null );
            name.setErrorEnabled( false );
            return true;

        }


    }


    public void validate(String username, String userPassword) {


        firebaseAuth.signInWithEmailAndPassword( username, userPassword ).addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingDialog.dismiss();
                    Intent intent=new Intent( MainActivity.this,Home.class );
                    startActivity( intent );
                    finish();
                }else {
                    loadingDialog.dismiss();
                    Toast.makeText( MainActivity.this, "Wrong Mail or Password", Toast.LENGTH_SHORT ).show( );
                }

            }
        } );


    }




    @Override
    public void onBackPressed() {


        Intent a = new Intent( Intent.ACTION_MAIN );
        a.addCategory( Intent.CATEGORY_HOME );
        a.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( a );

    }

    public static void setAlphaAnimation(final View v, final View v2) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat( v, "alpha", 1f, 0f );
        fadeOut.setDuration( 2000 ) ;
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat( v, "alpha", 0f, 1f );
        fadeIn.setDuration( 2000 );
        final AnimatorSet mAnimationSet = new AnimatorSet( );

        mAnimationSet.play( fadeOut ).after( fadeIn );

        mAnimationSet.addListener( new AnimatorListenerAdapter( ) {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd( animation );
                new Handler(  ).postDelayed( new Runnable( ) {
                    @Override
                    public void run(){

                        MainActivity.setAlphaAnimationV2( v,v2 );
                    }
                },800 );

            }
        } );
        mAnimationSet.start( );
    }

    public static void setAlphaAnimationV2(final View v, final View v2) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat( v2, "alpha", 1f, 0f );
        fadeOut.setDuration( 2000 );
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat( v2, "alpha", 0f, 1f );
        fadeIn.setDuration( 2000 );
        final AnimatorSet mAnimationSet = new AnimatorSet( );

        mAnimationSet.play( fadeOut ).after( fadeIn );


        mAnimationSet.addListener( new AnimatorListenerAdapter( ) {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd( animation );

                new Handler(  ).postDelayed( new Runnable( ) {
                    @Override
                    public void run(){

                        MainActivity.setAlphaAnimation( v,v2 );
                    }
                },800 );
            }
        } );
        mAnimationSet.start( );
    }
}
