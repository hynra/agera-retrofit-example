package hynra.com.ageraretrofitexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.agera.Function;
import com.google.android.agera.Functions;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import hynra.com.ageraretrofitexample.models.Gank;

import me.drakeet.retrofit2.adapter.agera.AgeraCallAdapterFactory;
import me.drakeet.retrofit2.adapter.agera.Ageras;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import hynra.com.ageraretrofitexample.services.CallService;

public class MainActivity extends AppCompatActivity implements Updatable {

    private Repository<String[]> repository1, repository2;
    private TextView textView;
    private static final String[] INITIAL_VALUE = {};





    private Function<Gank, String[]> gankToTitleArray = Functions.functionFrom(Gank.class)
            .apply(gank -> gank.results)
            .apply(Functions.functionFromListOf(Gank.ResultsEntity.class)
                    .thenMap(entity -> entity.desc))
            .thenApply(list -> list.toArray(new String[list.size()]));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/Android/10/")
                .client(new OkHttpClient())
                .addCallAdapterFactory(AgeraCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CallService callService = retrofit.create(CallService.class);

        repository1 = Ageras.goToNetworkExecutorWithInitialValue(INITIAL_VALUE)
                .attemptGetFrom(callService.android())
                .orSkip()
                .thenTransform(gankToTitleArray)
                .compile();

        repository2 = Ageras.goToNetworkExecutorWithInitialValue(INITIAL_VALUE)
                .attemptGetFrom(callService.android(2))
                .orSkip()
                .attemptTransform(response -> {
                    Result<Gank> result;
                    if (response.isSuccessful()) {
                        result = Result.success(response.body());
                    } else {
                        result = Result.failure(new HttpException(response));
                    }
                    return result;
                })
                .orEnd(input -> new String[] { "..." })
                .thenTransform(gankToTitleArray)
                .compile();

        repository1.addUpdatable(this);
        repository2.addUpdatable(new Updatable() {
            @Override public void update() {
                for (String s : repository2.get()) {
                    textView.append("* " + s + "\n");
                    Log.i("result 2", s);
                }
                repository2.removeUpdatable(this);
            }
        });
    }


    @Override public void update() {
        for (String s : repository1.get()) {
            textView.append("* " + s + "\n");
            Log.i("result 1", s);
        }
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        repository1.removeUpdatable(this);
    }
}