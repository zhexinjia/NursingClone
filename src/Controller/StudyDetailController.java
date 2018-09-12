package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StudyDetailController implements Initializable {	
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	DBhelper dbHelper = new DBhelper	();
	private HashMap<String, String> selectedStudy;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//selectedStudy = (StudyStatusController.selectedStudy );
		if (StudyStatusController.selectedStudy != null) {
			selectedStudy = StudyStatusController.selectedStudy ;
		}else if (StudyListController.selectedStudy != null) {
			selectedStudy = StudyListController.selectedStudy;
		}else {
			System.out.println("Something wrong here");
		}
		
		setupTable();
		getList();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
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
    void importButton() {
    		
    }

    @FXML
    void exportButton() {
    		String[] keys = {"name", "department", "position", "title", "finish_status"};
		String[] fields = {"姓名", "科室", "职位", "职称", "完成情况"};
		loader.exportExcel(list, fields, keys);
    }



    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/UserNew.fxml");
    }

    @FXML
    void modifyButton() {
    		loader.loadVBox(box, "/View/UserModify.fxml");
    }

    @FXML
    void deleteButton() {
    		
    }

    private void setupTable() {
		String[] keys = {"name", "department", "position", "title", "finish_status"};
		String[] fields = {"姓名", "科室", "职位", "职称", "完成情况"}	;
		loader.setupTable(tableView, keys, fields);
    }

    private void getList() {
    		String[] searchColumn = {"study_id"};
    		String[] values = {selectedStudy.get("id")};
    		String tableName = "study_history inner join user_primary_info on study_history.ssn = user_primary_info.ssn";
    		String[] columns = {"user_primary_info.name", "user_primary_info.department", "user_primary_info.position", "user_primary_info.title",
    				"study_history.finish_status", "study_history.id"};
    		list = dbHelper.getList(searchColumn, values, tableName, columns);
    }

    private void reload() {
    		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
    		tableView.setItems(searchList);
    		countLabel.setText("共 " +searchList.size()+ " 条");
    }
    
    
    
    boolean validate(TextField inputField){
    		if(inputField.getText().trim().isEmpty()) {
    			return false;
    		}else if(!isNumeric(inputField.getText())) {
    			return false;
    		}
    		return true;
    }

    public boolean isNumeric(String str)
    {
    		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

}
