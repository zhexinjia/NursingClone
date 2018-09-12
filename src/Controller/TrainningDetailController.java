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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TrainningDetailController implements Initializable {	
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	@FXML private CustomTextField searchField;
	
	Loader loader = new Loader();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
	DBhelper dbHelper = new DBhelper	();
	private HashMap<String, String> selectedTraining;
	
	int oldPoint, newPoint;
	int newScore;
	String training_id;
	String totalPoint;
	String name;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedTraining = TrainningListController.selectedTrainning;
		training_id = selectedTraining.get("id");
		name = selectedTraining.get("name");
		totalPoint = selectedTraining.get("totalPoint");
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
	void TrainningListButton() {
		loader.loadVBox(box, "/View/TrainningList.fxml");
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
	    	String[] fields =  { "姓名", "工号", "得分", "是否完成","备注"};
	    	String[] keys = { "name", "ssn", "point", "finish_status", "detail"};
		ArrayList<HashMap<String, String>> importlist = loader.importExcel(keys, fields);
		int importLength = importlist.size();
		int oldLength = list.size();
		int newLength;
		System.out.println("importLength" + importLength);
		if(importlist!=null) {
			if (dbHelper.insertTrainning(importlist, training_id, name, totalPoint)) {
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
		String[] keys = {"name", "department", "position", "title", "point", "detail"};
		String[] fields = {"姓名", "科室", "职位", "职称", "得分", "备注"};
		loader.exportExcel(list, fields, keys);
    }

    @FXML
    void newButton() {
    		if(selectedTraining.get("publish_status").equals("未发布")) {
    			loader.loadVBox(box, "/View/TrainingPublish.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "本培训已经添加过用户，无法多次添加。");
    		}
    }

    @FXML
    void modifyButton() {
    		/*
    		PopupWindow popUP = new PopupWindow();
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		HashMap<String, String> map = new HashMap<String, String>();
    		System.out.println("selected: " + selected);
    		if(selected==null) {
    			popUP.alertWindow("操作失败", "请选中一个用户");
    		}else {
    			popUP.inputField.setText(selected.get("point"));
    			popUP.textArea.setText(selected.get("detail"));
    			popUP.confirmButton.setOnAction(e->{
    				if(validate(popUP.inputField)) {
    					if (isInteger(popUP.inputField.getText()) == true) {
    						map.put("point", popUP.inputField.getText());
    						map.put("detail", popUP.textArea.getText());
        					map.put("id", selected.get("id"));
        					map.put("ssn", selected.get("ssn"));
        					
        					newPoint = Integer.parseInt(map.get("point"));
        					oldPoint = Integer.parseInt(selected.get("point"));
        					int diff = newPoint - oldPoint;
        					//System.out.println("######"+selected.get("currentScore"));
        					newScore = Integer.parseInt(selected.get("currentScore")) + diff;
        					//map.put("newScore", String.valueOf(newScore));
    					}
    					
    					if(dbHelper.newUpdateScore(map, "training_history", String.valueOf(newScore))) {
    						popUP.stage.close();
    						getList();
    						reload();
    					}else {
    						popUP.errorWindow();
    					}
    				}
    			});
    			popUP.modifyWindow("编辑得分", "输入分数", "输入备注");
    		}
    		*/
    		
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		
    		PopupWindow pop = new PopupWindow();
    		if(selected == null) {
    			pop.alertWindow("操作失败", "请选中一个用户");
    		}else {
    			pop.confirmButton.setOnAction(e->{
    				if(!dbHelper.delete(selected, "training_history")) {
    					pop.errorWindow();
    				}else {
    					pop.stage.close();
    					getList();
    					reload();
    				}
    			});
    			pop.confirmWindow("确认要删除培训吗？", "点击确认删除培训记录");
    		}
    }

    private void setupTable() {
    		String[] keys = {"name", "department", "position", "title", "point", "detail"};
    		String[] fields = {"姓名", "科室", "职位", "职称", "得分", "备注"};
		loader.setupTable(tableView, keys, fields);
	}

    private void getList() {
		String[] searchColumn = {"training_id"};
		String[] values = {selectedTraining.get("id")};
		String tableName = "training_history inner join user_primary_info on training_history.ssn = user_primary_info.ssn join user_score "
							+ "on user_primary_info.ssn = user_score.ssn";
		String[] columns = {"user_primary_info.name", "user_primary_info.department", "user_primary_info.position", "user_primary_info.title",
				"training_history.point", "training_history.detail", "training_history.id", "user_primary_info.ssn", "user_score.currentScore"};
		list = dbHelper.getList(searchColumn, values, tableName, columns);
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	boolean validate(TextField inputField){
		if(inputField.getText().trim().isEmpty()) {
			return false;
		}else if(!isNumeric(inputField.getText())) {
			return false;
		}
		return true;
	}
	
	public boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	   
	    return true;
	}
	
	

}
