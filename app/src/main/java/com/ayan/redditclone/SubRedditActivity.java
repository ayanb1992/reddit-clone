package com.ayan.redditclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayan.redditclone.model.Item;
import com.ayan.redditclone.model.ResponseModel;
import com.ayan.redditclone.service.RedditFeedService;
import com.ayan.redditclone.service.WebServiceCallback;
import com.ayan.redditclone.ui.SubredditFeedAdapter;
import com.ayan.redditclone.utils.Constants;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SubRedditActivity extends AppCompatActivity {
    private final String LOG_TAG = SubRedditActivity.class.getSimpleName();
    private RecyclerView subredditListView;
    private SubredditFeedAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_reddit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.sub_reddit_list_title);

        subredditListView = findViewById(R.id.subredditListView);
        progressBar = findViewById(R.id.subredditProgressbar);

        RedditFeedService redditFeedService = new RedditFeedService(this, Constants.BASE_URL);
        redditFeedService.getSubredditFeed(new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                ResponseModel responseModel = (ResponseModel) response;
                progressBar.setVisibility(View.GONE);

                DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                subredditListView.addItemDecoration(decoration);

                adapter = new SubredditFeedAdapter(SubRedditActivity.this, responseModel.data.children,

                        new SubredditFeedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Item item) {
                                Intent intent = new Intent(SubRedditActivity.this, SubredditListActivity.class);
                                intent.putExtra(Constants.SUB_REDDIT_NAME_KEY, item.data.display_name_prefixed);
                                startActivity(intent);
                            }
                        });
                subredditListView.setAdapter(adapter);
                subredditListView.setLayoutManager(new LinearLayoutManager(SubRedditActivity.this));
            }

            @Override
            public void onFailure() {

            }
        });
    }

}
