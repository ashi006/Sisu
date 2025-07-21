package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Class for data management of student
 */
public class StudentData implements iReadAndWriteToFile {

    private static List<Student> stdList = new ArrayList<>();;
    private static Student currentStd;

    /**
     * Clears all students from the list
     */
    public static void clear(){
        stdList = new ArrayList<>();
    }

    /**
     * Sets the currently logged in student
     * @param student The student to be set
     */
    public static void setCurrent(Student student){
        currentStd = student;
    }

    /**
     * Returns the currently logged in student
     * @return The currently logged in student
     */
    public static Student getCurrent(){
        return currentStd;
    }

    /**
     * Generate a random salt used in hashing password
     * @return a byte-string salt
     */
    private static String randomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Generate a hash of (text_password + salt) using PBKDF2WithHmacSHA1 cryptographic system
     * @param password password typed by users
     * @param saltString random string added to password before doing hash
     * @return a hashed password - String
     */
    public static String hashPassword(String password, String saltString){
        try {
            byte[] salt = Base64.getDecoder().decode(saltString);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // generating hash
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex){
            System.err.println(ex);
        }
        return null;
    }

    /**
     * Adds a student to the list.
     * @param name The name of student.
     * @param studentNumber The student number of student.
     * @param email The email of student.
     * @param password The password of student.
     * @param startYear The starting year of studies of student.
     * @param endYear The estimated completion of studies of student.
     */
    public static void addStudent(String name, String studentNumber, String email, String password, int startYear, int endYear){
        String salt = randomSalt();
        String hashedPassword = hashPassword(password, salt);
        Student student = new Student(name, studentNumber, startYear, endYear, email, hashedPassword, salt);
        stdList.add(student);
        StudentData.setCurrent(student);
    }

    /**
     * Removes a student from the list.
     * @param studentNumber Student number of the student to be removed.
     */
    public static void removeStudent(String studentNumber){
        stdList.removeIf(student -> student.getStudentNumber().equals(studentNumber));
    }

    /**
     * Checks if a student already exists based on student number of student.
     * @param studentNumber Student number of the student.
     * @return True if student was found, false otherwise.
     */
    public static boolean isNumberFound(String studentNumber) {
        return stdList.stream().anyMatch(student -> (student.getStudentNumber().equals(studentNumber)));
    }

    /**
     * Checks if a student already exists based on email of student.
     * @param email email of the student.
     * @return True if student was found, false otherwise.
     */
    public static boolean isEmailFound(String email) {
        return stdList.stream().anyMatch(student -> (student.getEmail().equals(email)));
    }

    /**
     * Returns the list of students.
     * @return The list of students.
     */
    public static List<Student> getStudents(){
        return stdList;
    }

