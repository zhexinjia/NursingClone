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
import Model.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TestOfflineChooseUserController implements Initializable {	
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	
	Loader loader = new Loader();
	DBhelper dbHelper = new DBhelper();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ObservableList<CheckMap> checklist;
	String[] keys = {"name", "department", "title", "position", "level"};
	String[] fields = {"姓名", "科室", "职称", "职务", "层级"};
	public static HashMap<String, String> selectedUser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		getList();
		reload();
	}

	
	@FXML
	void comfirmButton() {

		selectedUser = tableView.getSelectionModel().getSelectedItem();

			String name = selectedUser.get("name");
			String ssn = selectedUser.get("ssn");
			String button = StageManager.Button.get("button");
			//System.out.println("userList"+userList);
			TestOfflineSectionNewController index = (TestOfflineSectionNewController) StageManager.CONTROLLER.get("sectionControl");
			if (button == "1") {
				index.userID_1 = ssn;
				index.nameField1.setText(name);
				
			}else if (button == "2") {
				index.userID_2 = ssn;
				index.nameField2.setText(name);
			}else if (button == "3") {
				index.userID_3 = ssn;
				index.nameField3.setText(name);
			}else if (button == "4") {
				index.userID_4 = ssn;
				index.nameField4.setText(name);
			}else {
				System.out.println("Something goes wrong here");
			}
		
			StageManager.STAGE.get("choosenUser").close();
			
			//delete storage in map
			StageManager.STAGE.remove("choosenUser");
			StageManager.Button.remove("button");
			StageManager.CONTROLLER.remove("sectionControl");
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


	private void getList() {
		//list = dbHelper.getEntireList("user_primary_info");
		list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {LoginController.branch}, "user_primary_info");
	}
	
	
	private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	
	private void reload() {
		
		ObservableList<HashMap<String, String>> searchList = loader.search(list, "");
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	
	
	

}
