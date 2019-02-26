package com.ayan.redditclone.service;

public interface WebServiceCallback {
    public void onSuccess(Object response);

    public void onFailure();
}
