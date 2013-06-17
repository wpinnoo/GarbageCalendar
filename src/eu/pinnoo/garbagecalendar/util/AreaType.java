package eu.pinnoo.garbagecalendar.util;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum AreaType {

    V("V", BagType.YELLOW), L("L", BagType.GRAY), NONE("None", BagType.NONE);
    private final BagType bagType;
    private final String str;

    private AreaType(final String str, final BagType bagType) {
        this.bagType = bagType;
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public BagType getBagType() {
        return this.bagType;
    }
}
