package com.gaurishankarprashad.gsstore.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.gaurishankarprashad.gsstore.Models.HomeCategoryModels;
import com.gaurishankarprashad.gsstore.Models.HomeModel;
import com.gaurishankarprashad.gsstore.Models.dealsofthedayModel;
import com.gaurishankarprashad.gsstore.Models.sliderModel;
import com.gaurishankarprashad.gsstore.Product;
import com.gaurishankarprashad.gsstore.R;
import com.gaurishankarprashad.gsstore.SearchedProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeAdapter extends  RecyclerView.Adapter{

    private List<HomeModel> homeModelList;

    public HomeAdapter(List<HomeModel> homeModelList) {
        this.homeModelList = homeModelList;

    }

    @Override
    public int getItemViewType(int position) {
        switch (homeModelList.get( position ).getType( )) {
            case 1:
                return HomeModel.BANNER_SLIDER;

            case 2:
                return HomeModel.DEAL_OF_THE_DAY;
            case 3:
                return HomeModel.TRENDING;
            case 4:
                return HomeModel.CICULAR_HORIZONTAL;
            case 5:
                return HomeModel.FOUR_IMAGE;
            default:
                return -1;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case HomeModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.banner_activity, parent, false );
                return new homeBannerSlider( bannerSliderView );
            case HomeModel.DEAL_OF_THE_DAY:
                View dealofthedayview = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.dealoftheday_activity, parent, false );
                return new dealoftheday( dealofthedayview );
            case HomeModel.TRENDING:
                View trendingview = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.trnding_layout, parent, false );
                return new trendingVIewHolser( trendingview );
            case HomeModel.CICULAR_HORIZONTAL:
                View circularview = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.circular_horizontal_layout, parent, false );
                return new circularHorizontal( circularview );

            case HomeModel.FOUR_IMAGE:
                View fourview = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.four_image, parent, false );
                return new fourImage( fourview );

            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homeModelList.get( position ).getType( )) {
            case HomeModel.BANNER_SLIDER:
                List<sliderModel> sliderModelList = homeModelList.get( position ).getSliderModelList( );
                ((homeBannerSlider) holder).setBannerSliderViewPager( sliderModelList );
                break;
            case HomeModel.DEAL_OF_THE_DAY:
                String titile = homeModelList.get( position ).getTitle( );
                List<dealsofthedayModel> dealsofthedayModelList = homeModelList.get( position ).getDealsofthedayModelList( );
                ArrayList<String> ids=homeModelList.get( position ).getIds();
                String background_color=homeModelList.get( position ).getBackground_color();
                ((dealoftheday) holder).setDealsofthedayRecycler( titile, dealsofthedayModelList,ids,background_color );
                break;

            case HomeModel.TRENDING:
                String gridtitle = homeModelList.get( position ).getTitle( );
                List<dealsofthedayModel> gridLIst = homeModelList.get( position ).getDealsofthedayModelList( );
                ArrayList<String> Gridids=homeModelList.get( position ).getIds();
                String grid_background_color=homeModelList.get( position ).getBackground_color();
                ((trendingVIewHolser) holder).setGridAdapter( gridtitle, gridLIst,Gridids,grid_background_color );
                break;
            case HomeModel.CICULAR_HORIZONTAL:
                String circularTilte = homeModelList.get( position ).getCircularTitle( );
                List<HomeCategoryModels> circularList = homeModelList.get( position ).getHomeCategoryModelsList( );
                ((circularHorizontal) holder).circularAdapter( circularTilte, circularList );
                break;
            case HomeModel.FOUR_IMAGE:
                String image1 = homeModelList.get( position ).getImage_1( );
                String image2 = homeModelList.get( position ).getImage_2( );
                String image3 = homeModelList.get( position ).getImage_3( );
                String image4 = homeModelList.get( position ).getImage_4( );
                String name1 = homeModelList.get( position ).getName_1( );
                String name2 = homeModelList.get( position ).getName_2( );
                String name3 = homeModelList.get( position ).getName_3( );
                String name4 = homeModelList.get( position ).getName_4( );
                String tag1 = homeModelList.get( position ).getTag_1( );
                String tag2 = homeModelList.get( position ).getTag_2( );
                String tag3 = homeModelList.get( position ).getTag_3( );
                String tag4 = homeModelList.get( position ).getTag_4( );
                String title=homeModelList.get( position ).getFourImageTitle( );
                ((fourImage) holder).setData( image1,image2,image3,image4,name1,name2,name3,name4,title,tag1,tag2,tag3,tag4 );
                break;
            default:
                return;


        }


    }

    @Override
    public int getItemCount() {
        return homeModelList.size();
    }




    public class homeBannerSlider extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;

        private int currentPage = 2;
        private Timer timer;
        final private long delayTime = 2000;
        final private long periodTime = 3000;

        public homeBannerSlider(@NonNull View itemView) {
            super( itemView );


            bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_recycler );


        }

        @SuppressLint("ClickableViewAccessibility")
        private void setBannerSliderViewPager(final List<sliderModel> sliderModelList) {
            SliderAdapter sliderAdapter = new SliderAdapter( sliderModelList );
            bannerSliderViewPager.setAdapter( sliderAdapter );
            bannerSliderViewPager.setClipToPadding( false );
            bannerSliderViewPager.setPageMargin( 20 );
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener( ) {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper( sliderModelList );
                    }

                }
            };
            bannerSliderViewPager.addOnPageChangeListener( onPageChangeListener );


            startBannerSlideShow( sliderModelList );


            bannerSliderViewPager.setOnTouchListener( new View.OnTouchListener( ) {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper( sliderModelList );
                    stopBannerSlideShow( );
                    if (motionEvent.getAction( ) == MotionEvent.ACTION_UP) {
                        startBannerSlideShow( sliderModelList );
                    }
                    return false;
                }


            } );


        }

        private void pageLooper(List<sliderModel> sliderModelList) {

            if (currentPage == sliderModelList.size( ) - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem( currentPage, false );
            }

            if (currentPage == 1) {
                currentPage = sliderModelList.size( ) - 3;
                bannerSliderViewPager.setCurrentItem( currentPage, false );
            }

        }

        private void startBannerSlideShow(final List<sliderModel> sliderModelList) {
            final Handler handler = new Handler( );
            final Runnable update = new Runnable( ) {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size( )) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem( currentPage++, true );
                }
            };
            timer = new Timer( );
            timer.schedule( new TimerTask( ) {
                @Override
                public void run() {
                    handler.post( update );
                }
            }, delayTime, periodTime );


        }

        private void stopBannerSlideShow() {
            timer.cancel( );
        }
    }


    public class dealoftheday extends RecyclerView.ViewHolder {

        private Button viewAllbtn;
        private ConstraintLayout dealsCL;
        private TextView header;
        private RecyclerView dealsofthedayRecycler;

        public dealoftheday(@NonNull View itemView) {
            super( itemView );

            dealsCL=itemView.findViewById( R.id.dealsoftheday_layout );
            viewAllbtn = itemView.findViewById( R.id.dealsofthedaybutton );
            header = itemView.findViewById( R.id.dealsofthedaytitle );
            dealsofthedayRecycler = itemView.findViewById( R.id.dealsofthedayrecycler );
        }

        private void setDealsofthedayRecycler(String title, List<dealsofthedayModel> dealsofthedayModelList, final ArrayList<String> Ids, String background_color) {

            dealsCL.getBackground().setColorFilter(Color.parseColor(background_color), PorterDuff.Mode.SRC_ATOP);
            header.setText( title );
            if (dealsofthedayModelList.size( ) >= 8) {
                viewAllbtn.setVisibility( View. VISIBLE);
            } else {
                viewAllbtn.setVisibility( View.INVISIBLE );
            }
            dealsofthedayAdapter DealsofthedayAdapter = new dealsofthedayAdapter( dealsofthedayModelList );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext( ) );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            dealsofthedayRecycler.setLayoutManager( linearLayoutManager );
            dealsofthedayRecycler.setAdapter( DealsofthedayAdapter );
            DealsofthedayAdapter.notifyDataSetChanged( );

            viewAllbtn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), Product.class );
                    intent.putExtra( "ids",  Ids );
                    intent.putExtra( "from_grocery_category",2 );
                    itemView.getContext().startActivity( intent );

                }
            } );

        }
    }



    public class trendingVIewHolser extends RecyclerView.ViewHolder {

        private TextView gridtitle;
        private Button gridViewAll;
        private ConstraintLayout grid_CL;
        private GridView gridView;


        public trendingVIewHolser(@NonNull View itemView) {
            super( itemView );

            gridtitle = itemView.findViewById( R.id.trending_title );
            gridViewAll = itemView.findViewById( R.id.trending_viewall_btn );
            gridView = itemView.findViewById( R.id.trending_gridview );
            grid_CL=itemView.findViewById( R.id.grid_CL );
        }



        private void setGridAdapter(String titile, List<dealsofthedayModel> dealsofthedayModelList, final ArrayList<String> Ids, String background_color) {
            if(dealsofthedayModelList.size()>4){
                gridViewAll.setVisibility( View.VISIBLE );
            }else {
                gridViewAll.setVisibility( View.GONE );
            }

            grid_CL.getBackground().setColorFilter(Color.parseColor(background_color), PorterDuff.Mode.SRC_ATOP);
            gridtitle.setText( titile );
            gridView.setAdapter( new trendingAdapter( dealsofthedayModelList ) );

            gridViewAll.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), Product.class );
                    intent.putExtra( "ids",  Ids );
                    intent.putExtra( "from_grocery_category",2 );
                    itemView.getContext().startActivity( intent );

                }
            } );
        }
    }


    public class circularHorizontal extends RecyclerView.ViewHolder{

        private TextView name;
        private RecyclerView ciecularRecycler;

        public circularHorizontal(@NonNull View itemView) {
            super( itemView );

            name=itemView.findViewById( R.id.circular_horizontal_title );
            ciecularRecycler=itemView.findViewById( R.id.circular_horizontal_recycler );


        }
        private void circularAdapter (String titile,List<HomeCategoryModels > homeCategoryModelsList){


            name.setText( titile );
            CircularHorizontqalAdapter circularHorizontqalAdapter = new CircularHorizontqalAdapter( homeCategoryModelsList );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext( ) );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            ciecularRecycler.setLayoutManager( linearLayoutManager );
            ciecularRecycler.setAdapter( circularHorizontqalAdapter );
            circularHorizontqalAdapter.notifyDataSetChanged( );
        }
    }


    public class fourImage extends RecyclerView.ViewHolder{

        private ImageView image1,image2,image4,image3;
        private  TextView nmae1,nmae3,nmae2,nmae4;
        private TextView title;
        private LinearLayout exploreLL;


        public fourImage(@NonNull View itemView) {
            super( itemView );

            title=itemView.findViewById( R.id.four_image_title );
            exploreLL=itemView.findViewById( R.id.four_image_exploreLL );
            image1=itemView.findViewById( R.id.four_image_1 );
            image2=itemView.findViewById( R.id.four_image_2 );
            image3=itemView.findViewById( R.id.four_image_3 );
            image4=itemView.findViewById( R.id.four_image_4 );
            nmae1=itemView.findViewById( R.id.four_image_name_1 );
            nmae2=itemView.findViewById( R.id.four_image_name_2);
            nmae3=itemView.findViewById( R.id.four_image_name_3 );
            nmae4=itemView.findViewById( R.id.four_image_name_4);


        }

        private void setData(String url1, String url2, String url3, String url4, final String Name1, final String Name2, final String Name3, final String Name4, String Title, final String t1, final String t2, final String t3, final String t4){
            title.setText( Title );
            nmae1.setText( Name1 );
            nmae2.setText( Name2 );
            nmae3.setText( Name3);
            nmae4.setText( Name4 );
            Glide.with( itemView.getContext() ).load( url1 ).into( image1 );
            Glide.with( itemView.getContext() ).load( url2 ).into( image2 );
            Glide.with( itemView.getContext() ).load( url3 ).into( image3 );
            Glide.with( itemView.getContext() ).load( url4 ).into( image4 );

            image1.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), SearchedProduct.class );
                    intent.putExtra( "tag_string",t1 );
                    itemView.getContext().startActivity( intent );
                }
            } );
            image2.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), SearchedProduct.class );
                    intent.putExtra( "tag_string",t2 );
                    itemView.getContext().startActivity( intent );
                }
            } );
            image3.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), SearchedProduct.class );
                    intent.putExtra( "tag_string",t3 );
                    itemView.getContext().startActivity( intent );
                }
            } );
            image4.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), SearchedProduct.class );
                    intent.putExtra( "tag_string",t4 );
                    itemView.getContext().startActivity( intent );
                }
            } );

            exploreLL.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent( itemView.getContext(), SearchedProduct.class );
                    intent.putExtra( "tag_string",t1+" "+t2+" "+t3+" "+t4 );
                    itemView.getContext().startActivity( intent );
                }
            } );
        }
    }

}
