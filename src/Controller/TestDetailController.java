package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TestDetailController implements Initializable{
	@FXML VBox box;
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML PieChart pieChart;
	@FXML Label countLabel;
	@FXML Label passPercent;
	@FXML Label average;
	@FXML Label finishPercent;
	
	Loader loader = new Loader();
	private HashMap<String, String> selectedTest;
	private String examID;
	DBhelper dbHelper = new DBhelper();
	ArrayList<HashMap<String, String>> list;
	public static HashMap<String, String> selectedUser;
	
	String[] keys = {"name", "department", "position", "title", "level", "finish", "score"};
    String[] fields = {"姓名", "科室", "职位","职称","层级", "完成", "成绩"};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//FXIME: get total score of the test??????????? selected count of single, multi and tf, then add?
		selectedTest = TestListController.selectedTest;
		examID = selectedTest.get("id");
		setupTable();
		getList();
		setupLabelandChart();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML void detailButton() {
    		selectedUser = tableView.getSelectionModel().getSelectedItem();
    		
    		String id = selectedTest.get("question_bank");
    		boolean bankCheck = dbHelper.checkbankID(id);
    		
    		if (selectedUser.get("finish").equals("否")) {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("查看失败", "用户还没完成考试");
    		}else if (bankCheck == false) {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("查看失败", "考试题库已删除");
    		}
    		else if(selectedUser != null) {
    			loader.loadVBox(box, "/View/UserTestDetail.fxml");
    		}
    		else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("查看失败", "请选中一个用户");
    		}
    }
    
    @FXML void modifyButton() {
    	
    }
    
    @FXML
    void testListButton() {
    		loader.loadVBox(box, "/View/TestList.fxml");
    }
    
    @FXML
    void exportButton() {
    		String[] fieldlist = fields;
    		String[] keylist = keys;
    		loader.exportExcel(list, fieldlist, keylist);
    }
    
    
    private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
    
	private void getList() {
		//TODO: how to count total point??? should we remove it?
		String[] columns = {"exam_history.exam_id", "exam_history.id as id", "user_primary_info.department", "user_primary_info.name", "user_primary_info.position", 
				"user_primary_info.title", "user_primary_info.level", "exam_history.finish as finish", "exam_history.score as score", "exam_history.totalScore as totalScore"};
		String[] searchColumns = {"exam_history.exam_id"};
		String[] searchValues = {examID};
		
		list = dbHelper.getList(searchColumns, searchValues, "exam_history inner join user_primary_info on exam_history.ssn = user_primary_info.ssn", columns);
		
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, "");
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	private void setupLabelandChart() {
		int size = list.size();
		int finished = 0;
		int passed = 0;
		Double sum = 0.0;
		double totalScore = 0.0;
		if (size != 0) {
			String ts = list.get(0).get("totalScore");
			totalScore = Double.parseDouble(ts);
		}
		
		ArrayList<Double> scores = new ArrayList<Double>();
		for(HashMap<String, String> map:list) {
			if(map.get("finish").equals("是")) {
				finished++;
			}
			if (map.get("score")!=null) {
				scores.add(Double.parseDouble(map.get("score")));
			}else {
				scores.add(0.0);
			}
		}
		//double totalPoint = Double.parseDouble(selectedTest.get("totalPoint"));
		for(Double score:scores) {
			sum+=score;
			if(score/totalScore >= 0.6) {
				passed++;
			}
		}
		Double ave = sum/size;
		Double fin = 100*(double) finished/(double)size;
		Double pass = 100*(double)passed/(double)size;
		passPercent.setText(String.format("%.1f", pass)+"%");
		finishPercent.setText(String.format("%.1f", fin)+"%");
		//average.setText(ave.toString());
		average.setText(String.format("%.2f", ave));
		setupChart(scores, totalScore);
	}	
	
	private void setupChart(ArrayList<Double> scores, Double totalScore) {
		String[] infoArray = {"低于60", "60-69", "70-79", "80-89", "90-100"};
		int[] countArray = {0, 0, 0, 0, 0};
		for (Double score:scores) {
			Double result = score/totalScore;
			if (result < 0.6) {
				countArray[0]++;
			}else if(result < 0.7) {
				countArray[1]++;
			}else if(totalScore < 0.8) {
				countArray[2]++;
			}else if(totalScore < 0.9) {
				countArray[3]++;
			}else{
				countArray[4]++;
			}
		}
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (int i = 0; i< 5; i++) {
			pieChartData.add(new PieChart.Data(infoArray[i], countArray[i]));
		}
		pieChart.getData().setAll(pieChartData);
		
	}
	

	

}

