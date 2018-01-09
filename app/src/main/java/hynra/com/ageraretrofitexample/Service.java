package hynra.com.ageraretrofitexample;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface Service {
    @GET("1")
    Supplier<Result<Gank>> android();
    @GET("{page}")
    Supplier<Result<Response<Gank>>> android(@Path("page") int page);
}