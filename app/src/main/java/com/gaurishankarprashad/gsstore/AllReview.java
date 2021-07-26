package com.gaurishankarprashad.gsstore;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.ReviewAdapter;
import com.gaurishankarprashad.gsstore.Models.ReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllReview extends AppCompatActivity {

    /////productRating
    private RecyclerView reviewRecycler;
    private List<ReviewModel> reviewModelList;
    private ReviewAdapter reviewAdapter;

    private TextView star_1_count;
    private TextView star_2_count;
    private TextView star_3_count;
    private TextView star_4_count;
    private TextView star_5_count;
    private TextView avg_rating;
    private TextView totalstarCount;
    private TextView totalreviewCount;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;
    final int[] one_star_count = {0};
    final int[] two_star_count = {0};
    final int[] five_star_count = {0};
    final int[] four_star_count = {0};
    final int[] three_star_count = {0};
    final int[] total_rating_count = {0};
    final int[] total_review_count = {0};
    final double[] avg_star = {0.00000};
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_all_review );


        reviewRecycler=findViewById( R.id.review_recycler );
        star_1_count = findViewById( R.id.review_1_star_count );
        star_2_count = findViewById( R.id.review_2_star_count );
        star_3_count = findViewById( R.id.review_3_star_count );
        star_4_count = findViewById( R.id.review_4_star_count );
        star_5_count = findViewById( R.id.review_5_star_count );
        progressBar1 = findViewById( R.id.progressBar1star );
        progressBar2 = findViewById( R.id.progressBar2star );
        progressBar3 = findViewById( R.id.progressBar3star );
        progressBar4 = findViewById( R.id.progressBar4star );
        progressBar5 = findViewById( R.id.progressBar5star );
        avg_rating = findViewById( R.id.review_avg_star );
        totalstarCount = findViewById( R.id.review_total_rating );
        totalreviewCount = findViewById( R.id.review_total_review_count );

        final String product_id = getIntent( ).getStringExtra( "product_id" );


        reviewModelList = new ArrayList<>( );

        reviewAdapter = new ReviewAdapter( reviewModelList );
        LinearLayoutManager reviewlayoutManager = new LinearLayoutManager( this );
        reviewlayoutManager.setOrientation( RecyclerView.VERTICAL );
        reviewRecycler.setAdapter( reviewAdapter );
        reviewRecycler.setLayoutManager( reviewlayoutManager );
        reviewRecycler.setHasFixedSize( true);
        reviewRecycler.setNestedScrollingEnabled( false );




        FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document(product_id).collection( "REVIEWS" ).orderBy( "order_id", Query.Direction.DESCENDING )
                .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){

                        String rating=documentSnapshot.get( "rating" ).toString();
                        String review=documentSnapshot.get( "review" ).toString();

                        if(!(review.length()==0)){
                            total_review_count[0]= total_review_count[0]+1;
                            totalreviewCount.setText( String.valueOf(  total_review_count[0] ) );
                            reviewModelList.add( new ReviewModel(
                                    documentSnapshot.get("user_name").toString(),
                                    rating,
                                    documentSnapshot.get("date").toString(),
                                    review,
                                    documentSnapshot.get("image").toString()
                            ) );

                            reviewAdapter.notifyDataSetChanged();
                        }
                        totalreviewCount.setText( String.valueOf(  total_review_count[0] ) );
                        totalstarCount.setText( String.valueOf( total_rating_count[0] ) );

                        if(!rating.equals( "0" )){
                            total_rating_count[0] = total_rating_count[0] + 1;
                            totalstarCount.setText( String.valueOf( total_rating_count[0] ) );
                        }


                        if (rating.equals( "5" )) {
                            five_star_count[0] = five_star_count[0] + 1;
                            star_5_count.setText( String.valueOf( five_star_count[0] ) );
                        } else {
                            star_5_count.setText( String.valueOf( five_star_count[0] ) );
                        }
                        if (rating.equals( "4" )) {
                            four_star_count[0] = four_star_count[0] + 1;
                            star_4_count.setText( String.valueOf( four_star_count[0] ) );

                        } else {
                            star_4_count.setText( String.valueOf( four_star_count[0] ) );

                        }
                        if (rating.equals( "3" )) {
                            three_star_count[0] = three_star_count[0] + 1;
                            star_3_count.setText( String.valueOf( three_star_count[0] ) );

                        } else {
                            star_3_count.setText( String.valueOf( three_star_count[0] ) );
                        }
                        if (rating.equals( "2" )) {
                            two_star_count[0] = two_star_count[0] + 1;
                            star_2_count.setText( String.valueOf( two_star_count[0] ) );

                        } else {
                            star_2_count.setText( String.valueOf( two_star_count[0] ) );

                        }
                        if (rating.equals( "1" )) {
                            one_star_count[0] = one_star_count[0] + 1;
                            star_1_count.setText( String.valueOf( one_star_count[0] ) );
                        } else {
                            star_1_count.setText( String.valueOf( one_star_count[0] ) );

                        }


                        if(total_rating_count[0]!=0) {
                            avg_star[0] = (5.0 * five_star_count[0] + 4.0 * four_star_count[0] + 3.0 * three_star_count[0] + 2.0 * two_star_count[0] + one_star_count[0]) / total_rating_count[0];
                            avg_rating.setText( String.format( "%.1f", avg_star[0] ) );

                            Map< String,Object> Update_ratings= new HashMap< >( ) ;
                            Update_ratings.put( "rating",String.format( "%.1f", avg_star[0] ) );
                            Update_ratings.put( "review_count",String.valueOf( total_rating_count[0] ) );

                            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( product_id ).update( Update_ratings )
                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                        }
                                    } );


                            progressBar1.setProgress( (one_star_count[0] * 100) / total_rating_count[0] );
                            progressBar2.setProgress( (two_star_count[0] * 100) / total_rating_count[0] );
                            progressBar3.setProgress( (three_star_count[0] * 100) / total_rating_count[0] );
                            progressBar4.setProgress( (four_star_count[0] * 100) / total_rating_count[0] );
                            progressBar5.setProgress( (five_star_count[0] * 100) / total_rating_count[0] );
                        }

                    }
                    reviewAdapter.notifyDataSetChanged();
                }

            }
        } );
    }
}
