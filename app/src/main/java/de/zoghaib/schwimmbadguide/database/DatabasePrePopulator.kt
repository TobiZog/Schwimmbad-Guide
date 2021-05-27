package de.zoghaib.schwimmbadguide.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.contentValuesOf
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Pre populate the database with constant informations about the pools
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-26
 */
class DatabasePrePopulator(private val context : Context) {

    /* -------------------- Member Variables -------------------- */

    /** Database handler object */
    private var dbHandler : DatabaseHandler = DatabaseHandler(context)


    /**
     * Load all datasets from csv and write/update it to the database
     */
    fun initDatabase() {

        val isr = InputStreamReader(context.assets.open("POOLS.csv"))
        BufferedReader(isr).use { bufferedReader ->
            // Name of each col
            lateinit var titles : List<String>

            for(line in bufferedReader.readLines()) {
                // Split the line in values
                val commaSplitted = line.split(";")

                // Create or edit dataset, if this is not the header line
                if(commaSplitted[0].startsWith("Id")) {
                    titles = line.split(";")
                } else {
                    val oldDataset = dbHandler.readDatasetToContentValues("POOLS", contentValuesOf(Pair("NAME", commaSplitted[1])))

                    // Update, if there is already a dataset, else, create a new one
                    lateinit var newDataset : ContentValues

                    if(oldDataset == null) {
                        newDataset = ContentValues()
                        newDataset.put("Id", -1)
                    } else {
                        newDataset = oldDataset
                    }

                    // Create the dataset
                    for(i in 1 until commaSplitted.size - 1) {
                        newDataset.put(titles[i], if(commaSplitted[i].isEmpty()) { " " } else { commaSplitted[i] })
                    }

                    dbHandler.writeDatasetFromContentValues("POOLS", newDataset)
                }
            }
        }
    }
}