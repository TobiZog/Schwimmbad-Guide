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
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [NAME] TEXT, [CATEGORY] TEXT, [IMAGEURL] TEXT, [SUBTEXT] TEXT, [DESCRIPTION] TEXT, " +
				"[POOLS] TEXT, [RESTAURANT] TEXT, [SAUNA] TEXT, [OTHER] TEXT, [PHONENUMBER] TEXT, [EMAIL] TEXT, [ADDRESS] TEXT, [LATITUDE] REAL, [LONGITUDE] REAL," +
				"[MO1] TEXT, [MO2] TEXT, [DI1] TEXT, [DI2] TEXT, [MI1] TEXT, [MI2] TEXT, [DO1] TEXT, [DO2] TEXT, [FR1] TEXT, [FR2] TEXT, [SA1] TEXT, [SA2] TEXT," +
				"[SO1] TEXT, [SO2] TEXT, [PRICES] TEXT, [PUBLICTRANSPORT] TEXT)")


		// Table of the user favorites pools
		db.execSQL("CREATE TABLE [FAVORITES]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [POOL] INT, FOREIGN KEY(\"POOL\") REFERENCES \"POOLS\"(\"Id\"))")
	}
}