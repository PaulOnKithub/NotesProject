package com.notes.notesproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;



public class NotesController implements Initializable{
    @FXML
    private DatePicker date;
    @FXML
    private MenuBar menuBar;

    @FXML
    private ListView<NoteRecord> entireList;

    @FXML
    private TextArea noteText;

    @FXML
    private TextField tag;

    Database db = new Database();

    @FXML
    //Clear text to enable new input
    void clearTextArea(ActionEvent event) {
        tag.setText("");
        noteText.setText("");
        date.setValue(LocalDate.now());
    }

    @FXML
    //From list view,select an item and delete from database. Retrieve new list and update
    void deleteEntry(ActionEvent event) {
        NoteRecord r=entireList.getSelectionModel().getSelectedItem();
        //checking if the selected item is a null entry(i.e. no record has been selected)
        if(!Objects.isNull(r)) {
            //Calling the delete method in database class, which returns true if successfully deleted record
            if (db.deleteRecord(r)) {
                //Displaying delete success alert box
                Alert delSuccess = new Alert(Alert.AlertType.INFORMATION);
                delSuccess.setTitle("Successful Deletion");
                delSuccess.setHeaderText("Operation Successful");
                delSuccess.setContentText("The selected Note has been successfully deleted");
                delSuccess.showAndWait();
                //Update list
                entireList.getItems().remove(r);
            }
            //The selected entry was a null entry thus showing an alert box alerting user
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
    //From the list view, load selected item to the text area
    void loadEntry(ActionEvent event) {
        NoteRecord selectedItem=entireList.getSelectionModel().getSelectedItem();
        //Checking whether selected item is not a null record
        if(!Objects.isNull(selectedItem)) {
            clearTextArea(event);
            tag.setText(selectedItem.tag());
            noteText.setText(selectedItem.noteValue());
            date.setValue(LocalDate.parse(selectedItem.date()));
        }else {
            //No item was selected, alerting, user
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
    //Select user input from text area, pass to database through save method in database class
    void saveNote(ActionEvent event) {
        //Ensuring a database connection is established and setting up required table if not already set up
        if (db.dbConnection()) db.tableCheck();
        String tagText = tag.getText();
        String notes = noteText.getText();
        //Save to database only if there is user input in text area
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
    @FXML
    void readerView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView.fxml"));
        Parent readerLoader=loader.load();
        ReaderController controller=loader.getController();
        controller.setReaderText(noteText.getText());
        Stage readerStage=new Stage();
        readerStage.setScene(new Scene(readerLoader));
        readerStage.setTitle("Reader View");

        readerStage.show();
    }


    @Override
    //Populate list view on loading of scene
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(db.dbConnection()){
            db.tableCheck();
            entireList.getItems().addAll(db.getRecords());
        }
    }

}







