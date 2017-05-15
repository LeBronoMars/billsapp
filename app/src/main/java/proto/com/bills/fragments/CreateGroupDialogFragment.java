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

public class CreateGroupDialogFragment extends DialogFragment {

    @BindView(R.id.et_group_name)
    EditText et_group_name;

    @BindView(R.id.et_group_description)
    EditText et_group_description;

    private View view;
    private Dialog mDialog;
    private Unbinder unbinder;
    private BaseActivity activity;
    private OnCreateGroupListener onCreateGroupListener;

    public static CreateGroupDialogFragment newInstance() {
        final CreateGroupDialogFragment fragment = new CreateGroupDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_create_group, null);
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

    @OnClick(R.id.tv_create_group)
    public void createGroup() {
        final String groupName = et_group_name.getText().toString().trim();
        final String groupDescription = et_group_description.getText().toString().trim();

        if (groupName.isEmpty()) {
            activity.setError(et_group_name, Constants.WARN_FIELD_REQUIRED);
        } else {
            if (onCreateGroupListener != null) {
                onCreateGroupListener.onCreateGroup(groupName, groupDescription);
            }
        }
    }

    public interface OnCreateGroupListener {
        void onCreateGroup(final String groupName, final String description);
    }

    public void setOnCreateGroupListener(OnCreateGroupListener onCreateGroupListener) {
        this.onCreateGroupListener = onCreateGroupListener;
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
