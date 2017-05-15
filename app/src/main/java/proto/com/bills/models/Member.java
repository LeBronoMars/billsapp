package proto.com.bills.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by rsbulanon on 5/15/17.
 */
@Data
@AllArgsConstructor
public class Member implements Parcelable {

    private String userId;
    private String displayName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.displayName);
    }

    protected Member(Parcel in) {
        this.userId = in.readString();
        this.displayName = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
