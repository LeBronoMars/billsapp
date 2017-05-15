package proto.com.bills.Login;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import proto.com.bills.Base.BaseActivity;
import proto.com.bills.R;
import proto.com.bills.Utils.Constants;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    private FirebaseAuth firebaseAuth;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        final String email = et_email.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        if (email.isEmpty()) {
            setError(et_email, Constants.WARN_FIELD_REQUIRED);
        } else if (password.isEmpty()) {
            setError(et_password, Constants.WARN_FIELD_REQUIRED);
        } else {
            
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
