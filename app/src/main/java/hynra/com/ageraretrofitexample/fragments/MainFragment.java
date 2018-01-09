package hynra.com.ageraretrofitexample.fragments;


import com.google.android.agera.Updatable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hynra.com.ageraretrofitexample.OnRefreshObservable;
import hynra.com.ageraretrofitexample.R;
import hynra.com.ageraretrofitexample.fetcher.UsernamesFetcher;
import hynra.com.ageraretrofitexample.repositories.UsernamesRepository;

public class MainFragment extends Fragment implements Updatable {

    private OnRefreshObservable refreshObservable;
    private UsernamesRepository usernamesRepository;
    private ListAdapter listAdapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] allUsernames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag, container, false);

        listView = root.findViewById(R.id.list);

        allUsernames = new String[0];

        // Set pull to refresh as an observable and attach it to the view
        refreshObservable = new OnRefreshObservable();
        swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(refreshObservable);

        // Initialise the repository
        usernamesRepository = new UsernamesRepository(new UsernamesFetcher());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // We make sure the repository observes the refresh listener
        refreshObservable.addUpdatable(usernamesRepository);
        usernamesRepository.addUpdatable(this);
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void onPause() {
        super.onPause();
        // We remove the observations to avoid triggering updates when they aren't needed
        refreshObservable.removeUpdatable(usernamesRepository);
        usernamesRepository.removeUpdatable(this);
    }

    @Override
    public void update() {

        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));

        // Check error status
        if (usernamesRepository.isError()) {
            // Show error message, do not update list as we still want to show the last known list of
            // usernames
            Toast.makeText(getContext(), "error",
                    Toast.LENGTH_LONG).show();
        } else {
            // Update the list of usernames
            if(allUsernames.length <= 0){
                allUsernames = usernamesRepository.get();
            }else {
                List<String> tmpString = new ArrayList<>();
                for(int i =0; i < allUsernames.length; i++){
                    tmpString.add(allUsernames[i]);
                }
                for(int i = 0; i < usernamesRepository.get().length; i++){
                    tmpString.add(usernamesRepository.get()[i]);
                }

                allUsernames = new String[tmpString.size()];
                for(int i = 0; i < allUsernames.length; i++){
                    allUsernames[i] = tmpString.get(i);
                }
                tmpString.clear();

            }

            listAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, allUsernames);
            listView.setAdapter(listAdapter);
        }
    }
}