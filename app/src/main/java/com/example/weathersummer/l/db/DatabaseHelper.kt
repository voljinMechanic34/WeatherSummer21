package com.example.weathersummer.l.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weathersummer.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Error

class DataBaseHelper(private val mContext: Context) : SQLiteOpenHelper(mContext,
    DB_NAME, null,
    DB_VERSION
) {

    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false

    init {
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = mContext.applicationInfo.dataDir + "/databases/"
        else
            DB_PATH = "/data/data/" + mContext.packageName + "/databases/"

        copyDataBase()

        this.readableDatabase
    }


    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists())
                dbFile.delete()

            copyDataBase()

            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            this.close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }

        }
    }


    private fun copyDBFile() {
        //InputStream mInput = mContext.getAssets().open(DB_NAME);
        val mInput = mContext.resources.openRawResource(R.raw.database)
        val mOutput = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int = 0
        while ({mLength = mInput.read(mBuffer);mLength}() > 0)
            mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }


    fun openDataBase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDataBase != null
    }


    override  fun close() {
        if (mDataBase != null)
            mDataBase!!.close()
        super.close()
    }


    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion)
            mNeedUpdate = true
    }

    companion object {
        private val DB_NAME = "database.db"
        private var DB_PATH = ""
        private val DB_VERSION = 1
    }
}