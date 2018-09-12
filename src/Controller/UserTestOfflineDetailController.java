package Controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import Model.DBhelper;
import Model.Loader;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserTestOfflineDetailController implements Initializable {

	@FXML VBox box;
	
	@FXML JFXTextField nameField;
	@FXML JFXTextField supervisorField;
	@FXML JFXTextField takenDateField;
	@FXML JFXTextField scoreField;
	
	@FXML Label nameLabel;
	@FXML Label supervisorLabel;
	@FXML Label takenDateLabel;
	@FXML Label scoreLabel;
	@FXML Label commentLabel;
	
	@FXML JFXTextArea commentField;
	
	private HashMap<String, String> report;
	
	ArrayList<HashMap<String, String>> scoreList;
	HashMap<String, ArrayList<HashMap<String, String>>> departmentList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	HashMap<String, ArrayList<HashMap<String, String>>> levelList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	ArrayList<HashMap<String, String>> departmentExportList;
	ArrayList<HashMap<String, String>> levelExportList;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		report = TestOfflineDetailController.selectedUser;
		System.out.println("report: " + report);
		getList();
		setup();
		disableEdit();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML
    void contactButton() {
		loader.loadWeb();
    }
	
	@FXML
	void returnButton() {
		loader.loadVBox(box, "/View/TestOfflineDetail.fxml");

	}
	
	void getList(){
		String tableName = "offlineexam_history inner join user_primary_info on user_primary_info.ssn = offlineexam_history.ssn "
				+ "where user_primary_info.branch = '" + LoginController.branch + "'" + "and offlineexam_history.id = '" + report.get("id") + "';";
		String[] columns = {"offlineexam_history.score", "offlineexam_history.taken_date", "offlineexam_history.supervisor",
				"offlineexam_history.comment"};
		scoreList = dbHelper.getList(tableName, columns);
		System.out.println("scoreList: " + scoreList);
	}
	
	void setup() {
		nameField.setText(report.get("name"));
		
		String comment = scoreList.get(0).get("comment");
		String supervisor = scoreList.get(0).get("supervisor");
		String taken_date = scoreList.get(0).get("taken_date");
		String score = scoreList.get(0).get("score");
		
		if(comment != null) {
			commentField.setText(comment);
		}
		if (supervisor != null) {
			supervisorField.setText(supervisor);
		}
		if (taken_date != null) {
			takenDateField.setText(taken_date);
		}
		if (score != null) {
			scoreField.setText(score);
		}
	}
	
	void disableEdit() {
		//Set all fields disable for any editing. 
		nameField.setEditable(false);
		supervisorField.setEditable(false);
		takenDateField.setEditable(false);
		scoreField.setEditable(false);
		commentField.setEditable(false);
		
	}
	
}


