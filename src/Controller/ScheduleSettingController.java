package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import Model.QuestionBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ScheduleSettingController implements Initializable {	
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	String[] keys = {"name", "shift_length", "shift_point"};
	String[] fields = {"名称", "工作时长", "工作分值"};
	DBhelper dbHelper = new DBhelper();
	public static HashMap<String, String> selectedSchedule;
	private String branch;
 	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		branch = LoginController.branch;
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
    void newButton() {
    		schedulePopUp(null);
    
    }
    
    
    @FXML
    void modifyButton() {
    		HashMap<String, String> map = tableView.getSelectionModel().getSelectedItem();
		if(map!=null) {
			schedulePopUp(map);
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("编辑失败", "请选排班设置");
		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selectedSchedule = tableView.getSelectionModel().getSelectedItem();
    		if (selectedSchedule != null) {
    			if (dbHelper.delete(selectedSchedule, "schedule_list")) {
    				getList();
    				reload();
    			}
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("删除失败！", "请选中一个会议。");
    		}
    }	
    
    private void schedulePopUp(HashMap<String, String> inputMap) {
		QuestionBox box = new QuestionBox(); 
	box.confirmButton.setOnAction(e->{
		if(box.nameField.getText() != null && box.shift_lengthField.getText() != null) {
			
			HashMap<String, String> map;
			boolean is_empty;
			if(inputMap == null) {
				is_empty = true;
				map = new HashMap<String, String>();
			}else {
				is_empty = false;
				map = inputMap;
			}	
				map.put("name", box.nameField.getText());
				map.put("shift_length", box.shift_lengthField.getText());
				map.put("shift_point", box.shift_pointField.getText());
				map.put("branch", branch);
				
				
				if(is_empty) {
					if (dbHelper.insert(map, "schedule_list")) {
						getList();
	    					reload();
					}
				}else {
					if (dbHelper.update(map, "schedule_list")) {
						getList();
	    					reload();
					}
				}
				//this.getTfList();
				this.reload();
				box.stage.close();
		}else {
			PopupWindow popUP = new PopupWindow();
			popUP.alertWindow("选项不能为空", "所有选项不能为空");
		}
		
	});
	box.scheduleNew(inputMap);
}
    
    private boolean validate(List<TextField> fieldlist) {
		for(TextField field:fieldlist) {
			if (field.getText().trim().isEmpty()) {
				return false;
			}
		}
		return true;
    }
	
    private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	
	private void getList() {
		String tableName = "schedule_list";
		String[] columns = {"id", "name", "shift_length", "shift_point"};
		//list = dbHelper.getList(tableName, columns);
		list = dbHelper.getList(new String[] {"branch"}, new String[] {LoginController.branch}, tableName, columns);
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
