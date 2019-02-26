package com.ayan.redditclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView redditItemList;
    private RedditListAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.home);

        redditItemList = findViewById(R.id.redditItemList);
        progressBar = findViewById(R.id.progressBar);

        refreshItems();
    }

    public void refreshItems() {
        if (adapter != null) {
            adapter.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        RedditFeedService redditFeedService = new RedditFeedService(this, Constants.BASE_URL);
        redditFeedService.getFeedDetails(new WebServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                ResponseModel responseModel = (ResponseModel) response;

                progressBar.setVisibility(View.GONE);

                adapter = new RedditListAdapter(MainActivity.this, responseModel.data.children,

                        new RedditListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Item item) {
                                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                                intent.putExtra(Constants.POST_PERMALINK_KEY, item.data.permalink);
                                startActivityForResult(intent, 1);
                            }
                        });
                redditItemList.setAdapter(adapter);
                redditItemList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshItems();
                return true;
            case R.id.subreddits:
                Intent intent = new Intent(MainActivity.this, SubRedditActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
