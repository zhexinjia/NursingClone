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

public class TestOfflineDetailListController implements Initializable{
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
	String totalPoint;
	DBhelper dbHelper = new DBhelper();
	ArrayList<HashMap<String, String>> list;
	public static HashMap<String, String> selectedUser;
	
	String[] keys = { "ssn", "name", "section_id", "finish_status","score_list", "score", "comment", "taken_date"};
    String[] fields = {"工号", "名字", "档期编号", "是否完成", "得分情况", "总得分","备注", "考核日期"};

    String name;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedTest = TestOfflineController.selectedTest;
		System.out.println("selectedTest:" + selectedTest);
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
    
    @FXML
    void TestOfflineListButton() {
    		loader.loadVBox(box, "/View/TestOffline.fxml");
    }
    
    @FXML void detailButton() {
    		selectedUser = tableView.getSelectionModel().getSelectedItem();
    		if(selectedUser != null) {
    			loader.loadVBox(box, "/View/UserTestOfflineDetail.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("查看失败", "请选中一个用户");
    		}
    }
    
    @FXML 
    void cancelRegisterButton() {
    		selectedUser = tableView.getSelectionModel().getSelectedItem();
		if(selectedUser != null) {
			dbHelper.cancelRegister(selectedUser);
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("查看失败", "请选中一个用户");
		}
    		
    }
    
    @FXML void modifyButton() {
    	
    }
   
    
    @FXML 
    void deleteButton() {
		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
		
		PopupWindow pop = new PopupWindow();
		if(selected == null) {
			pop.alertWindow("操作失败", "请选中一个用户");
		}else {
			pop.confirmButton.setOnAction(e->{
				if(!dbHelper.delete(selected, "offlineexam_history")) {
					pop.errorWindow();
				}else {
					pop.stage.close();
					getList();
					reload();
				}
			});
			pop.confirmWindow("确认要删除该考核吗？", "点击确认删除考核记录");
		}
    }
    
    @FXML
    void importButton() {
		ArrayList<HashMap<String, String>> importlist = loader.importExcel(keys, fields);
		int importLength = importlist.size();
		int oldLength = list.size();
		int newLength;
		System.out.println("importLength" + importLength);
		String exam_name = selectedTest.get("exam_name");
		String totalScore = selectedTest.get("totalPoint");
		
		if(importlist!=null) {
			if (dbHelper.insertOfflineTest(importlist, examID, exam_name, totalScore)) {
				getList();
				reload();
				newLength = list.size();
				if((importLength + oldLength) != newLength) {
					int diff = newLength - oldLength;
					System.out.println("diff" + diff);
					
					PopupWindow pop = new PopupWindow();
	    				pop.alertWindow("部分导入失败", "总导入行数：" + importLength +
	    						",  实际导入行数："+ diff + ",\n              报错行数：第"+ (diff+1) + "行");
				}
			}else {
				getList();
				reload();
				newLength = list.size();
				int diff = newLength - oldLength;
				System.out.println("diff" + diff);
				
				PopupWindow pop = new PopupWindow();
    				pop.alertWindow("部分导入失败", "总导入行数：" + importLength +
    						",  实际导入行数："+ diff + ", \n              报错行数：第"+ (diff+1) + "行");
			}
		}
		
    }
    
    @FXML
    void exportButton() {
    		String[] fieldlist = {"姓名", "科室", "职位","职称","层级", "完成", "成绩"};
    		String[] keylist = {"name", "department", "position", "title", "level", "finish", "score"};
    		loader.exportExcel(list, fieldlist, keylist);
    }
    
    
    private void setupTable() {
    		String[] tableKeys = {"name", "department", "position", "title", "level", "finish", "score"};
        String[] tableFields = {"姓名", "科室", "职位","职称","层级", "完成", "成绩"};
		loader.setupTable(tableView, tableKeys, tableFields);
	}
    
	private void getList() {
		
		String[] columns = {"offlineexam_history.id as id", "user_primary_info.department", "user_primary_info.name", 
				"user_primary_info.position", "user_primary_info.title", "user_primary_info.level", "offlineexam_history.finish_status as finish", 
				"offlineexam_history.score as score", "offlineexam_list.totalPoint", "offlineexam_history.offlineexam_id", "offlineexam_history.section_id"};
		String[] searchColumns = {"offlineexam_history.offlineexam_id"};
		String[] searchValues = {examID};
		String table = "offlineexam_history inner join user_primary_info on offlineexam_history.ssn = user_primary_info.ssn "
				+ "inner join offlineexam_list on offlineexam_history.offlineexam_id  = offlineexam_list.id";
		
		list = dbHelper.getList(searchColumns, searchValues, table, columns);
		System.out.println("list:" +list);
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
		double totalPoint = Double.parseDouble(selectedTest.get("totalPoint"));
		for(Double score:scores) {
			sum+=score;
			if(score/totalPoint >= 0.6) {
				passed++;
			}
		}
		Double ave = sum/size;
		Double fin = 100*(double) finished/(double)size;
		Double pass = 100*(double)passed/(double)size;
		passPercent.setText(String.format("%.1f", pass)+"%");
		finishPercent.setText(String.format("%.1f", fin)+"%");
		average.setText(String.format("%.2f", ave));
		setupChart(scores, totalPoint);
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

