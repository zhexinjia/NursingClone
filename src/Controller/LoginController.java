package Controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {
	@FXML TextField usernameField;
	@FXML PasswordField passwordField;
    @FXML private AnchorPane box;
    
    String userName;
    String hospitalName;
    PopupWindow popUP = new PopupWindow();
    Loader loader = new Loader();
    
    public static String database;
    public static String url;
    public static String branch;
    public static String path;
    
    @FXML void contactButton(){
    		loader.loadWeb();
    }
    
    @FXML void loginButton() {
    		if(login()) {
    			Stage loginStage = (Stage) box.getScene().getWindow();
    			loginStage.close();
    			openView("/View/Main.fxml");
    		}else {
    			popUP.alertWindow("登陆失败", "账号或密码错误");
    		}
    }
    
    @FXML void registerButton() {
    		openRegister("/View/Register.fxml");
    }
  
	private boolean login() {
		if (usernameField.getText().trim().isEmpty() && passwordField.getText().trim().isEmpty()) {
			return false;
		}else {
			DBhelper request = new DBhelper();
			String param = "username="+usernameField.getText() + "&password=" + passwordField.getText();
			String output = request.sendHospitalGet("http://zhexinj.cn/API/login.php", param);
			//String output = request.sendHospitalGet("http://LocalHost/API/test.php", param);

			System.out.println(output);
			if(output.equals("no input")) {
				System.out.println("no input");
				return false;
			}else {
				JSONParser parser = new JSONParser();
				JSONArray temp = new JSONArray();
				try {
					temp = (JSONArray) parser.parse(output);
				} catch (ParseException e) {
					return false;
				}
				if (temp.size() != 0)  {
					JSONObject userInfo = (JSONObject) temp.get(0);
					userName = usernameField.getText();
					database = (String) userInfo.get("data_base");
					url = (String) userInfo.get("url");
					branch = (String)userInfo.get("branch");
					path = (String)userInfo.get("path");
					return true;
				}
			}
		}
		return false;
	}
	
	
	private void openRegister(String path) {
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(path));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);
            //primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            //primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void openView(String path) {
		Stage primaryStage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(path));
            SplitPane rootLayout = (SplitPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            //primaryStage.setFullScreen(true);
            //primaryStage.setResizable(false);
            primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
