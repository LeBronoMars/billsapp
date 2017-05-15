package proto.com.bills.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import proto.com.bills.R;
import proto.com.bills.base.BaseActivity;
import proto.com.bills.utils.Constants;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class RegistrationDialogFragment extends DialogFragment {

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_display_name)
    EditText et_display_name;

    private View view;
    private Dialog mDialog;
    private Unbinder unbinder;
    private BaseActivity activity;
    private OnRegistrationListener onRegistrationListener;

    public static RegistrationDialogFragment newInstance() {
        final RegistrationDialogFragment fragment = new RegistrationDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_registration, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();

        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.tv_create_account)
    public void createAccount() {
        final String email = et_email.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        final String displayName = et_display_name.getText().toString().trim();

        if (email.isEmpty()) {
            activity.setError(et_email, Constants.WARN_FIELD_REQUIRED);
        } else if (password.isEmpty()) {
            activity.setError(et_password, Constants.WARN_FIELD_REQUIRED);
        } else if (displayName.isEmpty()) {
            activity.setError(et_display_name, Constants.WARN_FIELD_REQUIRED);
        } else {
            if (onRegistrationListener != null) {
                onRegistrationListener.onRegister(email, password, displayName);
            }
        }
    }

    public interface OnRegistrationListener {
        void onRegister(final String email, final String password, final String displayName);
    }

    public void setOnRegistrationListener(OnRegistrationListener onRegistrationListener) {
        this.onRegistrationListener = onRegistrationListener;
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
