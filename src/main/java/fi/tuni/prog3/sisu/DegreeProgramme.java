package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.HashMap;

/**
 * A class to store information of a DegreeProgramme.
 */
public class DegreeProgramme extends DegreeModule {
    private String code;
    private HashMap<URL,String> urlRuleList = new HashMap<>();
    
    /**
     * Compares the two instances of Degree Programme module.
     * @param other The other instance to be compared.
     * @return 0 if the two instances are equal otherwise return 1 or -1.
     */
    @Override
    public int compareTo(Entity other) {
        DegreeProgramme x = (DegreeProgramme) other;
        return this.getName().compareTo(x.getName());
    }
    
    /**
     * Constructor for a DegreeProgramme.
     * @param id The Id of the DegreeProgramme.
     */
    public DegreeProgramme(String id){
        this.setId(id);
    }
    
    /**
     * Empty constructor.
     */
    public DegreeProgramme() {}

    /**
     * Returns the url-rule-list of the DegreeProgramme.
     * @return The url-rule-list of the DegreeProgramme.
     */
    public HashMap<URL,String> getUrlRuleList(){
        return urlRuleList;
    }
    
    /**
     * Sets the url-rule-list of the DegreeProgramme.
     * @param list The list to be set.
     */
    public void setUrlRuleList(HashMap<URL,String> list){
        urlRuleList = list;
    }

    /**
     * Sets the code of the Module.
     * @param code The code to be set.
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * Returns the code of the Module.
     * @return The code of the Module.
     */
    public String getCode(){
        return code;
    }
}
