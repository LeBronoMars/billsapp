package proto.com.bills.Login;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import proto.com.bills.Base.BaseActivity;
import proto.com.bills.R;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class LoginActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
