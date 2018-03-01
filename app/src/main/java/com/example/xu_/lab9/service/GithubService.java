package com.example.xu_.lab9.service;

import com.example.xu_.lab9.model.Github;
import com.example.xu_.lab9.model.Repos;

import java.util.List;
import java.util.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by xu_ on 2017/12/25.
 */

public interface GithubService {
    @GET ("/users/{user}")
    rx.Observable<Github> getUser(@Path("user") String user);

    @GET ("/users/{user}/repos")
    rx.Observable<List<Repos>> getUserRepos(@Path("user") String user);
}
