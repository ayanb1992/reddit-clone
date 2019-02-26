package com.ayan.redditclone.service;

import android.content.Context;
import android.util.Log;

import com.ayan.redditclone.model.Item;
import com.ayan.redditclone.model.ResponseModel;
import com.ayan.redditclone.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedditFeedService {
    APIInterface apiInterface;
    Context mContext;
    String URL;
    String LOG_TAG = RedditFeedService.class.getSimpleName();

    public RedditFeedService(Context context, String url) {
        this.mContext = context;
        this.URL = url;
    }

    public void getFeedDetails(final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(URL).create(APIInterface.class);

        Call<ResponseModel> call = apiInterface.getNewReddit();

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                call.cancel();
                callback.onFailure();
            }
        });
    }

    public void getThumbNail(String urlEndPoint, final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(URL).create(APIInterface.class);

        Call<Item> call = apiInterface.getThumbNail(Constants.BASE_URL + urlEndPoint);

        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                call.cancel();
                callback.onFailure();
            }
        });
    }

    public void getSubredditFeed(final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(URL).create(APIInterface.class);

        Call<ResponseModel> call = apiInterface.getSubredditList();

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                call.cancel();
                callback.onFailure();
            }
        });
    }

    public void getSubredditList(String urlEndPoint, final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(URL).create(APIInterface.class);

        Call<ResponseModel> call = apiInterface.getSubredditItemList(Constants.BASE_URL + urlEndPoint);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                call.cancel();
                callback.onFailure();
            }
        });
    }

    public void getItemDetail(String urlEndPoint, final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(URL).create(APIInterface.class);

        Call<List<ResponseModel>> call = apiInterface.getItemDetail(Constants.BASE_URL + urlEndPoint);

        call.enqueue(new Callback<List<ResponseModel>>() {
            @Override
            public void onResponse(Call<List<ResponseModel>> call, Response<List<ResponseModel>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ResponseModel>> call, Throwable t) {
                call.cancel();
                callback.onFailure();
            }
        });
    }

    public void getComments(final String endPoint, final WebServiceCallback callback) {
        apiInterface = APIClient.getClient(Constants.BASE_URL).create(APIInterface.class);

        Call<ArrayList<ResponseModel>> call = apiInterface.getComments(endPoint);

        call.enqueue(new Callback<ArrayList<ResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseModel>> call, Response<ArrayList<ResponseModel>> response) {
                Log.e(LOG_TAG, "[inside onResponse RedditReaderService]");
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseModel>> call, Throwable t) {
                Log.e(LOG_TAG, "[inside onFailure RedditReaderService]");

                t.printStackTrace();

                call.cancel();
                callback.onFailure();
            }
        });
    }
}
