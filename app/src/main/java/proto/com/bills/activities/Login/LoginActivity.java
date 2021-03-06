package proto.com.bills.activities.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import proto.com.bills.R;
import proto.com.bills.activities.MainActivity;
import proto.com.bills.base.BaseActivity;
import proto.com.bills.fragments.RegistrationDialogFragment;
import proto.com.bills.utils.Constants;
import proto.com.bills.utils.LogHelper;

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
        firebaseAuth = FirebaseAuth.getInstance();
        unbinder = ButterKnife.bind(this);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            animateToLeft();
            finish();
        }
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
            if (isNetworkAvailable()) {
                showProgressDialog("Login", "Verifying your credentials, Please wait...");
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                dismissProgressDialog();
                                startActivity(new Intent(this, MainActivity.class));
                                animateToLeft();
                                finish();
                            } else {
                                dismissProgressDialog();
                                if (task.getException() != null) {
                                    showConfirmDialog(null, "Login Failed", task.getException().getMessage(), "Close", null, null, false);
                                }
                            }
                        });
            } else {
                showConfirmDialog(null, "Connection Error", Constants.WARN_CONNECTION, "Close", null, null, true);
            }
        }
    }

    @OnClick(R.id.tv_register)
    public void register() {
        final RegistrationDialogFragment fragment = RegistrationDialogFragment.newInstance();
        fragment.setOnRegistrationListener((email, password, displayName) -> {
            if (isNetworkAvailable()) {
                showProgressDialog("Registration", "Creating your account, Please wait...");
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                LogHelper.log("reg", "user account successfully created!");
                                final FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    final UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(displayName)
                                            .setPhotoUri(Uri.parse("http://www.gravatar.com/avatar/" + email + "?d=identicon"))
                                            .build();
                                    user.updateProfile(userProfileChangeRequest)
                                            .addOnCompleteListener(updateTask -> {
                                                if (task.isSuccessful()) {
                                                    dismissProgressDialog();
                                                    fragment.dismiss();
                                                    LogHelper.log("reg", "user profile successfully updated!");
                                                } else {
                                                    dismissProgressDialog();
                                                    showConfirmDialog(null, "Registration Failed", updateTask.getException().getMessage(), "Close", null, null, false);
                                                    LogHelper.log("reg", "unable to update user profile --> " + updateTask.getException().getMessage());
                                                }
                                            });
                                }
                            } else {
                                dismissProgressDialog();
                                showConfirmDialog(null, "Registration Failed", task.getException().getMessage(), "Close", null, null, false);
                                LogHelper.log("reg", "unable to create user account --> " + task.getException().getMessage());
                            }
                        });
            } else {
                showConfirmDialog(null, "Connection Error", Constants.WARN_CONNECTION, "Close", null, null, true);
            }
        });
        fragment.show(getFragmentManager(), "register");
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
