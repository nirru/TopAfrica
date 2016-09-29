package net.topafrica.Annuaire.service;

import net.topafrica.Annuaire.AppConstant;
import net.topafrica.Annuaire.Config;
import net.topafrica.Annuaire.modal.weather.WeatherModal;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ericbasendra on 10/06/16.
 */
public interface TopAfricaWebService {
    @GET("/data/2.5/weather?appid=" + Config.OPEN_WEATHER_MAP_KEY) Observable<WeatherModal> getWeatherForLatLon(@Query("lat") double lat, @Query("lng") double lng);

}
