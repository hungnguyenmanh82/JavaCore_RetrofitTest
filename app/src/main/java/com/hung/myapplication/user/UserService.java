package com.hung.myapplication.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hungnm2 on 12/28/2017.
 */

public interface UserService {
    @GET("users")
    Call<List<User>> getUsers();

    /**
     * vd: host = https://api.github.com/
     * UserService.getCommitsByName("name1") => URL = host + users/name1
     * UserService.getCommitsByName("name2") => URL = host + users/name2
     */
    @GET("users/{name}")
    Call<List<User>> getCommitsByName(@Path("name") String name);

/*    @GET("users/{name}/commits")
    Call<List<Commit>> getCommitsByName(@Path("name") String name);*/

    /**
     * @Query: là tham số đưa và URL của http request
     *
     * vd: host = https://api.github.com/
     * UserService.getUserById(1)  => URL = host + "users?id=1
     *
     * vd: people.php?name=Joe&age=24
     */
    @GET("users")
    Call<User> getUserById(@Query("id") Integer id);


    @POST("users")
    Call<User> postUser(@Body User user); //User class will convert to Json or XML

    /**
     * insert pair of (key, value) to Header of http request
     *   key = Authorization
     *   value = credentials
     */
    @GET("user")
    Call<User> getUserDetails(@Header("Authorization") String credentials);

}
