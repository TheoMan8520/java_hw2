package app.todolist;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;

public class HelloController {

    private File textFile;
    @FXML
    private Label currentFile;
    @FXML
    private AnchorPane root;
    @FXML
    public ObservableList<Record> currentRecords = FXCollections.observableArrayList();
    @FXML
    public Record currentRecord;

    @FXML
    public TableView<Record> recordsTable;
    @FXML
    private TableColumn<Record, String> colTodo;
    @FXML
    private TableColumn<Record, String> colNote;
    @FXML
    private TableColumn<Record, String> colDeadline;

    @FXML
    public TextField todoNew;
    @FXML
    public TextField noteNew;
    @FXML
    public TextField deadlineNew;


    @FXML
    private void readRecords(File selectedFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));

            String firstLine = br.readLine();

            Record record;
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                record = new Record(values[1], values[2], values[3]);
                currentRecords.add(record);
            }
            br.close();
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot read records in the file.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void showRecords(File selectedFile) {
        if (currentRecords == null || currentRecords.isEmpty()){
            readRecords(selectedFile);
        }

        colTodo.setCellValueFactory(new PropertyValueFactory<Record, String>("todo"));
        colNote.setCellValueFactory(new PropertyValueFactory<Record, String>("note"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<Record, String>("deadline"));

        recordsTable.setItems(currentRecords);
    }

    private Boolean checkFileValid(File selectedFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));

            String firstLine = br.readLine();

            boolean checkTheRestLines = true;
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 4) {
                    checkTheRestLines = false;
                }
            }
            br.close();

            return firstLine.equals("id,todo,note,deadline") && checkTheRestLines;
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot check if file is valid.");
            errorAlert.showAndWait();
        }
        return false;
    }

    @FXML
    private void openFile() {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("Select File to be Opened");
        fileDialog.setInitialFileName(null);
        if (textFile == null)
            fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
        else
            fileDialog.setInitialDirectory(textFile.getParentFile());

        Window rootWindow = root.getScene().getWindow();
        File selectedFile = fileDialog.showOpenDialog(rootWindow);
        if (selectedFile == null)
            return;

        currentRecords.clear();
        if (checkFileValid(selectedFile)) {
            textFile = selectedFile;
            currentFile.setText("Current File: " + textFile.getName());
            showRecords(selectedFile);
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "File is not valid.");
            errorAlert.showAndWait();
        }

    }

    @FXML
    private void newFile() {
        currentFile.setText("Current file: untitled");
        FileChooser fileDialog = new FileChooser();

        fileDialog.setInitialFileName("untitled.txt");
        fileDialog.setInitialDirectory( new File( System.getProperty("user.home")));
        fileDialog.setTitle("Select File to be Saved");

        Window rootWindow = root.getScene().getWindow();
        File selectedFile = fileDialog.showSaveDialog(rootWindow);
        if ( selectedFile == null )
            return;

        PrintWriter outFile;
        try {
            FileWriter stream = new FileWriter(selectedFile);
            outFile = new PrintWriter( stream );
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot create a new file.");
            errorAlert.showAndWait();
            return;
        }

        try {
            outFile.print("id,todo,note,deadline");
            outFile.flush();
            outFile.close();
            if (outFile.checkError())
                throw new IOException("Error check failed.");
            currentRecords.clear();
            textFile = selectedFile;
            currentFile.setText("Current File: " + textFile.getName());
            showRecords(selectedFile);
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot write a new file.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void saveFile() {
        FileChooser fileDialog = new FileChooser();
        if (textFile == null) {
            currentFile.setText("Current file: untitled");
            fileDialog.setInitialFileName("untitled.txt");
            fileDialog.setInitialDirectory( new File( System.getProperty("user.home")));
        }
        else {
            fileDialog.setInitialFileName(textFile.getName());
            fileDialog.setInitialDirectory(textFile.getParentFile());
        }

        fileDialog.setTitle("Select File to be Saved");
        Window rootWindow = root.getScene().getWindow();
        File selectedFile = fileDialog.showSaveDialog(rootWindow);
        if ( selectedFile == null )
            return;
        PrintWriter outFile;
        try {
            FileWriter stream = new FileWriter(selectedFile);
            outFile = new PrintWriter( stream );
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot open a file to save.");
            errorAlert.showAndWait();
            return;
        }

        try {
            outFile.println("id,todo,note,deadline");
            for (Record record : currentRecords) {
                outFile.print(record.getId() + ",");
                outFile.print(record.getTodo() + ",");
                outFile.print(record.getNote() + ",");
                outFile.print(record.getDeadline() + "\n");
            }
            outFile.flush();
            outFile.close();
            if (outFile.checkError())   // (need to check for errors in PrintWriter)
                throw new IOException("Error check failed.");
            textFile = selectedFile;
            currentFile.setText("Current File: " + textFile.getName());
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Cannot save a file.");
            errorAlert.showAndWait();
        }
    }


    @FXML
    private void selectRecord(MouseEvent event) {
        currentRecord = recordsTable.getSelectionModel().getSelectedItem();

        todoNew.setText(currentRecord.getTodo());
        noteNew.setText(currentRecord.getNote());
        deadlineNew.setText(currentRecord.getDeadline());
    }

    @FXML
    private void resetTextField(){
        todoNew.setText("");
        noteNew.setText("");
        deadlineNew.setText("");
    }

    @FXML
    private void saveRecord() {
        if (!(todoNew.getText().isEmpty() && noteNew.getText().isEmpty() && deadlineNew.getText().isEmpty())) {
            if (!todoNew.getText().contains(",") && !noteNew.getText().contains(",") && !deadlineNew.getText().contains(",")) {
                currentRecords.add(new Record(todoNew.getText(), noteNew.getText(), deadlineNew.getText() ));
                resetTextField();
                showRecords(null);
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Every field must not contain ','.");
                errorAlert.showAndWait();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Every field must be filled.");
            errorAlert.showAndWait();
        }
    }
    @FXML
    private void updateRecord() {
        Record toUpdate = null;
        if (currentRecords != null && currentRecord != null) {
            for (Record record : currentRecords) {
                if (record.getId().equals(currentRecord.getId())) {
                    toUpdate = record;
                    break;
                }
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Please select record to update.");
            errorAlert.showAndWait();
        }

        if (toUpdate != null) {
            toUpdate.setTodo(todoNew.getText());
            toUpdate.setNote(noteNew.getText());
            toUpdate.setDeadline(deadlineNew.getText());
            resetTextField();

            recordsTable.refresh();
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Cannot update record.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void deleteRecord() {
        Record toDelete = null;
        if (currentRecords != null && currentRecord != null) {
            for (Record record : currentRecords) {
                if (record.getId().equals(currentRecord.getId())) {
                    toDelete = record;
                    break;
                }
            }
        }

        if (toDelete != null) {
            currentRecords.remove(toDelete);
            resetTextField();
            showRecords(null);
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Record to delete is not found.");
            errorAlert.showAndWait();
        }
    }
}