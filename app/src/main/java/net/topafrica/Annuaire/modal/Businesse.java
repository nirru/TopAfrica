package net.topafrica.Annuaire.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericbasendra on 21/09/16.
 */

public class Businesse implements Parcelable{

    private String category;

    private String description;

    private String drupal;

    private String email;

    private String facebookPage;

    private String logo;

    private Mapdata mapdata;

    private String name;

    private String officeLocation;

    private Openhours openhours;

    private String phoneNumber;

    private List<String> pictures = new ArrayList<String>();

    private List<String> tags = new ArrayList<String>();

    private String wordpress;

    private String website;

    private String state;

    private String suburb;

    private String road;

    private String Number_employes;

    private String city;

    private String country;

    private String listingtype;

    public Businesse(){

    }

    protected Businesse(Parcel in) {
        category = in.readString();
        description = in.readString();
        drupal = in.readString();
        email = in.readString();
        facebookPage = in.readString();
        logo = in.readString();
        mapdata = in.readParcelable(Mapdata.class.getClassLoader());
        name = in.readString();
        officeLocation = in.readString();
        openhours = in.readParcelable(Openhours.class.getClassLoader());
        phoneNumber = in.readString();
        pictures = in.createStringArrayList();
        tags = in.createStringArrayList();
        wordpress = in.readString();
    }

    public static final Creator<Businesse> CREATOR = new Creator<Businesse>() {
        @Override
        public Businesse createFromParcel(Parcel in) {
            return new Businesse(in);
        }

        @Override
        public Businesse[] newArray(int size) {
            return new Businesse[size];
        }
    };

    /**
     *
     * @return
     * The category
     */

    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The drupal
     */
    public String getDrupal() {
        return drupal;
    }

    /**
     *
     * @param drupal
     * The drupal
     */
    public void setDrupal(String drupal) {
        this.drupal = drupal;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The facebookPage
     */
    public String getFacebookPage() {
        return facebookPage;
    }

    /**
     *
     * @param facebookPage
     * The facebookPage
     */
    public void setFacebookPage(String facebookPage) {
        this.facebookPage = facebookPage;
    }

    /**
     *
     * @return
     * The logo
     */

    public String getLogo() {
        return logo;
    }

    /**
     *
     * @param logo
     * The logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     *
     * @return
     * The mapdata
     */

    public Mapdata getMapdata() {
        return mapdata;
    }

    /**
     *
     * @param mapdata
     * The mapdata
     */
    public void setMapdata(Mapdata mapdata) {
        this.mapdata = mapdata;
    }

    /**
     *
     * @return
     * The name
     */

    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The officeLocation
     */

    public String getOfficeLocation() {
        return officeLocation;
    }

    /**
     *
     * @param officeLocation
     * The officeLocation
     */
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    /**
     *
     * @return
     * The openhours
     */

    public Openhours getOpenhours() {
        return openhours;
    }

    /**
     *
     * @param openhours
     * The openhours
     */
    public void setOpenhours(Openhours openhours) {
        this.openhours = openhours;
    }

    /**
     *
     * @return
     * The phoneNumber
     */

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     * The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     * The pictures
     */

    public List<String> getPictures() {
        return pictures;
    }

    /**
     *
     * @param pictures
     * The pictures
     */
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    /**
     *
     * @return
     * The tags
     */

    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The wordpress
     */

    public String getWordpress() {
        return wordpress;
    }

    /**
     *
     * @param wordpress
     * The wordpress
     */
    public void setWordpress(String wordpress) {
        this.wordpress = wordpress;
    }

    @JsonIgnore
    public String getWebsite() {
        return website;
    }

    @JsonIgnore
    public String getState() {
        return state;
    }

    @JsonIgnore
    public String getSuburb() {
        return suburb;
    }

    @JsonIgnore
    public String getRoad() {
        return road;
    }

    @JsonIgnore
    public String getNumberEmployes() {
        return Number_employes;
    }

    @JsonIgnore
    public String getCity() {
        return city;
    }

    @JsonIgnore
    public String getCityountry() {
        return country;
    }

    @JsonIgnore
    public String getListingtype() {
        return listingtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeString(drupal);
        parcel.writeString(email);
        parcel.writeString(facebookPage);
        parcel.writeString(logo);
        parcel.writeParcelable(mapdata, i);
        parcel.writeString(name);
        parcel.writeString(officeLocation);
        parcel.writeParcelable(openhours, i);
        parcel.writeString(phoneNumber);
        parcel.writeStringList(pictures);
        parcel.writeStringList(tags);
        parcel.writeString(wordpress);
    }
}
