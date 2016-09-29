package net.topafrica.Annuaire.modal;

/**
 * Created by ericbasendra on 21/09/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Mapdata implements Parcelable{


    private List<Annotation> annotations = new ArrayList<Annotation>();

    public Mapdata(){

    }

    protected Mapdata(Parcel in) {
        annotations = in.createTypedArrayList(Annotation.CREATOR);
    }

    public static final Creator<Mapdata> CREATOR = new Creator<Mapdata>() {
        @Override
        public Mapdata createFromParcel(Parcel in) {
            return new Mapdata(in);
        }

        @Override
        public Mapdata[] newArray(int size) {
            return new Mapdata[size];
        }
    };

    /**
     *
     * @return
     * The annotations
     */

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     *
     * @param annotations
     * The annotations
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(annotations);
    }
}
