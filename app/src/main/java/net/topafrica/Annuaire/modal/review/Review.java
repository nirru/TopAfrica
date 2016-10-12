package net.topafrica.Annuaire.modal.review;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "author",
        "business",
        "comment",
        "date",
        "rate"
})

public class Review {
    @JsonProperty("author")
    private String author;
    @JsonProperty("business")
    private String business;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("date")
    private long date;
    @JsonProperty("rate")
    private float rate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Review(){

    }

    /**
     *
     * @return
     * The author
     */
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The business
     */
    @JsonProperty("business")
    public String getBusiness() {
        return business;
    }

    /**
     *
     * @param business
     * The business
     */
    @JsonProperty("business")
    public void setBusiness(String business) {
        this.business = business;
    }

    /**
     *
     * @return
     * The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     * The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return
     * The date
     */
    @JsonProperty("date")
    public long getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    @JsonProperty("date")
    public void setDate(long date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The rate
     */
    @JsonProperty("rate")
    public float getRate() {
        return rate;
    }

    /**
     *
     * @param rate
     * The rate
     */
    @JsonProperty("rate")
    public void setRate(float rate) {
        this.rate = rate;
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
