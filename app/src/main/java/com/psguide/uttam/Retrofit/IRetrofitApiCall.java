package com.psguide.uttam.Retrofit;


import com.psguide.uttam.Models.Aurthor.Author;
import com.psguide.uttam.Models.Category.Categorys;
import com.psguide.uttam.Models.Post.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitApiCall {
    //https://bjp24x7.com/wp-json/wp/v2/posts?page=8&per_page=10
     @GET("posts/?_fields=author,id,excerpt,title,date,jetpack_featured_media_url")
        Call<List<Posts>> getPosts(
                @Query("page") int page,
                @Query("per_page") int perPage

        );



    //https://bjp24x7.com/wp-json/wp/v2/categories
    @GET("categories/")
    Call<List<Categorys>> getCategories();

    //https://bjp24x7.com/wp-json/wp/v2/users/1
    @GET("users/{id}")
    Call<Author> getAuthorById(@Path("id") int id);

    //https://bjp24x7.com/wp-json/wp/v2/posts/1608?_embed=true
    @GET("posts/{id}?_embed=true")
    Call<Posts> getPostById(@Path("id") int id);

}
