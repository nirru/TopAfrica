package net.topafrica.Annuaire.modal.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.firebase.database.IgnoreExtraProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Number_employes",
        "category",
        "city",
        "country",
        "email",
        "listingtype",
        "logo",
        "mapdata",
        "name",
        "officeLocation",
        "openhours",
        "phoneNumber",
        "pictures",
        "road",
        "state",
        "suburb",
        "website"
})
@IgnoreExtraProperties
public class Businesse {
    @JsonProperty("Number_employes")
    private String numberEmployes;
    @JsonProperty("category")
    private String category;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("email")
    private String email;
    @JsonProperty("listingtype")
    private String listingtype;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("mapdata")
    private Mapdata mapdata;
    @JsonProperty("name")
    private String name;
    @JsonProperty("officeLocation")
    private String officeLocation;
    @JsonProperty("openhours")
    private Openhours openhours;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("pictures")
    private List<String> pictures = new ArrayList<String>();
    @JsonProperty("road")
    private String road;
    @JsonProperty("state")
    private String state;
    @JsonProperty("suburb")
    private String suburb;
    @JsonProperty("website")
    private String website;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Businesse(){

    }

    /**
     *
     * @return
     * The numberEmployes
     */
    @JsonProperty("Number_employes")
    public String getNumberEmployes() {
        return numberEmployes;
    }

    /**
     *
     * @param numberEmployes
     * The Number_employes
     */
    @JsonProperty("Number_employes")
    public void setNumberEmployes(String numberEmployes) {
        this.numberEmployes = numberEmployes;
    }

    /**
     *
     * @return
     * The category
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The city
     */
    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The country
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The listingtype
     */
    @JsonProperty("listingtype")
    public String getListingtype() {
        return listingtype;
    }

    /**
     *
     * @param listingtype
     * The listingtype
     */
    @JsonProperty("listingtype")
    public void setListingtype(String listingtype) {
        this.listingtype = listingtype;
    }

    /**
     *
     * @return
     * The logo
     */
    @JsonProperty("logo")
    public String getLogo() {
        return logo;
    }

    /**
     *
     * @param logo
     * The logo
     */
    @JsonProperty("logo")
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     *
     * @return
     * The mapdata
     */
    @JsonProperty("mapdata")
    public Mapdata getMapdata() {
        return mapdata;
    }

    /**
     *
     * @param mapdata
     * The mapdata
     */
    @JsonProperty("mapdata")
    public void setMapdata(Mapdata mapdata) {
        this.mapdata = mapdata;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The officeLocation
     */
    @JsonProperty("officeLocation")
    public String getOfficeLocation() {
        return officeLocation;
    }

    /**
     *
     * @param officeLocation
     * The officeLocation
     */
    @JsonProperty("officeLocation")
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    /**
     *
     * @return
     * The openhours
     */
    @JsonProperty("openhours")
    public Openhours getOpenhours() {
        return openhours;
    }

    /**
     *
     * @param openhours
     * The openhours
     */
    @JsonProperty("openhours")
    public void setOpenhours(Openhours openhours) {
        this.openhours = openhours;
    }

    /**
     *
     * @return
     * The phoneNumber
     */
    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     * The phoneNumber
     */
    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     * The pictures
     */
    @JsonProperty("pictures")
    public List<String> getPictures() {
        return pictures;
    }

    /**
     *
     * @param pictures
     * The pictures
     */
    @JsonProperty("pictures")
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    /**
     *
     * @return
     * The road
     */
    @JsonProperty("road")
    public String getRoad() {
        return road;
    }

    /**
     *
     * @param road
     * The road
     */
    @JsonProperty("road")
    public void setRoad(String road) {
        this.road = road;
    }

    /**
     *
     * @return
     * The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The suburb
     */
    @JsonProperty("suburb")
    public String getSuburb() {
        return suburb;
    }

    /**
     *
     * @param suburb
     * The suburb
     */
    @JsonProperty("suburb")
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     *
     * @return
     * The website
     */
    @JsonProperty("website")
    public String getWebsite() {
        return website;
    }

    /**
     *
     * @param website
     * The website
     */
    @JsonProperty("website")
    public void setWebsite(String website) {
        this.website = website;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
