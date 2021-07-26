package com.gaurishankarprashad.gsstore;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView appNmae;
    private static int TIME_OUT=3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );


        logo=findViewById( R.id.splash_logo );
        appNmae=findViewById( R.id.splash_app_name );
        setAlphaAnimation( logo );


        new Handler(  ).postDelayed( new Runnable( ) {
            @Override
            public void run(){

                FirebaseUser user = FirebaseAuth.getInstance( ).getCurrentUser( );

                if (user != null) {

                    Intent intent = new Intent( SplashScreen.this, Home.class );
                    startActivity( intent );
                    finish();


                } else {
                Intent homeIntent=new Intent( SplashScreen.this,MainActivity.class );
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair <View,String>(appNmae,"main_storeTxt");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation( SplashScreen.this,pairs );
                        startActivity( homeIntent,options.toBundle() );
                        finish();

                    }

                }

/*
                FirebaseUser user = FirebaseAuth.getInstance( ).getCurrentUser( );

                if (user != null) {

                    Toast.makeText( SplashScreen.this, "null", Toast.LENGTH_SHORT ).show( );

                    Intent intent = new Intent( SplashScreen.this, Home.class );
                    startActivity( intent );
                    finish();

                }else {

                    Intent intent = new Intent( SplashScreen.this, MainActivity.class );
                    startActivity( intent );
                    finish();
                }

*/

            }
        },TIME_OUT );

        new Handler(  ).postDelayed( new Runnable( ) {
            @Override
            public void run(){
                appNmae.animate().translationY( 0f ).setDuration( 1000);

            }
        },1500 );

        appNmae.animate().translationY( 25f ).setDuration( 800 ).setStartDelay( 200 );

       //


    }

    public static void setAlphaAnimation(View v) {
        v.setAlpha( 0f );
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        fadeIn.setDuration(1500);


        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeOut).after(fadeIn);

        mAnimationSet.start();
        /*mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();*/
    }
}
