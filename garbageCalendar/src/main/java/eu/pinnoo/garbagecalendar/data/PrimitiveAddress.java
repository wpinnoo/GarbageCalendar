/* 
 * Copyright 2014 Wouter Pinnoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
