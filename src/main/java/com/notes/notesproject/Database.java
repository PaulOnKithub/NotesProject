package com.notes.notesproject;

import java.sql.*;

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

    void insertRecord(NoteRecord r){

    }
    void getRecords(){

    }
}

record NoteRecord(String tag,String noteValue,String date){

}
