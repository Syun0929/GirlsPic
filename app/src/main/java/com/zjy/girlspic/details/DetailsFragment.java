package com.zjy.girlspic.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zjy.girlspic.R;
import com.zjy.girlspic.data.Photo;
import com.zjy.girlspic.service.PicDetailsParser;
import com.zjy.girlspic.util.ThreadUtils;
import com.zjy.girlspic.util.ui.ResizableImageView;

import java.util.ArrayList;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jiyoung.tsang on 16/7/4.
 */
public class DetailsFragment extends Fragment {

    public static final String DETAILS_URL = "DETAILS_URL";

    private String mDetailsUrl;
    private  DetailsAdapter detailsAdapter;

    public DetailsFragment(){}

    public static DetailsFragment newInstance(){
        return new DetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDetailsUrlIfAny();
        detailsAdapter = new DetailsAdapter(itemLongClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details_frag,container,false);

        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.details_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL,false));
        recyclerView.setAdapter(detailsAdapter);

        ThreadUtils.GENERAL_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                final List<Photo> list = PicDetailsParser.getData(mDetailsUrl);
                ThreadUtils.MAIN_HANDLE.post(new Runnable() {
                    @Override
                    public void run() {
                        detailsAdapter.setList(list);
                    }
                });
            }
        });

        return root;
    }

    DetailsAdapter.DetailsItemLongClickListener itemLongClickListener = new DetailsAdapter.DetailsItemLongClickListener() {
        @Override
        public void onDetailsLongClick(Photo photo) {
            Toast.makeText(getContext(),"下载功能尚在开发中……", Toast.LENGTH_SHORT).show();
        }
    };

    void setDetailsUrlIfAny(){
        if(getArguments()!=null && getArguments().containsKey(DETAILS_URL)){
            setmDetailsUrl(getArguments().getString(DETAILS_URL));
        }
    }

    public void setmDetailsUrl(String mDetailsUrl) {
        this.mDetailsUrl = mDetailsUrl;
    }

    public static class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder>{

        private List<Photo> list;
        private DetailsItemLongClickListener detailsItemLongClickListener;

        public DetailsAdapter(DetailsItemLongClickListener longClickListener){
            this.list = new ArrayList<Photo>();
            this.detailsItemLongClickListener = longClickListener;

        }

        public void setList(@NonNull List<Photo> data){
            checkNotNull(data);
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Photo photo = list.get(position);
            holder.textView.setText(photo.title);

            Picasso.with(holder.itemView.getContext()).load(photo.src).into(holder.imageView);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    detailsItemLongClickListener.onDetailsLongClick(photo);
                    return false;
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details,parent,false);
            return new ViewHolder(v);
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            public ResizableImageView imageView;
            public TextView textView;
            public ViewHolder(View v){
                super(v);
                imageView = (ResizableImageView) v.findViewById(R.id.item_details_image);
                textView = (TextView)v.findViewById(R.id.item_details_title);
            }
        }

        public interface DetailsItemLongClickListener{
            void onDetailsLongClick(Photo photo);
        }
    }
}
