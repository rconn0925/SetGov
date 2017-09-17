package com.setgov.android.networking;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ross on 9/17/2017.
 */

public class GoogleCivicApiEngine {
    private GoogleCivicAPIService mService;
    private static final String API_KEY = "AIzaSyBNOZCgDBHcM8WIfllk9wACNz58scFoQHw";

    public GoogleCivicApiEngine (){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://api.themoviedb.org/3/")
                .baseUrl("https://www.googleapis.com/civicinfo/v2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mService = retrofit.create(GoogleCivicAPIService.class);
    }

    public Call<String> getRepresentatives(String address){
        return mService.getRepresentatives(address,API_KEY);
    }
    public Call<String> getElections(String address){
        return mService.getElections(address,API_KEY);
    }
    public Call<String> getVoterInfo(String address,int electionID){
        return mService.getVoterInfo(address,electionID,API_KEY);
    }

}
