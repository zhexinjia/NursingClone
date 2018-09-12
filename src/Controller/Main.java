package Controller;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	
	private AnchorPane anchorPane;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Login.fxml"));
			//loader.setLocation(Main.class.getResource("/View/AlertMessage.fxml"));
			anchorPane = (AnchorPane)loader.load();
			Scene scene = new Scene(anchorPane);
			//remove for later
			//primaryStage.setTitle("护理宝");
			//primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			
			//FIXME: remove for later, this code remove title bar
			//primaryStage.initStyle(StageStyle.UNDECORATED);

			//primaryStage.setFullScreen(true);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
