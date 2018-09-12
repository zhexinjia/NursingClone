package Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionBox {
	
	public Stage stage;
	
	public TextField question = new TextField();
	public TextField option1 = new TextField();
	public TextField option2 = new TextField();
	public TextField option3 = new TextField();
	public TextField option4 = new TextField();
	public TextField option5 = new TextField();
	
	public CheckBox op1;
	public CheckBox op2;
	public CheckBox op3;
	public CheckBox op4;
	public CheckBox op5;
	
	

	public ToggleGroup group;
	
	
	public JFXButton confirmButton = new JFXButton("确认");
	
	
	
	public void single(HashMap<String, String> map) {
		//radio button and group
		
		group = new ToggleGroup();
		RadioButton op1 = new RadioButton("A");
		RadioButton op2 = new RadioButton("B");
		RadioButton op3 = new RadioButton("C");
		RadioButton op4 = new RadioButton("D");
		op1.setUserData("A");
		op2.setUserData("B");
		op3.setUserData("C");
		op4.setUserData("D");
		op1.setTextFill(Color.WHITE);
		op2.setTextFill(Color.WHITE);
		op3.setTextFill(Color.WHITE);
		op4.setTextFill(Color.WHITE);
		op1.setToggleGroup(group);
		op2.setToggleGroup(group);
		op3.setToggleGroup(group);
		op4.setToggleGroup(group);
				
		if(map!=null) {
			question.setText(map.get("question"));
			option1.setText(map.get("option1"));
			option2.setText(map.get("option2"));
			option3.setText(map.get("option3"));
			option4.setText(map.get("option4"));
			String ans = map.get("answer");
			if(ans.equals("A")) {
				op1.setSelected(true);
			}
			if(ans.equals("B")) {
				op2.setSelected(true);
			}
			if(ans.equals("C")) {
				op3.setSelected(true);
			}
			if(ans.equals("D")) {
				op4.setSelected(true);
			}
		}else {
			question.setPromptText("输入题干");
			option1.setPromptText("输入选项A");
			option2.setPromptText("输入选项B");
			option3.setPromptText("输入选项C");
			option4.setPromptText("输入选项D");
		}
		
		Label ansLabel = new Label("选择正确答案: ");
		ansLabel.setTextFill(Color.WHITE);
		HBox hbox = new HBox();
		HBox.setMargin(ansLabel, new Insets(0, 20, 0, 10));
		HBox.setMargin(op1, new Insets(0, 10, 0, 10));
		HBox.setMargin(op2, new Insets(0, 10, 0, 10));
		HBox.setMargin(op3, new Insets(0, 10, 0, 10));
		HBox.setMargin(op4, new Insets(0, 10, 0, 10));
		
		hbox.getChildren().addAll(ansLabel, op1, op2, op3, op4);
		
		
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(600);
		stage.setHeight(500);
		stage.setTitle("新建单项选择");
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 20, 0, 20));
			
		confirmButton.setPrefWidth(250);
		confirmButton.setStyle("-fx-background-color: #62baf0");
		Label label = new Label("新建单选题");
		label.setFont(new Font("Arial", 30));
		label.setTextFill(Color.WHITE);
		VBox.setMargin(label, new Insets(0, 30, 40, 30));
		VBox.setMargin(question, new Insets(0, 30, 30, 30));
		VBox.setMargin(option1, new Insets(0, 30, 30, 30));
		VBox.setMargin(option2, new Insets(0, 30, 30, 30));
		VBox.setMargin(option3, new Insets(0, 30, 30, 30));
		VBox.setMargin(option4, new Insets(0, 30, 30, 30));
		VBox.setMargin(hbox, new Insets(0, 30, 30, 30));
		VBox.setMargin(confirmButton, new Insets(20, 20, 20, 20));
		vbox.getChildren().addAll(label, question, option1, option2, option3, option4, hbox, confirmButton);
		vbox.setStyle("-fx-background-color: #393f4f");
		
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("/View/application.css");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
	public void multiple(HashMap<String, String> map) {
		op1 = new CheckBox("A");
		op2 = new CheckBox("B");
		op3 = new CheckBox("C");
		op4 = new CheckBox("D");
		op5 = new CheckBox("E");	
		op1.setTextFill(Color.WHITE);
		op2.setTextFill(Color.WHITE);
		op3.setTextFill(Color.WHITE);
		op4.setTextFill(Color.WHITE);
		op5.setTextFill(Color.WHITE);
		if(map!=null) {
			question.setText(map.get("question"));
			option1.setText(map.get("option1"));
			option2.setText(map.get("option2"));
			option3.setText(map.get("option3"));
			option4.setText(map.get("option4"));
			option5.setText(map.get("option5"));
			String ans = map.get("answer");
			String[] answer = ans.split("");
			List<String> answerList = Arrays.asList(answer);
			if (answerList.contains("A")) {
				op1.setSelected(true);
			}
			if (answerList.contains("B")) {
				op2.setSelected(true);
			}
			if (answerList.contains("C")) {
				op3.setSelected(true);
			}
			if (answerList.contains("D")) {
				op4.setSelected(true);
			}
			if (answerList.contains("E")) {
				op5.setSelected(true);
			}
			
		}else {
			question.setPromptText("输入题干");
			option1.setPromptText("输入选项A");
			option2.setPromptText("输入选项B");
			option3.setPromptText("输入选项C");
			option4.setPromptText("输入选项D");
			option5.setPromptText("输入选项E");
		}
		
		Label ansLabel = new Label("选择正确答案: ");
		ansLabel.setTextFill(Color.WHITE);
		HBox hbox = new HBox();
		HBox.setMargin(ansLabel, new Insets(0, 20, 0, 10));
		HBox.setMargin(op1, new Insets(0, 10, 0, 10));
		HBox.setMargin(op2, new Insets(0, 10, 0, 10));
		HBox.setMargin(op3, new Insets(0, 10, 0, 10));
		HBox.setMargin(op4, new Insets(0, 10, 0, 10));
		HBox.setMargin(op5, new Insets(0, 10, 0, 10));
		
		hbox.getChildren().addAll(ansLabel, op1, op2, op3, op4, op5);
		
		
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(600);
		stage.setHeight(700);
		stage.setTitle("新建单项选择");
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 20, 0, 20));
			
		confirmButton.setPrefWidth(250);
		confirmButton.setStyle("-fx-background-color: #62baf0");
		Label label = new Label("新建多选题");
		label.setFont(new Font("Arial", 30));
		label.setTextFill(Color.WHITE);
		VBox.setMargin(label, new Insets(0, 30, 40, 30));
		VBox.setMargin(question, new Insets(0, 30, 30, 30));
		VBox.setMargin(option1, new Insets(0, 30, 30, 30));
		VBox.setMargin(option2, new Insets(0, 30, 30, 30));
		VBox.setMargin(option3, new Insets(0, 30, 30, 30));
		VBox.setMargin(option4, new Insets(0, 30, 30, 30));
		VBox.setMargin(option5, new Insets(0, 30, 30, 30));
		VBox.setMargin(hbox, new Insets(0, 30, 30, 30));
		VBox.setMargin(confirmButton, new Insets(20, 20, 20, 20));
		vbox.getChildren().addAll(label, question, option1, option2, option3, option4, option5, hbox, confirmButton);
		vbox.setStyle("-fx-background-color: #393f4f");
		
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("/View/application.css");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
	public void tf(HashMap<String, String> map) {
		//radio button and group
		group = new ToggleGroup();
		RadioButton op1 = new RadioButton("对");
		RadioButton op2 = new RadioButton("错");
		op1.setUserData("对");
		op2.setUserData("错");
		op1.setTextFill(Color.WHITE);
		op2.setTextFill(Color.WHITE);
		op1.setToggleGroup(group);
		op2.setToggleGroup(group);
				
		if(map!=null) {
			question.setText(map.get("question"));
			String ans = map.get("answer");
			if(ans.equals("对")) {
				op1.setSelected(true);
			}else{
				op2.setSelected(true);
			}
		}else {
			question.setPromptText("输入题干");
		}
		
		Label ansLabel = new Label("选择正确答案: ");
		ansLabel.setTextFill(Color.WHITE);
		HBox hbox = new HBox();
		HBox.setMargin(ansLabel, new Insets(0, 20, 0, 20));
		HBox.setMargin(op1, new Insets(0, 10, 0, 10));
		HBox.setMargin(op2, new Insets(0, 10, 0, 10));
		
		hbox.getChildren().addAll(ansLabel, op1, op2);
		
		
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(600);
		stage.setHeight(400);
		stage.setTitle("新建判断题");
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 20, 0, 20));
			
		confirmButton.setPrefWidth(250);
		confirmButton.setStyle("-fx-background-color: #62baf0");
		Label label = new Label("新建判断题");
		label.setFont(new Font("Arial", 30));
		label.setTextFill(Color.WHITE);
		VBox.setMargin(label, new Insets(0, 30, 50, 30));
		VBox.setMargin(question, new Insets(0, 30, 30, 30));
		VBox.setMargin(hbox, new Insets(0, 30, 30, 30));
		VBox.setMargin(confirmButton, new Insets(20, 20, 20, 20));
		vbox.getChildren().addAll(label, question, hbox, confirmButton);
		vbox.setStyle("-fx-background-color: #393f4f");
		
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("/View/application.css");
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();		
	}
}
