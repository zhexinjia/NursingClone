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

public class DepartmentUsers implements Initializable {	
	@FXML TableView<HashMap<String,String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	@FXML Label managerLabel;
	
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	String[] keys = {"name", "ssn", "title", "position", "level"};
	String[] fields = {"姓名", "工号", "职称", "职务", "层级"};
	public static HashMap<String, String> selectedUser;
	PopupWindow popUP = new PopupWindow();
	DBhelper dbHelper = new DBhelper();
	private HashMap<String,String> department;
	private String managerName;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		department = DepartmentController.selectedDepartment;
		System.out.println("department: "+ department);
		managerName = department.get("name");
		if(managerName != null) {
			managerLabel.setText("管理： " + managerName);
		}
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
	void departmentListButton() {
		loader.loadVBox(box, "/View/DepartmentList.fxml");
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
    void managerButton() {
    		
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		String oldManager = department.get("ssn");
    		String newManager = selected.get("ssn");
    		if(loader.selectionCheck(selected)) {
    			/*
    			if(dbHelper.setManager(selected, department.get("departmentName"))) {
    				managerSSN = selected.get("ssn");
    				setManager(managerSSN);
    			}
    			*/
    			if (oldManager == null) {
    				dbHelper.setManager(newManager);
    				managerLabel.setText("管理： " + selected.get("name")); 
    			}
    			else {
    				if (dbHelper.updateManager(newManager, oldManager)) {
    					managerLabel.setText("管理： " + selected.get("name"));
    				}
    			}
    			loader.loadVBox(box, "/View/DepartmentList.fxml");	
    		}
    }

	private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	
	private void getList() {
		String[] searchColumn = new String[] {"department", "branch"};
		String[] values = new String[] {department.get("departmentName"), LoginController.branch};
		list = dbHelper.getList(searchColumn, values, "user_primary_info", keys);
		//setManager(department.get("manager_ssn"));
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	/*
	private void setManager(String ssn) {
		System.out.println(ssn + "ssN");
		String name = "管理： ";
		for(HashMap<String, String> map:list) {
			if(map.get("ssn").equals(ssn)) {
				name += map.get("name");
				
				break;
			}
		}
		managerLabel.setText(name);
	}
	*/

}
