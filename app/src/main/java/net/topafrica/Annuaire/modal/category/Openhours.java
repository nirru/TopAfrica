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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "days",
        "zone"
})
public class Openhours {

    @JsonProperty("days")
    private List<Day> days = new ArrayList<Day>();
    @JsonProperty("zone")
    private Integer zone;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The days
     */
    @JsonProperty("days")
    public List<Day> getDays() {
        return days;
    }

    /**
     *
     * @param days
     * The days
     */
    @JsonProperty("days")
    public void setDays(List<Day> days) {
        this.days = days;
    }

    /**
     *
     * @return
     * The zone
     */
    @JsonProperty("zone")
    public Integer getZone() {
        return zone;
    }

    /**
     *
     * @param zone
     * The zone
     */
    @JsonProperty("zone")
    public void setZone(Integer zone) {
        this.zone = zone;
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
