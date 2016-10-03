package net.topafrica.Annuaire.modal.category;

import java.util.HashMap;
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
        "closeAt",
        "day",
        "openAt"
})
@IgnoreExtraProperties
public class Day {
    @JsonProperty("closeAt")
    private Integer closeAt;
    @JsonProperty("day")
    private String day;
    @JsonProperty("openAt")
    private Integer openAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Day(){

    }
    /**
     *
     * @return
     * The closeAt
     */
    @JsonProperty("closeAt")
    public Integer getCloseAt() {
        return closeAt;
    }

    /**
     *
     * @param closeAt
     * The closeAt
     */
    @JsonProperty("closeAt")
    public void setCloseAt(Integer closeAt) {
        this.closeAt = closeAt;
    }

    /**
     *
     * @return
     * The day
     */
    @JsonProperty("day")
    public String getDay() {
        return day;
    }

    /**
     *
     * @param day
     * The day
     */
    @JsonProperty("day")
    public void setDay(String day) {
        this.day = day;
    }

    /**
     *
     * @return
     * The openAt
     */
    @JsonProperty("openAt")
    public Integer getOpenAt() {
        return openAt;
    }

    /**
     *
     * @param openAt
     * The openAt
     */
    @JsonProperty("openAt")
    public void setOpenAt(Integer openAt) {
        this.openAt = openAt;
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
