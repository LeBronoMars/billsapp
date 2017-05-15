package proto.com.bills.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import proto.com.bills.interfaces.OnConfirmDialogListener;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class BaseActivity extends AppCompatActivity {

    private static ProgressDialog progressDialog;

    public void setError(final TextView textView, final String message) {
        textView.setError(message);
        textView.requestFocus();
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void showConfirmDialog(final String action, final String title, final String message,
                                  final String positiveText, final String negativeText,
                                  final OnConfirmDialogListener onConfirmDialogListener,
                                  final boolean cancelable) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(cancelable);

        if (positiveText != null && !positiveText.isEmpty()) {
            alertDialogBuilder.setPositiveButton(positiveText, (dialog, id) -> {
                if (onConfirmDialogListener != null) {
                    onConfirmDialogListener.onConfirmed(action);
                }
                dialog.cancel();
            });
        }

        if (negativeText != null && !negativeText.isEmpty()) {
            alertDialogBuilder.setNegativeButton(negativeText, (dialog, id) -> {
                if (onConfirmDialogListener != null) {
                    onConfirmDialogListener.onCancelled(action);
                }
                dialog.cancel();
            });
        }
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showProgressDialog(final String header, final String message) {
        progressDialog = null;
        progressDialog = new ProgressDialog(this);

        if (header != null && !header.isEmpty()) {
            progressDialog.setTitle(header);
        }

        if (message != null && !message.isEmpty()) {
            progressDialog.setMessage(message);
        }

        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
