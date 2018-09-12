package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTabPane;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserNewController implements Initializable {
	
	

    @FXML
    private VBox box;
    
    @FXML
    private JFXTabPane tabPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ssnField;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private ChoiceBox<String> titleChoiceBox;
    
    @FXML
    private ChoiceBox<String> specialitiesBox;

    @FXML
    private ChoiceBox<String> positionChoiceBox;

    @FXML
    private ChoiceBox<String> levelChoiceBox;

    @FXML
    private ChoiceBox<String> sexChoiceBox;

    @FXML
    private DatePicker birthPicker;

    @FXML
    private TextField regionField;

    @FXML
    private DatePicker dateJoinPartyPicker;

    @FXML
    private DatePicker timeStartWorkPicker;

    @FXML
    private DatePicker certifactionDatePicker;

    @FXML
    private TextField certifactionNumField;

    //TODO: remove related
    //@FXML
    //private TextField technicalPositionField;

    @FXML
    private DatePicker dateReceivedTPPicker;

    @FXML
    private DatePicker hireDatePicker;

    @FXML
    private DatePicker N0Picker;

    @FXML
    private DatePicker N1Picker;

    @FXML
    private DatePicker N2Picker;

    @FXML
    private DatePicker N3Picker;

    @FXML
    private DatePicker N4Picker;

    @FXML
    private DatePicker N5Picker;

    @FXML
    private TextField primaryEdField;

    @FXML
    private DatePicker primaryEdTimePicker;

    @FXML
    private TextField primaryEdSchoolField;

    @FXML
    private TextField highestEdField;

    @FXML
    private DatePicker highestEdTimePicker;

    @FXML
    private TextField highestEdSchoolField;
    
    HashMap<String, String> user;
    DBhelper dbHelper;
    ArrayList<String> departmentList = new ArrayList<String>();
    ArrayList<HashMap<String, String>> departmentHashMaplist;
    Loader loader = new Loader();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		user = new HashMap<String, String>();
		dbHelper = new DBhelper();
		setupChoiceBox();
		setupDatePicker();
	}
    
    @FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
    
    private void setupDatePicker() {
    		loader.setupDatePicker(birthPicker);
    		loader.setupDatePicker(dateJoinPartyPicker);
    		loader.setupDatePicker(timeStartWorkPicker);
    		loader.setupDatePicker(certifactionDatePicker);
    		loader.setupDatePicker(hireDatePicker);
    		loader.setupDatePicker(dateReceivedTPPicker);
    		loader.setupDatePicker(N0Picker);
    		loader.setupDatePicker(N1Picker);
    		loader.setupDatePicker(N2Picker);
    		loader.setupDatePicker(N3Picker);
    		loader.setupDatePicker(N4Picker);
    		loader.setupDatePicker(N5Picker);
    		loader.setupDatePicker(primaryEdTimePicker);
    		loader.setupDatePicker(highestEdTimePicker);
    }

    @FXML
    void addButton() {
    		if(validate()) {
    			HashMap<String, String> map = this.getHashMap();
    			if(dbHelper.insertUser(map)) {
    				loader.loadVBox(box, "/View/UserList.fxml");
    			}else {
    				PopupWindow popUP = new PopupWindow();
        			popUP.alertWindow("添加新用户失败", "此工号已被使用");
    			}
    		}else {
    			PopupWindow popUP = new PopupWindow();
    			popUP.alertWindow("出错啦。。。", "姓名,工号,专业不能为空");
    		}
    }

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML
    void userListButton() {
    		loader.loadVBox(box, "/View/UserList.fxml");
    }

    @FXML
    void nextPane() {
    		tabPane.getSelectionModel().selectNext();
    }
    
    private void setupChoiceBox() {
    		//setup department ChoiceBox
    		getDepartment();
    		departmentChoiceBox.getItems().addAll(departmentList);
    		
    		//setup title ChoiceBox
    		String[] titleList = {"初级职称", "中级职称", "正高级职称", "高级职称"};
    		titleChoiceBox.getItems().addAll(titleList);
    		
    		//setup position ChoiceBox
    		String[] positionList = {"无", "副护士长", "护士长", "科护长", "护理部副主任", "护理部主任"};
    		positionChoiceBox.getItems().addAll(positionList);
    		
    		//setup level ChoiceBox
    		String[] levelList = {"N0", "N1", "N2", "N3", "N4", "N5"};
    		levelChoiceBox.getItems().addAll(levelList);
    		
    		//setup Sex ChoiceBox
    		String[] sexList = {"男", "女", "其他"};
    		sexChoiceBox.getItems().addAll(sexList);
    		
    		String[] specialities = {"医疗","护理","医技","药学","其他"};
    		specialitiesBox.getItems().addAll(specialities);
    }
    
    //convert user input into HashMap
    //used this function in addButton function
    private HashMap<String, String> getHashMap() {
    		HashMap<String, String> output = new HashMap<String, String>();
    		output.put("ssn", ssnField.getText());
    		output.put("name", nameField.getText());
    		if(departmentChoiceBox.getSelectionModel().getSelectedItem()!=null) {
    			output.put("department", departmentChoiceBox.getSelectionModel().getSelectedItem());
    		}
    		if(specialitiesBox.getSelectionModel().getSelectedItem()!=null) {
    			output.put("branch", specialitiesBox.getSelectionModel().getSelectedItem());
    		}
    		//output.put("department", departmentChoiceBox.getSelectionModel().getSelectedItem());
    		if(positionChoiceBox.getSelectionModel().getSelectedItem() != null) {
    			output.put("position", positionChoiceBox.getSelectionModel().getSelectedItem());
    		}
    		//output.put("position", positionChoiceBox.getSelectionModel().getSelectedItem());
    		if(titleChoiceBox.getSelectionModel().getSelectedItem()!=null) {
    			output.put("title", titleChoiceBox.getSelectionModel().getSelectedItem());
    		}
    		//output.put("title", titleChoiceBox.getSelectionModel().getSelectedItem());
    		if(levelChoiceBox.getSelectionModel().getSelectedItem()!=null) {
    			output.put("level", levelChoiceBox.getSelectionModel().getSelectedItem());
    		}
    		//output.put("level", levelChoiceBox.getSelectionModel().getSelectedItem());
    		if(birthPicker.getValue()!=null) {
    			output.put("birth", birthPicker.getValue().toString());
    		}    		
    		if(sexChoiceBox.getSelectionModel().getSelectedItem()!=null) {
    			output.put("sex", sexChoiceBox.getSelectionModel().getSelectedItem());
    		}
    		//output.put("sex", sexChoiceBox.getSelectionModel().getSelectedItem());
    		if(regionField.getText() != null) {
    			output.put("region", regionField.getText());
    		}
    		//output.put("region", regionField.getText());
    		if(dateJoinPartyPicker.getValue()!=null) {
        		output.put("dateJoinParty", dateJoinPartyPicker.getValue().toString());
    		}    		
    		if(timeStartWorkPicker.getValue()!=null) {
        		output.put("timeStartWork", timeStartWorkPicker.getValue().toString());
    		}
    		if(certifactionDatePicker.getValue()!=null) {
        		output.put("certifactionDate", certifactionDatePicker.getValue().toString());
    		}
    		if(certifactionNumField.getText()!=null) {
    			output.put("certifactionNum", certifactionNumField.getText());
    		}
    		//output.put("certifactionNum", certifactionNumField.getText());
    		//output.put("technicalPosition", technicalPositionField.getText());
    		if(dateReceivedTPPicker.getValue()!=null) {
        		output.put("dateReceivedTP", dateReceivedTPPicker.getValue().toString());
    		}
    		if(hireDatePicker.getValue()!=null) {
    			output.put("hireDate", hireDatePicker.getValue().toString());
    		}
    		if(N0Picker.getValue()!=null) {
    			output.put("N0", N0Picker.getValue().toString());
    		}
    		if(N1Picker.getValue()!=null) {
    			output.put("N1", N1Picker.getValue().toString());
    		}
    		if(N2Picker.getValue()!=null) {
    			output.put("N2", N2Picker.getValue().toString());
    		}
    		if(N3Picker.getValue()!=null) {
    			output.put("N3", N3Picker.getValue().toString());
    		}
    		if(N4Picker.getValue()!=null) {
    			output.put("N4", N4Picker.getValue().toString());
    		}
    		if(N5Picker.getValue()!=null) {
    			output.put("N5", N5Picker.getValue().toString());
    		}
    		output.put("primaryEd", primaryEdField.getText());
    		if(primaryEdTimePicker.getValue()!=null) {
        		output.put("primaryEdTime", primaryEdTimePicker.getValue().toString());
    		}
    		output.put("primaryEdSchool", primaryEdSchoolField.getText());
    		output.put("highestEd", highestEdField.getText());
    		if(highestEdTimePicker.getValue()!=null) {
    			output.put("highestEdTime", highestEdTimePicker.getValue().toString());
    		}
    		output.put("highestEdSchool", highestEdSchoolField.getText());
    		return output;
    }
    
    private void getDepartment() {
    		String[] columns = {"id", "departmentName"};
    		departmentHashMaplist = dbHelper.getList("hospital_department", columns);  
    		departmentList.add("无");
    		for(int i = 0; i< departmentHashMaplist.size(); i++) {
    			departmentList.add(departmentHashMaplist.get(i).get("departmentName"));
    		}
    }
    
    private boolean validate() {
    		
    		if(nameField.getText().trim().isEmpty() || ssnField.getText().trim().isEmpty() || 
    				specialitiesBox.getSelectionModel().getSelectedItem() == null) {
    			return false;
    		}
    		return true;
    }


	

}


