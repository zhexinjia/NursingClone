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
import Model.PopupWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TrainingPublishController implements Initializable {	
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
	private HashMap<String, String> selectedTraining;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedTraining = TrainningListController.selectedTrainning;
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
	void TrainningListButton() {
		loader.loadVBox(box, "/View/TrainningList.fxml");
	}
	
	@FXML void importButton(){
		String[] keylist = {"ssn", "point", "detail"};
		String[] fields = {"工号", "得分", "备注"};
		ArrayList<HashMap<String, String>> importList = loader.importExcel(keylist, fields);
		if(importList != null) {
			String id = selectedTraining.get("id");
			for (HashMap<String, String> map:importList) {
				map.put("training_id", id);
			}
			PopupWindow pop = new PopupWindow();
			if(dbHelper.insertList(importList, "training_history")) {
				pop.confirmWindow("导入完成", "点击确定返回");
				getList();
				reload();
			}else {
				pop.errorWindow();
			}
		}
	}
	
	@FXML void exportButton() {
		
	}

	@FXML
	void publishButton() {
		//FIXME: add one person twice, back to previous page
		if(dbHelper.insertList(getChecked(), "training_history")) {
			loader.loadVBox(box, "/View/TrainningDetail.fxml");
		}
		
		//dbHelper.publishTraining(getChecked(), "training_history");
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
		list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {LoginController.branch}, "user_primary_info");
		//list = dbHelper.getEntireList("user_primary_info");
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
		String id = selectedTraining.get("id");
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(CheckMap checkMap:checklist) {
			if (checkMap.cb.isSelected()) {
				HashMap<String, String> checkedMap = checkMap.map;
				HashMap<String, String> newMap = new HashMap<String, String>();
				newMap.put("training_id", id);
				newMap.put("ssn", checkedMap.get("ssn"));
				//newMap.put("point", checkedMap.get("point"));
				//newMap.put("detail", checkedMap.get("detail"));
				list.add(newMap);
			}
		}
		return list;
	}

}
