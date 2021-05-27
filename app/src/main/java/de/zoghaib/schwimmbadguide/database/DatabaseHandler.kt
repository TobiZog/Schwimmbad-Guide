package de.zoghaib.schwimmbadguide.database

import android.content.ContentValues
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

	/**
	 * Reading the values of one dataset in one table to Contentvalues
	 *
	 * @param   table               Table where to search for
	 * @param   searchParameter     ContentValues with the search parameter
	 */
	fun readDatasetToContentValues(table : String, searchParameter : ContentValues) : ContentValues? {
		// Saving the results here
		val resultContentValues = ContentValues()

		// Creating the SQLite-String
		var searchString = "SELECT * FROM $table WHERE"

		for(i in searchParameter.valueSet()) {
			searchString += " ${i.key} = '${i.value}' AND"
		}

		// Remove last "AND"-Statement
		searchString = searchString.substringBeforeLast(" AND")

		// Getting the cursor
		gettingCursor(searchString)


		// Converting the result to contentValues
		try {
			if(cursor?.count != 0) {
				cursor!!.moveToPosition(0)

				for(i in 0 until cursor!!.columnCount) {
					if(!cursor!!.getString(i).isNullOrEmpty()) {
						resultContentValues.put(cursor!!.getColumnName(i), cursor!!.getString(i))
					}
				}

				// Close DB connections
				closeConnection()

				return resultContentValues
			} else {
				// Close DB connections
				closeConnection()

				return null
			}
		} catch (e: Exception) {
			// Close DB connections
			closeConnection()

			return null
		}
	}


	/**
	 * Reading all datasets in one table to an ArrayList
	 *
	 * @param   table   Which table content should be get
	 *
	 * @return  ArrayList with one entry per one line in the table
	 */
	fun readTableToArrayList(table: String) : ArrayList<ContentValues>? {
		// Saving the results here
		val resultArrayList = ArrayList<ContentValues>()

		// Getting the cursor
		gettingCursor("SELECT * FROM $table")

		// Going through the results
		if(cursor?.count != 0) {

			// Going through the rows
			while(cursor!!.moveToNext()) {

				// Get every row
				val row = ContentValues()

				for(i in 0 until cursor!!.columnCount) {

					// Getting the content of the value or write an empty string
					if(!cursor!!.getString(i).isNullOrEmpty()) {
						row.put(cursor!!.getColumnName(i), cursor!!.getString(i))
					} else {
						row.put(cursor!!.getColumnName(i), "")
					}
				}

				resultArrayList.add(row)
			}
		}


		// Close the connection
		closeConnection()


		// Give back value
		return if(resultArrayList.count() > 0) {
			resultArrayList
		} else {
			null
		}
	}


	/* -------------------- Write methods -------------------- */

	/**
	 * Writing a dataset to table of choice in the database
	 *
	 * @param	table			Table of choice where to write the data
	 * @param	contentValues	Data entry for the database
	 */
	fun writeDatasetFromContentValues(table : String, contentValues : ContentValues) {
		// Getting the writer
		writer = database.writableDatabase


		// Decide to add or upgrade
		if(contentValues.getAsInteger("Id") == -1) {
			try {
				contentValues.remove("Id")
				writer!!.insert(table, null, contentValues)
			} catch (e : Exception) { }
		} else {
			try {
				writer!!.update(table, contentValues, "Id = ${contentValues.getAsString("Id")}", null)
			} catch (e : Exception) { }
		}


		// Close the connection
		writer?.close()
	}


	/* -------------------- Local methods -------------------- */

	/**
	 * Requesting the cursor
	 *
	 * @param   sqlCommand  Command to get a part of the table
	 */
	private fun gettingCursor(sqlCommand : String) {
		reader = database.readableDatabase
		cursor = reader!!.rawQuery(sqlCommand, null)
	}


	/**
	 * Closing all connections to the database
	 */
	private fun closeConnection() {
		cursor?.close()
		writer?.close()
		reader?.close()
	}

}