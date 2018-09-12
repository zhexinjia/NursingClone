package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import com.jfoenix.controls.JFXCheckBox;

import Model.CheckMap;
import Model.DBhelper;
import Model.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TestPublishController implements Initializable {	
	@FXML TableView<CheckMap> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	@FXML JFXCheckBox checkAll;
	
	Loader loader = new Loader();
	DBhelper dbHelper = new DBhelper();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ObservableList<CheckMap> checklist;
	String[] keys = {"name", "department", "title", "position", "level"};
	String[] fields = {"姓名", "科室", "职称", "职务", "层级"};
	private HashMap<String, String> selectedTest;
	String branch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedTest = TestListController.selectedTest;
		branch = LoginController.branch;
		setupTable();
		setupCheckBox();
		getList();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML void contact(){
		loader.loadWeb();
	}
	
	@FXML
	void publishButton() {
		boolean output = dbHelper.publish(getChecked(), selectedTest, "exam_list");
		if(output) {
			//TOOD: either back or change to un-publish able
			loader.loadVBox(box, "/View/TestList.fxml");
		}
		
	}


    @FXML
    void searchButton() {
    		reload();
    }

    @FXML
    void resetButton() {
    		searchField.setText("");
    		reload();
    }

    @FXML
    void testListButton() {
    		loader.loadVBox(box, "/View/TestList.fxml");
    }
    
    /*
    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/UserNew.fxml");
    }

    @FXML
    void modifyButton() {
    		loader.loadVBox(box, "/View/UserModify.fxml");
    }
    */

    private void setupCheckBox() {
    		checkAll.setOnAction(e->{
    			if(checkAll.isSelected()) {
    				for(CheckMap checkMap:checklist) {
    					checkMap.cb.setCheck();
    				}		
    			}else {
    				for(CheckMap checkMap:checklist) {
    					checkMap.cb.setUnCheck();
    				}
    			}
    		});
    }

	private void getList() {
		list = dbHelper.getEntireList("user_primary_info WHERE branch = '" + branch + "'");
		//TODO: get totalScore, uncomment above when finish
		/*
		String sqlStatement = "";
		String tableName = "user_primary_info inner join user_score on user_primary_info.ssn = user_score.ssn";
		String[] columns = new String[]{"user_primary_info.*", "user_score.totalScore"};
		list = dbHelper.getList(sqlStatement, tableName, columns);
		*/
		
	}
	
	private void setupTable() {
		loader.setupCheckTable(tableView, keys, fields);
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		checklist = FXCollections.observableArrayList();
		for(HashMap<String, String> map:searchList) {
			checklist.add(new CheckMap(map));
		}
		tableView.setItems(checklist);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	private ArrayList<HashMap<String, String>> getChecked(){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(CheckMap checkMap:checklist) {
			if (checkMap.cb.isSelected()) {
				list.add(checkMap.map);
			}
		}
		return list;
	}
	
	

}
