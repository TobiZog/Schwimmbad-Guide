package de.zoghaib.schwimmbadguide.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseHandler(context: Context) {

	/* -------------------- Member Variables -------------------- */

	/** Creating a database object */
	private var database = DatabaseOpenHelper(context)

	/** Writer to update or add datasets to the database */
	private var writer : SQLiteDatabase? = null

	/** Reader to get datasets from the database */
	private var reader : SQLiteDatabase? = null

	/** Cursor for database values */
	private var cursor : Cursor? = null


	/* -------------------- Initialization -------------------- */

	/**
	 * Primary constructor
	 */
	init {
		// Getting the cursor
		reader = database.readableDatabase
		val cursor = reader?.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null)

		cursor?.close()
	}


	/* -------------------- Read methods -------------------- */


	/* -------------------- Write methods -------------------- */

}