package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A class for maintaining student profile. 
 * Student's personal details, degree and achieved attainments are maintained.
 */
public class Student implements Comparable<Student> {
    private String studentNumber;
    private String name;
    private String email;
    private String password;
    private final String salt;
    private int startYear;
    private int endYear;
    private DegreeProgramme degree;
    private List<StudentAttainment> attainments = new ArrayList<>();
    private boolean courseFound = false;
    private Course checkCourse;
    
    /**
     * Constructs a student with the given parameters.
     * @param studentNumber The student number of the student.
     * @param name The name of the student.
     * @param startYear The year the student started their studies.
     * @param endYear The estimated completion year of the student.
     * @param email The email of the student.
     * @param password The password of the student.
     * @param salt The salt of to be used for hashing the password.
     */
    public Student(String name, String studentNumber, int startYear, int endYear, String email, String password, String salt){
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.startYear = startYear;
        this.endYear = endYear;
        this.salt = salt;
    }
  
    /**
     * Returns the student number of the student.
     * @return The student number of the student.
     */
    public String getStudentNumber(){
        return this.studentNumber;
    }
    
    /**
     * Returns the name of the student.
     * @return The name of the student.
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Returns the email of the student.
     * @return The email of the student.
     */
    public String getEmail(){
        return this.email;
    }
    
    /**
     * Returns the password of the student.
     * @return The password of the student.
     */
    public String getPassword(){
        return this.password;
    }
    
    /**
     * return salt string of student
     * @return salt string of student
     */
    public String getSalt(){
        return this.salt;
    }
    
    /**
     * Returns the starting year of the studies.
     * @return The starting year of the studies.
     */
    public int getStartYear(){
        return this.startYear;
    }
    
    /**
     * Returns the estimated completion year of the studies.
     * @return The estimated completion year of the studies.
     */
    public int getEndYear(){
        return this.endYear;
    }
    
    /**
     * Returns the degree of the student.
     * @return The degree of the student.
     */
    public DegreeProgramme getDegree(){
        return this.degree;
    }
    
    /**
     * Returns the attainments of the student.
     * @return The attainments of the student.
     */
    public List<StudentAttainment> getAttainments(){
        return this.attainments;
    }
    
    /**
     * Sets the student number of the student.
     * @param studentNumber The student number to be set.
     */
    public void setStudentNumber(String studentNumber){
        this.studentNumber = studentNumber;
    }
    
    /**
     * Sets the name of the student.
     * @param name The name to be set.
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Sets the email of the student
     * @param email The email to be set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Sets the new password of the student.
     * @param password The new password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Sets the starting year of studies of the student.
     * @param startYear The start year to be set.
     */
    public void setStartYear(int startYear){
        this.startYear = startYear;
    }
    
    /**
     * Sets the estimated completion year of studies of the student.
     * @param endYear The estimated completion year to be set.
     */
    public void setEndYear(int endYear){
        this.endYear = endYear;
    }
    
    /**
     * Sets the degree of the student.
     * @param degree The degree to be set.
     */
    public void setDegree(DegreeProgramme degree) {
        this.degree = degree;
    }
    
    /**
     * Find an attainment of the student with a course.
     * @param course The course of the attainment searched.
     * @return The attainment of the course. If the attainment is not found, then returns null.
     */
    private StudentAttainment findAttainment(Course course){
        for(var att : attainments){
            if(att.getCourse().compareTo(course) == 0){
                return att;
            }
        }
        return null;
    }

    /**
     * Finds all the courses in the module and its children recursively.
     * @param module The module to be searched for courses.
     */
    private void findAllCoursesUnderModule (DegreeModule module){
        for(URL cUrl : module.getCourseURLs()){
            if(cUrl.equals(checkCourse.getSource())){
                courseFound = true;
            }
        }
        for(DegreeModule rule : module.getRules()){
            if(!courseFound){
                this.findAllCoursesUnderModule(rule);
            }
        }
    }

