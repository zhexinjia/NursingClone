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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class DepartmentController implements Initializable {
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML private CustomTextField searchField;
	@FXML VBox box;
	
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); 
	ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> departmentList = new ArrayList<HashMap<String, String>>();
	Loader loader = new Loader();
	DBhelper dbHelper;
	public static HashMap<String, String> selectedDepartment;
	HashMap<String, String> temp = new HashMap<>();
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML void contact() {
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
    		PopupWindow popUP = new PopupWindow();
    		popUP.confirmButton.setText("创建科室");
    		popUP.confirmButton.setOnAction(e->{
    			if (!popUP.inputField.getText().trim().isEmpty()) {
    				String departmentName = popUP.inputField.getText();
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put("departmentName", departmentName);
    				if(dbHelper.insert(map, "hospital_department")) {
    					popUP.stage.close();
    					list.clear();
    					getList();
        				reload();
    				}
    				else {
    					popUP.alertWindow("新建科室出错", "科室已存在");
    				}
    			}else {
    				popUP.alertWindow("新建科室出错", "科室名称不能为空！");
    			}
    		});
    		popUP.inputWindow("输入科室名称", "输入科室名称", false);
    }

    @FXML
    void modifyButton() {
    		selectedDepartment = tableView.getSelectionModel().getSelectedItem();
    		System.out.println("selectedDepartment: " + selectedDepartment);
    		if(loader.selectionCheck(selectedDepartment)) {
    			loader.loadVBox(box, "/View/DepartmentUsers.fxml");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selected)) {
    			PopupWindow popup = new PopupWindow();
    			popup.confirmButton.setOnAction(e->{
    				if(dbHelper.delete(selected, "hospital_department")) {
    					list.clear();
    	    				getList();
    	    				reload();
    	    				popup.stage.close();
    	    			}
    			});
    			popup.confirmWindow("确认要删除吗", "删除科室并不会删除相关科室的工作人员");
    		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbHelper = new DBhelper();
		setupTable();
		getList();
		reload();
	}

	
	private void getList() {
		//TODO need to modify this code later on, kind of messy here.
		String[] searchColumn = {"is_manager", "branch"};
		String[] values = {"1", LoginController.branch};
		String tableName = "user_primary_info";
		String[] columns = {"ssn", "department", "name", "is_manager"};
		
		userList= dbHelper.getList(searchColumn, values, tableName, columns);
		
		String tableName2 = "hospital_department";
		String[] columns2 = {"departmentName", "id"};
		list = dbHelper.getList(tableName2, columns2);
		
		for(int i = 0; i < list.size(); i++) {
			String department = list.get(i).get("departmentName");
			for(int j = 0; j < userList.size(); j++) {
				if(userList.get(j).get("department").equals(department)) {
					list.get(i).put("name", userList.get(j).get("name"));
					list.get(i).put("ssn", userList.get(j).get("ssn"));
				}
			}
		}
		
		/*
		departmentList = dbHelper.getList(tableName2, columns2);
		int count  = 0;
			
		if (userList.isEmpty()) {
			for (int i = 0; i < departmentList.size(); i++) {
				departmentList.get(i).put("name", null);
				departmentList.get(i).put("ssn", null);
				list.add(departmentList.get(i));
			}
			
		}else {
			for (int i = 0; i < departmentList.size(); i++) {

				String a = departmentList.get(i).get("departmentName");
				String id = departmentList.get(i).get("id");
				System.out.println("a: " + a);
				
				for (int j = 0; j < userList.size(); j++) {
					
					if (userList.get(j).get("department").equals(a)) {
						
						userList.get(j).put("departmentName", a);
						userList.get(j).put("id", id);
						list.add(userList.get(j));
					
						count++;
						
					}
					
				}
				if (count == i) {
					departmentList.get(i).put("name", null);
					departmentList.get(i).put("ssn", null);
					list.add(departmentList.get(i));
					
					count++;
				}
				
			}
		}
		*/

	}
	
	private void setupTable() {
		String[] keys = {"departmentName", "name"};
		String[] fields = {"科室名称", "科室管理"};
		loader.setupTable(tableView, keys, fields);
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
