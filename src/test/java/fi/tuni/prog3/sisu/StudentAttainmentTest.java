package fi.tuni.prog3.sisu;

import static fi.tuni.prog3.sisu.StudentData.hashPassword;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Asma Jamil
 */
public class StudentAttainmentTest {
    URL url;
    Course course;
    Course course2;
    
    public StudentAttainmentTest() throws MalformedURLException {
        url = new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-47926&universityId=tuni-university-root-id");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        course2 = new Course("otm-hdyeniv-hdgfj", "CS.150", "Web Dev 1", 5, "Node js course", url, true);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of getCourse method, of class StudentAttainment.
     */
    @Test
    public void testGetCourse() {
        System.out.println("getCourse");
        StudentAttainment att = new StudentAttainment(course, 4);
        Course result = att.getCourse();
        assertEquals(course, result);
    }

    /**
     * Test of getGrade method, of class StudentAttainment.
     */
    @Test
    public void testGetGrade() {
        System.out.println("getGrade");
        int grade = 4;
        StudentAttainment att = new StudentAttainment(course, grade);
        int result = att.getGrade();
        assertEquals(grade, result);
    }

    /**
     * Test of setGrade method, of class StudentAttainment.
     */
    @Test
    public void testSetGrade() {
        System.out.println("setGrade");
        int grade = 5;
        StudentAttainment att = new StudentAttainment(course, 4);
        att.setGrade(grade);
        int result = att.getGrade();
        assertEquals(grade, result);
    }

    /**
     * Test of compareTo method, of class StudentAttainment.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        StudentAttainment att = new StudentAttainment(course, 4);
        StudentAttainment att2 = new StudentAttainment(course2, 4);
        int expResult = -1;
        int result = att.compareTo(att2);
        assertEquals(expResult, result);
    }
    
}
