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

public class MeetingListController implements Initializable {	
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	String[] keys = {"name", "date", "time", "totalPoint"};
	String[] fields = {"会议信息", "日期", "时间", "分值"};
	DBhelper dbHelper = new DBhelper();
	public static HashMap<String, String> selectedMeeting;
 	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		getList();
		reload();
	}
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
	
	@FXML void contact(){
		loader.loadWeb();
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
    void chooseMeetingMember() {
    		selectedMeeting = tableView.getSelectionModel().getSelectedItem();
    		if(selectedMeeting != null) {
    			loader.loadVBox(box, "/View/MeetingPublish.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选中一个会议");
    		}
    }



    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/MeetingNew.fxml");
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selectedMeeting = tableView.getSelectionModel().getSelectedItem();
    		if (selectedMeeting != null) {
    			if (dbHelper.deleteMeeting(selectedMeeting)) {
    				getList();
    				reload();
    			}
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("删除失败！", "请选中一个会议。");
    		}
    }	
	
    private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	
	private void getList() {
		String tableName = "meeting_list";
		String[] columns = {"id", "name", "date", "time", "totalPoint"};;
		//list = dbHelper.getList(tableName, columns);
		list = dbHelper.getList(new String[] {"branch"}, new String[] {LoginController.branch}, tableName, columns);
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
