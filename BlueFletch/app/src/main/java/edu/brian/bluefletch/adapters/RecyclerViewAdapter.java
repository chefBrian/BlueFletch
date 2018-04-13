package edu.brian.bluefletch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import edu.brian.bluefletch.models.Post;
import edu.brian.bluefletch.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mData;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<Post> mData){
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions();

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.feed_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_created_date.setText(mData.get(position).getCreatedDate());
        holder.tv_username.setText(mData.get(position).getUsername());
        holder.tv_post_text.setText(mData.get(position).getPostText());

        Glide.with(mContext).load(mData.get(position).getImageUrl()).apply(option).into(holder.img_user_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_post_text;
        TextView tv_created_date;
        ImageView img_user_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.username);
            tv_created_date = itemView.findViewById(R.id.postDate);
            tv_post_text = itemView.findViewById(R.id.postText);
            img_user_image = itemView.findViewById(R.id.userImage);
        }
    }
}
