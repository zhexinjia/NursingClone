package Controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import Model.SFTPtool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StudyNewController implements Initializable {
	
	@FXML JFXTextField nameField;
	@FXML JFXTextField pathField;
	//@FXML JFXTextField urlField;
	//@FXML JFXComboBox<String> if_countPicker;
	@FXML JFXComboBox<Integer> pointPicker;
	@FXML JFXComboBox<String> typePicker;
	@FXML JFXButton smallButton;
	@FXML JFXButton newButton;
	@FXML ProgressBar progressBar;
	@FXML Text progressLabel;
	@FXML StackPane stackPane;
	@FXML VBox box;
	
	
	//FIXME: path should store this information in database
	String path;
	Loader loader = new Loader();
	File file;
	String url;
	String branch;
	boolean fileType = true;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		url = LoginController.url;
		path = LoginController.path;
		branch = LoginController.branch;
		//progressBar.setVisible(false);
		setupChoiceBox();
		stackPane.setVisible(false);
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML 
    void studyListButton() {
    	loader.loadVBox(box, "/View/StudyList.fxml");
    }
    
    
    @FXML void createButton() {
    		if(validate()) {
    			if(fileType) {
    				stackPane.setVisible(true);
    				newButton.setDisable(true);
        			SFTPtool sftp = new SFTPtool(file, path, file.getName());
        			sftp.setOnSucceeded(EventHandler->{
        				this.insertDB();
        				stackPane.setVisible(false);
        				newButton.setDisable(false);
        			});
        			progressBar.progressProperty().bind(sftp.progressProperty());
        			progressLabel.textProperty().bind(sftp.getPercentage());
        			Thread thread = new Thread(sftp);
        			thread.start();
    			}else {
    				this.insertDB();
    			}
    			
    			/*
    			if(typePicker.getValue().toString().equals("网址")) {
    				this.insertDB();
    			}else {
    				stackPane.setVisible(true);
    				newButton.setDisable(true);
        			SFTPtool sftp = new SFTPtool(file, path, file.getName());
        			sftp.setOnSucceeded(EventHandler->{
        				this.insertDB();
        				stackPane.setVisible(false);
        				newButton.setDisable(false);
        			});
        			progressBar.progressProperty().bind(sftp.progressProperty());
        			progressLabel.textProperty().bind(sftp.getPercentage());
        			Thread thread = new Thread(sftp);
        			thread.start();
    			}
    			*/
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "所有选项不能为空。");
    		}
    }
    
    @FXML void chooseButton() {
    		file = loader.openFileChooser();
    		if(file!=null) {
    			pathField.setText(file.getAbsolutePath());
    		}
    }
    
    private void setupChoiceBox() {
    		String[] type = {"文档","视频"};
    		typePicker.getItems().setAll(type);
    		/*
    		typePicker.setOnAction(e->{
    			if(typePicker.getSelectionModel().getSelectedItem().equals("网址")) {
    				smallButton.setDisable(true);
    				urlField.setDisable(false);
    				pathField.setText(null);
    				fileType = false;
    			}
    			if(typePicker.getSelectionModel().getSelectedItem().equals("文件")) {
    				smallButton.setDisable(false);
    				urlField.setDisable(true);
    				urlField.setText(null);
    				fileType = true;
    			}
    		});
    		*/
    		
    		/*
    		String[] tfList = {"是", "否"};
    		if_countPicker.getItems().setAll(tfList);
    		if_countPicker.setOnAction(e->{
    			if(if_countPicker.getSelectionModel().getSelectedItem().equals("是")) {
    				pointPicker.setDisable(false);
    			}else if(if_countPicker.getSelectionModel().getSelectedItem().equals("否")){
    				pointPicker.setValue(0);
    				pointPicker.setDisable(true);
    				pointPicker.setValue(null);
    			}
    		});
    		*/
    		Integer[] array = new Integer[101];
    		Arrays.setAll(array, i -> i);
    		pointPicker.getItems().setAll(array);
    }
    
    private boolean validate() {
    		if(nameField.getText().trim().isEmpty()) {
    			return false;
    		}
    		if(typePicker.getValue()==null) {
    			return false;
    		}else {
    			if(file == null) {
        			return false;
        		}
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
    
    
    private void insertDB() {
    		//PopupWindow pop = new PopupWindow();
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("name", nameField.getText());
    		//map.put("publish_status", "未发布");
    		String type = typePicker.getValue().toString();
    		map.put("type", type);
    		map.put("url", url+file.getName());
    		map.put("file_name", file.getName());
    		map.put("branch", branch);
    		/*
    		if(fileType) {
    			map.put("url", url+file.getName());
    		}else {
    			map.put("url", urlField.getText());
    		}
    		*/
    		//map.put("if_count", if_countPicker.getValue().toString());
    		//if(if_countPicker.getValue().toString().equals("是")) {
    		map.put("point", pointPicker.getValue().toString());
    		//}
    		DBhelper dbHelper = new DBhelper();
    		/*
    		if(dbHelper.insert(map, "study_list")) {
    			//TODO: modify confirmButton
    			pop.confirmButton.setOnAction(e->{
    				pop.stage.close();
    				loader.loadVBox(box, "/View/StudyList.fxml");
    			});
    			pop.confirmWindow("新建课件成功", "新建课件成功");
    		}else {
    			pop.errorWindow();
    		}
    		*/
    		if (dbHelper.insert(map, "study_list")) {
    			loader.loadVBox(box, "/View/StudyList.fxml");
    		}
    }
    
    

	

}

