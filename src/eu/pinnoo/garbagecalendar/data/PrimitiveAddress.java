package eu.pinnoo.garbagecalendar.data;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class PrimitiveAddress {

    @SerializedName("straatnaam")
    public String straatnaam;
    @SerializedName("straatcode")
    public String straatcode;
    @SerializedName("lv")
    public String lv;
    @SerializedName("sector")
    public String sector;
    @SerializedName("even van ")
    public String even_van;
    @SerializedName("even tot")
    public String even_tot;
    @SerializedName("oneven van ")
    public String oneven_van;
    @SerializedName("oneven tot")
    public String oneven_tot;
    @SerializedName("postcode")
    public String postcode;
    @SerializedName("gemeente")
    public String gemeente;
}
