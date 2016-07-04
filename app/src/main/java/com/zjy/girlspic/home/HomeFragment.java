package com.zjy.girlspic.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zjy.girlspic.R;
import com.zjy.girlspic.data.Image;
import com.zjy.girlspic.details.DetailsActivity;
import com.zjy.girlspic.service.GirlsPicParser;
import com.zjy.girlspic.util.ActivityUtils;
import com.zjy.girlspic.util.ThreadUtils;
import com.zjy.girlspic.util.ui.OnRecyclerViewScrollListener;
import com.zjy.girlspic.util.ui.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jiyoung.tsang on 16/6/18.
 */
public class HomeFragment  extends Fragment{


    private int mPage;

    private HomeAdapter homeAdapter;

    public HomeFragment(){
        mPage = 1;
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeAdapter = new HomeAdapter(homeItemListenner);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag,container,false);

        RecyclerView recylerView = (RecyclerView) root.findViewById(R.id.home_list);
        recylerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recylerView.setAdapter(homeAdapter);

        recylerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                final  List<Image> images = GirlsPicParser.getGallery(++mPage);
                ThreadUtils.MAIN_HANDLE.post(new Runnable() {
                    @Override
                    public void run() {
                        homeAdapter.addImagesData(images);

                    }
                });
            }
        });

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setScrollUpChild(recylerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadUtils.GENERAL_THREAD_POOL.execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<Image> images = GirlsPicParser.getGallery(1);


                        ThreadUtils.MAIN_HANDLE.post(new Runnable() {
                            @Override
                            public void run() {
                                homeAdapter.setImagesData(images);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        ThreadUtils.GENERAL_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                final List<Image> images = GirlsPicParser.getGallery(1);

                ThreadUtils.MAIN_HANDLE.post(new Runnable() {
                    @Override
                    public void run() {
                        homeAdapter.setImagesData(images);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return root;
    }

    HomeAdapter.HomeItemListener homeItemListenner = new HomeAdapter.HomeItemListener() {
        @Override
        public void onHomeItemClick(Image image) {
//            Toast.makeText(getContext(),"详情功能尚在开发中……",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(),DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Image.TAG,image);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);


        }
    };

    public static class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

        private List<Image> list;
        private HomeItemListener homeItemListener;

        public HomeAdapter(HomeItemListener homeItemListener) {
            super();
            list = new ArrayList<Image>();
            this.homeItemListener = homeItemListener;
        }

        public void setImagesData(@NonNull List<Image> data){
            checkNotNull(data);
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }

        public void addImagesData(@NonNull List<Image> data){
            checkNotNull(data);
            this.list.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Image image = list.get(position);
            holder.textView.setText(image.title);
            Picasso.with(holder.itemView.getContext()).load(image.imgUrl).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemListener.onHomeItemClick(image);
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
            return new ViewHolder(v);
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView;
            public ViewHolder(View v){
                super(v);
                imageView = (ImageView)v.findViewById(R.id.item_home_image);
                textView = (TextView)v.findViewById(R.id.item_home_title);
            }
        }

        public interface HomeItemListener{
            void onHomeItemClick(Image image);
        }
    }
}
