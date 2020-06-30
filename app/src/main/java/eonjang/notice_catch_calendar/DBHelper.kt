package eonjang.notice_catch_calendar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
    ): SQLiteOpenHelper(context, name, factory, version){

    override fun onCreate(db: SQLiteDatabase) {
        var sql : String = "CREATE TABLE if not exists calendar " +
                            "(_id integer primary key autoincrement," +
                            "year," +
                            "month," +
                            "day," +
                            "title," +
                            "memo," +
                            "start_time," +
                            "finish_time," +
                             "color)"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists calendar"

        db.execSQL(sql)
        onCreate(db)
    }
}