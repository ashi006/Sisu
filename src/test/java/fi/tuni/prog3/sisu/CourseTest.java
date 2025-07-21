package fi.tuni.prog3.sisu;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Asma JAmil
 */
public class CourseTest { 
    URL url;
    Entity other;
    Course course;
    
    public CourseTest() throws MalformedURLException {
        url = new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-47926&universityId=tuni-university-root-id");
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of compareTo method, of class Course.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        other = (Entity) new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        int expResult = 0;
        int result = course.compareTo(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class Course.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        String expResult = "otm-hdgyfe-hdgfj";
        String result = course.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCode method, of class Course.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        String expResult = "CS.140";
        String result = course.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCode method, of class Course.
     */
    @Test
    public void testSetCode() {
        System.out.println("setCode");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        String code = "COMP.CS.140";
        course.setCode(code);
        String result = course.getCode();
        assertEquals(code, result);
    }

    /**
     * Test of setId method, of class Course.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        String id = "otm-comp-cs-140";
        course.setId(id);
        String result = course.getId();
        assertEquals(id, result);
    }

    /**
     * Test of getCredits method, of class Course.
     */
    @Test
    public void testGetCredits() {
        System.out.println("getCredits");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        int expResult = 5;
        int result = course.getCredits();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCredits method, of class Course.
     */
    @Test
    public void testSetCredits() {
        System.out.println("setCredits");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        int credits = 0;
        course.setCredits(credits);
        int result = course.getCredits();
        assertEquals(credits, result);
    }

    /**
     * Test of setShowGrades method, of class Course.
     */
    @Test
    public void testSetShowGrades() {
        System.out.println("setShowGrades");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        boolean showGrades = false;
        course.setShowGrades(showGrades);
        boolean result = course.showsGrades();
        assertEquals(showGrades, result);
    }

    /**
     * Test of showsGrades method, of class Course.
     */
    @Test
    public void testShowsGrades() {
        System.out.println("showsGrades");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        boolean expResult = true;
        boolean result = course.showsGrades();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Course.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        String expResult = "Programming 3 (5 cr)";
        String result = course.toString();
        assertEquals(expResult, result);
    }
    
}
