package com.ayan.redditclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ayan.redditclone.model.Item;
import com.ayan.redditclone.model.ResponseModel;
import com.ayan.redditclone.service.RedditFeedService;
import com.ayan.redditclone.service.WebServiceCallback;
import com.ayan.redditclone.ui.RedditListAdapter;
import com.ayan.redditclone.utils.Constants;

public class SubredditListActivity extends AppCompatActivity {

    private final String LOG_TAG = SubredditListActivity.class.getSimpleName();
    private RecyclerView subredditItemList;
    private RedditListAdapter adapter;
    private ProgressBar subredditProgressBar;
    private String endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.SUB_REDDIT_NAME_KEY));

        endPoint = getIntent().getStringExtra(Constants.SUB_REDDIT_NAME_KEY) + "/new.json";

        subredditItemList = findViewById(R.id.subredditItemList);
        subredditProgressBar = findViewById(R.id.subredditItemListProgressBar);

        refreshItems(endPoint);
    }

    public void refreshItems(String endPoint) {
        if (adapter != null) {
            adapter.clear();
            subredditProgressBar.setVisibility(View.VISIBLE);
        }

        RedditFeedService redditFeedService = new RedditFeedService(this, Constants.BASE_URL);
        redditFeedService.getSubredditList(endPoint, new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                ResponseModel responseModel = (ResponseModel) response;

                subredditProgressBar.setVisibility(View.GONE);

                adapter = new RedditListAdapter(SubredditListActivity.this, responseModel.data.children,
                        new RedditListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Item item) {
                                Intent intent = new Intent(SubredditListActivity.this, ItemDetailsActivity.class);
                                intent.putExtra(Constants.POST_PERMALINK_KEY, item.data.permalink);
                                startActivityForResult(intent, 1);
                            }
                        });
                subredditItemList.setAdapter(adapter);
                subredditItemList.setLayoutManager(new LinearLayoutManager(SubredditListActivity.this));
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subrddit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshItems(endPoint);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
