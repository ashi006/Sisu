package fi.tuni.prog3.sisu;

import static fi.tuni.prog3.sisu.StudentData.hashPassword;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Asma Jamil
 */
public class StudentTest {
    URL url;
    Entity other;
    Course course;
    Course course2;
    
    public StudentTest() throws MalformedURLException {
        url = new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-47926&universityId=tuni-university-root-id");
        course = new Course("otm-hdgyfe-hdgfj", "CS.140", "Programming 3", 5, "java course", url, true);
        course2 = new Course("otm-hdyeniv-hdgfj", "CS.150", "Web Dev 1", 5, "Node js course", url, true);
    }
    
    private static String randomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of getStudentNumber method, of class Student.
     */
    @Test
    public void testGetStudentNumber() {
        System.out.println("getStudentNumber");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String expResult = "A123";
        String result = student.getStudentNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Student.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String expResult = "Abc";
        String result = student.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmail method, of class Student.
     */
    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String expResult = "abc@gmail.com";
        String result = student.getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class Student.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String result = student.getPassword();
        assertEquals(hashedPassword, result);
    }

    /**
     * Test of getSalt method, of class Student.
     */
    @Test
    public void testGetSalt() {
        System.out.println("getSalt");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String result = student.getSalt();
        assertEquals(salt, result);
    }

    /**
     * Test of getStartYear method, of class Student.
     */
    @Test
    public void testGetStartYear() {
        System.out.println("getStartYear");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        int expResult = 2022;
        int result = student.getStartYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndYear method, of class Student.
     */
    @Test
    public void testGetEndYear() {
        System.out.println("getEndYear");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        int expResult = 2024;
        int result = student.getEndYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDegree method, of class Student.
     */
    @Test
    public void testGetDegree() {
        System.out.println("getDegree");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        DegreeProgramme expResult = new DegreeProgramme("otm-232");
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.setDegree(expResult);
        DegreeProgramme result = student.getDegree();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAttainments method, of class Student.
     */
    @Test
    public void testGetAttainments() {
        System.out.println("getAttainments");
        StudentAttainment att = new StudentAttainment(course, 4);
        List<StudentAttainment> expResult = new ArrayList<StudentAttainment>();
        expResult.add(att);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);
        List<StudentAttainment> result = student.getAttainments();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of setStudentNumber method, of class Student.
     */
    @Test
    public void testSetStudentNumber() {
        System.out.println("setStudentNumber");
        String studentNumber = "B201";
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.setStudentNumber(studentNumber);
        String result = student.getStudentNumber();
        assertEquals(studentNumber, result);
    }

    /**
     * Test of setName method, of class Student.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "David";
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.setName(name);
        String result = student.getName();
        assertEquals(name, result);
    }

    /**
     * Test of setEmail method, of class Student.
     */
    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
        String email = "david@hotmail.com";
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.setEmail(email);
        String result = student.getEmail();
        assertEquals(email, result);
    }

    /**
     * Test of setPassword method, of class Student.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String salt = randomSalt();
        String password = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", password, salt);
        student.setPassword(password);
        String result = student.getPassword();
        assertEquals(password, result);
    }

    /**
     * Test of setStartYear method, of class Student.
     */
    @Test
    public void testSetStartYear() {
        System.out.println("setStartYear");
        int startYear = 2019;
        String salt = randomSalt();
        String password = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", password, salt);
        student.setStartYear(startYear);
        int result = student.getStartYear();
        assertEquals(startYear, result);
    }

    /**
     * Test of setEndYear method, of class Student.
     */
    @Test
    public void testSetEndYear() {
        System.out.println("setEndYear");
        int endYear = 2022;
        String salt = randomSalt();
        String password = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", password, salt);
        student.setEndYear(endYear);
        int result = student.getEndYear();
        assertEquals(endYear, result);
    }

    /**
     * Test of setDegree method, of class Student.
     */
    @Test
    public void testSetDegree() {
        System.out.println("setDegree");
        String salt = randomSalt();
        String password = hashPassword("abc@123", salt);
        DegreeProgramme expResult = new DegreeProgramme("otm-232");
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", password, salt);
        student.setDegree(expResult);
        DegreeProgramme result = student.getDegree();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGrade method, of class Student.
     */
    @Test
    public void testGetGrade() {
        System.out.println("getGrade");
        StudentAttainment att = new StudentAttainment(course, 4);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);
        int result = student.getGrade(course);
        int expResult = 4;
        assertEquals(expResult, result);
    }

    /**
     * Test of addAttainment method, of class Student.
     */
    @Test
    public void testAddAttainment() {
        System.out.println("addAttainment");
        StudentAttainment att = new StudentAttainment(course, 3);
        List<StudentAttainment> expResult = new ArrayList<>();
        expResult.add(att);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);
        List<StudentAttainment> result = student.getAttainments();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of removeAttainment method, of class Student.
     */
    @Test
    public void testRemoveAttainment() {
        System.out.println("removeAttainment");
        StudentAttainment att = new StudentAttainment(course, 3);
        List<StudentAttainment> expResult = new ArrayList<>();
        expResult.add(att);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);
        student.removeAttainment(att);
        expResult.remove(att);
        List<StudentAttainment> result = student.getAttainments();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of isCourseCompleted method, of class Student.
     */
    @Test
    public void testIsCourseCompleted() {
        System.out.println("isCourseCompleted");
        StudentAttainment att = new StudentAttainment(course, 3);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);
        boolean expResult = true;
        boolean result = student.isCourseCompleted(course);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllCredits method, of class Student.
     */
    @Test
    public void testGetAllCredits() {
        System.out.println("getAllCredits");
        StudentAttainment att = new StudentAttainment(course, 3);
        StudentAttainment att2 = new StudentAttainment(course2, 4);
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        student.addAttainment(att);        
        student.addAttainment(att2);
        int expResult = 10;
        int result = student.getAllCredits();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Student.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
        String expResult = "Abc";
        String result = student.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Student.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Def", "B20123", 2021, 2023, "david@gmail.com", hashedPassword, salt);
        salt = randomSalt();
        hashedPassword = hashPassword("abc@123", salt);
        Student student2 = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt); 
        int expResult = 1;
        int result = student.compareTo(student2);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Student.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        String salt = randomSalt();
        String hashedPassword = hashPassword("abc@123", salt);
        Student student = new Student("Def", "B20123", 2021, 2023, "david@gmail.com", hashedPassword, salt);
        salt = randomSalt();
        hashedPassword = hashPassword("abc@123", salt);
        Student student2 = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt); 
        boolean expResult = false;
        boolean result = student.equals(student2);
        assertEquals(expResult, result);
    }
    
}
