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

public class UserlistController implements Initializable {	
	@FXML TableView<HashMap<String,String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	String[] keys = {"name", "ssn", "department", "title", "branch", "position", "sex", "birth", "region", "dateJoinParty", "timeStartWork", 
			"certifactionDate", "certifactionNum", "dateReceivedTP", "hireDate", "level", "N0", 
			"N1", "N2", "N3", "N4", "primaryEd", "primaryEdTime", "primaryEdSchool", "highestEd", "highestEdTime", "highestEdSchool"};
	String[] fields = {"姓名", "工号", "科室", "职称", "专业", "专业技术职务", "性别", "出生日期", "籍贯", "入党（入团）日期", "参加工作时间", 
			"取得执业资格时间", "执业资格证号码", "技术职务资格取得时间", "聘任时间", "护士分层等级", "晋级N0时间", 
			"晋级N1时间", "晋级N2时间", "晋级N3时间", "晋级N4时间", "第一学历", "第一学历取得时间", "第一学历取得学校", "最高学历", "最高学历取得时间", 
			"最高学历取得学校"};
	public static HashMap<String, String> selectedUser;
	PopupWindow popUP = new PopupWindow();
	DBhelper dbHelper = new DBhelper();
		
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
    void importButton() {
    		ArrayList<HashMap<String, String>> importlist = loader.importExcel(keys, fields);
    		int importLength = importlist.size();
    		int oldLength = list.size();
    		int newLength;
    		System.out.println("importLength" + importLength);
    		if(importlist!=null) {
    			if (dbHelper.insertUserList(importlist)) {
    				getList();
    				reload();
    				newLength = list.size();
    				if((importLength + oldLength) != newLength) {
    					int diff = newLength - oldLength;
    					System.out.println("diff" + diff);
    					
    					PopupWindow pop = new PopupWindow();
    	    				pop.alertWindow("部分导入失败", "总导入行数：" + importLength +
    	    						",  实际导入行数："+ diff + ",\n              报错行数：第"+ (diff+1) + "行");
    				}
    			}else {
    				getList();
    				reload();
    				newLength = list.size();
    				int diff = newLength - oldLength;
    				System.out.println("diff" + diff);
    				
    				PopupWindow pop = new PopupWindow();
        				pop.alertWindow("部分导入失败", "总导入行数：" + importLength +
        						",  实际导入行数："+ diff + ", \n              报错行数：第"+ (diff+1) + "行");
    			}
    		
    		}
    		
    }

    @FXML
    void exportButton() {
    		loader.exportExcel(list, fields, keys);
    }



    @FXML
    void newButton() {
    		loader.loadVBox(box, "/View/UserNew.fxml");
    }

    @FXML
    void modifyButton() {
    		selectedUser = tableView.getSelectionModel().getSelectedItem();
    		if(selectedUser == null) {
    			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
    		}else {
    			loader.loadVBox(box, "/View/UserModify.fxml");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(selected == null) {
    			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
    		}else {
    			if (dbHelper.deleteUser(selected)) {
    				getList();
    				reload();
    			}
    		}
    }
    
    @FXML
    void resetPassword() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(selected == null) {
    			popUP.alertWindow("没有选中目标","请选择要编辑的用户");
    		}else {
    			dbHelper.resetPW(selected);
    		}
    }

	
	private void setupTable() {
		String[] keys = {"name", "ssn", "department", "title", "position", "level"};
		String[] fields = {"姓名", "工号", "科室", "职称", "职务", "层级"};
		loader.setupTable(tableView, keys, fields);
	}
	
	private void getList() {
		String[] columns = new String[] {"user_primary_info.*", "user_sub_info.*"};
		String tableName = "user_primary_info inner join user_sub_info on user_primary_info.ssn = user_sub_info.ssn";
		list = dbHelper.getBranchList(tableName, columns);
		//list = dbHelper.getEntireList("user_primary_info");
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}

}
