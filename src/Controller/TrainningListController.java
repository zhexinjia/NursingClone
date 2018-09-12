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

public class TrainningListController implements Initializable {
	Loader loader = new Loader();
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	ArrayList<HashMap<String, String>> list;
	DBhelper dbHelper = new DBhelper();
	public static HashMap<String, String> selectedTrainning;
	PopupWindow popUP = new PopupWindow();

    @FXML
    void searchButton() {
    		reload();
    }
    
    @FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}

    @FXML
    void resetButton() {
    		searchField.setText("");
    		reload();
    }
    
    @FXML void contact(){
		loader.loadWeb();
	}
    
    @FXML void publishButton() {
    		selectedTrainning = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selectedTrainning)) {
    			loader.loadVBox(box, "/View/TrainingPublish.fxml");
    		}
    }
    
    @FXML
    void importButton() {
    		//remove this button
    }

    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/TrainningNew.fxml");
    }

    @FXML
    void viewButton() {
    		PopupWindow pop = new PopupWindow();
    		selectedTrainning = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTrainning == null) {
    			pop.alertWindow("操作失败", "请选中要查看的培训");
    		}else {
    			loader.loadVBox(box, "/View/TrainningDetail.fxml");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
		if(selected == null) {
			popUP.alertWindow("没有选中目标","请选择要删除的用户");
		}else {
			//FIXME:delete all informations from different tables using one SQL statement
			if (dbHelper.deleteTrainning(selected)) {
				getList();
				reload();
			}else {
				popUP.errorWindow();
			}
		}
    		
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		getList();
		System.out.println(list.size());
		reload();
	}
	
	private void setupTable() {
		String[] keys = {"name", "totalPoint", "comment"};
		String[] fields = {"培训名称", "积分", "备注"};
		loader.setupTable(tableView, keys, fields);
	}
	private void getList() {
		list = dbHelper.getEntireList(new String[] {"branch"}, new String[] {LoginController.branch}, "training_list");
		//list = dbHelper.getEntireList("training_list");
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
