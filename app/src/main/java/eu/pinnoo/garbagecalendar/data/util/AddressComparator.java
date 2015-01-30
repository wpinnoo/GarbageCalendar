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
package eu.pinnoo.garbagecalendar.data.util;

import java.io.Serializable;
import java.util.Comparator;

import eu.pinnoo.garbagecalendar.data.Address;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressComparator implements Comparator<Address>, Serializable {

    private static final long serialVersionUID = 4394556257114119818L;

    public int compare(Address lhs, Address rhs) {
        int cmp = lhs.getStreetname().toUpperCase().compareTo(rhs.getStreetname().toUpperCase());
        if (cmp != 0) {
            return cmp;
        }
        cmp += lhs.getCity().toUpperCase().compareTo(rhs.getCity().toUpperCase());
        if (cmp != 0) {
            return cmp;
        }
        cmp += lhs.getCode().toUpperCase().compareTo(rhs.getCode().toUpperCase());
        return cmp;
    }
}
