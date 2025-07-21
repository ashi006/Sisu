package fi.tuni.prog3.sisu;

/**
 * A class to handle a StudyModule.
 */
public class StudyModule extends DegreeModule {
    private String code;

    /**
     * Compares the two instances of study module.
     * @param other The other instance to be compared.
     * @return 0 if the two instances are equal otherwise return 1 or -1.
     */
    @Override
    public int compareTo(Entity other) {
        if (other.isStudy()){
            StudyModule study = (StudyModule) other;
            return this.getName().compareTo(study.getName());
        } else {
            return 0;
        }
    }
    
    /**
     * Empty constructor.
     */
    public StudyModule(){}
  
    /**
     * Returns the code of the StudyModule.
     * @return The code of the StudyModule.
     */
    public String getCode(){
        return this.code;
    }
    
    /**
     * Sets the code of the StudyModule.
     * @param code The code to be set.
     */
    public void setCode(String code){
        this.code = code;
    }
}
