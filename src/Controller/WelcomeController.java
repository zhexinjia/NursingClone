package Controller;

import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class WelcomeController implements Initializable {

    @FXML
    private VBox box;
    
    //Branch page start
    @FXML private JFXComboBox<String> branchPicker;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		if(LoginController.branch.equals("0")) {
			System.out.println("0");
		}
	}
    
    @FXML void changePW() {
    		
    }
    @FXML void newBranch() {
    	
    }
    //Branch page end

    @FXML
    void downloadMultiTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/multiQATemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }

    @FXML
    void downloadSingleTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/SingleQATemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }

    @FXML
    void downloadTfTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/tfQATemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }

    @FXML
    void downloadTrainingTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/trainingTemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }

    @FXML
    void downloadUserGuide() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/guide.pdf").toURI());
		}catch(Exception e) {
			
		}
    }

    @FXML
    void downloadUserTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/userTemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }
    
    @FXML
    void downloadOfflineTestTemplate() {
    		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn/template/offlineTemplate.xlsx").toURI());
		}catch(Exception e) {
			
		}
    }

}
