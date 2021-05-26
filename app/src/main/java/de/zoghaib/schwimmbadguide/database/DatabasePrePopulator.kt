package de.zoghaib.schwimmbadguide.database

import android.content.ContentValues
import android.content.Context
import androidx.core.content.contentValuesOf
import de.zoghaib.schwimmbadguide.R

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
     * Load all datasets from the string resources and write it to the database, if they don't exist yet
     */
    fun initDatabase() {
        val stringResources = arrayListOf(
            context.resources.getStringArray(R.array.anderter_bad),
            context.resources.getStringArray(R.array.aquaLaatzium)
        )

        for(i in stringResources) {
            val dataset = ContentValues()

            dataset.put("NAME", i[0])
            dataset.put("CATEGORY", i[1])
            dataset.put("SUBTEXT", i[2])
            dataset.put("DESCRIPTION", i[3])
            dataset.put("POOLS", i[4])
            dataset.put("RESTAURANT", i[5])
            dataset.put("EQUIPMENT", i[6])
            dataset.put("PHONENUMBER", i[7])
            dataset.put("EMAIL", i[8])
            dataset.put("IMAGEURL", i[9])

            if(dbHandler.readDatasetToContentValues("POOLS", contentValuesOf(Pair("NAME", i[0]))) == null) {
                dbHandler.writeDatasetFromContentValues("POOLS", dataset)
            }
        }
    }
}