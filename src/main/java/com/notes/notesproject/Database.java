package com.notes.notesproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
The app relies on a sqlite database which is available to the app without the need for a separate server process.
For the database process to run, jdbc sqlite library must be added to the dependencies.
A record is the interface between the underlying database and the frontend.
*/
public class Database {
    Connection conn=null;

    boolean dbConnection() {
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:Notes.db");
            return true;
        } catch (SQLException e) {
            System.out.println("DB connection failed");
        }
        return false;
    }
    //Check if table exists, if not create one
    void tableCheck(){
        DatabaseMetaData metaData;
        try(Connection conn1=DriverManager.getConnection("jdbc:sqlite:Notes.db")) {
            metaData= conn1.getMetaData();
            ResultSet rs =metaData.getTables(null, null, "NotesTable",null);
            //if NotesTable exist
            if (rs.next()) {
            //If table does not exist, create it
            } else {
                String createSQL="CREATE TABLE \"NotesTable\" (\n" +
                        "\t\"Tag\"\tTEXT,\n" +
                        "\t\"NoteContent\"\tTEXT,\n" +
                        "\t\"Date\"\tTEXT \n" +
                        ");";
                Statement st=conn1.createStatement();
                st.execute(createSQL);
            }
        } catch (SQLException e) {
            System.out.println("Table Creation"+e.getMessage());
        }
    }
//Insert record to database
    boolean insertRecord(NoteRecord recordVal){

        String sqlInput="INSERT INTO NotesTable(Tag,NoteContent,Date) VALUES(?,?,?);";
        try(Connection conn2=DriverManager.getConnection("jdbc:sqlite:Notes.db")) {
            PreparedStatement statement=conn2.prepareStatement(sqlInput);
            statement.setString(1, recordVal.tag());
            statement.setString(2, recordVal.noteValue());
            statement.setString(3, recordVal.date());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Insert operation "+e.getMessage());
            return false;
        }


    }
    //Retrieve all records from table, return a list of records
    List<NoteRecord> getRecords(){
        List<NoteRecord> allRecords= new ArrayList<>();
        try(Connection conn3=DriverManager.getConnection("jdbc:sqlite:Notes.db")){
            String sqlInput="SELECT * FROM NotesTable;";
            Statement statement= conn3.createStatement();
            ResultSet rs=statement.executeQuery(sqlInput);
            while(rs.next()){
                String tagVal= rs.getString("Tag");
                String noteVal=rs.getString("NoteContent");
                String dateVal=rs.getString("Date");
                NoteRecord retrievedRecord=new NoteRecord(tagVal,noteVal,dateVal);
                allRecords.add(retrievedRecord);
            }
        } catch (SQLException e){
            System.out.println("Retrieve operation "+e.getMessage());
        }
        return allRecords;
    }
    //Delete entry in database
    boolean deleteRecord(NoteRecord r) {
        try (Connection conn4 = DriverManager.getConnection("jdbc:sqlite:Notes.db")) {
            String sqlStatement="DELETE FROM NotesTable WHERE Tag=?;";
            PreparedStatement statement=conn4.prepareStatement(sqlStatement);
            statement.setString(1,r.tag());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Delete Operation "+e.getMessage());
            return false;
        }
    }
}

//Utilizing a record. A simple and effective way to interact with the database
record NoteRecord(String tag,String noteValue,String date){
    @Override
    public String toString() {
        return tag+ " ::: "+date;
    }
}
