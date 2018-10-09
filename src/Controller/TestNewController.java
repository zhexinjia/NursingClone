package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class TestNewController implements Initializable {
	
	@FXML JFXTextField nameField;
	@FXML JFXComboBox<Integer> pointPicker;
	@FXML JFXComboBox<Integer> timePicker;
	@FXML JFXDatePicker start_date;
	@FXML JFXTimePicker start_time;
	@FXML JFXDatePicker end_date;
	@FXML JFXTimePicker end_time;
	@FXML JFXComboBox<String> bankPicker;
	
	@FXML JFXComboBox<Integer> singlePointPicker;
	@FXML JFXComboBox<Integer> multiPointPicker;
	@FXML JFXComboBox<Integer> tfPointPicker;
	@FXML JFXComboBox<Integer> singleCountPicker;
	@FXML JFXComboBox<Integer> multiCountPicker;
	@FXML JFXComboBox<Integer> tfCountPicker;
	
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
    void testListButton() {
    		loader.loadVBox(box, "/View/TestList.fxml");
    }

    @FXML
    void contactButton() {
    		loader.loadWeb();
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
    		singleCountPicker.getItems().setAll(array);
    		multiCountPicker.getItems().setAll(array);
    		tfCountPicker.getItems().setAll(array);
    		
    		//setup question points
    		Integer[] points = new Integer[10];
    		Arrays.setAll(points, i->i+1);
    		singlePointPicker.getItems().setAll(points);
    		multiPointPicker.getItems().setAll(points);
    		tfPointPicker.getItems().setAll(points);
    		
    		Integer[] time = new Integer[121];
    		Arrays.setAll(time, i->i);
    		timePicker.getItems().setAll(time);
    		
    		ArrayList<String> banks = new ArrayList<String>();
    		for(HashMap<String, String> item:list) {
    			banks.add(item.get("name"));
    		}
    		//bankPicker.getItems().setAll(banks);
    		bankPicker.getItems().setAll(banks);
    	
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
    		if(singlePointPicker.getValue()==null||multiPointPicker.getValue()==null||tfPointPicker.getValue()==null) {
    			return false;
    		}
    		/*
    		if(typePicker.getValue()==null) {
    			return false;
    		}else if(typePicker.getValue().equals("随机题目")) {
    			if(singleCountPicker.getValue()==null||multiCountPicker.getValue()==null||tfCountPicker.getValue()==null||bankPicker.getValue()==null) {
    				return false;
    			}
    		}else { // 固定题库
    			
    		}
    		*/
    		return true;
    }
    
    
    private void insertDB() {
    		//type: 随机题库：1， 固定题库：0；  publish_status: 进行中：1， 已结束：0
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("examName", nameField.getText());
    		map.put("totalPoint", pointPicker.getValue().toString());
    		map.put("time", timePicker.getValue().toString());
    		
    		map.put("start_date", start_date.getValue().toString());
    		map.put("start_time", start_time.getValue().toString());
    		map.put("end_date", end_date.getValue().toString());
    		map.put("end_time", end_time.getValue().toString());
    		//map.put("type", typePicker.getValue().equals("随机题目")?"1":"0");
    		map.put("single_point", singlePointPicker.getValue().toString());
    		map.put("multi_point", multiPointPicker.getValue().toString());
    		map.put("tf_point", tfPointPicker.getValue().toString());
    		
    		//if(typePicker.getValue().equals("随机题目")) {
    		//map.put("question_bank", bankPicker.getValue());
    		map.put("question_bank", list.get(bankPicker.getSelectionModel().getSelectedIndex()).get("id"));
    		map.put("single_count", singleCountPicker.getValue().toString());
    		map.put("multi_count", multiCountPicker.getValue().toString());
    		map.put("tf_count", tfCountPicker.getValue().toString());
    		//}
    		map.put("branch", branch);
    		map.put("publish_status", "0");
    		if(dbHelper.insert(map, "exam_list")) {
    			loader.loadVBox(box, "/View/TestList.fxml");
    		}
    		
    }
    
    private void getList() {
    		list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {branch}, "question_bank");
    }
    
    

	

}

