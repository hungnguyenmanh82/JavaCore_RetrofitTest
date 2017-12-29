package com.hung.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;
import com.hung.myapplication.github.GitHubService;
import com.hung.myapplication.github.Repos;
import com.hung.myapplication.user.User;
import com.hung.myapplication.user.UserService;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class UserServiceTest {
    @Test
    public void retrofit_syn_Get_MockWebServer_Play() throws Exception {

        //=============================== start server ================================
        // use a proxy so we can manipulate the origin server's host name
        MockWebServer server = new MockWebServer();
        String bodyString = "{\"id\":1,"+
                "\"name\":\"nguyen manh hung\"," +
                "\"address\":\"Hanoi\","+
                "\"phone\":123455}" ;

        //add vào Queue của Server => khi có request sẽ lấy phần tử ra và trả về cho request (sau đó xóa phần tử này đi)
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("userHeader","value of UserHeader")
                .addHeader("Set-Cookie: c=third; Domain=.t-mobile.com")
                .addHeader("Content-Type: application/json; charset=utf-8")
                .setBody(bodyString));

        //start server here (tuần tự trả về cho request) => bắt buộc
        //phải run hàm này thì server.getUrl("/") mới đc cấp phát Port cho Socket
        server.play();

        // URL này ko capture với WireShark đc
        System.out.println(server.getUrl("/")); //http://DESKTOP-LBOHS1J:49265  => địa chi localhost
        //============end: server ========================

        //===========================================start: Client =============================
        //Step1: creat Retrofit using Gson (or Jackson)
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.getUrl("/").toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Step2: create Service
        /**
         *  "https://api.github.com/" + "users/hungnguyenmanh82/repos"
         *
         *  users/{user}/repos  = users/hungnguyenmanh82/repos
         */
        UserService service = retrofit.create(UserService.class);

        //build here, not run
        Call<User> call =  service.getUserById(1);

        try {
            // Step3: sent http GET request
            // synchronous
            Response<User> response = call.execute();
            //========================= header ================
            Map<String, List<String>> map = response.headers().toMultimap();
            System.out.println("======================== response header");
            System.out.println(map.toString());
            System.out.println("======================== response body");
            //========================body =====================
            User user = response.body();           // Json will convert to Repos class
            System.out.println("id="+user.getId());
            System.out.println("name="+user.getName());
            System.out.println("address="+ user.getAddress());
            System.out.println("phone=" + user.getPhone());
            System.out.println(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofit_syn_Get_MockWebServer_TakeRequest() throws Exception {

        //=============================== start server ================================
        // use a proxy so we can manipulate the origin server's host name
        MockWebServer server = new MockWebServer();
        String bodyResponse = "{\"id\":1,"+
                "\"name\":\"nguyen manh hung\"," +
                "\"address\":\"Hanoi\","+
                "\"phone\":123455}" ;

        //add vào Queue của Server => khi có request sẽ lấy phần tử ra và trả về cho request (sau đó xóa phần tử này đi)
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("userHeader","value of UserHeader")
                .addHeader("Set-Cookie: c=third; Domain=.t-mobile.com")
                .addHeader("Content-Type: application/json; charset=utf-8")
                .setBody(bodyResponse));

        //start server here (tuần tự trả về cho request) => bắt buộc
        //phải run hàm này thì server.getUrl("/") mới đc cấp phát Port cho Socket
        server.play();

        // URL này ko capture với WireShark đc
        System.out.println(server.getUrl("/")); //http://DESKTOP-LBOHS1J:49265  => địa chi localhost
        //============end: server ========================

        //===========================================start: Client =============================
        //Step1: creat Retrofit using Gson (or Jackson)
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.getUrl("/").toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Step2: create Service
        /**
         *  "https://api.github.com/" + "users/hungnguyenmanh82/repos"
         *
         *  users/{user}/repos  = users/hungnguyenmanh82/repos
         */
        UserService service = retrofit.create(UserService.class);

        //build here, not run
        Call<User> call =  service.getUserById(1);

        try {
            // Step3: sent http GET request
            // synchronous
            Response<User> response = call.execute();  //server not response here

            Map<String, List<String>> map = response.headers().toMultimap();
            System.out.println("======================== response header");
            System.out.println(map.toString());
            System.out.println("======================== response body");
            //========================body =====================
            User user = response.body();           // Json will convert to Repos class
            System.out.println("id="+user.getId());
            System.out.println("name="+user.getName());
            System.out.println("address="+ user.getAddress());
            System.out.println("phone=" + user.getPhone());
            System.out.println(user);

            //========================= Request ================
            //hàm này chỉ lấy đc khi đã có request hoàn thành (bước synchronous ở trên)
            //Lưu ý nó sẽ lấy Request gần nhất tới Server.
            RecordedRequest recordedRequest = server.takeRequest();

            List<String> headers = recordedRequest.getHeaders();
            System.out.println("======================== request header");
            for (String st: headers ) {
                System.out.println(st);
            }
            System.out.println("======================== request body");

            //convert byte array to String
            String body = new String(recordedRequest.getBody(), "UTF-8");
            //GET has no body => nothing here
            System.out.println(body);


        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofit_syn_Post_MockWebServer_TakeRequest() throws Exception {

        //=============================== start server ================================
        // use a proxy so we can manipulate the origin server's host name
        MockWebServer server = new MockWebServer();
        String bodyResponse = "{\"id\":1,"+
                "\"name\":\"nguyen manh hung\"," +
                "\"address\":\"Hanoi\","+
                "\"phone\":123455}" ;

        //add vào Queue của Server => khi có request sẽ lấy phần tử ra và trả về cho request (sau đó xóa phần tử này đi)
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("userHeader","value of UserHeader")
                .addHeader("Set-Cookie: c=third; Domain=.t-mobile.com")
                .addHeader("Content-Type: application/json; charset=utf-8")
                .setBody(bodyResponse));



        //start server here (tuần tự trả về cho request) => bắt buộc
        //phải run hàm này thì server.getUrl("/") mới đc cấp phát Port cho Socket
        server.play();

        // URL này ko capture với WireShark đc
        System.out.println(server.getUrl("/")); //http://DESKTOP-LBOHS1J:49265  => địa chi localhost
        //============end: server ========================

        //===========================================start: Client =============================
        //Step1: creat Retrofit using Gson (or Jackson)
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.getUrl("/").toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Step2: create Service
        /**
         *  "https://api.github.com/" + "users/hungnguyenmanh82/repos"
         *
         *  users/{user}/repos  = users/hungnguyenmanh82/repos
         */
        UserService service = retrofit.create(UserService.class);

        User user = new User();
        user.setId(2);
        user.setName("hungbeo");
        user.setAddress("blabla");
        user.setPhone(333);

        //build here, not run
        Call<User> call =  service.postUser(user);

        try {
            // Step3: sent http GET request
            // synchronous
            Response<User> response = call.execute();  //server not response here

            Map<String, List<String>> map = response.headers().toMultimap();
            System.out.println("======================== response header");
            System.out.println(map.toString());
            System.out.println("======================== response body");
            //========================response body =====================
            User userResponse = response.body();           // Json will convert to Repos class
            System.out.println("id="+userResponse.getId());
            System.out.println("name="+userResponse.getName());
            System.out.println("address="+ userResponse.getAddress());
            System.out.println("phone=" + userResponse.getPhone());
            System.out.println(userResponse);

            //========================= Request ============================
            //hàm này chỉ lấy đc khi đã có request hoàn thành (bước synchronous ở trên)
            //Lưu ý nó sẽ lấy Request gần nhất tới Server.
            RecordedRequest recordedRequest = server.takeRequest();

            List<String> headers = recordedRequest.getHeaders();
            System.out.println("======================== request header");
            for (String st: headers ) {
                System.out.println(st);
            }
            System.out.println("======================== request body");

            //convert byte array to String
            String body = new String(recordedRequest.getBody(), "UTF-8");
            System.out.println(body);


        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }
}
