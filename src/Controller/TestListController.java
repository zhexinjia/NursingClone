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

public class TestListController implements Initializable {
	
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
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    String[] keys = {"examName", "totalPoint", "publishStatus", "time"};
    String[] fields = {"试卷名称", "试卷积分", "当前状态", "考试时间"};
    public static HashMap<String, String> selectedTest;
    private String branch;
    
    @Override
 	public void initialize(URL location, ResourceBundle resources) {
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
    void publishButton() {
    		//未发布，已发布，已截止
    		selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTest==null) {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选中需要操作的考试。");
    		}else if (selectedTest.get("publish_status").equals("0")) {
    			loader.loadVBox(box, "/View/TestPublish.fxml");
    		}else {
    			PopupWindow popUP = new PopupWindow();
    			popUP.alertWindow("操作失败", "试卷已经截止，无法添加考试人员。");
    		}
    }
    
    @FXML void endButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selected)) {
    			if(selected.get("publish_status").equals("0")) {
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put("id", selected.get("id"));
    				map.put("publish_status", "1");
    				if(dbHelper.update(map, "exam_list")) {
    					this.getList();
    					this.reload();
    				}
    			}else {
        			PopupWindow pop = new PopupWindow();
        			pop.alertWindow("操作失败", "选中试卷已经截止");
        		}
    		}
    }


    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/TestNew.fxml");
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
    		selectedTest = tableView.getSelectionModel().getSelectedItem();	
    		if(loader.selectionCheck(selectedTest)) {
    			loader.loadVBox(box, "/View/TestDetail.fxml");
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
		String[] searchColumn = {"branch"};
		String[] values = {branch};
		String tableName = "exam_list";
		list = dbHelper.getEntireList(searchColumn, values, tableName);
		for(HashMap<String, String> item:list) {
			item.put("publishStatus", item.get("publish_status").equals("0")?"进行中":"已截止");
		}
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	private void deleteFunction(HashMap<String, String> map) {		
		PopupWindow popUP = new PopupWindow();
		popUP.confirmButton.setOnAction(e->{
			//delete from exam_list, exam_history
			if(dbHelper.deleteExam(map)) {
				popUP.stage.close();
				getList();
				reload();
			}
		});
		popUP.confirmWindow("确认要删除考卷吗？", "删除考卷将删除所有与本考卷相关信息");
	}

}

