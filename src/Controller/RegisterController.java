package Controller;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegisterController {
	@FXML TextField userNameField;
	@FXML PasswordField passwordField;
	@FXML PasswordField passwordField2;
	@FXML TextField hospitalNameField;
	@FXML TextField codeField;
	
    @FXML private AnchorPane box;
    
    String userName;
    String hospitalName;
    public static String hospitalID;
    Loader loader = new Loader();
    
    @FXML void contactButton(){
    		loader.loadWeb();
    }
    
    @FXML void loginButton() {
    		Stage currentStage = (Stage)box.getScene().getWindow();
    		currentStage.close();
    }
    
    @FXML void registerButton() {
    		register();
    }
    
    //TODO: register return string from server, display message
    private void register() {
    		PopupWindow pop = new PopupWindow();
    		if(validate() && passwordValidate()) {
    			DBhelper dbHelper = new DBhelper();
    			if(dbHelper.register(userNameField.getText(), passwordField.getText(), hospitalNameField.getText(), codeField.getText())) {
    				pop.confirmButton.setOnAction(e->{
    					loginButton();
    				});
    			}
    		}
    }
    
    boolean passwordValidate() {
    		if(passwordField.getText().equals(passwordField2.getText())) {
    			return true;
    		}
    		PopupWindow pop = new PopupWindow();
    		pop.alertWindow("密码错误", "确认密码和密码不同");
    		return false;
    }
    
    boolean validate() {
    		PopupWindow pop = new PopupWindow();
    		if(userNameField.getText().trim().isEmpty()) {
    			pop.alertWindow("所有选项不能为空", "所有选项不能为空");
    			return false;
    		}
    		if(passwordField.getText().trim().isEmpty()) {
    			pop.alertWindow("所有选项不能为空", "所有选项不能为空");
    			return false;
    		}
    		if(hospitalNameField.getText().trim().isEmpty()) {
    			pop.alertWindow("所有选项不能为空", "所有选项不能为空");
    			return false;
    		}
    		if(codeField.getText().trim().isEmpty()) {
    			pop.alertWindow("所有选项不能为空", "所有选项不能为空");
    			return false;
    		}
    		return true;
    }

}
