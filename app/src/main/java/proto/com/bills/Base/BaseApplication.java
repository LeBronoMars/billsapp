package proto.com.bills.base;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
