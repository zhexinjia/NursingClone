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

public class BankListController implements Initializable {
	
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
    String[] keys = {"name", "singleCount", "multipleCount", "tfCount"};
    String[] fields = {"题库名称", "单选题数", "多选题数", "判断题数"};
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
    			pop.alertWindow("发布失败", "请选中需要发布的考试。");
    		}else if (selectedTest.get("publish_status").equals("未发布")) {
    			loader.loadVBox(box, "/View/TestPublish.fxml");
    		}else {
    			PopupWindow popUP = new PopupWindow();
    			popUP.alertWindow("发布试卷失败", "试卷已经发布，无法重复发布。");
    		}
    }


    @FXML
    void newButton() {
    		//TODO: modify the box including a check box "记分"
    		PopupWindow popUP = new PopupWindow();
		popUP.confirmButton.setText("创建题库");
		popUP.confirmButton.setOnAction(e->{
			if (!popUP.inputField.getText().trim().isEmpty()) {
				//modify Below
				String name = popUP.inputField.getText();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", name);
				map.put("branch", branch);
				if(dbHelper.insert(map, "question_bank")) {
					//modify Above
					popUP.stage.close();
					getList();
					reload();
				}else {
					popUP.errorWindow();
				}
			}else {
				popUP.alertWindow("新建题库出错", "题库名称不能为空！");
			}
		});
		popUP.inputWindow("输入题库名称", "输入题库名称", false);
    }

    @FXML
    void modifyButton() {
    		selectedTest = tableView.getSelectionModel().getSelectedItem();
    		if(selectedTest!=null) {
    			loader.loadVBox(box, "/View/BankModify.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("编辑失败", "请选中需要编辑的题库。");
    		}
    }

    @FXML
    void deleteButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		System.out.println("!!!!!!!!!!!!!!!!!! "+selected);
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
		//String[] searchColumn = new String[]	{"branch"};
		//String[] values = new String[] {branch};
		/*
		String tableName = "question_bank left join exam_qa_single on question_bank.id = exam_qa_single.bank_id left join exam_qa_multiple on question_bank.id "
				+ "= exam_qa_multiple.bank_id left join exam_qa_tf on question_bank.id = exam_qa_tf.bank_id";
				*/
		String tableName = "question_bank qb left join (select bank_id as s_bk, count(exam_qa_single.id) "
				+ "as singleCount from exam_qa_single group by bank_id) ss on qb.id = ss.s_bk "
				+ "left join (select bank_id as m_bk, count(exam_qa_multiple.id) as multipleCount from exam_qa_multiple group by "
				+ "bank_id) mm on qb.id = mm.m_bk "
				+ "left join (select bank_id as tf_bk, count(exam_qa_tf.id) as tfCount from exam_qa_tf "
				+ "group by bank_id) tf on qb.id = tf.tf_bk";
		//String[] columns = new String[] {"id","name", "COUNT(exam_qa_single.id) as singleCount", "COUNT(exam_qa_multiple.id) as multipleCount", "COUNT(exam_qa_tf.id) as tfCount"};
		String[] columns = new String[] {"id", "name", "ss.singleCount", "mm.multipleCount", "tf.tfCount"};
		//list = dbHelper.getList(searchColumn, values, tableName, columns);
		String sqlStatement = "where branch = '" + branch + "'";
		list = dbHelper.getList(sqlStatement, tableName, columns);
	}
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	private void deleteFunction(HashMap<String, String> map) {		
		PopupWindow popUP = new PopupWindow();
		popUP.confirmButton.setOnAction(e->{
			if(dbHelper.deleteBank(map)) {
				popUP.stage.close();
				getList();
				reload();
			}
		});
		popUP.confirmWindow("确认要删除考卷吗？", "删除考卷将删除所有与本考卷相关信息");
	}

}

