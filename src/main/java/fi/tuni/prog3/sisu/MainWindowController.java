package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * A controller class for main window of the application.
 * @author Asma Jamil
 */
public class MainWindowController implements Initializable {
     @FXML
    private Label currStd;   
    @FXML
    private AnchorPane titleBar;
    @FXML
    private MenuBar menuBar;
    @FXML 
    private VBox degreeDetailsVBox;
    @FXML
    private Label degreeTitleLabel;
    @FXML
    private Label degreeCodeLabel;
    @FXML
    private Label degreeCreditsLabel;
    @FXML
    private Button degreeSelectBtn;
    @FXML
    private Label noDescriptionLabel;
    @FXML
    private WebView degreeDetailWebView; 
    @FXML 
    private VBox entityDetailsVBox;   
    @FXML
    private Label entityTitleLabel;
    @FXML
    private Label entityCodeLabel;
    @FXML
    private Label entityCreditsLabel;
    @FXML
    private Label noDescriptionLabel2;
    @FXML
    private Button additionalInfo;
    @FXML
    private VBox completeCourseVBox;
    @FXML
    private Label entitySubTitleLabel;
    @FXML 
    private ScrollPane coursesScrollPane;
    @FXML 
    private HBox coursesCompleteHBox;
    @FXML 
    private HBox singleCourseCompleteHBox;
    @FXML
    private VBox coursesVBox;
    @FXML 
    private CheckBox courseCheckBox;
    @FXML
    private ComboBox<Integer> gradeComboBox;
    @FXML
    private Label invalidGradeLabel;
    @FXML
    private Label selectGradeLabel2;
    @FXML
    private ComboBox<Integer> gradeComboBox2;
    @FXML
    private Label invalidGradeLabel2;
    @FXML
    private ListView<DegreeProgramme> degreesListView;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab degreesTab;
    @FXML
    private Tab structureTab;
    @FXML
    private TreeView<Entity> degreeTreeView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label degreeNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label stdNumberLabel;
    @FXML
    private Label startYearLabel;
    @FXML
    private Label endYearLabel;
    @FXML
    private Label gradeLabel;
    @FXML
    private Label creditsLabel;
    @FXML  
    private ProgressBar progressBar;
    @FXML
    private Label progressBarLabel;
    
