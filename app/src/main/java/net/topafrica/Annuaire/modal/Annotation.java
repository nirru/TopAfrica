package net.topafrica.Annuaire.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ericbasendra on 21/09/16.
 */
public class Annotation implements Parcelable,ClusterItem{


    private Double latitude;

    private Double longitude;

    private String title;

    public Annotation(){

    }

    protected Annotation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        title = in.readString();
    }

    public static final Creator<Annotation> CREATOR = new Creator<Annotation>() {
        @Override
        public Annotation createFromParcel(Parcel in) {
            return new Annotation(in);
        }

        @Override
        public Annotation[] newArray(int size) {
            return new Annotation[size];
        }
    };

    /**
     *
     * @return
     * The latitude
     */

    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */

    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The title
     */

    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(title);
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(),getLongitude());
    }
}
