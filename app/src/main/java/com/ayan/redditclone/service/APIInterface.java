package com.ayan.redditclone.service;

import com.ayan.redditclone.model.Item;
import com.ayan.redditclone.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface APIInterface {
    @GET("hot.json")
    Call<ResponseModel> getNewReddit();

    @GET
    Call<Item> getThumbNail(@Url String url);

    @GET("reddits.json")
    Call<ResponseModel> getSubredditList();

    @GET
    Call<ResponseModel> getSubredditItemList(@Url String url);

    @GET
    Call<List<ResponseModel>> getItemDetail(@Url String url);

    @GET
    Call<ArrayList<ResponseModel>> getComments(@Url String url);
}