package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class TestOfflineNewController implements Initializable {
	
	@FXML JFXTextField nameField;
	@FXML JFXComboBox<Integer> pointPicker;
	//@FXML JFXComboBox<String> typePicker;
	@FXML JFXComboBox<Integer> timePicker;
	@FXML JFXTextArea contextField;

	
	@FXML VBox box;
	
	private ArrayList<HashMap<String, String>> list;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	private String branch;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		branch = LoginController.branch;
		getList();
		setupChoiceBox();
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
    

    
    private void setupChoiceBox() {
    		
    		//setup points and question numbers
    		Integer[] array = new Integer[101];
    		Arrays.setAll(array, i -> i);
    		pointPicker.getItems().setAll(array);

    		
    		//setup question points
    		Integer[] points = new Integer[11];
    		Arrays.setAll(points, i->i);
    		
    		Integer[] time = new Integer[121];
    		Arrays.setAll(time, i->i);
    		timePicker.getItems().setAll(time);
    		/*
    		ArrayList<String> banks = new ArrayList<String>();
    		for(HashMap<String, String> item:list) {
    			banks.add(item.get("name"));
    		}
    	*/
    }
    
    private boolean validate() {
    		if(nameField.getText().trim().isEmpty()) {
    			return false;
    		}
    		if(pointPicker.getValue()==null) {
    			return false;
    		}
    		if(timePicker.getValue()==null) {
    			return false;
    		}
    		if(contextField.getText().isEmpty()) {
    			return false;
    		}

    		return true;
    }
    
    
    private void insertDB() {
    
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("exam_name", nameField.getText());
    		map.put("totalPoint", pointPicker.getValue().toString());
    		map.put("time", timePicker.getValue().toString());
    		//map.put("type", typePicker.getValue().equals("随机题目")?"1":"0");
    		map.put("context", contextField.getText().toString());

    		//}
    		map.put("branch", branch);
    		map.put("publishStatus", "未发布");
    		if(dbHelper.insert(map, "offlineexam_list")) {
    			loader.loadVBox(box, "/View/TestOffline.fxml");
    		}
    		
    }
    
    private void getList() {
    	
    		list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {branch}, "question_bank");
   
    }
    
    

	

}

