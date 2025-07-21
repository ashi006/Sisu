package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ArrayList;

/**
 * An abstract parent class to handle modules and courses.
 */
public abstract class Entity implements Comparable<Entity> {
    private String name;
    private URL source;
    private String description;
    public ArrayList<DegreeModule> rules = new ArrayList<>();
    public ArrayList<Course> courses = new ArrayList<>();
    public ArrayList<URL> courseURLs = new ArrayList<>();

    /**
     * Checks if entity is a Course
     * @return true if entity is a course, otherwise returns false.
     */
    public boolean isCourse(){
        return this instanceof Course;
    }
   
    /**
     * Checks if entity is a DegreeProgramme.
     * @return true if entity is a DegreeProgramme, otherwise returns false.
     */
    public boolean isDegree(){
        return this instanceof DegreeProgramme;
    }
    
    /**
     * Checks if entity is a StudyModule.
     * @return true if entity is a StudyModule, otherwise returns false.
     */
    public boolean isStudy(){
        return this instanceof StudyModule;
    }
    
    /**
     * Checks if entity is a GroupingModule.
     * @return true if entity is a GroupingModule, otherwise returns false.
     */
    public boolean isGrouping(){
        return this instanceof GroupingModule;
    }

    @Override
    public abstract int compareTo(Entity entity);

    /**
     * Adds a new Course to the Entity's container.
     * @param course A Course to be added.
     */
    public void addCourse(Course course){
        courses.add(course);
    }

    /**
    * Returns the course URLs of the GroupingModule.
    * @return The list of the course URLs.
    */
    public ArrayList<URL> getCourseURLs(){
        return courseURLs;
    }

    /**
     * Adds a new URL in the list of URLs of courses
     * @param url The URL to get details of a Course
     */
    public void addCourseUrl(URL url){
        courseURLs.add(url);

    }
    
    /**
     * Returns a list of rules of the Module.
     * @return A list of rules of the Module.
     */
    public ArrayList<DegreeModule> getRules(){
        return rules;
    }
    
    /**
     * Returns a list of courses of the Module.
     * @return A list of courses of the Module.
     */
    public ArrayList<Course> getCourses(){
        return courses;
    }

    /**
     * Returns the description of the Module.
     * @return The description of the Module.
     */
    public String getDescription(){
        return description;
    }
       
    /**
     * Sets the description of the Module.
     * @param description The description to be set.
     */
    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.getName();
    }
 
    /**
     * Sets the name of the Module.
     * @param name The name to be set.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets the source URL of the Module.
     * @param source The source URL to be set.
     */
    public void setSource(URL source){
        this.source = source;
    }

    /**
     * Returns the source URL of the Module.
     * @return The source URL of the Module.
     */
    public URL getSource(){
        return this.source;
    }

   /**
     * Returns the name of the Module.
     * @return The name of the Module.
     */
    public String getName(){
        return this.name;
    }
}
