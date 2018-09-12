package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import Model.DBhelper;
import Model.Loader;
import Model.PopupWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ReportModifyController implements Initializable{
	@FXML Label title;
	@FXML JFXTextArea personArea;
	@FXML JFXTextArea departmentArea;
	@FXML JFXTextArea hospitalArea;
	@FXML VBox box;
	
	@FXML JFXTextField reportDepartment;
	@FXML JFXDatePicker eventDate;
	@FXML JFXDatePicker reportDate;
	
	@FXML JFXTextField patientName;
	ArrayList<JFXCheckBox> patientSex = new ArrayList<JFXCheckBox>();
	@FXML JFXCheckBox male;
	@FXML JFXCheckBox female;
	@FXML JFXTextField patientAge;
	@FXML JFXTextField bedNum;
	@FXML JFXTextField hospitalNum;
	@FXML JFXTextField typeSickness;
	
	//Event Location
	
	ArrayList<JFXCheckBox> eventLocationList = new ArrayList<JFXCheckBox>();
	
	@FXML JFXCheckBox eventLocation1;
	@FXML JFXCheckBox eventLocation2;
	@FXML JFXCheckBox eventLocation3;
	@FXML JFXCheckBox eventLocation4;
	@FXML JFXCheckBox eventLocation5;
	@FXML JFXCheckBox eventLocation6;
	@FXML JFXTextField eventLocation6_text;
	
	@FXML JFXTextField eventDepartment;
	@FXML JFXTextField rpName;
	@FXML JFXTextField rpAge;
	@FXML JFXTextField rpLengthOfWork;
	@FXML JFXTextField rpTitle;
	@FXML JFXTextField rpEducation;
	
	ArrayList<JFXCheckBox> reporterTitleList = new ArrayList<JFXCheckBox>();
	@FXML JFXTextField reporterName;
	@FXML JFXCheckBox reporterTitle1;
	@FXML JFXCheckBox reporterTitle2;
	@FXML JFXCheckBox reporterTitle3;
	@FXML JFXCheckBox reporterTitle4;
	@FXML JFXCheckBox reporterTitle5;
	@FXML JFXTextField reporterPhoneNum;
	
	@FXML JFXTextArea eventProcess;
	
	ArrayList<JFXCheckBox> eventTypeList = new ArrayList<JFXCheckBox>();
	@FXML JFXCheckBox eventType1;
	@FXML JFXCheckBox eventType2;
	@FXML JFXCheckBox eventType3;
	@FXML JFXCheckBox eventType4;
	@FXML JFXCheckBox eventType5;
	@FXML JFXCheckBox eventType6;
	@FXML JFXCheckBox eventType7;
	@FXML JFXCheckBox eventType8;
	@FXML JFXCheckBox eventType9;
	
	ArrayList<JFXCheckBox> reportTypeList = new ArrayList<JFXCheckBox>();
	@FXML JFXCheckBox reportType1;
	@FXML JFXCheckBox reportType2;
	@FXML JFXCheckBox reportType3;
	@FXML JFXCheckBox reportType4;
	@FXML JFXCheckBox reportType5;
	@FXML JFXCheckBox reportType6;
	@FXML JFXCheckBox reportType7;
	@FXML JFXCheckBox reportType8;
	@FXML JFXCheckBox reportType9;
	@FXML JFXCheckBox reportType10;
	@FXML JFXCheckBox reportType11;
	@FXML JFXCheckBox reportType12;
	@FXML JFXCheckBox reportType13;
	@FXML JFXCheckBox reportType14;
	@FXML JFXCheckBox reportType15;
	@FXML JFXCheckBox reportType16;
	@FXML JFXCheckBox reportType17;
	@FXML JFXCheckBox reportType18;
	@FXML JFXCheckBox reportType19;
	@FXML JFXCheckBox reportType20;
	@FXML JFXCheckBox reportType21;
	@FXML JFXCheckBox reportType22;
	@FXML JFXCheckBox reportType23;
	@FXML JFXCheckBox reportType24;
	@FXML JFXCheckBox reportType25;
	@FXML JFXCheckBox reportType26;
	@FXML JFXCheckBox reportType27;
	@FXML JFXCheckBox reportType28;
	@FXML JFXCheckBox reportType29;
	@FXML JFXCheckBox reportType30;
	//.... total 30 
	
	ArrayList<JFXCheckBox> CauseReasonList = new ArrayList<JFXCheckBox>();
	@FXML JFXCheckBox CauseReason1;
	@FXML JFXCheckBox CauseReason2;
	@FXML JFXCheckBox CauseReason3;
	@FXML JFXCheckBox CauseReason4;
	@FXML JFXCheckBox CauseReason5;
	@FXML JFXCheckBox CauseReason6;
	@FXML JFXCheckBox CauseReason7;
	@FXML JFXCheckBox CauseReason8;
	@FXML JFXCheckBox CauseReason9;
	@FXML JFXCheckBox CauseReason10;
	@FXML JFXTextField CauseReason10_text;
	
	
	ArrayList<JFXCheckBox> eventLevelList = new ArrayList<JFXCheckBox>();
	@FXML JFXCheckBox eventLevel1;
	@FXML JFXCheckBox eventLevel2;
	@FXML JFXCheckBox eventLevel3;
	@FXML JFXCheckBox eventLevel4;
	
	@FXML JFXTextArea treatmentMeasure;
	@FXML JFXTextArea discussion;
	@FXML JFXTextArea followUpEvaluation;
	
	@FXML JFXTextField signature1;
	@FXML JFXTextField signature2;
	@FXML JFXDatePicker signatureDate1;
	@FXML JFXDatePicker signatureDate2;
	
	private ObservableList<JFXCheckBox> selected_patientSex = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_patientSex = FXCollections.observableArrayList();
    
	private ObservableList<JFXCheckBox> selected_eventLocation = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_eventLocation = FXCollections.observableArrayList();
    
    private ObservableList<JFXCheckBox> selected_reporterTitle = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_reporterTitle = FXCollections.observableArrayList();
    
    private ObservableList<JFXCheckBox> selected_eventType = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_eventType = FXCollections.observableArrayList();
   
    private ObservableList<JFXCheckBox> selected_reportType = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_reportType = FXCollections.observableArrayList();
    
    private ObservableList<JFXCheckBox> selected_CauseReason = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_CauseReason = FXCollections.observableArrayList();
    
    private ObservableList<JFXCheckBox> selected_eventLevel = FXCollections.observableArrayList();
    private ObservableList<JFXCheckBox> unselected_eventLevel = FXCollections.observableArrayList();
    
    
	private HashMap<String, String> report;
	Loader loader = new Loader();
	DBhelper dbHelper = new DBhelper();
	
	public void setupDatePickers() {
		loader.setupDatePicker(eventDate);
		loader.setupDatePicker(reportDate);
		loader.setupDatePicker(signatureDate1);
		loader.setupDatePicker(signatureDate2);
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupDatePickers();
		report = HospitalReportController.selectedReport;

		configureCheckBox(male, selected_patientSex, unselected_patientSex, patientSex);
		configureCheckBox(female, selected_patientSex, unselected_patientSex, patientSex);
		
		configureCheckBox(eventLocation1, selected_eventLocation, unselected_eventLocation, eventLocationList);
		configureCheckBox(eventLocation2, selected_eventLocation, unselected_eventLocation, eventLocationList);
		configureCheckBox(eventLocation3, selected_eventLocation, unselected_eventLocation, eventLocationList);
		configureCheckBox(eventLocation4, selected_eventLocation, unselected_eventLocation, eventLocationList);
		configureCheckBox(eventLocation5, selected_eventLocation, unselected_eventLocation, eventLocationList);
		configureCheckBox(eventLocation6, selected_eventLocation, unselected_eventLocation, eventLocationList);
		
		configureCheckBox(reporterTitle1, selected_reporterTitle, unselected_reporterTitle, reporterTitleList);
		configureCheckBox(reporterTitle2, selected_reporterTitle, unselected_reporterTitle, reporterTitleList);
		configureCheckBox(reporterTitle3, selected_reporterTitle, unselected_reporterTitle, reporterTitleList);
		configureCheckBox(reporterTitle4, selected_reporterTitle, unselected_reporterTitle, reporterTitleList);
		configureCheckBox(reporterTitle5, selected_reporterTitle, unselected_reporterTitle, reporterTitleList);
		
		configureCheckBox(eventType1, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType2, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType3, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType4, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType5, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType6, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType7, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType8, selected_eventType, unselected_eventType, eventTypeList);
		configureCheckBox(eventType9, selected_eventType, unselected_eventType, eventTypeList);

		
		configureCheckBox(reportType1, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType2, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType3, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType4, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType5, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType6, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType7, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType8, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType9, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType10, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType11, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType12, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType13, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType14, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType15, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType16, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType17, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType18, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType19, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType20, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType21, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType22, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType23, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType24, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType25, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType26, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType27, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType28, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType29, selected_reportType, unselected_reportType, reportTypeList);
		configureCheckBox(reportType30, selected_reportType, unselected_reportType, reportTypeList);
		
		configureCheckBox(CauseReason1, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason2, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason3, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason4, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason5, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason6, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason7, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason8, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason9, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		configureCheckBox(CauseReason10, selected_CauseReason, unselected_CauseReason, CauseReasonList);
		
		configureCheckBox(eventLevel1, selected_eventLevel, unselected_eventLevel, eventLevelList);
		configureCheckBox(eventLevel2, selected_eventLevel, unselected_eventLevel, eventLevelList);
		configureCheckBox(eventLevel3, selected_eventLevel, unselected_eventLevel, eventLevelList);
		configureCheckBox(eventLevel4, selected_eventLevel, unselected_eventLevel, eventLevelList);
		
		eventLocation6.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (isNowSelected) {
				eventLocation6_text.setEditable(true);
			}else {
				eventLocation6_text.clear();
				eventLocation6_text.setEditable(false);
			}
		});
		
		
		CauseReason10.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (isNowSelected) {
				CauseReason10_text.setEditable(true);
			}else {
				CauseReason10_text.clear();
				CauseReason10_text.setEditable(false);
			}
		});
		
		initializeDatePickers();
		setupPage();
		
	}
	
	// DatePicker disable to select any future date.
	private void initializeDatePickers() {
	    Callback<DatePicker, DateCell> dayCellFactory =
	        (final DatePicker datePicker) -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate item, boolean empty) {
	                super.updateItem(item, empty);

	                if(item.isAfter(ChronoLocalDate.from(LocalDate.now()))) {
	                    setDisable(true);
	                }
	            }
	        };
	    
	    eventDate.setDayCellFactory(dayCellFactory);
	    reportDate.setDayCellFactory(dayCellFactory);
	    signatureDate1.setDayCellFactory(dayCellFactory);
	    signatureDate2.setDayCellFactory(dayCellFactory);
	  
	}

	 private void configureCheckBox(JFXCheckBox checkBox, ObservableList<JFXCheckBox> selectedCheckBoxes,
			 ObservableList<JFXCheckBox> unselectedCheckBoxes, ArrayList<JFXCheckBox> List) {
		 	
		 	List.add(checkBox);
		 	
	        if (checkBox.isSelected()) {
	            selectedCheckBoxes.add(checkBox);
	        } else {
	            unselectedCheckBoxes.add(checkBox);
	        }
	        
	        
	        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
 		
	            if (isNowSelected) {
	            		if (selectedCheckBoxes.isEmpty()) {
	            			 unselectedCheckBoxes.remove(checkBox);
	     	              selectedCheckBoxes.add(checkBox);
	            		}else {
	            			 unselectedCheckBoxes.add(selectedCheckBoxes.get(0));
	            			 unselectedCheckBoxes.remove(checkBox);
	            			 selectedCheckBoxes.clear();
		     	         selectedCheckBoxes.add(checkBox);
	            		}
	            	unselectedCheckBoxes.forEach(cb -> cb.setSelected(false));
	            }else {
	            		if (selectedCheckBoxes.contains(checkBox)) {
	            			selectedCheckBoxes.remove(checkBox);
	            		}
	            }       
	        });

	    }
	
	@FXML void loadHome() {
		loader.loadVBox(box, "/View/Welcome.fxml"); 
	}
	
	@FXML void loadReportList() {
		loader.loadVBox(box, "/View/HospitalReport.fxml"); 
	}
	
	@FXML void uploadButton() {

		if(!report.get("level").equals("1")) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", report.get("id"));
			//map.put("comment2", hospitalArea.getText());
			map.put("level", "3");
	
			map.put("reportDepartment", reportDepartment.getText());

			//TODO - note
			//可以允许更改上面的两个日期吗？？？？？
			if (eventDate.getValue()!=null) {
				map.put("eventDate", eventDate.getValue().toString());
			}
			
			if (reportDate.getValue() != null) {
				map.put("reportDate", reportDate.getValue().toString());
			}
			
			map.put("patientName", patientName.getText());
			map.put("patientAge", patientAge.getText());
			map.put("bedNum", bedNum.getText());
			map.put("hospitalNum", hospitalNum.getText());
			map.put("typeSickness", typeSickness.getText());
			
			//第一行 - 患者性别 - 2
			if (!selected_patientSex.isEmpty()) {
				for (int i = 0; i <2; i++) {
					if (patientSex.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("patientSex", temp);
						break;
					}
				}
			}
			
			//第二行 - 发生场所 - 6
			if (!selected_eventLocation.isEmpty()) {
				for (int i = 0; i <6; i++) {
					if (eventLocationList.get(i).isSelected()) {
						if ((i == 5) && (eventLocation6_text != null)) {
							map.put("eventLocation6_text", eventLocation6_text.getText());
						}
						String temp = Integer.toString(i+1);
						map.put("eventLocation", temp);
						break;
					}
				}
			}
			
			//第三行 - 发生科室
			map.put("eventDepartment", eventDepartment.getText());
			map.put("rpName", rpName.getText());
			map.put("rpLengthOfWork", rpLengthOfWork.getText());
			map.put("rpAge", rpAge.getText());
			map.put("rpTitle", rpTitle.getText());
			map.put("rpEducation", rpEducation.getText());
			
			//第四行 - 报告人
			map.put("reporterName", reporterName.getText());
			if (!selected_reporterTitle.isEmpty()) {
				for (int i = 0; i <5; i++) {
					if (reporterTitleList.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("reporterPosition", temp);
						break;
					}
				}
			}
			
			map.put("reporterPhoneNum", reporterPhoneNum.getText());
			
			//第五行 - 事件主要过程
			map.put("eventProcess", eventProcess.getText());
			
			//第六行 - 事件类型
			if (!selected_eventType.isEmpty()) {
				for (int i = 0; i <9; i++) {
					if (eventTypeList.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("eventType", temp);
						break;
					}
				}
			}
			
			//不良事件类别
			if (!selected_reportType.isEmpty()) {
				for (int i = 0; i <30; i++) {
					if (reportTypeList.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("reportType", temp);
						break;
					}
				}
			}
			
			//导致事件原因
			if (!selected_CauseReason.isEmpty()) {
				for (int i = 0; i <10; i++) {
					if (CauseReasonList.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("CauseReason", temp);
						if ((i == 9) && (CauseReason10_text != null)) {
							map.put("CauseReason10_text", CauseReason10_text.getText());
						}
						break;
					}
				}
			}
			
			//事件级别
			if (!selected_eventLevel.isEmpty()) {
				for (int i = 0; i <4; i++) {
					if (eventLevelList.get(i).isSelected()) {
						String temp = Integer.toString(i+1);
						map.put("eventLevel", temp);
						break;
					}
				}
			}
			
			// 事件处理措施：：
			map.put("treatmentMeasure", treatmentMeasure.getText());
			map.put("discussion", discussion.getText());
			map.put("followUpEvaluation", followUpEvaluation.getText());
			map.put("signature1", signature1.getText());
			map.put("signature2", signature2.getText());
			
			if (signatureDate1.getValue() != null) {
				map.put("signatureDate1", signatureDate1.getValue().toString());
			}
			if (signatureDate2.getValue() != null) {
				map.put("signatureDate2", signatureDate2.getValue().toString());
			}
			
			if(dbHelper.update(map, "report_list")) {
				loader.loadVBox(box, "/View/HospitalReport.fxml");
			}
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("无法上传", "科室讨论未完成，不能上传医院处理意见");
		}
		
	}
	
	
	@FXML void contactButton() {
		loader.loadWeb();
	}
	
	
	private void setupPage() {
		reportDepartment.setText(report.get("reportDepartment"));
		eventDate.setPromptText(report.get("eventDate"));
		
		reportDate.setPromptText(report.get("reportDate"));
		
		patientName.setText(report.get("patientName"));
		patientAge.setText(report.get("patientAge"));
		bedNum.setText(report.get("bedNum"));
		hospitalNum.setText(report.get("hospitalNum"));
		typeSickness.setText(report.get("typeSickness"));
		
		//don't know why it can't use String, might be type different
		int patientSex = -1;
		
		if (report.get("patientSex") != null) {
			patientSex = Integer.parseInt(report.get("patientSex"));
		}
		if ( patientSex == -1) {
			System.out.print("ERROR: patientSex is NULL");
		}else {
			if (patientSex == 1) {
				male.setSelected(true);
			}else if (patientSex == 2) {
				female.setSelected(true);
			}else {
				System.out.println("ERROR: patientSex is out of range");
			}
		}
		
		
		//eventLocation
		int eventLocationNum = -1;
		if (report.get("eventLocation") == null) {
			System.out.print("ERROR: eventLocation is NULL");
		}else {
			eventLocationNum = Integer.parseInt(report.get("eventLocation"));
			if (eventLocationNum == 6) {
				eventLocation6.setSelected(true);
				eventLocation6_text.setEditable(true);
				eventLocation6_text.setText(report.get("eventLocation6_text"));
			}else {
				for (int i =1; i < 6; i++) {	
					if (eventLocationNum == i) {
						eventLocationList.get(i-1).setSelected(true);
					}
				}	
			}
		}

		
		eventDepartment.setText(report.get("eventDepartment"));
		rpName.setText(report.get("rpName"));
		rpLengthOfWork.setText(report.get("rpLengthOfWork"));
		rpAge.setText(report.get("rpAge"));
		rpTitle.setText(report.get("rpTitle"));
		rpEducation.setText(report.get("rpEducation"));
		
		reporterName.setText(report.get("reporterName"));
		
		
		int reporterTitleNum = -1;
		if (report.get("reporterPosition") != null) {
			reporterTitleNum = Integer.parseInt(report.get("reporterPosition"));
		}
		if (reporterTitleNum == -1) {
			System.out.print("ERROR: reporterPosition is NULL");
		}else {
			for (int i= 1; i < 6; i++) {
				if (reporterTitleNum == i) {
					reporterTitleList.get(i-1).setSelected(true);
				}
			}
		}

		
		reporterPhoneNum.setText(report.get("reporterPhoneNum"));
		
		eventProcess.setText(report.get("eventProcess"));
		
		
		int eventTypeNum = -1;
		if (report.get("eventType") != null) {
			eventTypeNum = Integer.parseInt(report.get("eventType"));
		}
		if ( eventTypeNum == -1) {
			System.out.print("ERROR: eventType is NULL");
		}else {
			for (int i=1; i < 10; i++) {
				if (eventTypeNum == i) {
					eventTypeList.get(i-1).setSelected(true);
				}
			}
		}

		
		int reportTypeNum = -1;
		if (report.get("reportType") != null) {
			 reportTypeNum = Integer.parseInt(report.get("reportType"));
		}
		if (reportTypeNum == -1) {
			System.out.print("ERROR: reportType is NULL");
		}else {
			for (int i=1; i < 31; i++) {
				if (reportTypeNum == i) {
					reportTypeList.get(i-1).setSelected(true);
				}
			}
		}

		int CauseReasonNum = -1;
		if (report.get("CauseReason") != null) {
			CauseReasonNum = Integer.parseInt(report.get("CauseReason"));
		}
		if (CauseReasonNum == -1) {
			System.out.println("ERROR: Cause Reason is NULL");
		}else if (CauseReasonNum == 10) {
			CauseReason10.setSelected(true);
			CauseReason10_text.setEditable(true);
			CauseReason10_text.setText(report.get("CauseReason10_text"));
		}
		else{
			for (int i=1; i < 10; i++) {
				if (CauseReasonNum == i) {
					CauseReasonList.get(i-1).setSelected(true);
				}
			}
		}
		
		
		int eventLevelNum = -1;
		if (report.get("eventLevel") != null) {
			eventLevelNum = Integer.parseInt(report.get("eventLevel"));
		}
		if (eventLevelNum == -1) {
			System.out.println("ERROR: eventLevel is NULL");
		}else{
			for (int i=1; i < 5; i++) {
				if (eventLevelNum == i) {
					eventLevelList.get(i-1).setSelected(true);
				}
			}
		}
		

		treatmentMeasure.setText(report.get("treatmentMeasure"));
		discussion.setText(report.get("discussion"));
		followUpEvaluation.setText(report.get("followUpEvaluation"));
		
		signature1.setText(report.get("signature1"));
		signature2.setText(report.get("signature2"));
		
		signatureDate1.setPromptText(report.get("signatureDate1"));
		signatureDate2.setPromptText(report.get("signatureDate2"));
		
	}


}
