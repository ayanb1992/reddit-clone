package com.ayan.redditclone;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayan.redditclone.model.Item;
import com.ayan.redditclone.model.ResponseModel;
import com.ayan.redditclone.service.RedditFeedService;
import com.ayan.redditclone.service.WebServiceCallback;
import com.ayan.redditclone.utils.Constants;
import com.ayan.redditclone.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemDetailsActivity extends AppCompatActivity {
    private String permalink;
    private final String LOG_TAG = ItemDetailsActivity.class.getSimpleName();
    private CircleImageView itemAvatar;
    private TextView itemTitle;
    private TextView itemSubtitle;
    private TextView itemSuperTitle;
    private ImageView itemThumbnail;
    private TextView itemVoteTextView;
    private TextView itemCommentTextView;
    private CardView itemCardView;
    private RelativeLayout progressBarLayout;
    private LinearLayout itemParentView;
    private TextView commentVoteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        permalink = getIntent().getStringExtra(Constants.POST_PERMALINK_KEY);

        itemAvatar = findViewById(R.id.itemAvatar);
        itemTitle = findViewById(R.id.itemTitle);
        itemSubtitle = findViewById(R.id.itemSubtitle);
        itemSuperTitle = findViewById(R.id.itemSuperTitle);
        itemThumbnail = findViewById(R.id.itemThumbnail);
        itemVoteTextView = findViewById(R.id.itemVoteTextView);
        itemCommentTextView = findViewById(R.id.itemCommentTextView);
        itemCardView = findViewById(R.id.itemCardView);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        RedditFeedService redditFeedService = new RedditFeedService(this, Constants.BASE_URL);
        redditFeedService.getItemDetail(permalink + ".json", new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                List<ResponseModel> responseModel = (ArrayList<ResponseModel>) response;
                createItemUI(responseModel.get(0).data.children.get(0));
            }

            @Override
            public void onFailure() {

            }
        });

        itemParentView = findViewById(R.id.itemParentView);

        redditFeedService.getComments(permalink + ".json", new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                ArrayList<ResponseModel> finalResponse = (ArrayList<ResponseModel>) response;

                createCommentScrollLayout(finalResponse.get(1).data.children);
                progressBarLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void createCommentScrollLayout(ArrayList<Item> list) {
        for (int index = 0; index < list.size(); index++) {
            createCommentView(list.get(index), index, itemParentView);
        }
    }

    private Float convertDpToPx(float dip) {
        Resources r = getResources();
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
    }

    private void createCommentView(Item commentData, int index, LinearLayout parentLayout) {
        LinearLayout replyLayout;
        String bulletDot = " \u2022 ";

        if (commentData.kind.contains("more")) {
            View moreView = getLayoutInflater().inflate(R.layout.tile_more, null);
            TextView loadMoreTv = moreView.findViewById(R.id.loadMoreTv);
            loadMoreTv.setText(commentData.data.count.toString() + " more replies");

            moreView.setPadding(convertDpToPx(16).intValue(), 0, 0, 0);

            parentLayout.addView(moreView);
        } else if (commentData.kind.contains("t1")) {
            View commentView = getLayoutInflater().inflate(R.layout.tile_individual_comment, null);
            TextView headerTV = commentView.findViewById(R.id.headerTV);
            headerTV.setText(commentData.data.author + bulletDot + Utils.getPostInterval(commentData.data.created));

            TextView commentTV = commentView.findViewById(R.id.commentTV);
            commentTV.setText(commentData.data.body);

            commentVoteTextView = commentView.findViewById(R.id.voteTv);
            commentVoteTextView.setText(Utils.getCommentVoteString(commentData.data.score.floatValue(), getString(R.string.vote)));

            replyLayout = commentView.findViewById(R.id.replyLayout);

            if ((commentData.data.depth != 0) || (commentData.data.depth == 0 & index == 0)) {
                View dividerView = commentView.findViewById(R.id.dividerView);
                dividerView.setVisibility(View.GONE);
            }

            if ((commentData.data.depth == 0)) {
                View threadView = commentView.findViewById(R.id.threadView);
                threadView.setVisibility(View.GONE);
            } else {
                commentView.setPadding(convertDpToPx(16).intValue(), 0, 0, 0);
            }

            LinearLayout parentLinearLayout = commentView.findViewById(R.id.parentLinearLayout);
            parentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View replyLayout = v.findViewById(R.id.replyLayout);
                    if (replyLayout.getVisibility() == View.GONE) {
                        replyLayout.setVisibility(View.VISIBLE);
                    } else if (replyLayout.getVisibility() == View.VISIBLE) {
                        replyLayout.setVisibility(View.GONE);
                    }
                }
            });

            parentLayout.addView(commentView);

            if (commentData.data.replies != null) {
                ArrayList<Item> list = commentData.data.replies.data.children;
                int size = list.size();
                for (int replyIndex = 0; replyIndex < size; replyIndex++) {
                    createCommentView(list.get(replyIndex), replyIndex, replyLayout);
                }
            }
        }
    }

    public void createItemUI(Item item) {
        String postInterval = Utils.getPostInterval(item.data.created);
        String bulletDot = " \u2022 ";

        StringBuffer finalString = new StringBuffer(getString(R.string.posted_prefix))
                .append(item.data.author)
                .append(bulletDot)
                .append(postInterval);

        finalString = (item.data.domain != null && !item.data.domain.contains(getString(R.string.self)))
                ? finalString.append(bulletDot).append(item.data.domain)
                : finalString.append("");

        itemTitle.setText(item.data.subreddit_name_prefixed);
        itemSubtitle.setText(finalString);
        itemSuperTitle.setText(item.data.title);

        Float totalVotes = item.data.ups.floatValue() - item.data.downs.floatValue();
        String voteStr = Utils.getCommentVoteString(totalVotes, getString(R.string.vote));
        itemVoteTextView.setText(voteStr);

        String commentStr = Utils.getCommentVoteString(item.data.num_comments.floatValue(), getString(R.string.comment));
        itemCommentTextView.setText(commentStr);

        itemAvatar.setImageDrawable(getDrawable(R.drawable.ic_person_outline_black_24dp));

        RedditFeedService redditFeedService = new RedditFeedService(this, Constants.BASE_URL);
        redditFeedService.getThumbNail(item.data.subreddit_name_prefixed + "/about.json", new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                Item responseModel = (Item) response;
                try {
                    Glide.with(ItemDetailsActivity.this).load(responseModel.data.icon_img)
                            .placeholder(R.drawable.ic_person_outline_black_24dp)
                            .into(itemAvatar);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });

        if (item.data.thumbnail != null &&
                !item.data.thumbnail.isEmpty() &&
                !item.data.thumbnail.toLowerCase().contains(getString(R.string.self))) {

            itemThumbnail.setVisibility(View.VISIBLE);
            try {
                Glide.with(ItemDetailsActivity.this).load(item.data.thumbnail)
                        .into(itemThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            itemThumbnail.setVisibility(View.GONE);
        }
        itemCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
