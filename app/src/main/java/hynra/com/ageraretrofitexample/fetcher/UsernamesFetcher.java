package hynra.com.ageraretrofitexample.fetcher;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UsernamesFetcher {

    public static int NUMBER_OF_USERS = 4;

    public void getUsernames(final UsernamesCallback callback) {
        if (NUMBER_OF_USERS < 0) {
            callback.setError();
            return;
        }

        Handler h = new Handler();
        Runnable r = () -> {
            // Create a fake list of usernames
            String name1 = "Joe";
            String name2 = "Amanda";
            final List<String> usernames = new ArrayList<String>();
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_USERS; i++) {
                int number = random.nextInt(50);
                if (System.currentTimeMillis() % 2 == 0) {
                    usernames.add(name1 + number);
                } else {
                    usernames.add(name2 + number);
                }
            }
            callback.setUsernames(usernames.toArray(new String[usernames.size()]));
        };

        // Simulate network latency
        h.postDelayed(r, 2000);
    }

    public interface UsernamesCallback {
        void setError();

        void setUsernames(String[] usernames);
    }
}