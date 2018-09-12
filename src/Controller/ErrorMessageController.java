package Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ErrorMessageController {
	@FXML AnchorPane pane;
	
	@FXML
	void closeButton() {
		Stage stage = (Stage)pane.getScene().getWindow();
		stage.close();
	}
	@FXML
	void contactButton() {
		
	}
}
