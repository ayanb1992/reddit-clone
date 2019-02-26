package com.ayan.redditclone.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayan.redditclone.R;
import com.ayan.redditclone.model.Item;
import com.bumptech.glide.Glide;

import java.util.List;

public class SubredditFeedAdapter extends RecyclerView.Adapter<SubredditFeedAdapter.ViewHolder> {

    private List<Item> mItems;
    private Context mContext;
    private final String LOG_TAG = SubredditFeedAdapter.class.getSimpleName();
    private final OnItemClickListener listener;

    public SubredditFeedAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.mItems = items;
        this.mContext = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View subredditView = inflater.inflate(R.layout.layout_feed_subreddit, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(subredditView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Item item = mItems.get(position);
        viewHolder.subredditTitle.setText(item.data.display_name_prefixed);

        if (item.data.header_img != null &&
                !item.data.header_img.isEmpty()) {

            Glide.with(mContext).load(item.data.header_img)
                    .placeholder(R.drawable.ic_person_outline_black_24dp)
                    .into(viewHolder.subredditAvatar);
        } else {
            viewHolder.subredditAvatar.setImageDrawable(mContext.getDrawable(R.drawable.ic_person_outline_black_24dp));
        }
        viewHolder.bind(mItems.get(position), listener);
    }

    public void clear() {
        final int size = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subredditTitle;
        public ImageView subredditAvatar;

        public ViewHolder(View itemView) {
            super(itemView);

            subredditTitle = (TextView) itemView.findViewById(R.id.subredditTitle);
            subredditAvatar = (ImageView) itemView.findViewById(R.id.subredditAvatar);
        }

        public void bind(final Item item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
