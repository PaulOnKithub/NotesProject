package com.notes.notesproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
//add error log, menu bar.



public class NotesController implements Initializable{
    @FXML
    private DatePicker date;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button SaveButton;

    @FXML
    private ListView<NoteRecord> entireList;

    @FXML
    private TextArea noteText;

    @FXML
    private TextField tag;

    Database db = new Database();

    @FXML
    void clearTextArea(ActionEvent event) {
        tag.setText("");
        noteText.setText("");
        date.setValue(LocalDate.now());
    }

    @FXML
    void deleteEntry(ActionEvent event) {
        NoteRecord r=entireList.getSelectionModel().getSelectedItem();
        if(!Objects.isNull(r)) {
            if (db.deleteRecord(r)) {
                Alert delSuccess = new Alert(Alert.AlertType.INFORMATION);
                delSuccess.setTitle("Successful Deletion");
                delSuccess.setHeaderText("Operation Successful");
                delSuccess.setContentText("The selected Note has been successfully deleted");
                delSuccess.showAndWait();
                entireList.getItems().remove(r);
            }
        }else {
                Alert noRecordSelected = new Alert(Alert.AlertType.ERROR);
                noRecordSelected.setTitle("Unsuccessful Deletion");
                noRecordSelected.setHeaderText("Operation Failure");
                noRecordSelected.setContentText("No Note Has Been Selected for Deletion");
                noRecordSelected.showAndWait();
            }
        }

    @FXML
    void exitButton(ActionEvent event) {
        Platform.exit();
        event.consume();

    }

    @FXML
    void loadEntry(ActionEvent event) {
        NoteRecord selectedItem=entireList.getSelectionModel().getSelectedItem();
        if(!Objects.isNull(selectedItem)) {
            clearTextArea(event);
            tag.setText(selectedItem.tag());
            noteText.setText(selectedItem.noteValue());
            date.setValue(LocalDate.parse(selectedItem.date()));
        }else {
            Alert errLoading = new Alert(Alert.AlertType.ERROR);
            errLoading.setTitle("Unsuccessful Load");
            errLoading.setHeaderText("Operation Failure");
            errLoading.setContentText("No Note Has Been Selected for Loading");
            errLoading.showAndWait();
        }
    }


    @FXML
    void minimizeButton(ActionEvent event) {
        Stage stage= (Stage) ((Node)event.getTarget()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void saveNote(ActionEvent event) {

        if (db.dbConnection()) db.tableCheck();
        String tagText = tag.getText();
        String notes = noteText.getText();
        if (!(StringUtils.isEmpty(tagText) && StringUtils.isEmpty(notes))) {
            try {
                String dateText = date.getValue().toString();
                if (!(StringUtils.isEmpty(dateText))) {
                    NoteRecord recordVal = new NoteRecord(tagText, notes, dateText);
                    if (db.insertRecord(recordVal)) clearTextArea(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Note successfully saved in database");
                    alert.setHeaderText("Save Operation");
                    alert.showAndWait();
                    entireList.getItems().add(recordVal);
                }
            }catch(Exception e){
                Alert dateWarning = new Alert(Alert.AlertType.WARNING);
                dateWarning.setTitle("Empty Date");
                dateWarning.setContentText("Set a date before saving the note entry");
                dateWarning.setHeaderText("Date Not Set");
                dateWarning.showAndWait();
            }
        }else {
            Alert saveErralert=new Alert(Alert.AlertType.WARNING);
            saveErralert.setTitle("Empty Content");
            saveErralert.setContentText("The is no text in the text area to save; add text before saving");
            saveErralert.setHeaderText("No Text input");
            saveErralert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(db.dbConnection()){
            db.tableCheck();
            entireList.getItems().addAll(db.getRecords());
        }
    }
}







