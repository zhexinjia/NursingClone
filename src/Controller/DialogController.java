package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DialogController {
	@FXML Label title;
	@FXML Label content;
	@FXML AnchorPane pane;
	
	@FXML void closeButton() {
		close();
	}
	@FXML void confirmButton() {
		close();
	}
	private void close() {
		Stage currentStage = (Stage) pane.getScene().getWindow();
		currentStage.close();
	}
	public void setTitle(String title) {
		this.title.setText(title);
	}
	public void setContent(String content) {
		this.content.setText(content);
	}
}
