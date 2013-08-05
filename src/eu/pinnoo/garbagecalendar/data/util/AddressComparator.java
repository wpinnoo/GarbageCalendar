package eu.pinnoo.garbagecalendar.data.util;

import eu.pinnoo.garbagecalendar.data.Address;
import java.util.Comparator;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressComparator implements Comparator<Address> {

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
