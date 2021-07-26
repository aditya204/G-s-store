package com.gaurishankarprashad.gsstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class alpha extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_alpha );


        button=findViewById( R.id.button3 );

        button.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword( "adityaraz.6020@gmail.com","1234567" ).addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent intent=new Intent( alpha.this,MainActivity.class);
                            startActivity( intent );

                        }
                    }
                } );

            }
        } );



    }
}
