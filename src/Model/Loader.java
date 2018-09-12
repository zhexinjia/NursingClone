package Model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXDatePicker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Loader {
	
	//navigate to web
	public void loadWeb() {
		try {
			Desktop.getDesktop().browse(new URL("http://saixuntech.cn").toURI());
		}catch(Exception e) {
			
		}
	}
	
	public boolean printBarCode(String barCode) {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("生成二维码");
		File file = fileChooser.showSaveDialog(stage);
		if(file != null) {
			Path path = file.toPath();
			try {
				BarCodeWriter generator = new BarCodeWriter();
				generator.generateCode(path, barCode);
				return true;
			} catch (WriterException | IOException e) {
				PopupWindow pop = new PopupWindow();
				pop.errorWindow();
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public File openFileChooser() {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择文件");
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}
	
	//import excel
	public ArrayList<HashMap<String, String>> importExcel(String[] keylist, String[] fields) {
		ArrayList<HashMap<String, String>> list = null;
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入文件");
		
		File file = fileChooser.showOpenDialog(stage);
		if(file != null) {
			String path = file.getAbsolutePath();
			try {
				list = ExcelHelper.readXLSXFile(path, keylist, fields);
				return list;
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//export excel
	public void exportExcel(ArrayList<HashMap<String, String>> maplist, String[] fieldlist, String[] keylist) {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("export");
		File file = fileChooser.showSaveDialog(stage);
		if(file != null) {
			String path = file.getAbsolutePath() + ".xlsx";
			ExcelHelper.writeXLSXFile(maplist, fieldlist, keylist, path);
		}else {
			PopupWindow popUP = new PopupWindow();
			popUP.alertWindow("找不到文件", "请确认选择了正确的文件");
		}
		
	}
	
	//replace the old VBox to new VBox
	public void loadVBox(VBox vbox, String path) {
		BorderPane mainPane = (BorderPane) vbox.getParent();
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(getClass().getResource(path));
			VBox mainView;
			mainView = (VBox) loader.load();
			mainPane.setCenter(mainView);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@param: table view, table view keySet, fields[]
	//set up the table
	public void setupTable(TableView<HashMap<String, String>> tableView, String[] keys, String[] fields) {
		ArrayList<TableColumn<HashMap<String, String>, String>> cols = new ArrayList<TableColumn<HashMap<String, String>, String>>();
		for(int i = 0; i < keys.length; i++) {
			final int index = i;
			TableColumn<HashMap<String, String>, String> col = new TableColumn<HashMap<String, String>, String>(fields[i]);
			col.setCellValueFactory(new Callback<CellDataFeatures<HashMap<String, String>, String>, ObservableValue<String>>(){
				@Override
				public ObservableValue<String> call(CellDataFeatures<HashMap<String, String>, String> param) {
					SimpleStringProperty output = new SimpleStringProperty();
					output.setValue(param.getValue().get(keys[index]));
					return output;
				}
				
			});
			cols.add(col);
		}
		tableView.getColumns().setAll(cols);
	}
	public void setupCheckTable(TableView<CheckMap> tableView, String[] keys, String[] fields) {
		ArrayList<TableColumn<CheckMap, String>> cols = new ArrayList<TableColumn<CheckMap, String>>();
		TableColumn<CheckMap, CheckBox> cbColumn = new TableColumn<CheckMap, CheckBox>();
		cbColumn.setCellValueFactory(cellData->cellData.getValue().cb.getCheckBox());
		for(int i = 0; i < keys.length; i++) {
			final int index = i;
			TableColumn<CheckMap, String> col = new TableColumn<CheckMap, String>(fields[i]);
			col.setCellValueFactory(new Callback<CellDataFeatures<CheckMap, String>, ObservableValue<String>>(){
				@Override
				public ObservableValue<String> call(CellDataFeatures<CheckMap, String> param) {
					SimpleStringProperty output = new SimpleStringProperty();
					output.setValue(param.getValue().map.get(keys[index]));
					return output;
				}
			});
			cols.add(col);
		}
		tableView.getColumns().add(cbColumn);
		tableView.getColumns().addAll(cols);
	}

	public ObservableList<HashMap<String, String>> search(ArrayList<HashMap<String, String>>list, String searchField){
		ObservableList<HashMap<String, String>> output = FXCollections.observableArrayList();
		if (searchField.trim().isEmpty()) {
			output.addAll(list);
		}else {
			output.addAll(searchCallBack(list, searchField.split(" ")));
		}
		return output;
	}
	//search function, return list of hash-map contains search item
	public ObservableList<HashMap<String, String>> searchCallBack(ArrayList<HashMap<String, String>> list, String[] searchList) {
		List<String> searchCollection = Arrays.asList(searchList);
		ObservableList<HashMap<String, String>> output = FXCollections.observableArrayList();
		for(HashMap<String, String> map:list) {
			//remove id from collection so it won't search for id
			String id = map.get("id");
			Collection<String> values = map.values();
			values.remove(id);
			if(values.containsAll(searchCollection)) {
				output.add(map);
			}
			/*
			if (!Collections.disjoint(values, searchCollection)) {
				output.add(map);
			}
			*/
		}
		return output;
	}
	
	//return array of given key, used to collect the list of total scores, list of sign in, etc
	public ArrayList<String> getArray(ArrayList<HashMap<String, String>> maplist, String key) {
		ArrayList<String> output = new ArrayList<String>();
		for(HashMap<String, String> map:maplist) {
			output.add(map.get(key));
		}
		return output;
	}
	
	//setup the format of date picker to match yyyy/MM/dd
	public void setupDatePicker(JFXDatePicker datePicker) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    @Override
		    public String toString(LocalDate localDate)
		    {
		        if(localDate==null)
		            return "";
		        return dateTimeFormatter.format(localDate);
		    }
		    @Override
		    public LocalDate fromString(String dateString)
		    {
		        if(dateString==null || dateString.trim().isEmpty())
		        {
		            return null;
		        }
		        return LocalDate.parse(dateString,dateTimeFormatter);
		    }
		});
	}
	public void setupDatePicker(DatePicker datePicker) {
		String pattern = "yyyy-MM-dd";
		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(pattern);
		    @Override
		    public String toString(LocalDate localDate)
		    {
		        if(localDate==null) {
		        		return "";
		        }
		        return dateTimeFormatter.format(localDate);
		    }
		    @Override
		    public LocalDate fromString(String dateString)
		    {
		        if(dateString==null || dateString.trim().isEmpty())
		        {
		            return null;
		        }
		        return LocalDate.parse(dateString,dateTimeFormatter);
		    }
		});
		datePicker.setPromptText(pattern.toLowerCase());
		datePicker.requestFocus();
	}
	public boolean selectionCheck(HashMap<String, String> map) {
		if(map==null) {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("操作失败", "请选中一个目标。");
			return false;
		}else {
			return true;
		}
	}

}
