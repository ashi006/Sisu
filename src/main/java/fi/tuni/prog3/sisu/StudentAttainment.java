package fi.tuni.prog3.sisu;

/**
 * A class for maintaining attainments of students for passing courses.
 */
public class StudentAttainment implements Comparable<StudentAttainment> {
    private final Course course;
    private int grade;
    
    /**
     * Constructs an attainment with the given parameters.
     * @param course The course passed.
     * @param grade Grade achieved by the student.
     */
    public StudentAttainment(Course course, int grade){
        this.course = course;
        this.grade = grade;
    }

    /**
     * Returns the attained course.
     * @return The attained course.
     */
    public Course getCourse(){
        return course;
    }
    
    /**
     * Returns the grade achieved by the student.
     * @return The grade achieved by  the student.
     */
    public int getGrade(){
        return grade;
    }
   
    /**
     * Sets the grade of the attainment.
     * @param grade The grade to be set.
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }
    
    /**
     * Comparing other attainment's course with current course.
     * @param other The attainment to compare to.
     * @return 0 if the courses are equal otherwise returns 1 or -1.
     */
    @Override
    public int compareTo(StudentAttainment other){
        return course.compareTo(other.course);
    }
}
