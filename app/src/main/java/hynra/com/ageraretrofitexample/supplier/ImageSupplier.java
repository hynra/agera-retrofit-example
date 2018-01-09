package hynra.com.ageraretrofitexample.supplier;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import hynra.com.ageraretrofitexample.App;

import static com.google.android.agera.Result.absentIfNull;

public class ImageSupplier implements Supplier<Result<Bitmap>> {
    private String mUri;

    public ImageSupplier(String uri) {
        mUri = uri;
    }

    @NonNull
    @Override
    public Result<Bitmap> get() {
        return loadImage();
    }

    private Result<Bitmap> loadImage() {
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(App.getAppContext()).load(mUri).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return absentIfNull(bitmap);
    }


}