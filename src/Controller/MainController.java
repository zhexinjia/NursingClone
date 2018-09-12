package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	@FXML private BorderPane mainPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadVBox("/View/Welcome.fxml");
		/*
		if(LoginController.branch.equals("0")) {
			loadVBox("/View/MainBranch.fxml");
		}else {
			loadVBox("/View/Welcome.fxml");
		}
		*/
	}
	
	
	//点击左边链接打开不同界面
	@FXML
    void openUserFile() {
		loadVBox("/View/UserList.fxml");
    }

    @FXML
    void openDepartmentFile() {
    		loadVBox("/View/DepartmentList.fxml");
    }

    @FXML
    void openTestList() {
    		loadVBox("/View/BankList.fxml");
    }

    //@FXML
    //void openNewTest() {
    	//	loadVBox("/View/UserNew.fxml");
    //}

    @FXML
    void openTestStats() {
    		loadVBox("/View/TestList.fxml");
    }
    
    @FXML
    void openOfflineTest() {
    		loadVBox("/View/TestOffline.fxml");
    }

    @FXML
    void openStudyList() {
    		loadVBox("/View/StudyList.fxml");
    }

    @FXML
    void openTrainingList() {
    		loadVBox("/View/TrainningList.fxml");
    }

    @FXML
    void openStudyStats() {
    		loadVBox("/View/StudyStatus.fxml");
    }

    @FXML
    void openMeetingList() {
    		loadVBox("/View/MeetingList.fxml");
    }

    @FXML
    void openMettingStats() {
    		loadVBox("/View/MeetingStatus.fxml");
    }

    @FXML
    void openRecordList() {
    		loadVBox("/View/RecordList.fxml");
    }

    @FXML
    void openRecordStats() {
    		loadVBox("/View/RecordStatus.fxml");
    }

    @FXML
    void openScheduleList() {
    		loadVBox("/View/ScheduleList.fxml");
    }

    @FXML
    void openScheduleManagement() {
    		loadVBox("/View/ScheduleManagement.fxml");
    }
    
    @FXML
    void hospitalReportButton() {
    		loadVBox("/View/HospitalReport.fxml");
    }
    
    @FXML
    void departmentReportButton() {
    		loadVBox("/View/DepartmentReport.fxml");
    }
    
    @FXML
    void personalReportButton() {
    		loadVBox("/View/PersonReport.fxml");
    }
	
    //载入split pane的右半边 VBox
	private void loadVBox(String path) {
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource(path));
	        VBox mainView;
			mainView = (VBox) loader.load();	
			mainPane.setCenter(mainView);
		} catch (IOException e) {
			System.out.println("in exception");
			e.printStackTrace();
		}
	}
	
}
