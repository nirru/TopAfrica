package net.topafrica.Annuaire.modal;

/**
 * A dummy item representing a piece of content.
 */
public  class DummyItem {
    private  String id;
    private  String content;
    private  String details;

    public DummyItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }

    public String getDetails() {
        return details;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}