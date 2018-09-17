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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class UserTestOfflineDetailController implements Initializable {

	@FXML VBox box;
	
	@FXML JFXTextField nameField;
	@FXML JFXTextField ssnField;
	@FXML JFXTextField takenDateField;
	@FXML JFXTextField advisorField;
	@FXML JFXTextField totalScoreField;
	
	@FXML TableView<HashMap<String,String>> tableView;
	
	private HashMap<String, String> report;
	
	HashMap<String, String> scoreList;
	ArrayList<HashMap<String, String>> reqList = new ArrayList<HashMap<String, String>>();	
	//HashMap<String, ArrayList<HashMap<String, String>>> levelList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	ArrayList<HashMap<String, String>> departmentExportList;
	ArrayList<HashMap<String, String>> levelExportList;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	
	String[] keys = {"req", "point", "score"};
	String[] fields = {"考核要求", "满分", "得分"};

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		// 
		report = TestOfflineDetailListController.selectedUser;
		if (report == null) {
			report = TestOfflineDetailController.selectedUser;
		}
		//System.out.println("report: " + report);
		getList();
		setup();
		reload();
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
	
	@FXML
	void offlineSectionClicked() {
		loader.loadVBox(box, "/View/TestOfflineSection.fxml");
	}
	
	
	
	void getList(){
		String tableName = "offlineexam_history inner join user_primary_info on user_primary_info.ssn = offlineexam_history.ssn "
				+ "where user_primary_info.branch = '" + LoginController.branch + "'" + "and offlineexam_history.id = '" + report.get("id") + "';";
		String[] columns = {"user_primary_info.ssn","offlineexam_history.score", "offlineexam_history.taken_date", "offlineexam_history.supervisor",
				"offlineexam_history.score_list"};
		scoreList = dbHelper.getList(tableName, columns).get(0);
		//System.out.println("scoreList: " + scoreList);
		
		
		String req_table = "offline_req_bank where offline_req_bank.offlineexam_id = '" + report.get("offlineexam_id") + "';" ;
		String[] req_col = {"offline_req_bank.req","offline_req_bank.point"};
		reqList = dbHelper.getList(req_table, req_col);
		//System.out.println("reqList: " + reqList);
	}
	
	void setup() {
		nameField.setText(report.get("name"));
		
		String ssn = scoreList.get("ssn");
		String supervisor = scoreList.get("supervisor");
		String taken_date = scoreList.get("taken_date");
		String totalScore = scoreList.get("score");
		
		if(ssn != null) {
			ssnField.setText(ssn);
		}
		if (supervisor != null) {
			advisorField.setText(supervisor);
		}
		if (taken_date != null) {
			takenDateField.setText(taken_date);
		}
		if (totalScore != null) {
			totalScoreField.setText(totalScore);
		}
		
		loader.setupTable(tableView, keys, fields);
		
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(reqList, "");
		System.out.println("searchList: " + searchList);
		String score_list = scoreList.get("score_list");
		String[] score_array = null;
		try {
			score_array = score_list.split("\\,|，", -1);
		}catch (Exception e){
			System.out.println(e);
		}
		
		if (score_array != null) {
			for (int i = 0; i < searchList.size(); i++) {
				searchList.get(i).put("score", score_array[i]);
			}
		
		}else {
			for (int i = 0; i < searchList.size(); i++) {
				searchList.get(i).put("score", "");
			}
		
		}
		
		tableView.setItems(searchList);
		//countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	void disableEdit() {
		//Set all fields disable for any editing. 
		nameField.setEditable(false);
		ssnField.setEditable(false);
		takenDateField.setEditable(false);
		totalScoreField.setEditable(false);
		advisorField.setEditable(false);
		
	}
	
}


