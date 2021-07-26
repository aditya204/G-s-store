package com.gaurishankarprashad.gsstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaurishankarprashad.gsstore.Adapters.AdminOrderAdaptrer;
import com.gaurishankarprashad.gsstore.Models.AdminOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminConfirmOrder extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView confirmOrderRecycler;
    private AdminOrderAdaptrer adminOrderAdaptrer;
    private List<AdminOrderModel> adminOrderModelList;
    private TextView coinfirmed,pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_confirm_order );


        toolbar=findViewById( R.id.confirm_toolbar );
        confirmOrderRecycler=findViewById( R.id.confirmOrderRecycler );
        pending=findViewById( R.id.admin_confirmed_pending_order_txt );
        coinfirmed=findViewById( R.id.admin_confirmed_confirmed_order_txt );


        toolbar.setTitle( "Confirm Order" );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        adminOrderModelList = new ArrayList<>( );

        adminOrderAdaptrer = new AdminOrderAdaptrer( adminOrderModelList );
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        confirmOrderRecycler.setLayoutManager( linearLayoutManager );
        confirmOrderRecycler.setAdapter( adminOrderAdaptrer );


        FirebaseFirestore.getInstance().collection( "ORDERS").orderBy( "id" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                        boolean is_paid=(boolean) documentSnapshot.get( "is_paid" );
                        if(is_paid){

                            adminOrderModelList.add( new AdminOrderModel( documentSnapshot.get( "id" ).toString(),
                                    documentSnapshot.get( "time" ).toString(),
                                    documentSnapshot.get( "otp" ).toString(),
                                    documentSnapshot.get( "grand_total" ).toString(),
                                    (boolean)documentSnapshot.get( "is_paid" ),
                                    (boolean)documentSnapshot.get( "is_pickUp" )
                            ) );


                        }

                    }
                    adminOrderAdaptrer.notifyDataSetChanged();





                }
            }
        } );


        pending.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( AdminConfirmOrder.this,PendingOrders.class );
                startActivity( intent );
            }
        } );
        coinfirmed.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( AdminConfirmOrder.this,AdminConfirmOrder.class );
                startActivity( intent );            }
        } );
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,PendingOrders.class);
        startActivity(intent);
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
           onBackPressed();
        }

        return super.onOptionsItemSelected( item );
    }


}
