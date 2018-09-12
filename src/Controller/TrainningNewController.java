package Controller;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TrainningNewController implements Initializable {

	@FXML TextField nameField;
	//@FXML ChoiceBox<String> if_countPicker;
	@FXML ChoiceBox<Integer> pointPicker;
	@FXML JFXTextArea commentArea;
	@FXML VBox box;
	Loader loader = new Loader();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupChoiceBox();		
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
    @FXML
    void contactButton() {
    		loader.loadWeb();
    }

    @FXML void newButton() {
    		if(validate()) {
    			insertDB();
    		}
    }
    
	@FXML
	void TrainningListButton() {
		loader.loadVBox(box, "/View/TrainningList.fxml");
	}

	private boolean validate() {
		if(nameField.getText().trim().isEmpty()) {
			return false;
		}
		/*
		if(if_countPicker.getValue()==null) {
			return false;
		}else if(if_countPicker.getValue().toString().equals("是")) {
			if(pointPicker.getValue() == null) {
				return false;
			}
		}
		*/
		if(pointPicker.getValue() == null) {
			return false;
		}
		return true;
	}
	
	private void setupChoiceBox() {
		/*
		String[] tfList = {"是", "否"};
		if_countPicker.getItems().setAll(tfList);
		if_countPicker.setOnAction(e->{
			if(if_countPicker.getSelectionModel().getSelectedItem().equals("是")) {
				pointPicker.setDisable(false);
			}else if(if_countPicker.getSelectionModel().getSelectedItem().equals("否")){
				pointPicker.setValue(null);
				pointPicker.setDisable(true);
				pointPicker.setValue(null);
			}
		});
		*/
		Integer[] array = new Integer[101];
		Arrays.setAll(array, i -> i);
		pointPicker.getItems().setAll(array);
	}
	
	private void insertDB() {
		PopupWindow pop = new PopupWindow();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", nameField.getText());
		map.put("comment", commentArea.getText());
		//map.put("if_count", if_countPicker.getValue().toString());
		//map.put("publish_status", "未发布");
		//if(if_countPicker.getValue().toString().equals("是")) {
		map.put("totalPoint", pointPicker.getValue().toString());
		map.put("branch", LoginController.branch);
		//}
		DBhelper dbHelper = new DBhelper();
		if(dbHelper.insert(map, "training_list")) {
			Loader loader = new Loader();
			pop.confirmButton.setOnAction(e->{
				pop.stage.close();
				loader.loadVBox(box, "/View/TrainningList.fxml");
			});
			pop.confirmWindow("新建课件成功", "新建课件成功");
		}else {
			pop.errorWindow();
		}
}
}

