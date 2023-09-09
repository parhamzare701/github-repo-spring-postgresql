package com.ada.github.service;

import com.ada.github.model.GithubServiceModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GithubRepositoryInterface {
    @GET("users/{username}/repos")
    Call<List<GithubServiceModel>> getGithubRepository(@Path("username") String username);
}
