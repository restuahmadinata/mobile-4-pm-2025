package com.example.tp6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/character")
    Call<CharacterResponse> getCharacters(@Query("page") int page);

    @GET("api/character/{id}")
    Call<Character> getCharacterById(@Path("id") int id);
}