    /**
     * Check if the provided email and password are correct
     * @param email The email types by user
     * @param password password typed by user
     * @return True if student with same email found and hash of typed password and stored-hashed password are the same, returns false otherwise.
     */
    public static boolean validatePassword(String email, String password) {
        boolean found = isEmailFound(email);
        if (found) {
            Student student = null;
            for( Student std : getStudents()){
                if(std.getEmail().equals(email))
                    student = std;
            }
            String hashedPassword = student.getPassword();
            String salt = student.getSalt();
            if (hashPassword(password, salt).equals(hashedPassword)) {
                StudentData.setCurrent(student);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the password of student profile.
     * @param password The new password to be set.
     */
    public static void updatePassword(String password) {
        String hashedPassword = hashPassword(password, getCurrent().getSalt());
        getCurrent().setPassword(hashedPassword);
    }

    /**
     * Reads data from the json file to populate students list.
     * @param fileName The name of the file where students data is saved.
     * @return True if the file is found and data is read successfully, return false otherwise.
     */
    @Override
    public boolean readFromFile(String fileName) {
        try {
            APIData api = new APIData();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            if(br.readLine() == null){
                return true;
            }
            JsonArray studentFields = new JsonParser().parse(new FileReader(fileName)).getAsJsonArray();
            for( var studentField : studentFields){
                JsonObject studentObject = studentField.getAsJsonObject();
                String studentNumber = studentObject.get("studentNumber").getAsString();
                String studentName = studentObject.get("name").getAsString();
                String email = studentObject.get("email").getAsString();
                String password = studentObject.get("password").getAsString();
                String salt = studentObject.get("salt").getAsString();
                int startYear = studentObject.get("startYear").getAsInt();
                int endYear = studentObject.get("endYear").getAsInt();
                Student student = new Student(studentName, studentNumber, startYear, endYear, email, password, salt);
                if(studentObject.has("degree")) {
                    String degreeID = studentObject.get("degree").getAsString();
                    DegreeProgramme newDegree = api.fetchDegreeDetailsFromAPI(degreeID);
                    student.setDegree(newDegree);
                }
                if(studentObject.has("attainments")) {
                    JsonArray attainmentFields = studentObject.get("attainments").getAsJsonArray();
                    for(var attainmentField : attainmentFields){
                        JsonObject attainmentObject = attainmentField.getAsJsonObject();
                        String id = attainmentObject.get("id").getAsString();
                        String code = attainmentObject.get("code").getAsString();
                        String courseName = attainmentObject.get("name").getAsString();
                        int credits = attainmentObject.get("credits").getAsInt();
                        String description;
                        if (attainmentObject.has("description")){
                            description = attainmentObject.get("description").getAsString();
                        }
                        else {
                            description = "";
                        }
                        URL source = new URL(api.getCourseURL()+id);
                        boolean showGrades = attainmentObject.get("showGrades").getAsBoolean();
                        Course course = new Course(id, code, courseName, credits, description, source, showGrades);
                        int grade = attainmentObject.get("grade").getAsInt();
                        student.addAttainment(new StudentAttainment(course, grade));
                    }
                }
                stdList.add(student);
            }
            return true;
        }
        catch(FileNotFoundException e){
            System.err.println("students.json file not found.");
            return false;
        }
        catch(MalformedURLException e){
            System.err.println("A source URL of a course wasn't valid.");
            return false;
        }
        catch(IOException e){
            System.err.println("An I/O error occurred.");
            return false;
        }
        catch(JsonIOException e){
            System.err.println("A Json I/O error occurred.");
            return false;
        }
        catch(JsonParseException e){
            System.err.println("A Json parse error occurred.");
            return false;
        }
        catch(final Exception e){
            System.err.println("Unknown error occurred.");
            return false;
        }
    }

    /**
     * Writes the updated students list to the given json file.
     * @param fileName The name of the json file to write data to.
     * @return True if the file is found and data is written successfully, otherwise returns false.
     * @throws IOException
     */
    @Override
    public boolean writeToFile(String fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray students = new JsonArray();
        for(Student student : stdList) {
            JsonObject studentJson = new JsonObject();
            studentJson.addProperty("name", student.getName());
            studentJson.addProperty("email", student.getEmail());
            studentJson.addProperty("password", student.getPassword());
            studentJson.addProperty("salt", student.getSalt());
            studentJson.addProperty("studentNumber", student.getStudentNumber());
            studentJson.addProperty("startYear", student.getStartYear());
            studentJson.addProperty("endYear", student.getEndYear());
            if(student.getDegree() != null)
                studentJson.addProperty("degree", student.getDegree().getId());
            if (!student.getAttainments().isEmpty()) {
                JsonArray attainments = new JsonArray();
                for (var att : student.getAttainments()) {
                    JsonObject attJson = new JsonObject();
                    Course course = att.getCourse();
                    attJson.addProperty("id", course.getId());
                    attJson.addProperty("name", course.getName());
                    attJson.addProperty("code", course.getCode());
                    attJson.addProperty("credits", course.getCredits());
                    attJson.addProperty("grade", Integer.toString(att.getGrade()));
                    attJson.addProperty("showGrades", course.showsGrades());
                    attJson.addProperty("description", course.getDescription());
                    attainments.add(attJson);
                }
                studentJson.add("attainments", attainments);
            }
            students.add(studentJson);
        }
        try (Writer writer = Files.newBufferedWriter(Path.of(fileName))) {
            gson.toJson(students, writer);
        }
        return true;
    }
}
