package Main;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.controlsfx.control.action.Action;

import com.sun.demo.jvmti.hprof.Tracker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller implements Initializable {
		
	//has been reset previously
	boolean reset = false;
	
	//flags for data collection options
	boolean flag1, flag2, flag3 = false;
	
	//string passing the exam 'from' period
	private String examPeriodFrom;
	//string passing the exam 'to' period
	private String examPeriodTo;
	
	@FXML //fx:id="mainPane"
	private Pane mainPane;
	
	@FXML //fx:id="examPeriodPane"
	private Pane examPeriodPane;
	
	@FXML //fx:id="tablePane"
	private Pane tablePane;
	
	@FXML //fx:id="topPane"
	private Pane topPane;
	
	@FXML //fx:id="bottomPane"
	private Pane bottomPane;
	
	@FXML //fx:id="addButton"
	private Button addButton;
	
	@FXML //fx:id="test"
	private Button test;
	
	@FXML //fx:id="resetButton"
	private Button resetButton;
	
	@FXML //fx:id="exportButton"
	private Button exportButton;
	
	@FXML //fx:id="importButton1"
	private Button importButton1;
	
	@FXML //fx:id="importButton2"
	private Button importButton2;
	
	@FXML //fx:id="importButton3"
	private Button importButton3;

	@FXML //fx:id="labelStudentID"
	private Text labelStudentID;
	
	@FXML //fx:id="labelModuleCode"
	private Text labelModuleCode;
	
	@FXML //fx:id="labelDate"
	private Text labelDate;
	
	@FXML //fx:id="labelBuildingNumber"
	private Text labelBuildingNumber;
	
	@FXML //fx:id="labelRoomNumber"
	private Text labelRoomNumber;
	
	@FXML //fx:id="fieldStudentID"
	private TextField fieldStudentID;
	
	@FXML //fx:id="fieldModuleCode"
	private TextField fieldModuleCode;
	
	@FXML //fx:id="fieldDate"
	private TextField fieldDate;
	
	@FXML //fx:id="fieldBuildingNumber"
	private TextField fieldBuildingNumber;
	
	@FXML //fx:id="fieldRoomNumber"
	private TextField fieldRoomNumber;
	
	@FXML //fx:id="dateFrom"
	private DatePicker dateFrom;

	@FXML //fx:id="dateTo"
	private DatePicker dateTo;
	
	@FXML //fx:id="columnCode"
	private TableColumn<Test, String> columnCode;
	
	@FXML //fx:id="columnCode"
	private TableColumn<Test, String> columnLastName;
	
	@FXML //fx:id="columnCode"
	private TableColumn<Test, String> columnEmail;
	
	@FXML //fx:id="treeTable"
	private TableView<Test> tableView;
	
	//list with values for column 1
	 private ObservableList<Test> data =
		        FXCollections.observableArrayList(
		            new Test("Jacob", "Smith", "jacob.smith@example.com"),
		            new Test("Isabella", "Johnson", "isabella.johnson@example.com"),
		            new Test("Ethan", "Williams", "ethan.williams@example.com"),
		            new Test("Emma", "Jones", "emma.jones@example.com"),
		            new Test("Jacob", "Smith", "jacob.smith@example.com"),
		            new Test("Isabella", "Johnson", "isabella.johnson@example.com"),
		            new Test("Ethan", "Williams", "ethan.williams@example.com"),
		            new Test("Emma", "Jones", "emma.jones@example.com"),
		            new Test("Jacob", "Smith", "jacob.smith@example.com"),
		            new Test("Isabella", "Johnson", "isabella.johnson@example.com"),
		            new Test("Ethan", "Williams", "ethan.williams@example.com"),
		            new Test("Emma", "Jones", "emma.jones@example.com"),
		            new Test("Jacob", "Smith", "jacob.smith@example.com"),
		            new Test("Isabella", "Johnson", "isabella.johnson@example.com"),
		            new Test("Ethan", "Williams", "ethan.williams@example.com"),
		            new Test("Emma", "Jones", "emma.jones@example.com"),
		            new Test("Michael", "Brown", "michael.brown@example.com")
		        );
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		addButton.setOnAction(event -> {
			//if has been reset, add new data to the table
			if(reset == true) {
				if(allFieldsAreFilledUp()) {
					reset = false;
					tableView.setLayoutX(214);
					tableView.setLayoutY(14);
					tableView.setPrefSize(1073, 371);
					ObservableList<Test> data =FXCollections.observableArrayList(new Test(fieldStudentID.getText(), fieldModuleCode.getText(), fieldBuildingNumber.getText()));
					bottomPane.getChildren().add(tableView);
					organizeColumnCode();
					this.tableView.setItems(data);					
				} else {
					//prompt blank fields
					triggerPopUp();
				}
			} else {
				if(allFieldsAreFilledUp()) {
					bottomPane.getChildren().remove(tablePane);
					organizeColumnCode();
					tableView.setItems(populateTableWithManuallyInsertedData(data));					
				} else {
					//prompt blank fields
					triggerPopUp();
				}
			}
		});	
		
		test.setOnAction(event -> {
			examPeriodFrom = dateFrom.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			examPeriodTo = dateTo.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			System.out.println(examPeriodFrom +  " " + examPeriodTo); 
			openProgressBar();			
			organizeColumnCode();
			tableView.setItems(data);
		});	
		
		exportButton.setOnAction(event -> {
			PDFExport pdf = new PDFExport(); 
			pdf.export1(data);
			pdf.export2(data);
		});
		
		resetButton.setOnAction(event -> {
			resetEverything();
		});
		
		checkImports();
	}
	
	private void openProgressBar() {
		ProgressIndicator progress = new ProgressIndicator();
		Stage stage = new Stage();
		FlowPane p = new FlowPane();
		Scene scene = new Scene(p);
		stage.setScene(scene);
		scene.setRoot(progress);
		stage.show();
		mainPane.setDisable(true);
	}
	
	private void organizeColumnCode() {
		columnCode.setCellValueFactory(new PropertyValueFactory<Test, String>("firstName"));
		columnLastName.setCellValueFactory(new PropertyValueFactory<Test, String>("lastName"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<Test, String>("email"));
	}
	
	private void checkImports() {
		importButton1.setOnAction(event -> {
			flag1 = true;
			optionSelected();
		});
		
		importButton2.setOnAction(event -> {
			flag2 = true;
			optionSelected();
		});
		
		importButton3.setOnAction(event -> {
			flag3 = true;
			optionSelected();
			if(flag1 == true && flag2 == true) {
				topPane.getChildren().remove(examPeriodPane);
			}
			flag3 = false;
		});
		
	}
	
	//loading bar
	
	private boolean allFieldsAreFilledUp() {
		if(fieldStudentID.getText().trim().isEmpty() || fieldModuleCode.getText().trim().isEmpty() || fieldDate.getText().trim().isEmpty() || fieldBuildingNumber.getText().trim().isEmpty() || fieldRoomNumber.getText().trim().isEmpty()){
			return false;
		} else {
			return true;
		}
	}
	
	private ObservableList<Test> populateTableWithManuallyInsertedData(ObservableList<Test> data) {
		data = FXCollections.observableArrayList(new Test(fieldStudentID.getText(), fieldModuleCode.getText(), fieldBuildingNumber.getText()));
		return data;
	}
	
	private void triggerPopUp() {
		Stage st = new Stage();
		FlowPane p = new FlowPane();
		Scene s = new Scene(p);
		st.setScene(s);
		st.show();
	}
	
	private void optionSelected() {
		if(flag1 == true || flag2 == true || flag3 == true) {
			labelStudentID.setDisable(true);
			labelModuleCode.setDisable(true);
			labelDate.setDisable(true);
			labelBuildingNumber.setDisable(true);
			labelRoomNumber.setDisable(true);
			
			fieldStudentID.setDisable(true);
			fieldModuleCode.setDisable(true);
			fieldDate.setDisable(true);
			fieldBuildingNumber.setDisable(true);
			fieldRoomNumber.setDisable(true);
		}
	}
	
	private void resetEverything() {
		reset = true;
		labelStudentID.setDisable(false);
		labelModuleCode.setDisable(false);
		labelDate.setDisable(false);
		labelBuildingNumber.setDisable(false);
		labelRoomNumber.setDisable(false);
		
		fieldStudentID.setDisable(false);
		fieldModuleCode.setDisable(false);
		fieldDate.setDisable(false);
		fieldBuildingNumber.setDisable(false);
		fieldRoomNumber.setDisable(false);
		
		bottomPane.getChildren().remove(tableView);
	}
	
	//fill tableview
}
