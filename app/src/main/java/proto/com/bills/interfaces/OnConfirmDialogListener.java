package proto.com.bills.interfaces;

/**
 * Created by rsbulanon on 5/15/17.
 */
public interface OnConfirmDialogListener {

    void onConfirmed(final String action);
    void onCancelled(final String action);
}
