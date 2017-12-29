package com.hung.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hung.myapplication.github.GitHubService;
import com.hung.myapplication.github.Repos;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *  https://github.com/square/retrofit/wiki
 *
 * https://www.programcreek.com/java-api-examples/index.php?api=com.google.mockwebserver.MockWebServer
 */
public class GithubServiceTest {
    @Test
    public void retrofit_syn_Get() throws Exception {

        //Step1: creat Retrofit using Gson (or Jackson)
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Step2: create Service
        /**
         *  "https://api.github.com/" + "users/hungnguyenmanh82/repos"
         *
         *  users/{user}/repos  = users/hungnguyenmanh82/repos
         */
        GitHubService service = retrofit.create(GitHubService.class);

        //build here, not run
        Call<List<Repos>> call =  service.listRepos("hungnguyenmanh82");

        try {
            // Step3: sent http GET request
            // synchronous
            Response<List<Repos>> response = call.execute();
            List<Repos> repos = response.body();           // Json will convert to Repos class

            System.out.print(repos.get(0).getFull_name());
            System.out.print(repos.get(0).getClone_url());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofit_async_Get() throws Exception {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        /**
         *  "https://api.github.com/" + "users/hungnguyenmanh82/repos"
         *
         *  users/{user}/repos  = users/hungnguyenmanh82/repos
         */
        GitHubService service = retrofit.create(GitHubService.class);

        //build here, not run
        Call<List<Repos>> call =  service.listRepos("hungnguyenmanh82");


        //sent http GET request
        // asynchronous
        call.enqueue(new Callback<List<Repos>>(){
            @Override
            public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    List<Repos> repos = response.body();

                    System.out.println(repos.get(0).getFull_name());
                    System.out.println(repos.get(0).getClone_url());
                    System.out.println(response.code() + ":" + response.message());
                } else {
                    // error response, no access to resource?
                    System.out.println(response.code() + ":" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Repos>> call, Throwable t) {
                // error response, no access to resource?
                t.printStackTrace();
            }
        });

        Thread.sleep(10000); //10 second to waiting for worker thread stop
        assertEquals(4, 2 + 2);
    }


}