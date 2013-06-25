package eu.pinnoo.garbagecalendar.data.util;

import eu.pinnoo.garbagecalendar.data.Address;
import java.util.Comparator;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class AddressComparator implements Comparator<Address> {

    public int compare(Address lhs, Address rhs) {
        int cmp = lhs.getStreetname().compareTo(rhs.getStreetname());
        if (cmp != 0) {
            return cmp;
        }
        cmp += lhs.getCity().compareTo(rhs.getCity());
        if (cmp != 0) {
            return cmp;
        }
        cmp += lhs.getCode().compareTo(rhs.getCode());
        return cmp;
    }
}
