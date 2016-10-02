package net.topafrica.Annuaire.modal.category;

import android.support.v7.widget.AppCompatCheckBox;

/**
 * Created by ericbasendra on 01/10/16.
 */

public class Category {

    private String categoryname;
    private boolean isChecked;

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
