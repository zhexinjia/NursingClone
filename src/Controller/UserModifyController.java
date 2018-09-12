package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class UserModifyController implements Initializable {
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
    private ChoiceBox<String> specialitiesBox;

    @FXML
    private ChoiceBox<String> titleChoiceBox;

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
    
    //HashMap<String, String> userPrimary;
    //HashMap<String, String> userSub;
    HashMap<String, String>selectedUser;
    DBhelper dbHelper;
    ArrayList<String> departmentList = new ArrayList<String>();
    ArrayList<HashMap<String, String>> departmentHashMaplist;
    //String ssn;
    Loader loader = new Loader();
    String department; //用以检查department是否改变
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    		ssnField.setDisable(true);
    		dbHelper = new DBhelper();
		setupChoiceBox();
		selectedUser = UserlistController.selectedUser;
		department = selectedUser.get("department");
		loadPage();
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

    //update modified user
    @FXML
    void addButton() {
    		if(validate()) {
    			HashMap<String, String> map = getHashMap();
    			if (dbHelper.updateUser(map)) {
    				loader.loadVBox(box, "/View/UserList.fxml");
    			}
    			
    		}else {
    			PopupWindow popUP = new PopupWindow();
    			popUP.alertWindow("出错啦。。。", "姓名和工号不能为空");
    		}
    }

    //navigate to website
    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML
    void userListButton() {
    		loader.loadVBox(box, "/View/UserList.fxml");
    }

    //navigate to next tabPane page
    @FXML
    void nextPane() {
    		tabPane.getSelectionModel().selectNext();
    }
    
    //setup all choiceBox();
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
    		String[] sexList = {"男", "女"};
    		sexChoiceBox.getItems().addAll(sexList);
    		
    		String[] specialities = {"医疗","护理","医技","药学","其他"};
    		specialitiesBox.getItems().addAll(specialities);
    }
    
    //modify the HashMap, return modified HashMap
    /*
    private HashMap<String, String> getHashMap() {
    		HashMap<String, String> output = new HashMap<String, String>();
    		output.put("ssn", ssnField.getText());
    		output.put("name", nameField.getText());
    		//FIXME: if not using deaprtment_id in future, change it
    		output.put("department", departmentChoiceBox.getSelectionModel().getSelectedItem());
    		
    		output.put("position", positionChoiceBox.getSelectionModel().getSelectedItem());
    		output.put("title", titleChoiceBox.getSelectionModel().getSelectedItem());
    		output.put("level", levelChoiceBox.getSelectionModel().getSelectedItem());
    		if(birthPicker.getValue()!=null) {
    			output.put("birth", birthPicker.getValue().toString());
    		}    		
    		output.put("sex", sexChoiceBox.getSelectionModel().getSelectedItem());
    		output.put("region", regionField.getText());
    		if(dateJoinPartyPicker.getValue()!=null) {
        		output.put("dateJoinParty", dateJoinPartyPicker.getValue().toString());
    		}    		
    		if(timeStartWorkPicker.getValue()!=null) {
        		output.put("timeStartWork", timeStartWorkPicker.getValue().toString());
    		}
    		if(certifactionDatePicker.getValue()!=null) {
        		output.put("certifactionDate", certifactionDatePicker.getValue().toString());
    		}
    		output.put("certifactionNum", certifactionNumField.getText());
    		output.put("technicalPosition", technicalPositionField.getText());
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
    */
    private HashMap<String, String> getHashMap() {
		HashMap<String, String> output = new HashMap<String, String>();
		output.put("ssn", ssnField.getText());
		output.put("name", nameField.getText());
		if(departmentChoiceBox.getSelectionModel().getSelectedItem()!=null) {
			String departmentText = departmentChoiceBox.getSelectionModel().getSelectedItem();
			output.put("department", departmentText);
			if(!departmentText.equals(department)) {
				output.put("is_manager", "0");
			}
		}
		//output.put("department", departmentChoiceBox.getSelectionModel().getSelectedItem());
		if(positionChoiceBox.getSelectionModel().getSelectedItem() != null) {
			output.put("position", positionChoiceBox.getSelectionModel().getSelectedItem());
		}
		//output.put("position", positionChoiceBox.getSelectionModel().getSelectedItem());
		if(titleChoiceBox.getSelectionModel().getSelectedItem()!=null) {
			output.put("title", titleChoiceBox.getSelectionModel().getSelectedItem());
		}
		if(specialitiesBox.getSelectionModel().getSelectedItem()!=null) {
			output.put("branch", specialitiesBox.getSelectionModel().getSelectedItem());
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
    //get list of department
    private void getDepartment() {
    		String[] columns = {"id", "departmentName"};
    		departmentHashMaplist = dbHelper.getList("hospital_department", columns);  
    		departmentList.add("无");
    		for(int i = 0; i< departmentHashMaplist.size(); i++) {
    			departmentList.add(departmentHashMaplist.get(i).get("departmentName"));
    		}
    }
    
    //check if name and ssn are entered
    private boolean validate() {
    		if(nameField.getText().trim().isEmpty() || ssnField.getText().trim().isEmpty()) {
    			return false;
    		}
    		return true;
    }
    
    //write message from HashMap to fields
    private void loadPage() {
    		//FIXME: using selected user
    		//primary
    		nameField.setText(selectedUser.get("name"));
    		ssnField.setText(selectedUser.get("ssn"));
    		departmentChoiceBox.getSelectionModel().select(selectedUser.get("department"));
    		
    		titleChoiceBox.getSelectionModel().select(selectedUser.get("title"));
    		positionChoiceBox.getSelectionModel().select(selectedUser.get("position"));
    		levelChoiceBox.getSelectionModel().select(selectedUser.get("level"));
    		specialitiesBox.getSelectionModel().select(selectedUser.get("branch"));
    		
    		//sub
    		try {
    		sexChoiceBox.getSelectionModel().select(selectedUser.get("sex"));
    		birthPicker.setValue(date(selectedUser.get("birth")));
    		regionField.setText(selectedUser.get("region"));
    		dateJoinPartyPicker.setValue(date(selectedUser.get("dateJoinParty")));
    		timeStartWorkPicker.setValue(date(selectedUser.get("timeStartWork")));
    		certifactionDatePicker.setValue(date(selectedUser.get("certifactionDate"))); 		
    		certifactionNumField.setText(selectedUser.get("certifactionNum"));
    		//technicalPositionField.setText(selectedUser.get("technicalPosition"));
    		dateReceivedTPPicker.setValue(date(selectedUser.get("dateReceivedTP")));
    		hireDatePicker.setValue(this.date(selectedUser.get("dateReceivedTP")));
    		N0Picker.setValue(date(selectedUser.get("N0")));
    		N1Picker.setValue(date(selectedUser.get("N1")));
    		N2Picker.setValue(date(selectedUser.get("N2")));
    		N3Picker.setValue(date(selectedUser.get("N3")));
    		N4Picker.setValue(date(selectedUser.get("N4")));
    		N5Picker.setValue(date(selectedUser.get("N5")));
    		primaryEdField.setText(selectedUser.get("primaryEd"));
    		primaryEdTimePicker.setValue(date(selectedUser.get("primaryEdTime")));
    		primaryEdSchoolField.setText(selectedUser.get("primaryEdSchool"));
    		highestEdField.setText(selectedUser.get("highestEd"));
    		highestEdTimePicker.setValue(date(selectedUser.get("highestEdTime")));
    		highestEdSchoolField.setText(selectedUser.get("highestEdSchool"));
    		}catch(Exception e) {
    			System.out.println(e);
    		}
    		
    }
    private LocalDate date(String dateString) {
    		
    		if (dateString != null) {
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        		LocalDate localDate = LocalDate.parse(dateString, formatter);
        		return localDate;
    		}
    		
    		return null;
    		
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	

}


