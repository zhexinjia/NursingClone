package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class MeetingStatusController implements Initializable{
	@FXML VBox box;
	@FXML Label percentLabel;
	@FXML Label highestPercentLabel;
	@FXML Label lowestPercentLabel;
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML CustomTextField searchField;
	Loader loader = new Loader();
	DBhelper dbHelper = new DBhelper();
	
	ArrayList<HashMap<String, String>> meetingList;
	ArrayList<HashMap<String, String>> meeting_historyList;
	//public HashMap<String, String> selectedMeeting;
	public static ArrayList<HashMap<String, String>> passedMeetingHistory;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getList();
		setupTable();
		caculatePercents();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML
	void searchButton() {
		reload();
	}
	
	@FXML
	void resetButton() {
		searchField.setText("");
		reload();
	}
	
    @FXML
    void contactButton() {
    		loader.loadWeb();
    }
    
    @FXML
    void exportButton() {
    		String[] fieldlist = new String[] {"会议信息", "出勤率", "是否记分"};
    		String[] keylist = new String[] {"name", "percent","if_count"};
    		loader.exportExcel(meetingList, fieldlist, keylist);
    }
    
    @FXML
    void detailButton() {
    		HashMap<String, String> selectedMeeting = tableView.getSelectionModel().getSelectedItem();
    		if(selectedMeeting != null) {
    			passedMeetingHistory = new ArrayList<HashMap<String, String>>();
    			String id = selectedMeeting.get("id");
    			for(HashMap<String, String> history: meeting_historyList) {
    				if(history.get("meeting_id").equals(id)) {
    					passedMeetingHistory.add(history);
    				}
    			}
    			loader.loadVBox(box, "/View/MeetingDetail.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选中一个会议记录。");
    		}
    }
    
    private void reload() {
    		ObservableList<HashMap<String, String>> searchList = loader.search(meetingList, searchField.getText());
		tableView.setItems(searchList);
		//countLabel.setText("共 " +searchList.size()+ " 条");
    }
    
    private void setupTable() {
    		String[] keys = {"name", "percent"};
    		String[] fields = {"会议信息", "出勤率"};
    		loader.setupTable(tableView, keys, fields);
    }
    
    private void getList() {
    		getMeetingList();
    		getHistoryList();
    }
    private void getMeetingList() {
    		String[] columns = {"id", "name", "if_count"};
    		String tableName = "meeting_list";
    		//meetingList = dbHelper.getList(tableName, columns);
    		meetingList = dbHelper.getList(new String[] {"branch"}, new String[] {LoginController.branch}, tableName, columns);
    }    
    private void getHistoryList() {
    		String[] columns = {"meeting_history.meeting_id", "meeting_history.checkin", "meeting_history.checkout", 
    				"user_primary_info.ssn", "user_primary_info.department", "user_primary_info.name", "user_primary_info.position", 
    				"user_primary_info.title", "user_primary_info.level"};
    		//String tableName = "user_primary_info inner join meeting_history on meeting_history.ssn = user_primary_info.ssn";
    		String tableName = "user_primary_info inner join meeting_history on meeting_history.ssn = user_primary_info.ssn "
    				+ "inner join meeting_list on meeting_history.meeting_id = meeting_list.id where meeting_list.branch = '" + LoginController.branch + "'";
    		meeting_historyList = dbHelper.getList(tableName, columns);
    		System.out.println(meeting_historyList.size());
    }
    
    //{examID, totalNum, checkinNum, checkoutNum}
    private void caculatePercents() {
    		int checkinTotal = 0;
    		int checkoutTotal = 0;
    		HashMap<String, int[]> countMap = new HashMap<String, int[]>(); 
    		for (HashMap<String, String> map:meeting_historyList) {
    			int checkin = 0;
    			int checkout = 0;
    			int totalNum = 1;
    			if(map.get("checkin").equals("是")) {
    				checkin = 1;
    				checkinTotal++;
    			}
    			if(map.get("checkout").equals("是")) {
    				checkout = 1;
    				checkoutTotal++;
    			}
    			String id = map.get("meeting_id");
    			if(countMap.get(id) == null) {
    				countMap.put(id, new int[] {totalNum, checkin, checkout});
    			}else {
    				int[] list = countMap.get(id);
    				list[0] += totalNum;
    				list[1] += checkin;
    				list[2] += checkout;
    				countMap.put(id, list);
    			}
    		}
    		//countMap done
    		double highest = 0;
    		double lowest = 1;
    		for(HashMap<String, String> meeting:meetingList) {
    			String id = meeting.get("id");
    			int[] resultList = countMap.get(id);
    			if(resultList != null) {
    				double percent = (((double)resultList[1] + (double)resultList[2])/2)/(double)resultList[0];
    				meeting.put("percent", String.format("%.2f", percent*100)+"%");
    				//meeting.put("percent", Double.toString(percent*100)+"%");
        			if (percent > highest) {
        				highest = percent;
        			}
        			if(percent < lowest) {
        				lowest = percent;
        			}
    			}
    		}
    		double totalP = 0.5*((double)checkinTotal + (double)checkoutTotal)/(double) meeting_historyList.size();
    		highestPercentLabel.setText(Double.toString(Math.round(highest*100))+"%");
    		lowestPercentLabel.setText(Double.toString(Math.round(lowest*100))+"%");
    		percentLabel.setText(Double.toString(Math.round(totalP*100))+"%");
    }
    

}

