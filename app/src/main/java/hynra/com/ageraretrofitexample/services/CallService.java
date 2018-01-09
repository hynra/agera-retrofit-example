package hynra.com.ageraretrofitexample.services;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import hynra.com.ageraretrofitexample.models.Gank;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CallService {
    @GET("1")
    Supplier<Result<Gank>> android();
    @GET("{page}")
    Supplier<Result<Response<Gank>>> android(@Path("page") int page);
}