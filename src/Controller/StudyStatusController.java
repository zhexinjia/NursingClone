package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import Model.DBhelper;
import Model.Loader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class StudyStatusController implements Initializable {
	@FXML VBox box;
	@FXML TableView<HashMap<String, String>> studyTableView;
	@FXML TableView<HashMap<String, String>> trainingTableView;
	@FXML Label studyPercent;
	@FXML Label studyHighestPercent;
	@FXML Label studyLowestPercent;
	@FXML Label trainingPercent;
	@FXML Label trainingHighestPercent;
	@FXML Label trainingLowestPercent;
	
	Loader loader = new Loader();
	DBhelper dbHelper = new DBhelper();
	ArrayList<HashMap<String, String>> studyList;
	ArrayList<HashMap<String, String>> studyHistory;
	ArrayList<HashMap<String, String>> trainingList;
	ArrayList<HashMap<String, String>> trainingHistory;
	public static HashMap<String, String> selectedStudy;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		getLists();
		caculateStudyPercent();
		caculateTrainingPercent();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    @FXML
    void studyDetailButton() {
    		selectedStudy = studyTableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selectedStudy)) {
    			loader.loadVBox(box, "/View/StudyDetail.fxml");
    		}
    		
    }
    @FXML
    void trainningDetailButton() {
    		HashMap<String, String> selected = trainingTableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selected)) {
    			TrainningListController.selectedTrainning = selected;
    			loader.loadVBox(box, "/View/TrainningDetail.fxml");
    		}
    }
    
    void setupTable(){
    		String[] keys = {"name", "percent"};
		String[] fields = {"课件信息", "出勤率"};
		loader.setupTable(studyTableView, keys, fields);
		String[] fields1 = {"培训信息", "出勤率"};
		loader.setupTable(trainingTableView, keys, fields1);
    }
    
    private void getLists() {
    		getStudyList();
    		getStudyHistoryList();
    		getTrainingList();
    		getTrainingHistoryList();
    }
    private void reload() {
		ObservableList<HashMap<String, String>> studyOBlist = loader.search(studyList, "");
		studyTableView.setItems(studyOBlist);
		ObservableList<HashMap<String, String>> trainingOBlist = loader.search(trainingList, "");
		trainingTableView.setItems(trainingOBlist);
		//countLabel.setText("共 " +searchList.size()+ " 条");
    }
    
    private void getStudyList() {
		String[] columns = {"id", "name"};
		String tableName = "study_list";
		//studyList = dbHelper.getList(tableName, columns);
		String[] searchColumn = {"branch"};
		String[] values = {LoginController.branch};
		studyList = dbHelper.getList(searchColumn, values, tableName, columns);
    }    
    private void getStudyHistoryList() {
		String[] columns = {"study_history.study_id", "study_history.finish_status",
				"user_primary_info.ssn", "user_primary_info.department", "user_primary_info.name", "user_primary_info.position", 
				"user_primary_info.title", "user_primary_info.level"};
		String tableName = "user_primary_info inner join study_history on study_history.ssn = user_primary_info.ssn "
				+ "inner join study_list on study_list.id = study_history.study_id WHERE study_list.branch = '" + 
				LoginController.branch + "'";
		studyHistory = dbHelper.getList(tableName, columns);
    }
    private void getTrainingList() {
		String[] columns = {"id", "name", "if_count"};
		String tableName = "training_list";
		//trainingList = dbHelper.getList(tableName, columns);
		String[] searchColumn = {"branch"};
		String[] values = {LoginController.branch};
		trainingList = dbHelper.getList(searchColumn, values, tableName, columns);
    }    
    private void getTrainingHistoryList() {
		String[] columns = {"training_history.training_id", "training_history.finish_status", 
				"user_primary_info.ssn", "user_primary_info.department", "user_primary_info.name", "user_primary_info.position", 
				"user_primary_info.title", "user_primary_info.level"};
		String tableName = "user_primary_info inner join training_history on training_history.ssn = user_primary_info.ssn "
				+ "inner join training_list on training_list.id = training_history.training_id WHERE training_list.branch = '" + 
				LoginController.branch + "'";
		trainingHistory = dbHelper.getList(tableName, columns);
    }
    
    private void caculateStudyPercent() {
		int totalCount = 0;
		HashMap<String, int[]> countMap = new HashMap<String, int[]>(); 
		for (HashMap<String, String> map:studyHistory) {
			int finish = 0;
			int totalNum = 1;
			if(map.get("finish_status").equals("是")) {
				finish = 1;
				totalCount++;
			}	
			String id = map.get("study_id");
			if(countMap.get(id) == null) {
				countMap.put(id, new int[] {totalNum, finish});
			}else {
				int[] list = countMap.get(id);
				list[0] += totalNum;
				list[1] += finish;
				countMap.put(id, list);
			}
		}
		//countMap done
		double highest = 0;
		double lowest = 1;
		for(HashMap<String, String> study:studyList) {
			String id = study.get("id");
			int[] resultList = countMap.get(id);
			if(resultList != null) {
				double percent = (double)resultList[1]/(double)resultList[0];
				
				study.put("percent", Double.toString(Math.round(percent*10000)/100.0)+"%");
    			if (percent > highest) {
    				highest = percent;
    			}
    			if(percent < lowest) {
    				lowest = percent;
    			}
			}
		}
		double totalP = (double)totalCount/(double) studyHistory.size();
		studyHighestPercent.setText(Double.toString(Math.round(highest*100))+"%");
		studyLowestPercent.setText(Double.toString(Math.round(lowest*100))+"%");
		studyPercent.setText(Double.toString(Math.round(totalP*100))+"%");
    }
    
    private void caculateTrainingPercent() {
		int totalCount = 0;
		HashMap<String, int[]> countMap = new HashMap<String, int[]>(); 
		for (HashMap<String, String> map:trainingHistory) {
			int finish = 0;
			int totalNum = 1;
			if(map.get("finish_status").equals("是")) {
				finish = 1;
				totalCount++;
			}	
			String id = map.get("training_id");
			if(countMap.get(id) == null) {
				countMap.put(id, new int[] {totalNum, finish});
			}else {
				int[] list = countMap.get(id);
				list[0] += totalNum;
				list[1] += finish;
				countMap.put(id, list);
			}
		}
		//countMap done
		double highest = 0;
		double lowest = 1;
		for(HashMap<String, String> training:trainingList) {
			String id = training.get("id");
			int[] resultList = countMap.get(id);
			if(resultList != null) {
				double percent = (double)resultList[1]/(double)resultList[0];
				training.put("percent", Double.toString(percent*100)+"%");
				System.out.println(percent);
    			if (percent > highest) {
    				highest = percent;
    			}
    			if(percent < lowest) {
    				lowest = percent;
    			}
			}
		}
		double totalP = (double)totalCount/(double) trainingHistory.size();
		trainingHighestPercent.setText(Double.toString(Math.round(highest*100))+"%");
		trainingLowestPercent.setText(Double.toString(Math.round(lowest*100))+"%");
		trainingPercent.setText(Double.toString(Math.round(totalP*100))+"%");
    }
	

}

