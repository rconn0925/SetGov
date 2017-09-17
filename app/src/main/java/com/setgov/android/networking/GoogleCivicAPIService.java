package com.setgov.android.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ross on 9/17/2017.
 */

public interface GoogleCivicAPIService {

    @GET("representatives")
    Call<String> getRepresentatives(@Query("address") String address, @Query("key") String apiKey);
    @GET("elections")
    Call<String> getElections(@Query("address") String address, @Query("key") String apiKey);
    @GET("voterinfo")
    Call<String> getVoterInfo(@Query("address") String address,@Query("electionID") int electionID, @Query("key") String apiKey);
}
