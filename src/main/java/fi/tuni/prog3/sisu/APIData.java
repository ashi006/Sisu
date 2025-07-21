package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * A class to handle API requests and responses.
 *
 */
public class APIData implements iAPI {
    private static String lang = "en";
    private static HashMap<String, Course> coursesList = new HashMap<>();
    private static HashMap<String, DegreeModule> modulesList = new HashMap<>();
    private static ArrayList<DegreeProgramme> degreesList = new ArrayList<>();

    public final String DEGREE_LIST_URL = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
    public final String GET_MODULE_BY_ID_URL = "https://sis-tuni.funidata.fi/kori/api/modules/";
    public final String GET_MODULE_BY_GROUP_ID_URL = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?&universityId=tuni-university-root-id&groupId=";
    public final String GET_COURSE_BY_GROUP_ID_URL = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?&universityId=tuni-university-root-id&groupId=";

    /**
     * Returns the URL that is used to fetch course information.
     * @return The URL to fetch course information from.
     */
    public String getCourseURL() {
        return GET_COURSE_BY_GROUP_ID_URL;
    }

    /**
     * Sends request to Sisu API and returns the response as Json object
     * @param urlString The URL to send request to
     * @return Returns the response body as Json object
     */
    @Override
    public JsonObject getJsonObjectFromApi(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            Scanner scan = new Scanner(url.openStream());
            String temp = scan.nextLine();
            scan.close();
            http.disconnect();
            JsonElement root =  JsonParser.parseString(temp);
            if(root instanceof JsonArray)
                return ((JsonArray) root).get(0).getAsJsonObject();
            else
                return root.getAsJsonObject();
        } catch (MalformedURLException ex) {
            System.err.println(ex);
            return null;
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    /**
     * Returns the list of degrees.
     * @return list of degrees.
     */
    public static ArrayList<DegreeProgramme> getDegrees(){
        return degreesList;
    }

    /**
     * Return the given attribute in English language.
     * @param root The json object to use to search for the given attribute.
     * @param attribute The given attribute to be searched.
     * @return The attribute in English language.
     */
    private String getAttributeInRequiredLanguage(JsonObject root, String attribute){
        if (root.has(attribute)){
            if (root.getAsJsonObject().get(attribute).isJsonObject()){
                if (root.getAsJsonObject().get(attribute).getAsJsonObject().has(lang)){
                    attribute = root.getAsJsonObject().get(attribute).getAsJsonObject().get(lang).getAsString();
                } else {  
                    Set<String> keys = root.getAsJsonObject().get(attribute).getAsJsonObject().keySet();
                    attribute = root.getAsJsonObject().get(attribute).getAsJsonObject().get(keys.iterator().next()).getAsString();
                }
            } else {
                if (root.getAsJsonObject().get(attribute).isJsonNull()){
                    attribute = null;
                } else{
                    attribute = root.getAsJsonObject().get(attribute).getAsString();
                }
            }
            return attribute;
        }
        else{
            return null;
        }
    }

    /**
     * Populates the basic details of a DegreeModule.
     * @param module The module whose details to be set
     * @param root The json object to get details from.
     */
    private void populateBasicDetails(DegreeModule module, JsonObject root) {
        String id = root.getAsJsonObject().get("id").getAsString();
        String name = getAttributeInRequiredLanguage(root.getAsJsonObject(), "name");
        String groupId = root.getAsJsonObject().get("groupId").getAsString();
        int minCredits = 0;
        if(root.has("targetCredits"))
            minCredits = root.getAsJsonObject().get("targetCredits").getAsJsonObject().get("min").getAsInt();
        else
            minCredits = root.getAsJsonObject().get("credits").getAsJsonObject().get("min").getAsInt();
        module.setName(name);
        module.setGroupId(groupId);
        module.setId(id);
        module.setMinCredits(minCredits);
    }

    /**
     * Populates the details of a DegreeProgramme by calling Sisu API.
     * @param degreeID The Id of the DegreeProgramme to use to call the Sisu API.
     * @return The DegreeProgramme objects with populated details.
     * @throws IOException
     */
    public DegreeProgramme fetchDegreeDetailsFromAPI(String degreeID) throws IOException{
        JsonObject root = getJsonObjectFromApi(GET_MODULE_BY_ID_URL + degreeID).getAsJsonObject();
        int credits = root.get("targetCredits").getAsJsonObject().get("min").getAsInt();
        DegreeProgramme degree = new DegreeProgramme(degreeID);
        degree.setMinCredits(credits);
        String code = root.get("code").getAsString();
        degree.setCode(code);
        populateBasicDetails(degree, root);
        String description = getAttributeInRequiredLanguage(root, "learningOutcomes");
        degree.setDescription(description);
        degree.setSource(new URL(GET_MODULE_BY_ID_URL + degreeID));
        return degree;
    }

    /**
     * Constructs DegreeProgramme objects of all degrees from the Sisu API of year 2022 and gathers them in a list.
     * @throws IOException
     */
    public void buildDegreeList() throws IOException{
        JsonObject root = getJsonObjectFromApi(DEGREE_LIST_URL);
        JsonArray degrees = root.get("searchResults").getAsJsonArray();
        for (JsonElement degree: degrees){
            String id = degree.getAsJsonObject().get("id").getAsString();
            DegreeProgramme newDegree = new DegreeProgramme(id);
            populateBasicDetails(newDegree, degree.getAsJsonObject());
            String code = degree.getAsJsonObject().get("code").getAsString();
            String source = GET_MODULE_BY_ID_URL + newDegree.getId();
            URL url_deg = new URL(source);
            newDegree.setSource(url_deg);
            newDegree.setCode(code);
            degreesList.add(newDegree);
        }
    }

    /**
     * Creates a list of rules based on the types CompositeRule, ModuleRule, CourseUnitRule, CreditsRule.
     * @param ruleRoot The json object to look into for the rules.
     * @param ruleList The rules list to be updated by adding rules.
     * @return A HashMap of rules list
     */
    private HashMap<URL, String> getRules(JsonElement ruleRoot, HashMap<URL, String> ruleList){
        try{
            String ruleType = ruleRoot.getAsJsonObject().get("type").getAsString();
            if (ruleType.equals("ModuleRule")){
                String source = GET_MODULE_BY_GROUP_ID_URL + ruleRoot.getAsJsonObject().get("moduleGroupId").getAsString();
                URL url = new URL(source);
                ruleList.put(url, "Module");
            }
            else if (ruleType.equals("CourseUnitRule")){
                String source = GET_COURSE_BY_GROUP_ID_URL + ruleRoot.getAsJsonObject().get("courseUnitGroupId").getAsString();
                URL url = new URL(source);
                ruleList.put(url, "Course");
            }
            else if(ruleType.equals("CompositeRule")){
                JsonArray ruleArray = ruleRoot.getAsJsonObject().get("rules").getAsJsonArray();
                for (JsonElement thisRule : ruleArray){
                    getRules(thisRule, ruleList);
                }
            }
            else if (ruleType.equals("CreditsRule")){
                getRules(ruleRoot.getAsJsonObject().get("rule"), ruleList);
            }
            return ruleList;
        }
        catch (MalformedURLException e){
            System.err.println("Error loading mainlist Json file" + e.getMessage());
        }
        return null;
    }

    /** Creates Course object from URL given as parameter.
     * @param url The URL to be used to query course data from Sisu API.
     * @return The created course object.
     */
    public Course createCourse(URL url) {
        if (coursesList.containsKey(url)){
            return coursesList.get(url);
        }
        JsonObject root = getJsonObjectFromApi(url.toString());
        String id = root.getAsJsonObject().get("groupId").getAsString();
        String name = getAttributeInRequiredLanguage(root.getAsJsonObject(), "name");
        String code = "";
        if (root.getAsJsonObject().get("code").isJsonNull()){
            code = null;
        }
        else {
            code = root.getAsJsonObject().get("code").getAsString();
        }
        int credits_max = 0;
        JsonElement x = root.getAsJsonObject().get("credits").getAsJsonObject().get("max");
        if (!x.isJsonNull()){
            credits_max = root.getAsJsonObject().get("credits").getAsJsonObject().get("max").getAsInt();
        }
        String content = getAttributeInRequiredLanguage(root.getAsJsonObject(), "content");
        Course returnCourse = new Course();
        returnCourse.setId(id);
        returnCourse.setCode(code);
        returnCourse.setCredits(credits_max);
        returnCourse.setDescription(content);
        returnCourse.setSource(url);
        returnCourse.setName(name);
        if (root.getAsJsonObject().get("gradeScaleId") != null){
            if (root.getAsJsonObject().get("gradeScaleId").getAsString().equals("sis-hyl-hyv")){
                returnCourse.setShowGrades(false);
            }
            else if (root.getAsJsonObject().get("gradeScaleId").getAsString().equals("sis-0-5")){
                returnCourse.setShowGrades(true);
            }
        }
        coursesList.put(url.toString(), returnCourse);
        return returnCourse;
    }

    /**
     * Fills DegreeProgramme module object recursively from child to child.
     * @param degree The DegreeProgramme module to create tree structure for.
     * @return The completed tree of objects.
     */
    public DegreeProgramme createDegreeTree(DegreeProgramme degree){
        URL url = degree.getSource();
        try{
            JsonObject root = getJsonObjectFromApi(url.toString());
            HashMap<URL, String> ruleList = new HashMap<>();
            getRules(root.get("rule"), ruleList);
            degree.setUrlRuleList(ruleList);
            for (Map.Entry<URL, String> entry : ruleList.entrySet()) {
                if (entry.getValue() == "Module")
                    degree.addRule(parseModuleData(entry.getKey()));
            }
            return degree;
        } catch(IOException e){
            System.err.println("Error building the degreetree" + e.getMessage());
        }
        return null;
    }

    /**
     * Recursive method to build tree of Module objects upwards from the URL of wanted module
     * @param url The URL to be used to get the module information from Sisu API.
     * @return The completed DegreeModule tree
     * @throws IOException
     */
    public DegreeModule parseModuleData(URL url) throws IOException{
        try {
            if(modulesList.containsKey(url)) {
                System.out.println(modulesList.get(url).getName());
                return modulesList.get(url);
            }
            JsonElement root = getJsonObjectFromApi(url.toString());
            if (root.isJsonArray() && (root.getAsJsonArray().size() == 1)){
                root = root.getAsJsonArray().get(0);
            }
            String type  = root.getAsJsonObject().get("type").getAsString();
            DegreeModule returnModule = null;
            if (type.equals("GroupingModule")) {
                GroupingModule thisModule = new GroupingModule();
                String name = getAttributeInRequiredLanguage(root.getAsJsonObject(), "name");
                thisModule.setName(name);
                String description = getAttributeInRequiredLanguage(root.getAsJsonObject().get("rule")
                        .getAsJsonObject(), "description");
                thisModule.setDescription(description);
                returnModule = thisModule;
                String groupId = root.getAsJsonObject().get("groupId").getAsString();
                returnModule.setGroupId(groupId);
            } else if (type.equals("StudyModule")) {
                StudyModule thisModule = new StudyModule();
                String code = root.getAsJsonObject().get("code").getAsString();
                thisModule.setCode(code);
                returnModule = thisModule;
                String description = getAttributeInRequiredLanguage(root.getAsJsonObject(), "contentDescription");
                if (description == null)
                    description = getAttributeInRequiredLanguage(root.getAsJsonObject(), "outcomes");
                returnModule.setDescription(description);
                populateBasicDetails(returnModule, root.getAsJsonObject());
            }
            returnModule.setSource(url);
            HashMap<URL, String> ruleList = new HashMap<>();
            getRules(root.getAsJsonObject().get("rule"), ruleList);
            for (Map.Entry<URL, String> entry : ruleList.entrySet()) {
                if (entry.getValue() == "Course")
                    returnModule.addCourseUrl(entry.getKey());
                else
                    returnModule.addRule(parseModuleData(entry.getKey()));
            }
            modulesList.put(url.toString(), returnModule);
            return returnModule;
        } catch (IOException e){
            System.err.println("Error building degree tree" + e.getMessage());
        }
        return null;
    }
}
