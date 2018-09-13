package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class TestOfflineNewController implements Initializable {
	
	@FXML JFXTextField nameField;
	@FXML JFXComboBox<Integer> pointPicker;
	@FXML JFXDatePicker startDatePicker;
	@FXML JFXDatePicker endDatePicker;

	@FXML JFXTextField inpuContentField;
	@FXML JFXTextField inputPointField;
	
	@FXML private TableView<HashMap<String, String>> tableView;
	//@FXML private TableColumn<HashMap<String, String>, String> content;
	@FXML VBox box;
	
	//private ArrayList<HashMap<String, String>> list;
	private ObservableList<HashMap<String, String>> req_list;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	private String branch;
	PopupWindow popUP = new PopupWindow();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupChoiceBox();
		req_list = FXCollections.observableArrayList();
		branch = LoginController.branch;
		setupTable();
		reload();
		//getList();
		
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML
    void TestOfflineListButton() {
    		loader.loadVBox(box, "/View/TestOffline.fxml");
    }
    
    @FXML void createButton() {
    		if(validate()) {
    			insertDB();
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "所有选项不能为空。");
    		}
    }
    
    @FXML void addButtonClicked() {
    		//ArrayList<TableColumn<HashMap<String, String>, String>> cols = new ArrayList<TableColumn<HashMap<String, String>, String>>();
    		String req  = inpuContentField.getText();
    		String point = inputPointField.getText();
    		
    		HashMap<String, String> tst = new HashMap<String, String>();
		tst.put("req", req);
		tst.put("point", point);
		req_list.add(tst);
		inpuContentField.clear();
		inputPointField.clear();
		System.out.println("req_list:" + req_list);
    }
    
    @FXML void deleteRowClicked(){
		ObservableList<HashMap<String, String>> selected = tableView.getSelectionModel().getSelectedItems();
		if(selected == null) {
			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
		}else {
			if (req_list.removeAll(selected)) {
				reload();
			}
		}
    }
    
    
    
    
    private void setupTable() {
    		loader.setupTable(tableView, new String[] {"req", "point"}, new String[] {"要求", "分值"});
    }
   
    private void reload() { 		
    		tableView.setItems(req_list);
    }

    
    private void setupChoiceBox() {
    		
    		//setup points
    		Integer[] array = new Integer[101];
    		Arrays.setAll(array, i -> i);
    		pointPicker.getItems().setAll(array);

    }
    
    private boolean validate() {
    		if(nameField.getText().trim().isEmpty()) {
    			return false;
    		}
    		if(pointPicker.getValue()==null) {
    			return false;
    		}
    		if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
    			return false;
    		}

    		return true;
    }
    
    
    private void insertDB() {
    
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("exam_name", nameField.getText());
    		map.put("totalPoint", pointPicker.getValue().toString());
    		map.put("start_date", startDatePicker.getValue().toString());
    		map.put("end_date", endDatePicker.getValue().toString());
 
    		map.put("branch", branch);
    		map.put("publishStatus", "未发布");
    		
    		
    		if(dbHelper.insertOffline(map, req_list)) {
    			loader.loadVBox(box, "/View/TestOffline.fxml");
    		}
    		
    		
    }
    
    private void getList() {
    	
    		//list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {branch}, "question_bank");
   
    }
    
    

	

}

