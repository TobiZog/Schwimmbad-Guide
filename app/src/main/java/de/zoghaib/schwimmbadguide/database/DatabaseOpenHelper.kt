package de.zoghaib.schwimmbadguide.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, DBVERSION) {

	/* -------------------- Member Variables -------------------- */

	/**
	 * Companion object with important db values
	 */
	companion object {
		const val DBNAME = "swgDatabase"
		const val DBVERSION = 1
	}


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Creates all tables which are not existing
	 *
	 * @param   db      The SQLite database object
	 */
	override fun onCreate(db: SQLiteDatabase) {
		version1(db)
	}


	/**
	 * Handles action, if the database version changes on app upgrade
	 *
	 * @param   db              The SQLite database object
	 * @param   oldVersion      DB version before the upgrade
	 * @param   newVersion      New DB version
	 */
	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		// for future purpose
	}


	/* -------------------- Local methods -------------------- */

	/**
	 * Changes on Version 1 of the database
	 *
	 * @param   db      The SQLite database object
	 */
	private fun version1(db : SQLiteDatabase) {

		// Table of all pools
		db.execSQL("CREATE TABLE [POOLS]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [NAME] TEXT, [DESCRIPTION] TEXT, [LONGITUDE] REAL, [LATITUDE] REAL)")

		// Table of the user favorites pools
		db.execSQL("CREATE TABLE [FAVORITES]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [POOL] INT, FOREIGN KEY(\"POOL\") REFERENCES \"POOLS\"(\"Id\"))")
	}
}