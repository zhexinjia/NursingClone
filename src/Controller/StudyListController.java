package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import Model.sftpDelete;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class StudyListController implements Initializable {
	Loader loader = new Loader();
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	ArrayList<HashMap<String, String>> list;
	DBhelper dbHelper = new DBhelper();
	public static HashMap<String, String> selectedStudy;
	//public static HashMap<String, String> selected;
	PopupWindow popUP = new PopupWindow();
	String branch;


    @FXML
    void searchButton() {
    		reload();
    }
    
    @FXML void contact() {
		loader.loadWeb();
	}
    
    @FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void resetButton() {
    		searchField.setText("");
    		reload();
    }
    
    @FXML
    void publishButton() {
    		//未发布，已发布，已截止
		selectedStudy = tableView.getSelectionModel().getSelectedItem();
		if(selectedStudy==null) {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("发布失败", "请选中需要发布的考试。");
		}else {
			loader.loadVBox(box, "/View/StudyPublish.fxml");
			/*
			PopupWindow popUP = new PopupWindow();
			popUP.alertWindow("发布课件失败", "课件已经发布，无法重复发布。");
			*/
		}
    }
    
    @FXML
    void studyDetailButton() {
    		selectedStudy = tableView.getSelectionModel().getSelectedItem();
    		System.out.println("selectedStudy: "+selectedStudy);
		if(loader.selectionCheck(selectedStudy)) {
			loader.loadVBox(box, "/View/StudyDetail.fxml");
		}
    }

    @FXML
    void newButton() {
    		if(list.size() < 10) {
    			loader.loadVBox(box, "/View/StudyNew.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "课件最大数量为10，请删除不使用的课件后再新建课件");
    		}
    		
    }

    @FXML
    void modifyButton() {
    		//TODO：remove
    		loader.loadVBox(box, "/View/UserModify.fxml");
    }

    @FXML
    void deleteButton() throws Exception {
    		System.out.println("del");
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
		if(selected == null) {
			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
		}else {
			//FIXME:delete all informations from different tables using one SQL statement
			if (dbHelper.deleteStudy(selected)) {
				sftpDelete sftpDel = new sftpDelete();
				sftpDel.sftpDel(LoginController.path, selected.get("file_name"));
				getList();
				reload();
			}
		}
    		
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		branch = LoginController.branch;
		setupTable();
		getList();
		reload();
	}
	
	private void setupTable() {
		String[] keys = {"name", "type", "publish_status", "point"};
		String[] fields = {"课件名称","类型", "发布状态", "分值"};
		loader.setupTable(tableView, keys, fields);
	}
	private void getList() {
		String[] columns = {"id", "name", "type", "publish_status", "file_name", "point"};
		list = dbHelper.getList(new String[] {"branch"}, new String[] {branch}, "study_list", columns);
		//list = dbHelper.getList("study_list", columns);
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
