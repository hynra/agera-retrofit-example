package hynra.com.ageraretrofitexample.services;

import java.util.List;

import hynra.com.ageraretrofitexample.models.GithubUser;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GithubService {
    @GET("orgs/pptik/members")
    Call<List<GithubUser>> listUser();
}