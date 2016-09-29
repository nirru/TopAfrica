package net.topafrica.Annuaire.modal;

/**
 * Created by ericbasendra on 21/09/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Openhours implements Parcelable{

    private List<Day> days = new ArrayList<Day>();

    private Integer zone;

    public Openhours(){

    }

    protected Openhours(Parcel in) {
        days = in.createTypedArrayList(Day.CREATOR);
        zone = in.readInt();
    }

    public static final Creator<Openhours> CREATOR = new Creator<Openhours>() {
        @Override
        public Openhours createFromParcel(Parcel in) {
            return new Openhours(in);
        }

        @Override
        public Openhours[] newArray(int size) {
            return new Openhours[size];
        }
    };

    /**
     *
     * @return
     * The days
     */
    @JsonGetter("days")
    public List<Day> getDays() {
        return days;
    }

    /**
     *
     * @param days
     * The days
     */
    public void setDays(List<Day> days) {
        this.days = days;
    }

    /**
     *
     * @return
     * The zone
     */
    @JsonGetter("zone")
    public Integer getZone() {
        return zone;
    }

    /**
     *
     * @param zone
     * The zone
     */
    public void setZone(Integer zone) {
        this.zone = zone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(days);
        parcel.writeInt(zone);
    }
}
