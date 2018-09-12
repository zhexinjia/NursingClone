package Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;

public class TestStatusController implements Initializable {
	
	Loader loader = new Loader();
	
	@FXML
	private VBox box;

    @FXML
    private CustomTextField searchField;

    @FXML
    private TableView<HashMap<String, String>> tableView;

    @FXML
    private Label countLabel;
    
    DBhelper dbHelper;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    String[] keys = {"examName", "publish_status", "user_count", "if_count"};
    String[] fields = {"考试名称", "发布状态", "参加考试人数","是否记分"};
    public static HashMap<String, String> selectedTest;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
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
    void testListButton() {
    		loader.loadVBox(box, "/View/TestList.fxml");
    }

    @FXML
    void endTestButton() {
    		HashMap<String, String> selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTest!=null) {
    			selectedTest.put("publish_status", "已截止");
    			if(dbHelper.update(selectedTest, "exam_list")) {
    				//TODO: getList() and reload() executed before update!
    				this.getList();
        			this.reload();
    			}
    		}else {
    			PopupWindow popup = new PopupWindow();
    			popup.alertWindow("操作失败", "请选中一个考试");
    		}
    }

    @FXML
    void detailButton() {
    		selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if (selectedTest != null) {
    			loader.loadVBox(box, "/View/TestDetail.fxml");
    		}else {
    			PopupWindow popup = new PopupWindow();
    			popup.alertWindow("操作失败", "请选中一个考试");
    		}
    }
    
    private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	private void getList() {
		//TODO: how to count total point??? should we remove it?
		String[] columns = {"id", "examName", "publish_status", "single_point", "multi_point", "tf_point", "if_count", "totalPoint"};
		list = dbHelper.getList("where publish_status = '已发布' or publish_status = '已截止'", "exam_list", columns);
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

    

	

}
