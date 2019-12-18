package com.emito.eribus.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,DB_NAME,null, DB_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_USERS)
        onCreate(db)
    }

    companion object{
        //Database Information
        val DB_NAME="EriBus.db"
        val DB_VERSION=3

        //Login-User Table Information
        val USER_TABLE="Users"
        val USER_COLUMN_USERNAME="UserName"
        val USER_COLUMN_EMAIL="Email"
        val USER_COLUMN_PASSWORD="Password"
        val CREATE_TABLE_USERS="CREATE TABLE If not exists $USER_TABLE(" +
                "$USER_COLUMN_USERNAME TEXT NOT NULL," +
                "$USER_COLUMN_EMAIL TEXT," +
                "$USER_COLUMN_PASSWORD TEXT," +
                "PRIMARY KEY($USER_COLUMN_USERNAME)" +
                ")"
        val DROP_TABLE_USERS="DROP TABLE IF EXISTS $USER_TABLE"
    }
}