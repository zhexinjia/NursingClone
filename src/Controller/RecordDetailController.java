package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class RecordDetailController implements Initializable {	
	@FXML TableView<HashMap<String, String>> sumTable;
	@FXML TableView<HashMap<String, String>> testTable;
	@FXML TableView<HashMap<String, String>> studyTable;
	@FXML TableView<HashMap<String, String>> trainingTable;
	@FXML TableView<HashMap<String, String>> meetingTable;
	@FXML TableView<HashMap<String, String>> offLineExamTable;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	Loader loader = new Loader();
	ArrayList<ArrayList<HashMap<String, String>>> list = new ArrayList<ArrayList<HashMap<String, String>>>();
	ArrayList<HashMap<String, String>> examList;
	ArrayList<HashMap<String, String>> studyList;
	ArrayList<HashMap<String, String>> trainingList;
	ArrayList<HashMap<String, String>> meetingList;
	ArrayList<HashMap<String, String>> offLineExamList;
	private HashMap<String, String> selectedUser;
	DBhelper dbHelper = new DBhelper();
	String ssn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedUser = RecordListController.selectedUser;
		ssn = selectedUser.get("ssn");
		setupTable();
		getList();
		setupSumTable();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML void contact(){
		loader.loadWeb();
	}


	@FXML
	void testDetail() {
		HashMap<String, String> selected = testTable.getSelectionModel().getSelectedItem();
		if(loader.selectionCheck(selected)) {
			TestDetailController.selectedUser = selected;
			loader.loadVBox(box, "/View/UserTestDetail.fxml");
		}
	}
	@FXML
	void RecordListButton(){
		loader.loadVBox(box, "/View/RecordList.fxml");
	}
	
    @FXML
    void searchButton() {
    		
    }

    @FXML
    void resetButton() {

    }

    @FXML
    void importButton() {

    }

    @FXML
    void exportButton() {

    }



    @FXML
    void newButton() {
    		
    }

    @FXML
    void modifyButton() {
    		
    }

    @FXML
    void deleteButton() {
    }
	
	private void setupTable() {
		loader.setupTable(sumTable, new String[] {"date", "detail"}, new String[] {"日期","得分详情"});
		loader.setupTable(testTable, new String[] {"examName", "score"}, new String[] {"考试", "得分"});
		loader.setupTable(studyTable, new String[] {"name", "finish_status"}, new String[] {"课件名称","是否完成"});
		loader.setupTable(trainingTable, new String[] {"name", "point"}, new String[] {"培训名称", "得分"});
		loader.setupTable(meetingTable, new String[] {"name", "checkin", "checkout"}, new String[] {"会议", "签到", "签出"});
		loader.setupTable(offLineExamTable, new String[] {"exam_name", "score"}, new String[] {"考核","得分"});
	}
	
	private void setupSumTable() {
		if(selectedUser.get("comment")!=null) {
			String[] comments = selectedUser.get("comment").split(",|，");
			ArrayList<HashMap<String, String>> sumList = new ArrayList<HashMap<String, String>>();
			for(String comment:comments) {
				String[] info = comment.split(":");
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("date", info[0]);
				//use info.length to check if info[1] is null, shouldn't be null if added info correct
				temp.put("detail", info.length==1?"":info[1]);
				sumList.add(temp);
			}
			sumTable.getItems().setAll(sumList);
		}
		
	}
	
	private void getList() {
		getExamList();
		getStudyList();
		getTrainingList();
		getMeetingList();
		getOffLineExamList();
		studyTable.getItems().setAll(studyList);
		testTable.getItems().setAll(examList);
		trainingTable.getItems().setAll(trainingList);
		meetingTable.getItems().setAll(meetingList);
		offLineExamTable.getItems().setAll(offLineExamList);
	}
	
	private void getExamList() {
		String[] searchColumn = {"exam_history.ssn"};
		String[] values = {ssn};
		String tableName = "exam_history inner join exam_list on exam_history.exam_id = exam_list.id";
		String[] columns = {"exam_history.exam_id", "exam_history.id", "exam_list.examName", "exam_history.score"};
		examList =dbHelper.getList(searchColumn, values, tableName, columns);
		/*
		list = dbHelper.getRecordLists(selectedUser.get("ssn"));
		examList = list.get(0);
		studyList = list.get(1);
		trainingList = list.get(2);
		meetingList = list.get(3);
		studyTable.getItems().setAll(studyList);
		testTable.getItems().setAll(examList);
		trainingTable.getItems().setAll(trainingList);
		meetingTable.getItems().setAll(meetingList);
		*/
	}
	
	private void getStudyList() {
		String[] searchColumn = {"study_history.ssn"};
		String[] values = {ssn};
		String tableName = "study_history inner join study_list on study_history.study_id = study_list.id";
		String[] columns = {"study_list.name", "study_history.finish_status"};
		studyList =dbHelper.getList(searchColumn, values, tableName, columns);
	}
	
	private void getTrainingList() {
		String[] searchColumn = {"training_history.ssn"};
		String[] values = {ssn};
		String tableName = "training_history inner join training_list on training_history.training_id = training_list.id";
		String[] columns = {"training_list.name", "training_history.point"};
		trainingList =dbHelper.getList(searchColumn, values, tableName, columns);
	
	}
	
	private void getMeetingList() {
		String[] searchColumn = {"meeting_history.ssn"};
		String[] values = {ssn};
		String tableName = "meeting_history inner join meeting_list on meeting_history.meeting_id = meeting_list.id";
		String[] columns = {"meeting_list.name", "meeting_history.checkin", "meeting_history.checkout"};
		meetingList =dbHelper.getList(searchColumn, values, tableName, columns);
	
	}
	
	private void getOffLineExamList() {
		String[] searchColumn = {"offlineexam_history.ssn"};
		String[] values = {ssn};
		String tableName = "offlineexam_history inner join offlineexam_list on offlineexam_history.offlineexam_id = offlineexam_list.id";
		String[] columns = {"offlineexam_history.offlineexam_id", "offlineexam_history.id", "offlineexam_list.exam_name", "offlineexam_history.score"};
		offLineExamList =dbHelper.getList(searchColumn, values, tableName, columns);
	}

}
