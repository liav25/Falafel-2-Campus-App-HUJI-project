package com.example.myapplication;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import java.util.Calendar;


public class OrderListItem implements Parcelable {

    static final String LOCKED = "locked";
    static final String OPEN = "open";

    private final static int MIN_ORDER = 70;
    private int price;
    private String status;
    private  Timestamp timestamp;


    protected OrderListItem(Parcel in) {
        price = in.readInt();
        status = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<OrderListItem> CREATOR = new Creator<OrderListItem>() {
        @Override
        public OrderListItem createFromParcel(Parcel in) {
            return new OrderListItem(in);
        }

        @Override
        public OrderListItem[] newArray(int size) {
            return new OrderListItem[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public OrderListItem() {
    }

    public OrderListItem(Timestamp time) {
        this.price = 0;
        this.status = "open";
        this.timestamp = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(price);
        parcel.writeString(status);
        parcel.writeParcelable(timestamp, i);
    }
}
