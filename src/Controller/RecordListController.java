package Controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import com.jfoenix.controls.JFXButton;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RecordListController implements Initializable {
	Loader loader = new Loader();
	@FXML TableView<HashMap<String, String>> tableView;
	@FXML Label countLabel;
	@FXML VBox box;
	
	@FXML private CustomTextField searchField;
	DBhelper dbHelper = new DBhelper();
	private ArrayList<HashMap<String, String>> list;
	public static HashMap<String, String> selectedUser;
	
	String[] keys = {"name", "department", "position", "title", "level", "currentScore"};
	String[] fields = {"姓名", "科室", "职务", "职称", "层级", "得分"};
	
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
    void exportButton() {
    		String[] fieldlist = new String[] {"姓名", "科室", "职务", "职称", "层级", "得分", "总分","百分比"};
    		String[] keylist = new String[] {"name", "department", "position", "title", "level", "currentScore", "totalScore", "percent"};
    		loader.exportExcel(list, fieldlist, keylist);
    }


    @FXML
    void modifyButton() {
    		HashMap<String, String> selected = tableView.getSelectionModel().getSelectedItem();
    		if(loader.selectionCheck(selected)) {
    			this.updateScore(selected);
    		}
    }

    @FXML
    void detailButton() {
    		selectedUser = tableView.getSelectionModel().getSelectedItem();
    		if(selectedUser!=null) {
    			loader.loadVBox(box, "/View/RecordDetail.fxml");
    		}else {
    			PopupWindow pop = new PopupWindow();
    			pop.alertWindow("操作失败", "请选中一个用户。");
    		}
    }
    @FXML
    void clearButton() {
    		PopupWindow pop = new PopupWindow();
    		pop.confirmButton.setOnAction(e->{
    			if(dbHelper.emptyScore()) {
    				pop.stage.close();
    				reload();
    			}
    		});
    		pop.confirmWindow("是否确认要清空积分？", "清空积分将保留用户历史记录，但将所有用户积分设置为0，是否继续？");
    		
    }
	
	private void setupTable() {
		loader.setupTable(tableView, keys, fields);
	}
	
	private void getList() {
		String tableName = "user_primary_info inner join user_score on user_primary_info.ssn = user_score.ssn where user_primary_info.branch = '" 
				+ LoginController.branch + "'";
		String[] columns = {"user_primary_info.department", "user_primary_info.name", "user_primary_info.position", "user_primary_info.title", 
				"user_primary_info.level", "user_score.totalScore	", "user_score.currentScore", "user_primary_info.ssn", "user_score.comment"};
		list = dbHelper.getList(tableName, columns);
		for(HashMap<String, String> map:list) {
			String current = map.get("currentScore");
			String total = map.get("totalScore");
			if(current!=null && total != null) {
				double currentScore = Double.parseDouble(current);
				double totalScore = Double.parseDouble(total);
				double percentScore = Math.round(1000*currentScore/totalScore)/10;
				map.put("percent", Double.toString(percentScore)+"%");
			}
		}
	}
	
	private void reload() {
		ObservableList<HashMap<String, String>> searchList = loader.search(list, searchField.getText());
		tableView.setItems(searchList);
		countLabel.setText("共 " +searchList.size()+ " 条");
	}
	
	//自定义更新成绩
	private void updateScore(HashMap<String, String> selectedUser) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(400);
		stage.setHeight(300);
		stage.setTitle("添加分数");
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 20, 0, 20));
		
		JFXButton confirmButton = new JFXButton();
		confirmButton.setPrefWidth(250);
		confirmButton.setStyle("-fx-background-color: #62baf0");
		Label label = new Label("添加分数");
		label.setFont(new Font("Arial", 30));
		label.setTextFill(Color.WHITE);
		TextField score = new TextField();
		TextField total = new TextField();
		TextField comment = new TextField();
		score.setPromptText("得分");
		total.setPromptText("总分");
		comment.setPromptText("备注");
		confirmButton.setText("确认");
		confirmButton.setOnAction(e->{
			//TODO:
			if(!score.getText().trim().isEmpty() && !total.getText().trim().isEmpty()&&!comment.getText().trim().isEmpty()) {
				double currentScore = Double.parseDouble(score.getText());
				double totalScore = Double.parseDouble(total.getText());
				
				double current = Double.parseDouble(selectedUser.get("currentScore"));
				double sum = Double.parseDouble(selectedUser.get("totalScore"));
				sum+=totalScore;
				current+=currentScore;
				String commentNote = selectedUser.get("comment");
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				LocalDateTime now = LocalDateTime.now();  
				if(commentNote==null) {
					commentNote = dtf.format(now) + ":"+comment.getText()+",";
				}else {
					commentNote += dtf.format(now) + ":"+comment.getText()+",";
				}
				
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ssn", selectedUser.get("ssn"));
				map.put("currentScore", Double.toString(current));
				map.put("totalScore", Double.toString(sum));
				map.put("comment", commentNote);
				if(dbHelper.updateScore(map, "user_score")) {
					stage.close();
					this.getList();
					this.reload();
				}
			}else {
				PopupWindow pop = new PopupWindow();
				pop.alertWindow("选项不能为空", "所有选项不能为空");
			}
		});
		
		VBox.setMargin(label, new Insets(0, 30, 40, 30));
		VBox.setMargin(score, new Insets(0, 30, 40, 30));
		VBox.setMargin(total, new Insets(0, 30, 40, 30));
		VBox.setMargin(comment, new Insets(0, 30, 40, 30));
		VBox.setMargin(confirmButton, new Insets(0, 30, 40, 30));
		vbox.getChildren().addAll(label, score, total, comment, confirmButton);
		vbox.setStyle("-fx-background-color: #393f4f");
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("/View/application.css");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}

}
