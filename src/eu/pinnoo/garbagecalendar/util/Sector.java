package eu.pinnoo.garbagecalendar.util;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Sector {

    private AreaType type;
    private String code;

    public Sector(AreaType type, String code) {
        this.type = type;
        this.code = code;
    }
    
    public Sector(){
        this(LocalConstants.DEFAULT_SECTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sector) {
            Sector s = (Sector) o;
            return s.getCode().equals(getCode())
                    && s.getType().equals(getType());
        }
        return false;
    }
    
    @Override
    public String toString(){
        return type.toString() + code;
    }

    public Sector(String str) {
        if (str.length() == 3) {
            switch (str.charAt(0)) {
                case 'L':
                    type = AreaType.L;
                    break;
                case 'V':
                    type = AreaType.V;
                    break;
                default:
                    type = AreaType.NONE;
            }
            code = str.substring(1);
        }
    }

    public AreaType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}
