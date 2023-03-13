package com.notes.notesproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    Connection conn=null;

    boolean dbConnection() {
        try {
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
        try {
            metaData= conn.getMetaData();
            ResultSet rs =metaData.getTables(null, null, "NotesTable",null);
            //if NotesTable exist
            if (rs.next()) {
                return;
            //If table does not exist, create it
            } else {
                String createSQL="CREATE TABLE \"NotesTable\" (\n" +
                        "\t\"Key\"\tINTEGER NOT NULL UNIQUE,\n" +
                        "\t\"Tag\"\tTEXT,\n" +
                        "\t\"NoteContent\"\tTEXT,\n" +
                        "\t\"Date\"\tTEXT,\n" +
                        "\tPRIMARY KEY(\"Key\" AUTOINCREMENT)\n" +
                        ");";
                Statement st=conn.createStatement();
                st.execute(createSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    boolean insertRecord(NoteRecord recordVal){

        String sqlInput="INSERT INTO NotesTable(Tag,NoteContent,Date) VALUES(?,?,?);";
        try(Connection conn2=conn) {
            PreparedStatement statement=conn2.prepareStatement(sqlInput);
            statement.setString(1, recordVal.tag());
            statement.setString(2, recordVal.noteValue());
            statement.setString(3, recordVal.date());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }


    }
    List<NoteRecord> getRecords(){
        List<NoteRecord> allRecords= new ArrayList<>();
        try(Connection conn3=conn){
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
            System.out.println(e.getMessage());
        }
        return allRecords;
    }
}

record NoteRecord(String tag,String noteValue,String date){
    @Override
    public String toString() {
        return tag+ " ::: "+date;
    }
}
