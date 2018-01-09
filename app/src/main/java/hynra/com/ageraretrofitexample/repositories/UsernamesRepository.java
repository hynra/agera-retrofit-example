package hynra.com.ageraretrofitexample.repositories;


import android.support.annotation.NonNull;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import hynra.com.ageraretrofitexample.fetcher.UsernamesFetcher;

public class UsernamesRepository extends BaseObservable
        implements Supplier<String[]>, Updatable, UsernamesFetcher.UsernamesCallback {

    private String[] usernames;
    private boolean lastRefreshError;
    private final UsernamesFetcher usernamesFetcher;

    public UsernamesRepository(UsernamesFetcher usernamesFetcher) {
        super();
        this.usernamesFetcher = usernamesFetcher;
    }

    @NonNull
    @Override
    public String[] get() {
        return usernames;
    }

    public boolean isError() {
        return lastRefreshError;
    }


    @Override
    public void update() {
        usernamesFetcher.getUsernames(this);
    }


    @Override
    public void setError() {
        lastRefreshError = true;
        dispatchUpdate();
    }

    @Override
    public void setUsernames(String[] usernames) {
        this.usernames = usernames;
        lastRefreshError = false;
        dispatchUpdate();
    }

    @Override
    protected void observableActivated() {
        // Now that this is activated, we trigger an update to ensure the repository contains up to
        // date data.
        update();
    }
}