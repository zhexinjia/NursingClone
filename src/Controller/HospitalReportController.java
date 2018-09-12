package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class HospitalReportController implements Initializable {	
	@FXML TableView<HashMap<String,String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	@FXML ChoiceBox<String> levelPicker;
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	String[] keys = {"name", "department", "reportDate", "title","status"};
	String[] fields = {"上报员工", "所属科室", "上报时间", "上报事件", "处理情况"};
	PopupWindow popUP = new PopupWindow();
	DBhelper dbHelper = new DBhelper();
	String choiceBoxString = "";
	public static HashMap<String, String> selectedReport;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupChoiceBox();
		setupTable();
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
    void searchButton() {
    		reload();
    }

    @FXML
    void resetButton() {
    		searchField.setText("");
    		reload();
    }

    @FXML
    void modifyButton() {
    		selectedReport = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selectedReport)) {
    			loader.loadVBox(box, "/View/ReportModify.fxml");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(selected == null) {
    			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
    		}else {
    			if (dbHelper.delete(selected, "report_list")) {
    				getList();
    				reload();
    			}else {
    				popUP.alertWindow("操作失败", "删除用户失败");
    			}
    		}
    }
	
	private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	private void setupChoiceBox() {
		String[] list = {"所有上报","个人上报至科室","科室已讨论/上报医院","医院已处理"};
		levelPicker.getItems().addAll(list);
		levelPicker.setOnAction(e->{
			if (levelPicker.getSelectionModel().getSelectedIndex()==0) {
				choiceBoxString = "";
				reload();
			}else if (levelPicker.getSelectionModel().getSelectedIndex()==1) {
				choiceBoxString = "上报至科室";
				reload();
			}else if (levelPicker.getSelectionModel().getSelectedIndex()==2) {
				choiceBoxString = "科室已讨论/上报医院";
				reload();
			}else if (levelPicker.getSelectionModel().getSelectedIndex()==3) {
				choiceBoxString = "医院已处理";
				reload();
			}
		});
	}
	
	private void getList() {
		String tableName = "user_primary_info right join report_list on user_primary_info.ssn = report_list.ssn";
		String[] columns = {"report_list.*", "user_primary_info.name", "user_primary_info.department"};
		//String[] columns = {"report_list.id","report_list.date", "report_list.title", "report_list.level", "user_primary_info.name", "user_primary_info.department"};
		list = dbHelper.getList(tableName, columns );
		for(HashMap<String, String> map:list) {
			if(map.get("level").equals("1")) {
				map.put("status", "上报至科室");
			}
			if(map.get("level").equals("2")) {
				map.put("status", "科室已讨论/上报医院");
			}
			if(map.get("level").equals("3")) {
				map.put("status", "医院已处理");
			}
		}
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, choiceBoxString + " " + searchField.getText());
		//System.out.println(searchField.getText() + " " + field);
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
