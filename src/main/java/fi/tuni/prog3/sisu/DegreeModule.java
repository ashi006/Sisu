package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 *An abstract class for storing information of Modules.
 */
public abstract class DegreeModule extends Entity {
    private String id;
    private String name;
    private String groupId;
    private int minCredits;
    
    /**
     * Returns the name of the Module.
     * @return name of the Module.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the id of the Module.
     * @return id of the Module.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Returns the group id of the Module.
     * @return group id of the Module.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the minimum credits of the Module.
     * @return minimum credits of the Module.
     */
    public int getMinCredits() {
        return this.minCredits;
    }
    
    /**
     * Sets the id of the Module.
     * @param id The id to be set.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Sets the name of the Module.
     * @param name The name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Sets the group ID of the Module.
     * @param groupId The group ID to be set.
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    /**
     * Sets the minimum credits of the Module.
     * @param credits The credits to be set.
     */
    public void setMinCredits(int credits) {
        this.minCredits = credits;
    }
    
    /**
     * Returns the Module in string form (name).
     * @return The Module in string form (name).
     */
    @Override
    public String toString() {
        if ((!this.getCourseURLs().isEmpty()) && 
            (this.getCourses().isEmpty()) &&
            (this.getRules().isEmpty())) {
                return ("\u25B6 " + this.getName() + " (" + this.getMinCredits() + " cr)");

        } else {
            return this.getName() + " (" + this.getMinCredits() + " cr)";
        }
    }
    
    /**
     * Adds a rule to the Module's container.
     * @param rule The rule to be added.
     */
    public void addRule(DegreeModule rule){
        rules.add(rule);
    }
    
    /**
     * Adds many rules to the Module's container.
     * @param ruleArr A list of rules to be added.
     */
    public void addRule(ArrayList<DegreeModule> ruleArr){
        ruleArr.forEach(rule -> {rules.add(rule);});
    }
    
    /**
     * Clears the list of previously created rules int the Module's container.
     */
    public void clearRules() {
        rules.clear();
    }
}
