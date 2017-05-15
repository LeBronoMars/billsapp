package proto.com.bills.models;

import android.os.Parcel;

import java.util.ArrayList;

import lombok.Data;

/**
 * Created by rsbulanon on 5/15/17.
 */
@Data
public class Group extends BaseModel {

    private String groupName;
    private String groupDescription;
    private ArrayList<Member> members;
    private Member createdBy;

    public Group() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.groupName);
        dest.writeString(this.groupDescription);
        dest.writeTypedList(this.members);
        dest.writeParcelable(this.createdBy, flags);
    }

    protected Group(Parcel in) {
        super(in);
        this.groupName = in.readString();
        this.groupDescription = in.readString();
        this.members = in.createTypedArrayList(Member.CREATOR);
        this.createdBy = in.readParcelable(Member.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
