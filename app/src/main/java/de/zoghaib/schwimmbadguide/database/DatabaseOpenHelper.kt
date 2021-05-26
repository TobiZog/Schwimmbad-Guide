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
				"[EQUIPMENT] TEXT, [PHONENUMBER] TEXT, [EMAIL] TEXT, [ADDRESS] TEXT, [LONGITUDE] REAL, [LATITUDE] REAL, [OPENINGTIMES] INT," +
				"[PRICES] TEXT, FOREIGN KEY(\"OPENINGTIMES\") REFERENCES \"OPENINGTIMES\"(\"Id\"))")

		// Table of opening times
		db.execSQL("CREATE TABLE [OPENINGTIMES]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [MOOPEN1] TEXT, [MOCLOSE1] TEXT, [MOOPEN2] TEXT, [MOCLOSE2] TEXT," +
				"[DIOPEN1] TEXT, [DICLOSE1] TEXT, [DIOPEN2] TEXT, [DICLOSE2] TEXT, [MIOPEN1] TEXT, [MICLOSE1] TEXT, [MIOPEN2] TEXT, [MICLOSE2] TEXT," +
				"[DOOPEN1] TEXT, [DOCLOSE1] TEXT, [DOOPEN2] TEXT, [DOCLOSE2] TEXT, [FROPEN1] TEXT, [FRCLOSE1] TEXT, [FROPEN2] TEXT, [FRCLOSE2] TEXT," +
				"[SAOPEN1] TEXT, [SACLOSE1] TEXT, [SAOPEN2] TEXT, [SACLOSE2] TEXT, [SOOPEN1] TEXT, [SOCLOSE1] TEXT, [SOOPEN2] TEXT, [SOCLOSE2] TEXT)")

		// Table of prices
		db.execSQL("CREATE TABLE [PRICES]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [DAYCHILD] TEXT, [DAYREDUCED] TEXT, [DAYADULT] TEXT, " +
				"[TENTICKETSCHILD] TEXT, [TENTICKETSREDUCED] TEXT, [TENTICKETSADULT] TEXT," +
				"[SAISONCHILD] TEXT, [SAISONREDUCED] TEXT, [SAISONADULT] TEXT, [SAISONFAMILYREDUCED] TEXT, [SAISONFAMILY] TEXT)")

		// Table of the user favorites pools
		db.execSQL("CREATE TABLE [FAVORITES]" +
				"([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [POOL] INT, FOREIGN KEY(\"POOL\") REFERENCES \"POOLS\"(\"Id\"))")
	}
}