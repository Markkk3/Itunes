package com.mark.itunes.network;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestToApi {

    @GET("/search")
    Observable<ItemsList> searchRequest(@Query("entity") String entity, @Query("term") String term, @Query("limit") int limit);
}
