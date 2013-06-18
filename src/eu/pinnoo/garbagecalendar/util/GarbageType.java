package eu.pinnoo.garbagecalendar.util;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public enum GarbageType {

    REST("Rest"), GFT("GFT"), PMD("PMD"), PK("Papier en karton"), GLAS("Glas"), NONE("-");
    
    private String strValue;
    
    private GarbageType(String strValue){
        this.strValue = strValue;
    }
    
    @Override
    public final String toString(){
        return strValue;
    }
}
