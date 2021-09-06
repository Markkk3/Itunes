package com.mark.itunes.network;


import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://itunes.apple.com/";
    private final Retrofit mRetrofit;

    private NetworkService() {

        RxJava3CallAdapterFactory rxAdapter =
            RxJava3CallAdapterFactory
                .createWithScheduler(Schedulers.io());

        mRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public RequestToApi getApi() {
        return mRetrofit.create(RequestToApi.class);
    }
}