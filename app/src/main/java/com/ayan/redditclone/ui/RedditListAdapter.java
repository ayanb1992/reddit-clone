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
import com.ayan.redditclone.service.RedditFeedService;
import com.ayan.redditclone.service.WebServiceCallback;
import com.ayan.redditclone.utils.Constants;
import com.ayan.redditclone.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.ViewHolder> {

    private List<Item> mItems;
    private Context mContext;
    private final String LOG_TAG = RedditListAdapter.class.getSimpleName();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public RedditListAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.mItems = items;
        this.mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View redditView = inflater.inflate(R.layout.layout_feed_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(redditView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Item item = mItems.get(position);

        String postInterval = Utils.getPostInterval(item.data.created);
        String bulletDot = " \u2022 ";

        StringBuffer finalString = new StringBuffer(mContext.getString(R.string.posted_prefix))
                .append(item.data.author)
                .append(bulletDot)
                .append(postInterval);

        finalString = (item.data.domain != null && !item.data.domain.contains(mContext.getString(R.string.self)))
                ? finalString.append(bulletDot).append(item.data.domain)
                : finalString.append("");

        viewHolder.redditTitle.setText(item.data.subreddit_name_prefixed);
        viewHolder.redditSubtitle.setText(finalString);
        viewHolder.titleTextView.setText(item.data.title);

        Float totalVotes = item.data.ups.floatValue() - item.data.downs.floatValue();
        String voteStr = Utils.getCommentVoteString(totalVotes, mContext.getString(R.string.vote));
        viewHolder.voteTextView.setText(voteStr);

        String commentStr = Utils.getCommentVoteString(item.data.num_comments.floatValue(), mContext.getString(R.string.comment));
        viewHolder.commentTextView.setText(commentStr);

        viewHolder.avatarImgView.setImageDrawable(mContext.getDrawable(R.drawable.ic_person_outline_black_24dp));

        RedditFeedService redditFeedService = new RedditFeedService(mContext, Constants.BASE_URL);
        redditFeedService.getThumbNail(item.data.subreddit_name_prefixed + "/about.json", new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                Item responseModel = (Item) response;
                try {
                    Glide.with(mContext).load(responseModel.data.icon_img)
                            .placeholder(R.drawable.ic_person_outline_black_24dp)
                            .into(viewHolder.avatarImgView);
                } catch (Exception ex) {
                    Glide.with(mContext).load(R.drawable.ic_person_outline_black_24dp).into(viewHolder.avatarImgView);
                }
            }

            @Override
            public void onFailure() {

            }
        });

        if (item.data.thumbnail != null &&
                !item.data.thumbnail.isEmpty() &&
                !item.data.thumbnail.toLowerCase().contains(mContext.getString(R.string.self))) {

            viewHolder.thumbnailImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(item.data.thumbnail)
                    .into(viewHolder.thumbnailImageView);
        } else {
            viewHolder.thumbnailImageView.setVisibility(View.GONE);
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
        public TextView redditTitle;
        public TextView redditSubtitle;
        public ImageView avatarImgView;
        public TextView titleTextView;
        public TextView voteTextView;
        public TextView commentTextView;
        public ImageView thumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            redditTitle = (TextView) itemView.findViewById(R.id.redditTitle);
            redditSubtitle = (TextView) itemView.findViewById(R.id.redditSubtitle);
            avatarImgView = (ImageView) itemView.findViewById(R.id.redditAvatarImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            voteTextView = (TextView) itemView.findViewById(R.id.voteTextView);
            commentTextView = (TextView) itemView.findViewById(R.id.commentTextView);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbNailImgView);
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
