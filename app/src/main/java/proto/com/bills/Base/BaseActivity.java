package proto.com.bills.Base;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by rsbulanon on 5/15/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void setError(final TextView textView, final String message) {
        textView.setError(message);
        textView.requestFocus();
    }

}
