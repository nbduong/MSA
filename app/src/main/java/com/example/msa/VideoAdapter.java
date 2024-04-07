package com.example.msa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final List<Video> allVideos;
    private final Context  context;


    private  DatabaseHelper dbHelper;
    public VideoAdapter (Context ctx, List<Video> videos){
        this.allVideos = videos;
        this.context = ctx;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        dbHelper = new DatabaseHelper(context);
        float Rate =dbHelper.getAverageRating(allVideos.get(position).getTitle());
        System.out.println(allVideos.get(position).getTitle());
        holder.title.setText(allVideos.get(position).getTitle());
        holder.rate.setText(String.format("Đánh giá: %.2f", Rate));
        System.out.println(Rate);
//        holder.rate.setText("haha");
        String link = allVideos.get(position).getImageUrl();
        link = link.substring(0, 4) + "s" + link.substring(4);
        Picasso.get().load(link).into(holder.videoImage);


        holder.vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("videoData", allVideos.get(position));
                Intent i = new Intent(context, Player.class);
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allVideos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImage;
        TextView title,rate;
        View vv;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            videoImage = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            rate = itemView.findViewById(R.id.Rate);
            vv = itemView;
        }
    }
}

