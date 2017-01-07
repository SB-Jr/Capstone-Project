package com.project.sbjr.showinfodatabase.handler;

import android.util.Log;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sbjr on 27/12/16.
 */
public abstract class ShowHandler<T> implements Callback<T> {


    Call<T> call;
    Response<T> response;

    View onFailureView, onDataFetchView, onSuccessView;

    abstract public void onResult(T result);

    abstract public void onFailure();

    public void startFetch(Call<T> call) {
        this.call = call;
        if (onFailureView != null) {
            onFailureView.setVisibility(View.GONE);
        }
        if (onSuccessView != null) {
            onSuccessView.setVisibility(View.GONE);
        }
        if (onDataFetchView != null) {
            onDataFetchView.setVisibility(View.VISIBLE);
        }
        call.enqueue(this);
    }

    public void initViews(View onCompleteShow, View onDataFetchShow, View onFailureShow) {
        this.onFailureView = onFailureShow;
        onSuccessView = onCompleteShow;
        onDataFetchView = onDataFetchShow;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (onFailureView != null) {
            onFailureView.setVisibility(View.GONE);
        }
        if (onSuccessView != null) {
            onSuccessView.setVisibility(View.VISIBLE);
        }
        if (onDataFetchView != null) {
            onDataFetchView.setVisibility(View.GONE);
        }
        this.response = response;
        onResult(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (onFailureView != null) {
            onFailureView.setVisibility(View.VISIBLE);
        }
        if (onSuccessView != null) {
            onSuccessView.setVisibility(View.GONE);
        }
        if (onDataFetchView != null) {
            onDataFetchView.setVisibility(View.GONE);
        }
        onFailure();
        Log.d(ShowHandler.class.getName(), t.getMessage() + "---" + t.toString());
    }
}
