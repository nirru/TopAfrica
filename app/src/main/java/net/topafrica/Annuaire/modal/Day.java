package net.topafrica.Annuaire.modal;

/**
 * Created by ericbasendra on 21/09/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Day implements Parcelable{

    private long closeAt;

    private long day;

    private long openAt;

    public Day(){

    }

    protected Day(Parcel in) {
        closeAt = in.readLong();
        day = in.readLong();
        openAt = in.readLong();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    /**
     *
     * @return
     * The closeAt
     */

    @JsonIgnore
    public long getCloseAt() {
        return closeAt;
    }

    /**
     *
     * @param closeAt
     * The closeAt
     */
    public void setCloseAt(Integer closeAt) {
        this.closeAt = closeAt;
    }

    /**
     *
     * @return
     * The day
     */

    @JsonIgnore
    public long getDay() {
        return day;
    }

    /**
     *
     * @param day
     * The day
     */
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     *
     * @return
     * The openAt
     */

    @JsonIgnore
    public long getOpenAt() {
        return openAt;
    }

    /**
     *
     * @param openAt
     * The openAt
     */
    public void setOpenAt(Integer openAt) {
        this.openAt = openAt;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(closeAt);
        parcel.writeLong(day);
        parcel.writeLong(openAt);
    }
}
