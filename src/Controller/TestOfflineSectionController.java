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

public class TestOfflineSectionController implements Initializable {
	
	@FXML
	private VBox box;

    @FXML
    private CustomTextField searchField;

    @FXML
    private TableView<HashMap<String, String>> tableView;

    @FXML
    private Label countLabel;
    
    Loader loader = new Loader();
    DBhelper dbHelper;
    public String examID;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    String[] keys = {"id", "date", "start_time", "end_time", "quantity", "location"};
    String[] fields = {"档期编号","考核日期", "开始时间", "结束时间", "可报名人数", "地点"};
    public static HashMap<String, String> selectedTest;
    public static HashMap<String, String> selectedSection;
    private String branch;
    
    @Override
 	public void initialize(URL location, ResourceBundle resources) {
    		selectedTest = TestOfflineController.selectedTest;
		examID = selectedTest.get("id");
    		branch = LoginController.branch;
    		dbHelper = new DBhelper();
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
    void testOfflineButton() {
    		loader.loadVBox(box, "/View/TestOffline.fxml");
    }
    
    
    /*
    @FXML
    void publishButton() {
    		//未发布，已发布，已截止
    		selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTest==null) {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选中需要操作的考试。");
    		}else if (selectedTest.get("publishStatus").equals("未发布")) {
    			loader.loadVBox(box, "/View/TestOfflinePublish.fxml");
    		}else {
    			PopupWindow popUP = new PopupWindow();
    			popUP.alertWindow("操作失败", "试卷已经截止或者已发布，无法添加考试人员。");
    		}
    }
    
    @FXML void endButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selected)) {
    			if(selected.get("publishStatus").equals("已发布")) {
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put("id", selected.get("id"));
    				map.put("publishStatus", "已截止");
    				if(dbHelper.update(map, "offlineexam_list")) {
    					this.getList();
    					this.reload();
    				}
    			}else {
        			PopupWindow pop = new PopupWindow();
        			pop.alertWindow("操作失败", "选中试卷已经截止");
        		}
    		}
    }
*/

    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/TestOfflineSectionNew.fxml");
    }

    /*
    @FXML
    void modifyButton() {
    		selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTest!=null && selectedTest.get("type").equals("0")) {
    			loader.loadVBox(box, "/View/TestModify.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选择固定题目的考试。");
    		}
    }
    */
    @FXML void detailButton() {
    		selectedSection = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selectedTest)) {
    			loader.loadVBox(box, "/View/TestOfflineDetail.fxml");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if (selected!=null) {
    			deleteFunction(selected);
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("删除失败！", "请选中需要删除的题库。");
    		}
    }

	private void setupTable() {
		loader.setupTable(tableView, keys, fields);

	}
	
	private void getList() {
	
		String[] columns = {"offline_section.id", "offline_section.date", "offline_section.start_time", "offline_section.end_time", 
				"offline_section.quantity", "offline_section.location"};
		String[] searchColumns = {"offline_section.offlineexam_id"};
		String[] searchValues = {examID};
				
		list = dbHelper.getList(searchColumns, searchValues, "offline_section", columns);
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	private void deleteFunction(HashMap<String, String> map) {		
		PopupWindow popUP = new PopupWindow();
		popUP.confirmButton.setOnAction(e->{
			//TODO: fix me, delete exam also delete exam-question relation table
			if(dbHelper.deleteOfflineSection(map)) {
				popUP.stage.close();
				getList();
				reload();
			}
		});
		popUP.confirmWindow("确认要删除该考核档期吗？", "删除考核档期将删除所有相关信息");
	}

}

