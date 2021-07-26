package com.gaurishankarprashad.gsstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Edit_pofile extends AppCompatActivity {


    private EditText editName;
    private TextView editMail,changePassword;
    private EditText editPhone;
    private Toolbar toolbar;
    private Button update;


    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_pofile );


        editName = findViewById( R.id.editTextName );
        changePassword=findViewById( R.id.edit_profile_changePassword );

        editMail = findViewById( R.id.editTextMail );
        editPhone = findViewById( R.id.editTextPhone );
         toolbar = findViewById( R.id.toolbar );
         update=findViewById( R.id.edit_profile_update_btn );
        firebaseFirestore = FirebaseFirestore.getInstance( );

        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    editName.setHint( task.getResult().get("fullname").toString());
                    editPhone.setHint( task.getResult().get("phone").toString());

                }else{
                    String error = task.getException( ).getMessage( );
                    Toast.makeText( Edit_pofile.this, error, Toast.LENGTH_SHORT ).show( );
                }

            }
        } );







        changePassword.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( Edit_pofile.this,ForgotPassword.class );
                startActivity( intent );

            }
        } );


        update.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                Map<String, Object> Edit_name = new HashMap<>( );
                Edit_name.put( "fullname", editName.getText( ).toString( ) );


                firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                        .update( Edit_name ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful( )) {
                            Toast.makeText( Edit_pofile.this, "name is updated", Toast.LENGTH_SHORT ).show( );


                        } else {
                            String error = task.getException( ).getMessage( );
                            Toast.makeText( Edit_pofile.this, error, Toast.LENGTH_SHORT ).show( );


                        }


                    }
                } );



                Map<String, Object> Edit_phone = new HashMap<>( );
                Edit_phone.put( "phone", editPhone.getText( ).toString( ) );

                firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                        .update( Edit_phone ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful( )) {
                            Toast.makeText( Edit_pofile.this, "phone no is updated", Toast.LENGTH_SHORT ).show( );


                        } else {
                            String error = task.getException( ).getMessage( );
                            Toast.makeText( Edit_pofile.this, error, Toast.LENGTH_SHORT ).show( );


                        }
                    }
                } );

            }
        } );




        toolbar.setTitle( "Edit Profile" );
        setSupportActionBar( toolbar );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );









    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId( ) == android.R.id.home) {
            finish( );
        }

        return super.onOptionsItemSelected( item );
    }


}
