package fi.tuni.prog3.sisu;

import java.net.URL;

/**
 * A class for maintaining details of a course.
 */
public class Course extends Entity {
    private String id;
    private String code;
    private int credits;
    private boolean showGrades;
     
    /**
     * Constructs a course with the given parameters.
     * @param id The id of the course.
     * @param code The course code.
     * @param name Name of the course.
     * @param credits The amount of credits of the course.
     * @param description The description of the course.
     * @param source The source URL to find course info.
     * @param showGrades The flag to check whether course has grade or pass/fail criteria
     */
    public Course(String id, String code, String name, int credits, String description, URL source, boolean showGrades) {
        this.id = id; 
        this.code = code;
        this.credits = credits;
        this.showGrades = showGrades;
        this.setName(name);
        this.setDescription(description);
        this.setSource(source);
    }
    
    /**
     * Empty constructor.
     */
    public Course(){}
    
    /**
     * Compares two instances of courses.
     * @param other SisuItem that the course is compared to.
     * @return 0 if courses match otherwise return -1 or 1.
     */
    @Override
    public int compareTo(Entity other){
        Course x = (Course) other;
        return this.getCode().compareTo(x.getCode());
    }

    /**
     * Returns the course id.
     * @return The course id.
     */
    public String getId(){
        return this.id;
    }
 
    /**
     * Returns the course code.
     * @return The course code.
     */
    public String getCode(){
        return code;
    }

    /**
     * Sets the course code.
     * @param code The course code to be set.
     */
    public void setCode(String code){
        this.code = code;
    }
    
     /**
     * Sets the course id.
     * @param id The course id to be set.
     */
    public void setId(String id){
        this.id = id;
    }
    
    /**
     * Returns the total credits of the course.
     * @return The total credits of the course.
     */
    public int getCredits(){
        return credits;
    }

    /**
     * Sets the total credits of the course.
     * @param credits The total credits of the course.
     */
    public void setCredits(int credits){
        this.credits = credits;
    }
    
    /**
     * Sets the boolean value if a course shows grades or not.
     * @param showGrades The boolean value to be set.
     */
    public void setShowGrades(boolean showGrades ){
        this.showGrades = showGrades;

    }
    
    /**
     * Returns the boolean value if a course shows grades or not.
     * @return The boolean value if a course shows grades or not.
     */
    public boolean showsGrades(){
        return showGrades;
    }
    
    /**
     * Returns the course in string form (name (5 cr)).
     * @return The course in string form.
     */
    @Override  
    public String toString() {
        return this.getName() + " (" + this.getCredits() + " cr)";      
    }
}
