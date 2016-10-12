package net.topafrica.Annuaire.rx;

import android.location.Address;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DisplayAddressOnViewAction implements Action1<ArrayList<String>> {
    private final TextView target_country, target_city, target_country_code,target_fax_code,target_address,target_cordinate;

    public DisplayAddressOnViewAction(TextView target_country, TextView target_city, TextView country_code,TextView fax_code,TextView target_address, TextView target_cordinate) {
        this.target_country = target_country;
        this.target_city = target_city;
        this.target_address = target_address;
        this.target_cordinate  = target_cordinate;
        this.target_country_code = country_code;
        this.target_fax_code = fax_code;
    }

    @Override
    public void call(ArrayList<String> s) {
        target_country.setText(s.get(0));
        target_city.setText(s.get(1));
        target_address.setText(s.get(2));
        target_cordinate.setText(s.get(3) + "," + s.get(4));
        target_country_code.setText(s.get(5) !=null ? s.get(5) : "+91");
        target_fax_code.setText(s.get(5) !=null ? s.get(5) : "+91");
    }
}