package hynra.com.ageraretrofitexample;

import android.support.v4.widget.SwipeRefreshLayout;
import com.google.android.agera.BaseObservable;


public class OnRefreshObservable extends BaseObservable
        implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onRefresh() {
        dispatchUpdate();
    }
}