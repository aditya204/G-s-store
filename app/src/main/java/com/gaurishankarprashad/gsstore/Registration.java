package com.gaurishankarprashad.gsstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity {


    private static TextInputLayout name, userPassword, userPhone, userMail;
    private TextView userLogin;
    private Button userRegister;
    public FirebaseAuth firebase;
    public ProgressDialog progressDialog;
    public FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registration );

        userPassword = findViewById( R.id.regUserPassword );
        userPhone = findViewById( R.id.regUserPhone );
        userMail = findViewById( R.id.regUserEmainl );
        name = findViewById( R.id.regUserName );
        userLogin = findViewById( R.id.regUserLogin );
        userRegister = findViewById( R.id.regUserRegister );

        progressDialog = new ProgressDialog( this );
        firebase = FirebaseAuth.getInstance( );
        firebaseFirestore = FirebaseFirestore.getInstance( );

        userRegister.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( Registration.this, VerifyPhone.class );
                intent.putExtra( "phoneNo", userPhone.getEditText( ).getText( ).toString() );

                startActivity( intent );
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


    }


    private boolean validateName() {

        String val = name.getEditText( ).getText( ).toString( );

        if (val.isEmpty( )) {

            name.setError( "Field cannot be empty" );
            return false;
        } else {
            name.setError( null );
            return true;

        }


    }


    private boolean validateEmail() {
        userMail.setErrorEnabled( true );
        String val = userMail.getEditText( ).getText( ).toString( );
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty( )) {

            userMail.setError( "Field cannot be empty" );
            return false;
        } else if (!val.matches( emailPattern )) {
            userMail.setError( "Invalid email address" );
            return false;
        } else {
            userMail.setError( null );
            userMail.setErrorEnabled( false );
            return true;
        }


    }


    private boolean validatePhone() {

        String val = userPhone.getEditText( ).getText( ).toString( );

        if (val.isEmpty( )) {

            userPhone.setError( "Field cannot be empty" );
            return false;
        } else if (val.length( ) < 10) {
            userPhone.setError( "Enter 10 digit no" );
            return false;

        } else {
            userPhone.setError( null );
            userPhone.setErrorEnabled( false );
            return true;

        }


    }

    private boolean validatePassword() {

        String val = userPassword.getEditText( ).getText( ).toString( );
        String passwordval =

                "\\A\\w{1,20}\\z";

        if (val.isEmpty( )) {

            userPassword.setError( "Field cannot be empty" );
            return false;
        } else if (val.length( ) <= 5) {
            userPassword.setError( "password too short" );
            return false;

        } else if (!val.matches( passwordval )) {
            userPassword.setError( "invalid password" );
            return false;
        } else {
            userPassword.setError( null );
            userPassword.setErrorEnabled( false );
            return true;

        }


    }




}
