package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;

import Model.DBhelper;
import Model.ExcelHelper;
import Model.Loader;
import Model.PopupWindow;
import Model.QuestionBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class TestModifyController implements Initializable {

    @FXML private VBox box;
    @FXML private JFXCheckBox checkBox;
    @FXML private ChoiceBox<String> timeChoiceBox;
    @FXML private ChoiceBox<String> singleChoiceBox;
    @FXML private Label singleLabel;
    @FXML private ChoiceBox<String> multiChoiceBox;
    @FXML private Label multiLabel;
    @FXML private ChoiceBox<String> tfChoiceBox;
    @FXML private Label tfLabel;
    @FXML private TableView<HashMap<String, String>> singleTableView;
    @FXML private TableView<HashMap<String, String>> multiTableView;
    @FXML private TableView<HashMap<String, String>> tfTableView;
    
    private HashMap<String, String> selectedTest;
    private ArrayList<HashMap<String, String>> singleList;
    private ArrayList<HashMap<String, String>> multiList;
    private ArrayList<HashMap<String, String>> tfList;
    DBhelper dbHelper = new DBhelper();
    String[] keys = {"question"};
    String[] fields = {"题干"};
    Loader loader = new Loader();
    boolean if_count = false;
    ExcelHelper excelHelper = new ExcelHelper();
    String[] singleKeys = {"question", "option1", "option2", "option3", "option4", "answer"};
    String[] singleFields = {"题干", "选项1", "选项2", "选项3", "选项4", "答案"};
    String[] multiKeys = {"question", "option1", "option2", "option3", "option4", "option5", "answer"};;
    String[] multiFields = {"题干", "选项1", "选项2", "选项3", "选项4", "选项5", "答案"};;
    String[] tfKeys = {"question", "answer"};
    String[] tfFields = {"题干", "答案"};
    
    String[] searchColumn = {"exam_id"};
    
    
    
    public void initialize(URL location, ResourceBundle resources) {
    		selectedTest = BankListController.selectedTest;
    		if(selectedTest.get("if_count").equals("是")){
    			if_count = true;
    		}
    		setupTable();
    		setupCheckBox();
    		setupChoiceBox();
    		
    		setupList();
    		reload();
    }
    
    @FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml");
	}
    
    @FXML
    void testListButton() {
    		loader.loadVBox(box, "/View/TestList.fxml");
    }
    
    boolean entryCheck() {
    		if(if_count) {
    			if(singleChoiceBox.getSelectionModel().isEmpty() || multiChoiceBox.getSelectionModel().isEmpty() ||
    					tfChoiceBox.getSelectionModel().isEmpty()) {
    				return false;
    			}
    		}	
    		if(timeChoiceBox.getSelectionModel().isEmpty()) {
    			return false;
    		}
    		return true;	
    }
    
    @FXML
    void saveSetting() {
    		if(entryCheck()) {
    			HashMap<String, String> map = new HashMap<String, String>();
        		map.put("id", selectedTest.get("id"));
        		map.put("time", timeChoiceBox.getSelectionModel().getSelectedItem());
        		if(if_count) {
        			map.put("if_count", "是");
        			map.put("single_point", singleChoiceBox.getSelectionModel().getSelectedItem());
        			map.put("multi_point", multiChoiceBox.getSelectionModel().getSelectedItem());
        			map.put("tf_point", tfChoiceBox.getSelectionModel().getSelectedItem());
        		}else {
        			map.put("if_count", "否");
        			map.put("single_point","0");
        			map.put("multi_point", "0");
        			map.put("tf_point", "0");
        		}
        		dbHelper.update(map, "exam_list");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("设置选项不能为空", "请补充完整基本设置选项");
    		}
    		
    }

    @FXML
    void contactButton() {
    		loader.loadWeb();
    }

    @FXML
    void deleteMulti() {
    		PopupWindow popUP = new PopupWindow();
    		if(has_selected(multiTableView)) {
        		popUP.confirmButton.setOnAction(e->{
        			dbHelper.delete(multiTableView.getSelectionModel().getSelectedItem(), "exam_qa_multiple");
        		});
        		popUP.confirmWindow("删除试题", "是否确认删除？");
    		}else {
    			popUP.alertWindow("删除失败", "请选中试题");
    		}
    }

    @FXML
    void deleteSingle() {
    		PopupWindow popUP = new PopupWindow();
		if(has_selected(singleTableView)) {
    			popUP.confirmButton.setOnAction(e->{
    				dbHelper.delete(singleTableView.getSelectionModel().getSelectedItem(), "exam_qa_single");
    			});
    			popUP.confirmWindow("删除试题", "是否确认删除？");
		}else {
			popUP.alertWindow("删除失败", "请选中试题");
		}
    }

    @FXML
    void deleteTF() {
		PopupWindow popUP = new PopupWindow();
		if(has_selected(tfTableView)) {
			popUP.confirmButton.setOnAction(e->{
			dbHelper.delete(tfTableView.getSelectionModel().getSelectedItem(), "exam_qa_tf");
			});
			popUP.confirmWindow("删除试题", "是否确认删除？");
		}else {
			popUP.alertWindow("删除失败", "请选中试题");
		}
    }

    @FXML
    void exportMulti() {
    		loader.exportExcel(multiList, multiKeys, multiFields);
    }

    @FXML
    void exportSingle() {
    		loader.exportExcel(singleList, singleKeys, singleFields);
    }

    @FXML
    void exportTF() {
    		loader.exportExcel(tfList, tfKeys, tfFields);
    }

    @FXML
    void homeButton() {
    		
    }

    @FXML
    void importMulti() {
    		ArrayList<HashMap<String, String>> maplist = loader.importExcel(multiKeys, multiFields);
    		dbHelper.insertList(maplist, "exam_qa_multiple");
    		this.getMultiList();
    		this.reload();
    }

    @FXML
    void importSingle() {
    		ArrayList<HashMap<String, String>> maplist = loader.importExcel(singleKeys, singleFields);
    		dbHelper.insertList(maplist, "exam_qa_single");
    		this.getSingleList();
    		this.reload();
    }

    @FXML
    void importTF() {
    		ArrayList<HashMap<String, String>> maplist = loader.importExcel(tfKeys,  tfFields);
    		dbHelper.insertList(maplist, "exam_qa_tf");
    		this.getTfList();
    		this.reload();
    }

    @FXML
    void modifyMulti() {
    		HashMap<String, String> map = multiTableView.getSelectionModel().getSelectedItem();
    		if(map!=null) {
    			multiplePopUp(map);
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("编辑失败", "请选中考题");
    		}
    }

    @FXML
    void modifySingle() {
    		HashMap<String, String> map = singleTableView.getSelectionModel().getSelectedItem();
		if(map!=null) {
			singlePopUp(map);
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("编辑失败", "请选中考题");
		}
    }

    @FXML
    void modifyTF() {
    		HashMap<String, String> map = tfTableView.getSelectionModel().getSelectedItem();
		if(map!=null) {
			tfPopUp(map);
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("编辑失败", "请选中考题");
		}
    }

    @FXML
    void newMulti() {
    		multiplePopUp(null);
    }

    @FXML
    void newSingle() {
    		singlePopUp(null);
    }

    @FXML
    void newTF() {
    		tfPopUp(null);
    }
    
    private void setupTable() {
    		loader.setupTable(singleTableView, keys, fields);
    		loader.setupTable(multiTableView, keys, fields);
    		loader.setupTable(tfTableView, keys, fields);
    }
    
    private void setupCheckBox() {
    		checkBox.setOnAction(e->{
    			if(checkBox.isSelected()) {
        			if_count = true;
        			singleChoiceBox.setDisable(false);
        			multiChoiceBox.setDisable(false);
        			tfChoiceBox.setDisable(false);
        		}else {
        			if_count=false;
        			singleChoiceBox.setDisable(true);
        			multiChoiceBox.setDisable(true);
        			tfChoiceBox.setDisable(true);
        		}
    		});
    		checkBox.setSelected(if_count);
    		//setupChoiceBox();
    }
    private void setupChoiceBox() {
    		//TODO: action for submit changing of point, setup init points and times
    		//记分
    		ArrayList<String> times = new ArrayList<String>();
    		for(int i = 0; i < 121; i++) {
    			times.add(Integer.toString(i));
    		}
    		timeChoiceBox.getItems().addAll(times);
    		timeChoiceBox.getSelectionModel().select(selectedTest.get("time"));
    		String[] scores = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    		singleChoiceBox.getItems().addAll(scores);
    		singleChoiceBox.getSelectionModel().select(selectedTest.get("single_point"));
    		multiChoiceBox.getItems().addAll(scores);
    		multiChoiceBox.getSelectionModel().select(selectedTest.get("multi_point"));
    		tfChoiceBox.getItems().addAll(scores);
    		tfChoiceBox.getSelectionModel().select(selectedTest.get("tf_point"));
    		if(!checkBox.isSelected()) {
    			singleChoiceBox.setDisable(true);
    			multiChoiceBox.setDisable(true);
    			tfChoiceBox.setDisable(true);
    		}
    		/*
    		if (if_count) {
    			String[] scores = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        		singleChoiceBox.getItems().addAll(scores);
        		singleChoiceBox.getSelectionModel().select(selectedTest.get("single_point"));
        		multiChoiceBox.getItems().addAll(scores);
        		multiChoiceBox.getSelectionModel().select(selectedTest.get("multi_point"));
        		tfChoiceBox.getItems().addAll(scores);
        		tfChoiceBox.getSelectionModel().select(selectedTest.get("tf_point"));
    		}else {
    			singleChoiceBox.setDisable(true);
    			multiChoiceBox.setDisable(true);
    			tfChoiceBox.setDisable(true);
    		}
    		*/
    }
    
    private void setupList() {
    		getSingleList();
    		getMultiList();
    		getTfList();
    }
    
    private void reload() {
    		ObservableList<HashMap<String, String>> singleOblist = FXCollections.observableArrayList();
    		singleOblist.addAll(singleList);
    		ObservableList<HashMap<String, String>> multiOblist = FXCollections.observableArrayList();
    		multiOblist.addAll(multiList);
    		ObservableList<HashMap<String, String>> tfOblist = FXCollections.observableArrayList();
    		tfOblist.addAll(tfList);
    		singleTableView.setItems(singleOblist);
    		multiTableView.setItems(multiOblist);
    		tfTableView.setItems(tfOblist);
    }
    private void getSingleList() {
    		String[] values = {selectedTest.get("id")};
    		singleList = dbHelper.getEntireList(searchColumn, values, "exam_qa_single");
    }
    private void getMultiList() {
    		String[] values = {selectedTest.get("id")};
    		multiList = dbHelper.getEntireList(searchColumn, values, "exam_qa_multiple");
    }
    private void getTfList() {
    		String[] values = {selectedTest.get("id")};
    		tfList = dbHelper.getEntireList(searchColumn, values, "exam_qa_tf");
    }
    
    private boolean has_selected(TableView<HashMap<String, String>> tableView) {
    		if(tableView.getSelectionModel().getSelectedItem()!=null) {
    			return true;
    		}
    		return false;
    }
    
    private String getMultiAnswer(CheckBox cb1, CheckBox cb2, CheckBox cb3, CheckBox cb4, CheckBox cb5) {
    		String answer = "";
    		if (cb1.isSelected()) {
    			answer += "A";
    		}
    		if (cb2.isSelected()) {
    			answer += "B";
    		}
    		if (cb3.isSelected()) {
    			answer += "C";
    		}
    		if (cb4.isSelected()) {
    			answer += "D";
    		}
    		if (cb5.isSelected()) {
    			answer += "E";
    		}
    		return answer;
    }
    
    //check if all the field has input
    private boolean validate(List<TextField> fieldlist, ToggleGroup group) {
    		for(TextField field:fieldlist) {
    			if (field.getText().trim().isEmpty()) {
    				return false;
    			}
    		}
    		if(group.getSelectedToggle() == null) {
    			return false;
    		}
    		return true;
    }
    private boolean validate(List<TextField> fieldlist, List<CheckBox> checklist){
    		boolean checked = false;
    		for(TextField field:fieldlist) {
			if (field.getText().trim().isEmpty()) {
				return false;
			}
		}
    		for(CheckBox box:checklist) {
    			if(box.isSelected()) {
    				checked = true;
    				break;
    			}
    		}
    		return checked;
    }
    
    private void multiplePopUp(HashMap<String, String> inputMap) {
    		QuestionBox box = new QuestionBox();
		box.confirmButton.setOnAction(e->{
			List<TextField> fieldlist = Arrays.asList(box.question, box.option1, box.option2, box.option3, box.option4, box.option5);
			List<CheckBox> checklist = Arrays.asList(box.op1, box.op2, box.op3, box.op4, box.op5);
			if(validate(fieldlist, checklist)) {
				HashMap<String, String> map;
				boolean is_empty;
				if(inputMap == null) {
					is_empty = true;
					map = new HashMap<String, String>();
					map.put("exam_id", selectedTest.get("id"));
				}else {
					is_empty = false;
					map = inputMap;
				}
				map.put("question", box.question.getText());
				map.put("option1", box.option1.getText());
				map.put("option2", box.option2.getText());
				map.put("option3", box.option3.getText());
				map.put("option4", box.option4.getText());
				map.put("option5", box.option5.getText());
				map.put("answer", getMultiAnswer(box.op1, box.op2, box.op3, box.op4, box.op5));
				//dbHelper.insert(map, "exam_qa_single");
				boolean output = false;
				if(is_empty) {
					output = dbHelper.insert(map, "exam_qa_multiple");
				}else {
					output = dbHelper.update(map, "exam_qa_multiple");
				}
				if(output) {
					this.getMultiList();
					this.reload();
					box.stage.close();
				}
			}else {
				PopupWindow popUP = new PopupWindow();
				popUP.alertWindow("选项不能为空", "所有选项不能为空");
			}
			
		});
		box.multiple(inputMap);
    }
    private void singlePopUp(HashMap<String, String> inputMap) {
    		QuestionBox box = new QuestionBox();
		box.confirmButton.setOnAction(e->{
			List<TextField> fieldlist = Arrays.asList(box.question, box.option1, box.option2, box.option3, box.option4);
			if(validate(fieldlist, box.group)) {
				HashMap<String, String> map;
				boolean is_empty;
				if(inputMap == null) {
					is_empty = true;
					map = new HashMap<String, String>();
					map.put("exam_id", selectedTest.get("id"));
				}else {
					is_empty = false;
					map = inputMap;
				}
    				map.put("question", box.question.getText());
    				map.put("option1", box.option1.getText());
    				map.put("option2", box.option2.getText());
    				map.put("option3", box.option3.getText());
    				map.put("option4", box.option4.getText());
    				map.put("answer", box.group.getSelectedToggle().getUserData().toString());
    				if(is_empty) {
    					dbHelper.insert(map, "exam_qa_single");
    				}else {
    					dbHelper.update(map, "exam_qa_single");
    				}
    				this.getSingleList();
    				this.reload();
    				box.stage.close();
			}else {
				PopupWindow popUP = new PopupWindow();
				popUP.alertWindow("选项不能为空", "所有选项不能为空");
			}
			
		});
		box.single(inputMap);
    }
    private void tfPopUp(HashMap<String, String> inputMap) {
    		QuestionBox box = new QuestionBox();
		box.confirmButton.setOnAction(e->{
			List<TextField> fieldlist = Arrays.asList(box.question);
			if(validate(fieldlist, box.group)) {
				HashMap<String, String> map;
				boolean is_empty;
				if(inputMap == null) {
					is_empty = true;
					map = new HashMap<String, String>();
					map.put("exam_id", selectedTest.get("id"));
				}else {
					is_empty = false;
					map = inputMap;
				}	
    				map.put("question", box.question.getText());
    				map.put("answer", box.group.getSelectedToggle().getUserData().toString());
    				if(is_empty) {
    					dbHelper.insert(map, "exam_qa_tf");
    				}else {
    					dbHelper.update(map, "exam_qa_tf");
    				}
    				this.getTfList();
    				this.reload();
    				box.stage.close();
			}else {
				PopupWindow popUP = new PopupWindow();
				popUP.alertWindow("选项不能为空", "所有选项不能为空");
			}
			
		});
		box.tf(inputMap);
    }

}



