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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class ScheduleStatusController implements Initializable {

	@FXML VBox box;
	@FXML PieChart pieChart;
	
	@FXML BarChart<String, Integer> barChart;
	@FXML CategoryAxis xAxis;
	@FXML NumberAxis yAxis;
	
	@FXML Label averageField;
	@FXML Label highestField;
	@FXML Label lowestField;

	@FXML JFXComboBox<String> departmentPicker;
	@FXML TableView<HashMap<String, String>> departmentTableView;
	
	
	ArrayList<HashMap<String, String>> userList;
	ArrayList<HashMap<String, String>> scheduleItemList;
	
	HashMap<String, ArrayList<HashMap<String, String>>> departmentList = new HashMap<String, ArrayList<HashMap<String, String>>>();
	
	ArrayList<HashMap<String, String>> departmentExportList;
	ArrayList<HashMap<String, String>> levelExportList;
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	String branch = LoginController.branch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		getList();
		setupDepartmentTable();
		
		caculatePercent();
		setupDepartmentPicker();
			
		//setupBarChart();
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
	
	
	void getList(){
		
		String tableName = " user_primary_info "
				+ "where user_primary_info.branch = '" + LoginController.branch + "'";
		String[] columns = {"user_primary_info.name", "user_primary_info.ssn",
				"user_primary_info.department", "user_primary_info.position"};
		userList = dbHelper.getList(tableName, columns);
		
		
		String[] item_columns = new String[] {"schedule_list.*"};
		String item_tableName = "schedule_list where schedule_list.branch = '" + LoginController.branch + "';"; 
		scheduleItemList = dbHelper.getList(item_tableName, item_columns);
		System.out.println("scheduleItemList" + scheduleItemList);
		
		
		ArrayList<HashMap<String, String>> itemList = null;
		for (int i=0; i < scheduleItemList.size(); i++) {
			itemList = dbHelper.getScheduleHours(i);
			
			for (int j = 0; j < userList.size(); j++) {
				for (int z = 0; z < itemList.size(); z++) {
					HashMap<String, String> tempp = itemList.get(z);
					if (userList.get(j).get("ssn").equals(tempp.get("ssn"))) {
						String a = "sectionName"+i;
						userList.get(j).put(a, tempp.get(a));
					}
				}
					
			}
			
			
		}
		System.out.println("itemList: " + itemList);
		System.out.println("userList: " + userList);
		
	}
	
	
	void caculatePercent() {
		Double highest = (double) 0;
		Double lowest = (double) 1;
		int[] countArray = {0, 0, 0, 0, 0};
		double sum = 0;
 		//HashMap<String, ArrayList<HashMap<String, String>>> departmentList = new HashMap<String, ArrayList<HashMap<String, String>>>();
		for(HashMap<String, String> score:userList) {
			/*
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
			*/
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
			
		}
		/*
		Double ave = sum/userList.size();
		DecimalFormat df = new DecimalFormat("#.##");
		averageField.setText(df.format(ave*100));
		lowestField.setText(df.format(lowest*100) + "%");
		highestField.setText(df.format(highest*100) + "%");
		//setupChart(countArray);
		 * 
		 */
	}
	
	void setupDepartmentPicker() {
		Set<String> keyset = departmentList.keySet();
		System.out.println("Department keyset: " + keyset);
		departmentPicker.getItems().setAll(keyset);
		departmentPicker.setOnAction(e->{
			loadDepartmentTable(departmentPicker.getSelectionModel().getSelectedItem());
		});
		//System.out.println("departmentList: " + departmentList);
		String firstDepartment = keyset.iterator().next();
		//init default display
		departmentPicker.setValue(firstDepartment);
		loadDepartmentTable(firstDepartment);
		
	}

	
	private void loadDepartmentTable(String department) {
		if(department != null) {
			ArrayList<HashMap<String, String>> list = departmentList.get(department);
			ObservableList<HashMap<String, String>> searchList = loader.search(list, "");
			departmentTableView.setItems(searchList);
			
			departmentExportList = list;
			//caculateDepartment(list);
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

	}
	

	private void setupDepartmentTable() {
		
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> fields = new ArrayList<String>();	
		keys.add("name");
		keys.add("position");
		fields.add("姓名");
		fields.add("职位");
		
	
		for (int i = 0; i < scheduleItemList.size(); i++) {
			String temp = "sectionName" + i;
			keys.add(temp);
			fields.add(scheduleItemList.get(i).get("name"));
		}
		
		String[] arr = keys.toArray(new String[keys.size()]);
		String[] array = fields.toArray(new String[fields.size()]);
		
		loader.setupTable(departmentTableView, arr, array);

	}

	
	private void setupBarChart() {
		System.out.println("scoreList " + userList);
		double max = 0.0, min = 0.0;
		double current;
		
		for (HashMap<String, String>user : userList) {
				current = Double.parseDouble(user.get("currentScore"));
			if (current > max) {
				max = current;
			}
			if (current < min) {
				min = current;
			}
		}

		int upperBound = (int) Math.round(max) + 10;
		
		//setup x-axis
		int temp = upperBound / 10;
		System.out.println("temp: " + temp);
		ObservableList<String> scoreNames = FXCollections.observableArrayList();
		for (int i = temp; i <= upperBound; i+=temp) {
			String str;
			if (i == temp) {
				str = "0 - " + Integer.toString(i);
			}else {
				str = (i - temp) + " - " + Integer.toString(i);
			}
			
			scoreNames.add(str);
		}
		
		xAxis.setCategories(scoreNames);
		
		//calculate number of people in each range
		int [] peopleCount = new int[10];
		double currentPerson;
		for (HashMap<String, String>user : userList) {
			currentPerson = Double.parseDouble(user.get("currentScore"));
			int tp = (int) (currentPerson / temp);
			peopleCount[tp]++;
		}
		
		
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		for (int i = 0; i < peopleCount.length; i++) {
            series.getData().add(new XYChart.Data<>(scoreNames.get(i), peopleCount[i]));
        }
		
        barChart.getData().add(series);
	}
	
		

}

