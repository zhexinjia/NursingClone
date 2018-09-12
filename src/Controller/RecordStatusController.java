package Controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import com.jfoenix.controls.JFXComboBox;

import Model.DBhelper;
import Model.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class RecordStatusController implements Initializable {

	@FXML VBox box;
	@FXML PieChart pieChart;
	@FXML Label averageField;
	@FXML Label highestField;
	@FXML Label lowestField;
	@FXML Label departmentHighest;
	@FXML Label departmentLowest;
	@FXML Label departmentAverage;
	@FXML JFXComboBox<String> departmentPicker;
	@FXML TableView<HashMap<String, String>> departmentTableView;
	
	@FXML Label levelAvg;
	@FXML Label levelHighest;
	@FXML Label levelLowest;
	@FXML JFXComboBox<String> levelPicker;
	@FXML TableView<HashMap<String, String>> levelTableView;
	
	ArrayList<HashMap<String, String>> scoreList;
	HashMap<String, ArrayList<HashMap<String, String>>> departmentList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	HashMap<String, ArrayList<HashMap<String, String>>> levelList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	ArrayList<HashMap<String, String>> departmentExportList;
	ArrayList<HashMap<String, String>> levelExportList;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	String branch = LoginController.branch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		setupDepartmentTable();
		setupLevelTable();
		getList();
		caculatePercent();
		setupDepartmentPicker();
		System.out.println(branch);
		if (branch.equals("护理")) {
			setupLevelPicker();
		}	
	}
	@FXML void detailButton() {
		loader.loadVBox(box, "/View/RecordList.fxml");
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML
    void contactButton() {
		loader.loadWeb();
    }
	
	@FXML 
	void exportDepartmentButton(){
		String[] fieldlist = {"姓名", "职位", "层级", "得分"};
		String[] keylist = {"name", "position", "level", "currentScore"};
		loader.exportExcel(departmentExportList, fieldlist, keylist);
	}
	
	@FXML 
	void exportlevelButton(){
		String[] fieldlist = {"姓名", "科室", "职位", "得分"};
		String[] keylist = {"name", "department", "position", "currentScore"};
		loader.exportExcel(levelExportList, fieldlist, keylist);
	}
	
	void getList(){
		String tableName = "user_score inner join user_primary_info on user_primary_info.ssn = user_score.ssn "
				+ "where user_primary_info.branch = '" + LoginController.branch + "'";
		String[] columns = {"user_score.totalScore", "user_score.currentScore", "user_primary_info.name",
				"user_primary_info.department", "user_primary_info.level", "user_primary_info.position"};
		scoreList = dbHelper.getList(tableName, columns);
	}
	void caculatePercent() {
		Double highest = (double) 0;
		Double lowest = (double) 1;
		int[] countArray = {0, 0, 0, 0, 0};
		double sum = 0;
 		//HashMap<String, ArrayList<HashMap<String, String>>> departmentList = new HashMap<String, ArrayList<HashMap<String, String>>>();
		for(HashMap<String, String> score:scoreList) {
			double currentScore = Double.parseDouble(score.get("currentScore"));
			double totalScore = Double.parseDouble(score.get("totalScore"));
			double percent;
			if(totalScore != 0) {
				percent = currentScore/totalScore;
				score.put("percent", Double.toString(percent));
				sum += percent;
				if(percent > highest) {
					highest = percent;
				}
				if(percent < lowest) {
					lowest = percent;
				}
				if (percent < 0.6) {
					countArray[0]++;
				}else if(percent < 0.7) {
					countArray[1]++;
				}else if(percent < 0.8) {
					countArray[2]++;
				}else if(percent < 0.9) {
					countArray[3]++;
				}else if (percent <= 1){
					countArray[4]++;
				}
			}else {
				percent = 1.0;
				score.put("percent", Double.toString(percent));
				sum += percent;
				if(percent > highest) {
					highest = percent;
				}
				if(percent < lowest) {
					lowest = percent;
				}
				if (percent < 0.6) {
					countArray[0]++;
				}else if(percent < 0.7) {
					countArray[1]++;
				}else if(percent < 0.8) {
					countArray[2]++;
				}else if(percent < 0.9) {
					countArray[3]++;
				}else if (percent <= 1){
					countArray[4]++;
				}
			}
			String department = score.get("department");
			if(department!=null) {
				ArrayList<HashMap<String, String>> list;
				if(departmentList.get(department)==null) {
					list = new ArrayList<HashMap<String, String>>();
				}else {
					list = departmentList.get(department);
				}
				list.add(score);
				departmentList.put(department, list);
			}
			//TESTING
			
			String level = score.get("level");
			if(level!=null) {
				ArrayList<HashMap<String, String>> list;
				if(levelList.get(level)==null) {
					list = new ArrayList<HashMap<String, String>>();
				}else {
					list = levelList.get(level);
				}
				list.add(score);
				levelList.put(level, list);
			}
			
			//END TESTING
		}
		Double ave = sum/scoreList.size();
		DecimalFormat df = new DecimalFormat("#.##");
		averageField.setText(df.format(ave*100));
		lowestField.setText(df.format(lowest*100) + "%");
		highestField.setText(df.format(highest*100) + "%");
		setupChart(countArray);
	}
	
	void setupDepartmentPicker() {
		Set<String> keyset = departmentList.keySet();
		System.out.println("Department keyset: " + keyset);
		departmentPicker.getItems().setAll(keyset);
		departmentPicker.setOnAction(e->{
			loadDepartmentTable(departmentPicker.getSelectionModel().getSelectedItem());
		});
		String firstDepartment = keyset.iterator().next();
		//init default display
		departmentPicker.setValue(firstDepartment);
		loadDepartmentTable(firstDepartment);
		
	}
	void setupLevelPicker() {
		Set<String> keyset = levelList.keySet();
		System.out.println("Level keyset: " + keyset);
		levelPicker.getItems().setAll(keyset);
		levelPicker.setOnAction(e->{
			loadLevelTable(levelPicker.getSelectionModel().getSelectedItem());
		});
		String firstLevel = keyset.iterator().next();
		//init default display
		levelPicker.setValue(firstLevel);
		loadLevelTable(firstLevel);
	}
	
	private void loadDepartmentTable(String department) {
		if(department != null) {
			ArrayList<HashMap<String, String>> list = departmentList.get(department);
			ObservableList<HashMap<String, String>> searchList = loader.search(list, "");
			departmentTableView.setItems(searchList);
			//System.out.println("list: " + list);
			//System.out.println("search list: " + searchList);
			departmentExportList = list;
			caculateDepartment(list);
		}
	}
	
	private void loadLevelTable(String level) {
		if (level != null) {
			ArrayList<HashMap<String, String>> list = levelList.get(level);
			ObservableList<HashMap<String, String>> searchList = loader.search(list, "");
			levelTableView.setItems(searchList);
			levelExportList = list;
			calculateLevel(list);
		}
	}
	
	private void caculateDepartment(ArrayList<HashMap<String, String>> list) {
		Double total = (double) 0;
		Double low = list.size()!=0?(double)Double.parseDouble(list.get(0).get("currentScore")):0;
		Double high = list.size()!=0?(double)Double.parseDouble(list.get(0).get("currentScore")):0;
		for(HashMap<String, String> map:list) {
			//Double current = map.get("percent").equals("NaN")?100:Double.parseDouble(map.get("percent"));
			Double current = Double.parseDouble(map.get("currentScore"));
			total+=current;
			if(current > high) {
				high =current;
			}
			if(current < low) {
				low=current;
			}
		}
		Double average = total/list.size();
		DecimalFormat df = new DecimalFormat("#.##");
		departmentAverage.setText(df.format(average));
		departmentHighest.setText(df.format(high));
		departmentLowest.setText(df.format(low));
	}
	
	
	private void calculateLevel(ArrayList<HashMap<String, String>> list) {
		//System.out.println("list is : "+list);
		Double total = (double) 0;
		Double low = list.size()!=0?(double)Double.parseDouble(list.get(0).get("currentScore")):0;
		Double high = list.size()!=0?(double)Double.parseDouble(list.get(0).get("currentScore")):0;
		for(HashMap<String, String> map:list) {
			//Double current = map.get("percent").equals("NaN")?100:Double.parseDouble(map.get("percent"));
			Double current = Double.parseDouble(map.get("currentScore"));
			System.out.println("map: "+map);
			total+=current;
			if(current > high) {
				 high = current;
			}
			if(current < low) {
				low = current;
			}
		}
		Double average = total/list.size();
		DecimalFormat df = new DecimalFormat("#.##");
		levelAvg.setText(df.format(average));
		levelHighest.setText(df.format(high));
		levelLowest.setText(df.format(low));
	}
	
	private void setupDepartmentTable() {
		String[] keys = {"name", "position", "level", "currentScore"};
		String[] fields = {"姓名", "职位", "层级", "得分"};
		//System.out.println("dTV: "+ departmentTableView);
		loader.setupTable(departmentTableView, keys, fields);

	}
	
	private void setupLevelTable() {
		String[] keys = {"name", "department", "position", "currentScore"};
		String[] fields = {"姓名", "科室", "职位", "得分"};
		loader.setupTable(levelTableView, keys, fields);
	}
	
	private void setupChart(int[] countArray) {
		String[] infoArray = {"低于60", "60-69", "70-79", "80-89", "90-100"};
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (int i = 0; i< 5; i++) {
			pieChartData.add(new PieChart.Data(infoArray[i], countArray[i]));
		}
		pieChart.getData().setAll(pieChartData);
	}

}

