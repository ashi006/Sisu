package fi.tuni.prog3.sisu;

import static fi.tuni.prog3.sisu.StudentData.hashPassword;
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
public class StudentDataTest {
    String salt;
    Student student;
    String hashedPassword;
    
    public StudentDataTest() {
        salt = randomSalt();
        hashedPassword = hashPassword("abc@123", salt);
        student = new Student("Abc", "A123", 2022, 2024, "abc@gmail.com", hashedPassword, salt);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    private static String randomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Test of clear method, of class StudentData.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        List<Student> list = new ArrayList<>();
        list.add(student);
        StudentData.clear();
        List<Student> result = StudentData.getStudents();
        assertEquals(0, result.size());
    }

    /**
     * Test of setCurrent method, of class StudentData.
     */
    @Test
    public void testSetCurrent() {
        System.out.println("setCurrent");
        StudentData.setCurrent(student);
        Student result = StudentData.getCurrent();
        assertEquals(student, result);
    }

    /**
     * Test of getCurrent method, of class StudentData.
     */
    @Test
    public void testGetCurrent() {
        System.out.println("getCurrent");
        StudentData.setCurrent(student);
        Student result = StudentData.getCurrent();
        assertEquals(student, result);
    }

    /**
     * Test of isNumberFound method, of class StudentData.
     */
    @Test
    public void testIsNumberFound() {
        System.out.println("isNumberFound");
        StudentData.addStudent(student.getName(), student.getStudentNumber(), student.getEmail(), student.getPassword(), student.getStartYear(), student.getEndYear());
        boolean expResult = true;
        boolean result = StudentData.isNumberFound(student.getStudentNumber());
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmailFound method, of class StudentData.
     */
    @Test
    public void testIsEmailFound() {
        System.out.println("isEmailFound");
        StudentData.addStudent(student.getName(), student.getStudentNumber(), student.getEmail(), student.getPassword(), student.getStartYear(), student.getEndYear());
        boolean expResult = true;
        boolean result = StudentData.isEmailFound(student.getEmail());
        assertEquals(expResult, result);
    }

    /**
     * Test of validatePassword method, of class StudentData.
     */
    @Test
    public void testValidatePassword() {
        System.out.println("validatePassword");
        StudentData.addStudent("David", "C15f4", "david@gmail.com", "abc@123", 2021, 2023);
        boolean expResult = true;
        boolean result = StudentData.validatePassword("david@gmail.com", "abc@123");
        assertEquals(expResult, result);
    }    
}
