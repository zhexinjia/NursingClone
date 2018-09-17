package Controller;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import Model.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class TestOfflineSectionNewController implements Initializable {
	
	@FXML JFXTextField locationField;
	@FXML JFXComboBox<Integer> quantityPicker;
	@FXML JFXDatePicker datePicker;
	@FXML JFXTimePicker startTimePicker;
	@FXML JFXTimePicker endTimePicker;
	
	@FXML JFXTextField nameField1;
	@FXML JFXTextField nameField2;
	@FXML JFXTextField nameField3;
	@FXML JFXTextField nameField4;

	@FXML VBox box;
	
	public String userID_1;
	public String userID_2;
	public String userID_3;
	public String userID_4;
	
	//private ArrayList<HashMap<String, String>> list;
	private ObservableList<HashMap<String, String>> req_list;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	private String branch;
	PopupWindow popUP = new PopupWindow();
	private HashMap<String, String> selectedOfflineTest;
	
	public ArrayList<HashMap<String, String>> selectedUser1;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupChoiceBox();
		selectedOfflineTest = TestOfflineController.selectedTest;
		req_list = FXCollections.observableArrayList();
		branch = LoginController.branch;
		
		//getList();

	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML
    void TestOfflineListButton() {
    		loader.loadVBox(box, "/View/TestOffline.fxml");
    }
    
    @FXML void offlineSectionClicked() {
    		loader.loadVBox(box, "/View/TestOfflineSection.fxml");
    }
    
    @FXML void createButton() {
    		if(validate()) {
    			insertDB();
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "档期设置选项不能为空。");
    		}
    }

    @FXML void chooseTeacherClicked1() throws IOException {  	
    		putStage("1");
    }
    
    @FXML void chooseTeacherClicked2() throws IOException {	
    		putStage("2");
    }
    
    @FXML void chooseTeacherClicked3() throws IOException {
    		putStage("3");
    }
    
    @FXML void chooseTeacherClicked4() throws IOException {
    		putStage("4");
    }
    
    @FXML void deleteTeacherClicked1() {
    		nameField1.clear();
    		userID_1 = null;
    }
 
    @FXML void deleteTeacherClicked2() {
    		nameField2.clear();
    		userID_2 = null;
    }
 
    @FXML void deleteTeacherClicked3() {
    		nameField3.clear();
    		userID_3 = null;
    }
    
    @FXML void deleteTeacherClicked4() {
     	nameField4.clear();
     	userID_4 = null;
    }
    
    
    
    private void setupChoiceBox() {
    		Integer[] array = new Integer[101];
		Arrays.setAll(array, i -> i);
		quantityPicker.getItems().setAll(array);
    }
    
    
    public void putStage(String buttonNum) throws IOException {
    	Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/TestOfflineChooseUser.fxml"));
		
		stage.setScene(new Scene(root));
		stage.show();
		
		StageManager.STAGE.put("choosenUser", stage);
		StageManager.Button.put("button", buttonNum);
		StageManager.CONTROLLER.put("sectionControl", this);
		
    }

    
    private boolean validate() {
    		if(locationField.getText().trim().isEmpty()) {
    			return false;
    		}
    		if(datePicker.getValue()==null) {
    			return false;
    		}
    		if (startTimePicker.getValue() == null || endTimePicker.getValue() == null) {
    			return false;
    		}

    		return true;
    }
    
    
    private void insertDB() {
    
    		String exam_id = selectedOfflineTest.get("id");
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("offlineexam_id", exam_id);
    		map.put("location", locationField.getText());
    		map.put("quantity", quantityPicker.getValue().toString());
    		map.put("date", datePicker.getValue().toString());
    		map.put("start_time", startTimePicker.getValue().toString());
    		map.put("end_time", endTimePicker.getValue().toString());
    		map.put("advisor1", userID_1);
    		map.put("advisor2", userID_2);
    		map.put("advisor3", userID_3);
    		map.put("advisor4", userID_4);
    		
    		if(dbHelper.insert(map, "offline_section")) {
    			loader.loadVBox(box, "/View/TestOfflineSection.fxml");
    		}
    	
    }
    
    private void getList() {
    	
    		//list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {branch}, "question_bank");
   
    }
    
    

	

}

