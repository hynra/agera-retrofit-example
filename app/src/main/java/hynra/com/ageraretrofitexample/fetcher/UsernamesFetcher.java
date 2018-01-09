package hynra.com.ageraretrofitexample.fetcher;

import java.util.List;


import hynra.com.ageraretrofitexample.models.GithubUser;
import hynra.com.ageraretrofitexample.services.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UsernamesFetcher {


    public void getUsernames(final UsernamesCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GithubService service = retrofit.create(GithubService.class);
        service.listUser().enqueue(new Callback<List<GithubUser>>() {
            @Override
            public void onResponse(Call<List<GithubUser>> call, Response<List<GithubUser>> response) {
                String[] strs = new String[response.body().size()];
                for(int i = 0; i < strs.length; i++){
                    strs[i] = response.body().get(i).getLogin();
                }
                callback.setUsernames(strs);
            }

            @Override
            public void onFailure(Call<List<GithubUser>> call, Throwable t) {
                t.fillInStackTrace();
                callback.setError();
            }
        });
    }

    public interface UsernamesCallback {
        void setError();

        void setUsernames(String[] usernames);
    }
}