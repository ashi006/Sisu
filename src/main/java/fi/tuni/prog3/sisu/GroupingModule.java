package fi.tuni.prog3.sisu;

/**
 * A class to handle a GroupingModule.
 */
public class GroupingModule extends DegreeModule {
    /**
     * Empty constructor.
     */
    public GroupingModule(){}
    
    /**
     * Compares the two instances of Grouping module.
     * @param other The other instance to be compared.
     * @return 0 if the two instances are equal otherwise return 1 or -1.
     */
    @Override
    public int compareTo(Entity other) {
        if (other.isGrouping()){
            GroupingModule gModule = (GroupingModule) other;
            return this.getName().compareTo(gModule.getName());
        } else {
            return 0;
        }
    }
}