    private ArrayList<DegreeProgramme> degreesList;
    private DegreeProgramme currentDegree;
    private ArrayList<Course> coursesInSelectedModule = new ArrayList<>();
    private ArrayList<Course>  completedCourses = new ArrayList<>();
    private ArrayList<Course>  coursesToAdd = new ArrayList<>();
    private ArrayList<Course>  coursesToDel = new ArrayList<>();
    TreeItem<Entity> currentTreeItem;
    private boolean hasDegreeSelected = false;
    private Image image;
    private ImageView iView;
    APIData api;
    StudentData std;
     
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        api = new APIData();
        std = new StudentData();
        iView = new ImageView();
        URL _url = getClass().getResource("completed.png");
        image = new Image(_url.toExternalForm());
        iView.setImage(image);
        iView.setFitHeight(18);
        iView.setFitWidth(18);       
        Student student = StudentData.getCurrent();
        currStd.setText(student.getName());
        ImageView imageView = new ImageView("file:arrow.png");
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        Menu menu = new Menu();
        menu.setGraphic(imageView);
        menu.setStyle("-fx-background-color: transparent;");
        menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: transparent;");
        menuBar.getMenus().add(menu);
        menuBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        MenuItem menuItem1 = new MenuItem("Sign Out");
        MenuItem menuItem2 = new MenuItem("Quit");
        menuItem1.setOnAction((ActionEvent event) -> {
            signOut(event);
        });
        menuItem2.setOnAction((ActionEvent event) -> {
            try {
                std.writeToFile("students.json");
                Platform.exit();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);
        menuBar.setLayoutX(960);
        menuBar.setLayoutY(13);
        titleBar.getChildren().add(menuBar);
        if (StudentData.getCurrent().getDegree() != null) {
            hasDegreeSelected = true;
            try {
                initDegreeProgramme();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        } else {
            structureTab.setDisable(true);
        }
        if(APIData.getDegrees().isEmpty()) { 
            try {
                api.buildDegreeList();
            } catch (IOException e) {
                System.err.println("An I/O error occurred while fetching degrees list form Sisu API.");
            }
        }
        degreesList = APIData.getDegrees();
        try {
            populateDegreesList();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        degreesTab.setOnSelectionChanged(e -> {
            try {
                populateDegreesList();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });        
    }
        
    /**
    * Populates the degreesListView and selects the current degree (if current degree exists).
    * @throws IOException 
    */
    public void populateDegreesList() throws IOException {
        int index = 0;
        int target = 0;
        for(DegreeProgramme d : degreesList) {
            degreesListView.getItems().add(d);
            if(StudentData.getCurrent().getDegree() != null) {
                if(StudentData.getCurrent().getDegree().compareTo(d) == 0) {
                    target = index;
                }
                index++;
            }
        } 
        if(StudentData.getCurrent().getDegree() != null) {
            degreesListView.getSelectionModel().select(target);
            degreesListView.getFocusModel().focus(target);
            degreesListView.scrollTo(target);
            viewDegreeDetails();
        }
    }
    
    /**
     * Handles the change in degree selection in degreesListView.
     * @param event
     * @throws IOException
     */
    public void degreeSelectionChanged(MouseEvent event) throws IOException {
        viewDegreeDetails();
    }
    
    /**
     * Gets information of the selected degree item from degreesListView and displays it.
     * @throws IOException 
     */
    public void viewDegreeDetails() throws IOException {
        WebEngine myEngine = degreeDetailWebView.getEngine();
        myEngine.loadContent(""); 
        DegreeProgramme selectedItem = degreesListView.getSelectionModel().getSelectedItem();
        currentDegree = api.fetchDegreeDetailsFromAPI(selectedItem.getId());
        degreeTitleLabel.setText(currentDegree.getName());
        degreeCodeLabel.setText(currentDegree.getCode());
        degreeCreditsLabel.setText(Integer.toString(currentDegree.getMinCredits()));     
        if (currentDegree.getDescription() == null) {
            noDescriptionLabel.setVisible(true);
            degreeDetailWebView.setVisible(false);    
        } else {  
            degreeDetailWebView.setVisible(true); 
            noDescriptionLabel.setVisible(false);
            myEngine.loadContent(currentDegree.getDescription());      
        }
        degreeDetailsVBox.setVisible(true);
        degreeSelectBtn.setOnAction((ActionEvent e) -> {
            try {
                initDegreeProgramme();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
    }
    
    /**
     * Recursively calls itself and closes all the module tree nodes.
     * @param item
     * @param expanded 
     */
    private void recurseTree(TreeItem<Entity> item, boolean expanded){
        for (var item2 : item.getChildren()){
            item2.setExpanded(expanded);
            recurseTree(item2, expanded);
        }
    }
    
    /**
     * Recursion to get the module tree of selected degree.
     * 
     */
    private TreeItem<Entity> getChdn(Entity module, TreeItem<Entity> node){
        TreeItem<Entity> newNode = new TreeItem<>();
        for (Entity rule : module.getRules()){
            newNode = new TreeItem<Entity>(rule);
            node.getChildren().add(getChdn(rule, newNode)); 
        }
        return node;         
    }
    
    /**
     * Landing method when entering the Structure of studies tab.
     * @throws IOException
     */
    public void initDegreeProgramme() throws IOException {
        if (hasDegreeSelected) {
            currentDegree = StudentData.getCurrent().getDegree();  
            hasDegreeSelected = false;       
        } 
        currentDegree.clearRules();
        currentDegree = api.createDegreeTree(currentDegree);
        StudentData.getCurrent().setDegree(currentDegree);
        boolean isNewTree;
        if (degreeTreeView.getRoot() == null){
            isNewTree = true;
        } else if (degreeTreeView.getRoot().getValue() != currentDegree){ 
            isNewTree = true;
        } else {
            isNewTree = false;
        }
        if (isNewTree){
            degreeTreeView.setRoot(null); 
            std.writeToFile("students.json");
            TreeItem<Entity> node = new TreeItem<Entity>(currentDegree);               
            degreeTreeView.setRoot(node);
            getChdn(currentDegree, node);
            recurseTree(degreeTreeView.getRoot(), false);
            degreeTreeView.getRoot().setExpanded(false);
        }
        entityDetailsVBox.setVisible(false);
        structureTab.setDisable(false);
        mainTabPane.getSelectionModel().select(structureTab);       
    }
    
    /**
     * Called when degree treeView selected item changes and displays the child modules of the newly selected tree item.
     * @param event 
     */
    public void treeItemSelectionChanged(MouseEvent event){
        coursesInSelectedModule.clear();
        entityDetailsVBox.setVisible(false);
        completeCourseVBox.setVisible(false);
        coursesScrollPane.setVisible(false);
        coursesCompleteHBox.setVisible(false);
        singleCourseCompleteHBox.setVisible(false);
        TreeItem<Entity> selectedTItem = degreeTreeView.getSelectionModel().getSelectedItem();
        currentTreeItem = selectedTItem;
        if (selectedTItem == null){
            selectedTItem = degreeTreeView.getRoot();
        } else {
            Entity selectedItem = selectedTItem.getValue();
            boolean isFreshModule = false;
            if (selectedItem.getCourses().isEmpty()){
                isFreshModule = true;
            }
            boolean hasCourses = false;
            for (var child : selectedTItem.getChildren()){
                if (child.getValue().isCourse()){
                    hasCourses = true;
                }
            }
            if (!hasCourses){
                for (URL url : selectedItem.getCourseURLs()){               
                    Course newCourse = api.createCourse(url);
                    coursesInSelectedModule.add(newCourse);
                    entitySubTitleLabel.setText("Mark the courses in this module as completed/incompleted");
                    showCourses();
                    coursesScrollPane.setVisible(true);
                    coursesCompleteHBox.setVisible(true);
                    completeCourseVBox.setVisible(true);
                        gradeComboBox.getItems().removeAll(gradeComboBox.getItems());
                        gradeComboBox.getItems().addAll(1, 2, 3, 4, 5);
                        gradeComboBox.setOnAction(e -> {
                            invalidGradeLabel.setVisible(false);
                        });
                        gradeComboBox.setVisible(true);
                    if (isFreshModule){
                        selectedItem.addCourse(newCourse);
                    } 
                    TreeItem<Entity> CTItem;
                    if (StudentData.getCurrent().isCourseCompleted(newCourse)) {
                        ImageView nView = new ImageView(image);
                        nView.setFitHeight(16);
                        nView.setFitWidth(16);
                        CTItem = new TreeItem<Entity>(newCourse, nView);
                        completedCourses.add(newCourse);
                    }
                    else{
                        CTItem = new TreeItem<Entity>(newCourse);
                    }
                    selectedTItem.getChildren().add(CTItem);
                    selectedTItem.setExpanded(true);   
                }        
            } 
            else {
                for (URL url : selectedItem.getCourseURLs()){               
                    Course newCourse = api.createCourse(url);
                    coursesInSelectedModule.add(newCourse);
                    entitySubTitleLabel.setText("Mark the courses in this module as completed/incompleted");
                    showCourses();
                    coursesScrollPane.setVisible(true);
                    coursesCompleteHBox.setVisible(true);
                    completeCourseVBox.setVisible(true);
                        gradeComboBox.setVisible(true);
                        gradeComboBox.getItems().removeAll(gradeComboBox.getItems());
                        gradeComboBox.getItems().addAll(1, 2, 3, 4, 5);
                        gradeComboBox.setOnAction(e -> {
                            invalidGradeLabel.setVisible(false);
                        });
                }                
            }            
            TreeItem<Entity> spareItem = new TreeItem<Entity>();
            spareItem.getChildren().addAll(selectedTItem.getChildren().sorted((l,r) -> l.getValue().compareTo(r.getValue())));
            selectedTItem.getChildren().clear();
            selectedTItem.getChildren().addAll(spareItem.getChildren());
            if (selectedItem.getDescription() == null){
                noDescriptionLabel2.setVisible(true);
                additionalInfo.setVisible(false);
            } 
            else {
                noDescriptionLabel2.setVisible(false);
                additionalInfo.setVisible(true);
            }
            if (selectedItem.getName() != null){
                entityTitleLabel.setText(selectedItem.getName());
                additionalInfo.setOnAction((ActionEvent e) -> {
                    Dialog<String> dialog = new Dialog<String>();
                    dialog.setTitle(selectedItem.getName());
                    ButtonType btn = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    WebView content = new WebView();
                    content.getEngine().loadContent(selectedItem.getDescription());
                    dialog.setHeight(400);
                    dialog.getDialogPane().setContent(content);
                    dialog.getDialogPane().getButtonTypes().add(btn);
                    dialog.showAndWait();
                });
            }
            if (selectedItem.isCourse()) {
                entitySubTitleLabel.setText("Mark this course as completed/incompleted");
                Course tempCourse = (Course) selectedItem;
                entityCodeLabel.setText(tempCourse.getCode());
                entityCreditsLabel.setText(Integer.toString(tempCourse.getCredits()));
                entityDetailsVBox.setVisible(true);
                completeCourseVBox.setVisible(true);
                singleCourseCompleteHBox.setVisible(true);   
                coursesCompleteHBox.setVisible(false);
                if (tempCourse.showsGrades()) {
                    gradeComboBox2.setVisible(true);
                    gradeComboBox2.getItems().removeAll(gradeComboBox2.getItems());
                    gradeComboBox2.getItems().addAll(1, 2, 3, 4, 5);
                    gradeComboBox2.setOnAction(e -> {
                        invalidGradeLabel2.setVisible(false);
                    });
                    selectGradeLabel2.setVisible(true);
                } else {
                    gradeComboBox2.setVisible(false);
                    selectGradeLabel2.setVisible(false);
                }
                if (StudentData.getCurrent().isCourseCompleted(tempCourse)) {
                    courseCheckBox.setSelected(true);
                    if (tempCourse.showsGrades())
                        gradeComboBox2.setValue(StudentData.getCurrent().getGrade(tempCourse));
                } else {
                    courseCheckBox.setSelected(false);
                    if (tempCourse.showsGrades())
                        gradeComboBox2.setPromptText("-- SELECT GRADE --");
                }              
            }
            else if (selectedItem.isStudy()){
                StudyModule temp = (StudyModule) selectedItem;
                entityCodeLabel.setText(temp.getCode());
                entityCreditsLabel.setText(Integer.toString(temp.getMinCredits()));  
            }            
            else if (selectedItem.isDegree()){
                DegreeProgramme temp = (DegreeProgramme) selectedItem;
                entityCodeLabel.setText(temp.getCode());
                entityCreditsLabel.setText(Integer.toString(temp.getMinCredits())); 
            }      
            entityDetailsVBox.setVisible(true);
        } 
    }
    
    /**
     * Saves the Course progress.
     * @param event
     */
    public void saveProgress(MouseEvent event) throws IOException{
        Object gradeObject = gradeComboBox2.getValue();
        int grade = -1;
        if (!gradeComboBox.isVisible()) {
            grade = 0;
        } else {
            if (gradeObject != null){
                grade = (int) gradeObject;
            }
        }
        boolean completed = courseCheckBox.isSelected();
        TreeItem<Entity> selectedTItem =  degreeTreeView.getSelectionModel().getSelectedItem();
        Entity selectedItem = selectedTItem.getValue();
        Course selectedCourse = (Course) selectedItem;
        String result = StudentData.getCurrent().updateAttainment(selectedCourse, grade, completed);     
        if (result.equals("Course(s) marked as incompleted") ||
            result.equals("Course(s) marked as completed") ||
            result.equals("Course(s) progress updated")){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Toast.makeText(stage, result, 2500, 400, 400);
            if (completed) {
                ImageView nView = new ImageView(image);
                nView.setFitHeight(20);
                nView.setFitWidth(20);
                selectedTItem.setGraphic(nView);
                if(!completedCourses.contains(selectedCourse))
                    completedCourses.add(selectedCourse);
            } else {
                selectedTItem.setGraphic(null);
                if(completedCourses.contains(selectedCourse))
                    completedCourses.remove(selectedCourse);
            }
        } else {
            invalidGradeLabel2.setVisible(true);
        }
        updateProgressBar();
        std.writeToFile("students.json");
    }
    
    /**
     * Saves multiple courses progress.
     * @param event
     * @throws IOException 
     */
    public void saveMutlipleCoursesProgress(MouseEvent event) throws IOException {
        int grade = -1;
        boolean allOkay = true;
        Object gradeObject = gradeComboBox.getValue();
        if (gradeObject != null){
            grade = (int) gradeObject;
        }
        for(Course course : coursesToAdd) {
            String result = StudentData.getCurrent().updateAttainment(course, grade, true); 
            if (result.equals("Invalid grade")) {
                invalidGradeLabel.setVisible(true);
                allOkay = false;
            }
            if(!completedCourses.contains(course))
                completedCourses.add(course);
        }
        for(Course course : coursesToDel) {
            StudentData.getCurrent().updateAttainment(course, grade, false); 
            if(completedCourses.contains(course))
                completedCourses.remove(course);
        }
        if(allOkay) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Toast.makeText(stage, "Course(s) progress saved successfully", 2500, 400, 400);
            for(TreeItem<Entity> item : currentTreeItem.getChildren()) {
                for(Course course : coursesToDel) {
                    if(course.toString().equals(item.getValue().toString()))
                        item.setGraphic(null);
                }
                 for(Course course : coursesToAdd) {
                    if(course.toString().equals(item.getValue().toString())) {
                        ImageView nView = new ImageView(image);
                        nView.setFitHeight(18);
                        nView.setFitWidth(18);
                        item.setGraphic(nView); 
                    } 
                 }
            } 
            coursesToDel.clear();
            coursesToAdd.clear();
            updateProgressBar();
            std.writeToFile("students.json");
        }
    }
    
    /**
     * Creates check boxes list of courses under a module.
     */
    private void showCourses() {
        coursesVBox.getChildren().clear(); 
        coursesInSelectedModule.forEach( course -> {
            String courseInfo = course.toString();
            CheckBox newCheckbox = new CheckBox(courseInfo);   
            if (StudentData.getCurrent().isCourseCompleted(course)) {
                newCheckbox.setSelected(true);
            }
            newCheckbox.setOnAction( (ActionEvent e) -> {
                if(!newCheckbox.isSelected() && coursesToAdd.contains(course))
                    coursesToAdd.remove(course);
                if (newCheckbox.isSelected() && !completedCourses.contains(course))
                    coursesToAdd.add(course);
                if (!newCheckbox.isSelected() && completedCourses.contains(course))
                    coursesToDel.add(course);
                if(newCheckbox.isSelected()&& completedCourses.contains(course))
                    coursesToDel.remove(course);
                if (!newCheckbox.isSelected() && !completedCourses.contains(course))
                    coursesToAdd.remove(course);
            });     
            coursesVBox.getChildren().add(newCheckbox);
        });
        
    }
    
    /**
     * Sets up the Profile details, degree title and average/total credits views.
     */
    public void populateProfileDetails(){
        nameLabel.setText(StudentData.getCurrent().getName());
        stdNumberLabel.setText(StudentData.getCurrent().getStudentNumber());
        startYearLabel.setText(Integer.toString(StudentData.getCurrent().getStartYear()));
        endYearLabel.setText(Integer.toString(StudentData.getCurrent().getEndYear()));
        emailLabel.setText(StudentData.getCurrent().getName());
        if(StudentData.getCurrent().getDegree() == null) {
            degreeNameLabel.setText("-");
            creditsLabel.setText("0 / 0 cr");
            gradeLabel.setText("0");
            progressBarLabel.setText("0% completed");
        } else {
            degreeNameLabel.setText(StudentData.getCurrent().getDegree().getName());
            int totalCredits = StudentData.getCurrent().getDegree().getMinCredits();       
            int currentCredits = StudentData.getCurrent().getCurrentCredits();
            String avgGrade = String.format("%.1f", StudentData.getCurrent().getAverageGrade());
            creditsLabel.setText(Integer.toString(currentCredits) + " / " + Integer.toString(totalCredits) + " cr");
            if (avgGrade.equals("NaN"))
                avgGrade = "0";
            gradeLabel.setText(avgGrade);
            double percentage = (( (double) currentCredits)/ ( (double) totalCredits));
            progressBar.setProgress(percentage);
            String progressText = String.format("%.2f %% completed", percentage*100);
            progressBarLabel.setText(progressText);
        }
        
    }
    
    /**
     * Updates the progress bar, credit label and completion percentage label to match the current situation. 
     */
    @FXML
    private void updateProgressBar() {
        if (StudentData.getCurrent().getDegree() != null) {     
            int minCredits = StudentData.getCurrent().getDegree().getMinCredits();
            int currCredits = StudentData.getCurrent().getCurrentCredits();
            creditsLabel.setText(currCredits + " / " + minCredits + " cr" );
            double percentage = (( (double) currCredits)/ ( (double) minCredits));
            progressBar.setProgress(percentage);
            String progressText = String.format("%.2f %% completed", percentage*100);
            progressBarLabel.setText(progressText);
            String avgGrade = String.format("%.1f", StudentData.getCurrent().getAverageGrade());
            if (avgGrade.equals("NaN"))
                avgGrade = "0";
            gradeLabel.setText(avgGrade);
        }
    }
    
    /**
     * Opens a new stage to display update profile form.
     * @throws IOException 
     */
    public void openUpdateProfileForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Sisu.class.getResource("updateStudent.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Update Profile");
        stage.show();
    }
    
    /**
     * Opens dialog box to get confirmation before deleting the student profile.
     * @param event 
     */
    public void deleteProfile(ActionEvent event) {
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Delete Profile");
        ButtonType btn = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
        ButtonType btn2 = new ButtonType("Yes", ButtonBar.ButtonData.APPLY);
        dialog.setContentText("Deleted profile can not be recovered. Are you sure you want to delete your profile?");
        dialog.getDialogPane().getButtonTypes().add(btn);
        dialog.getDialogPane().getButtonTypes().add(btn2);
        dialog.getDialogPane().lookupButton(btn2).addEventFilter(ActionEvent.ACTION, ev -> {
            try {
                StudentData.removeStudent(StudentData.getCurrent().getStudentNumber());
                std.writeToFile("students.json");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Sisu.changeScene(stage, "start.fxml");
            } catch (IOException e) {
                System.err.println(e);
            }
            event.consume();
        });
        dialog.showAndWait();
    }
    
    /**
     * Logs out the student, saves information and returns to Sign in page.
     * @param event
     */
    public void signOut(ActionEvent event){
        try {
            std.writeToFile("students.json");
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Sisu.changeScene(stage, "start.fxml");
        } catch (IOException e){
            System.err.println("Error reading start.fxml" + e.getMessage());  

        }
    }
}
