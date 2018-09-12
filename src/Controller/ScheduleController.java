package Controller;


import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class ScheduleController implements Initializable {
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML ChoiceBox<String> departmentPicker;
	@FXML JFXDatePicker datePicker;
	@FXML CustomTextField searchField;
	@FXML JFXTextArea commentArea;
	@FXML VBox box;
	
	DBhelper dbHelper = new DBhelper();
	Loader loader = new Loader();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loader.setupDatePicker(datePicker);
		setupTable();
		setupChoiceBox();
	}	
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML void checkSchedule() {
		PopupWindow popup  = new PopupWindow();
		if(validate()) {
			try {
				String monday = getMonday();
				String department = departmentPicker.getValue().toString();
				reload(department, monday);
			} catch (Exception e) {
				popup.errorWindow();
				e.printStackTrace();
			}
		}else {
			popup.alertWindow("操作失败", "日期和科室不能为空");
		}
		
	}
	
	@FXML void contact(){
		loader.loadWeb();
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
	

	
	private void setupChoiceBox() {
		ArrayList<HashMap<String, String>> departmentList = dbHelper.getList("hospital_department", new String[] {"departmentName"});
		ArrayList<String> choiceList = new ArrayList<String>();
		for(HashMap<String, String> department:departmentList) {
			choiceList.add(department.get("departmentName"));
		}
		departmentPicker.getItems().setAll(choiceList);
	}
	private ArrayList<HashMap<String, String>> getScheduleList(String departmentName, String date) {
		String[] searchColumn = {"departmentName", "date", "branch"};
		String[] values = {departmentName, date, LoginController.branch};
		//String tableName = "hospital_schedule where hospital_schedule.branch = '" + LoginController.branch + "'";
		String tableName = "hospital_schedule";
		ArrayList<HashMap<String, String>> scheduleList = dbHelper.getEntireList(searchColumn, values, tableName);
		return scheduleList;
	}
	private void setupTable() {
		String[] keys = {"user", "mon", "tue", "wed", "thur", "fri", "sat", "sun", "note"};
		String[] fields = {"姓名", "周一", "周二", "周三", "周四", "周五", "周六", "周日", "备注"};
		loader.setupTable(tableView, keys, fields);
	}
	private void reload(String departmentName, String date) {
		ArrayList<HashMap<String, String>> scheduleList = getScheduleList(departmentName, date);
		if(scheduleList.size()==0) {
			PopupWindow popup = new PopupWindow();
			popup.alertWindow("没有找到记录", "本周没有上传排班表。");
			return;
		}
		HashMap<String, String> schedule = scheduleList.get(0);
		String name = schedule.get("departmentName");
		String comment = schedule.get("comment");
		commentArea.setText(comment);
		//String[] keys = {"mon", "tue", "wed", "thur", "fri", "sat", "sun", "note", "users"};
		String[] mon = schedule.get("mon").split(",|，");
		System.out.println(mon.toString());
		String[] tue = schedule.get("tue").split(",|，");
		System.out.println(tue[0]+" " + tue[1]);
		String[] wed = schedule.get("wed").split(",|，");
		String[] thur = schedule.get("thur").split(",|，");
		String[] fri = schedule.get("fri").split(",|，");
		String[] sat = schedule.get("sat").split(",|，");
		String[] sun = schedule.get("sun").split(",|，");
		String[] note = schedule.get("note").split(",|，");
		String[] users = schedule.get("users").split(",|，");
		System.out.println(users.toString());
		ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < users.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			try {
				map.put("mon", mon[i]);
			}catch(Exception e) {
				
			}
			//map.put("mon", mon[i]);
			try {
				map.put("tue", tue[i]);
			}catch(Exception e) {
				
			}
			try {
				map.put("wed", wed[i]);
			}catch(Exception e) {
				
			}
			try {
				map.put("thur", thur[i]);
			}catch(Exception e) {
				
			}
			try {
				map.put("fri", fri[i]);
			}catch(Exception e) {
				
			}
			try {
				map.put("sat", sat[i]);
			}catch(Exception e) {
				
			}
			try {
				map.put("sun", sun[i]);
			}catch(Exception e) {
				
			}
			//map.put("tue", tue[i]);
			//map.put("wed", wed[i]);
			//map.put("thur", thur[i]);
			//map.put("fri", fri[i]);
			//map.put("sat", sat[i]);
			//map.put("sun", sun[i]);
			//map.put("note", note[i]);
			
			try {
				map.put("note", note[i]);
			}catch(Exception e) {
				System.out.println(e);
			}
			
			map.put("user", users[i]);
			tableList.add(map);
		}
		System.out.println(tableList.size());
		ObservableList<HashMap<String, String>> searchList = loader.search(tableList, "");
		tableView.setItems(searchList);
		
	}
	
	private String getMonday() throws ParseException {
		LocalDate localDate = datePicker.getValue();
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(c.getTime());
	}
	
	private boolean validate() {
		if(datePicker.getValue()==null||departmentPicker.getValue()==null) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
