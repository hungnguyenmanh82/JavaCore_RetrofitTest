package com.hung.myapplication.github;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *  Giống  vơi Service của Jersey Server. Cú pháp ko tuan theo chuan của
 */

public interface GitHubService {
    // https://api.github.com/hungnguyenmanh82/repos
    @GET("users/{user}/repos")
    Call<List<Repos>> listRepos(@Path("user") String user);
}