    /**
     * Get a grade from an attainment of a course.
     * @param course The course of the attainment.
     * @return The grade of the attainment of the course. If the attainment of the course is not found, returns -1.
     */
    public int getGrade(Course course){
        StudentAttainment att = findAttainment(course);
        if(att != null && att.getCourse().showsGrades()){
            return att.getGrade();
        }
        return -1;
    }
    
    /**
     * Adds a new attainment for the student.
     * @param attainment The attainment to be added.
     */
    public void addAttainment(StudentAttainment attainment){
        attainments.add(attainment);
    }
    
    /**
     * Removes an attainment from the student.
     * @param attainment The attainment to be removed.
     */
    public void removeAttainment(StudentAttainment attainment){
        attainments.remove(attainment);
    }
    
    /**
     * Checks if the student has completed the given course.
     * @param course The course to be checked for completion.
     * @return True if the student has completed the course, false if not.
     */
    public boolean isCourseCompleted(Course course){
        for(var att : attainments){
            if( att.getCourse().compareTo(course) == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the achieved credits in the selected DegreeProgramme of the student.
     * @return The credits achieved in the selected DegreeProgramme of the student.
     */
    public int getCurrentCredits(){
        int result = 0;
        for(var att : this.getAttainments()){
            checkCourse = att.getCourse();
            findAllCoursesUnderModule(this.getDegree());             
            if(courseFound){
                result += att.getCourse().getCredits();
                courseFound = false;
            }
        }
        return result;
    }

    /**
     * Sums up all the credits the student has achieved.
     * @return The total credits the student has achieved.
     */
    public int getAllCredits(){
        int result = 0;
        for(var att: attainments){
            result += att.getCourse().getCredits();
        }
        return result;
    }
    
    /**
     * Calculates the student's average grade in the selected DegreeProgramme.
     * @return The student's average grade in the selected DegreeProgramme.
     */
    public double getAverageGrade(){
        double totalGrade = 0;
        double courseAmount = 0;
        for(var att : this.getAttainments()){
            checkCourse = att.getCourse();
            findAllCoursesUnderModule(this.getDegree());             
            if(courseFound){
                if(att.getGrade() != 0 && checkCourse.showsGrades()){
                    totalGrade += att.getGrade();      
                    courseAmount++;
                }
                courseFound = false;
            }
        }
        return totalGrade/courseAmount;
    }
    
    /**
     * Handles changes to the student's attainments list. No changes are made if any of the parameters are not valid.
     * @param selectedCourse The selected course.
     * @param grade The grade of the selected course.
     * @param completed Boolean value if the course is marked completed or not.
     * @return String "invalid grade" if the grade value is incorrect, returns success messages instead.
     */
    public String updateAttainment(Course selectedCourse, int grade, boolean completed){
        StudentAttainment att = findAttainment(selectedCourse);
        if(!completed){
            if(att != null){
                attainments.remove(att);
                return "Course(s) marked as incompleted";
            }
        }
        if(grade < 0 || grade > 5){
            return "Invalid grade";
        }
        if(att != null) {
            if(selectedCourse.showsGrades()){
                att.setGrade(grade);
            } else{
                att.setGrade(0);
            }
            return "Course(s) progress updated";
        }
        attainments.add(new StudentAttainment(selectedCourse, selectedCourse.showsGrades() ? grade : 0));
        return "Course(s) marked as completed";
    }
    
    /**
     * Return the student in string form (name).
     * @return The student in string form.
     */
    @Override
    public String toString(){
        String s = this.name;
        return s;
    }
    
    /**
     * Compares two instances of students based on their student numbers.
     * @param other The student compared to.
     * @return 0 if the student numbers are equal, return -1 or 1 otherwise.
     */
    @Override
    public int compareTo(Student other){
        return studentNumber.compareTo(other.studentNumber);
    }
    
    /**
     * comparison mechanism of two Student objects
     * @param o object to be compared
     * @return True if two objects are equal
     */
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof Student)){
            return false;
        }
        Student student = (Student) o;
        return this.email.equals(student.getEmail()) && this.password.equals(student.getPassword())
                && this.name.equals(student.getName()) && this.salt.equals(student.salt);
    }
}
