package de.zoghaib.schwimmbadguide.objects

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.core.content.contentValuesOf
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.data.PoolCategoryEnum
import de.zoghaib.schwimmbadguide.data.PoolInformations
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import java.sql.Time
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * The swimming pool object
 * Every pool is a own object
 * Offers properties from database
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-27
 */
class SwimmingPool(
    /** Application context */
    val context : Context,

    /** Id in the POOLS table in the SQ-Lite database */
    dbId : Int) {

    /* -------------------- Member Variables -------------------- */

    /** Contains all informations about this pool like name, opening times, etc. */
    var poolInformations : PoolInformations

    /** Database handler object */
    private var dbHandler = DatabaseHandler(context)



    /* -------------------- Lifecycle -------------------- */

    /**
     * Initialization
     */
    init {
        val dataset = dbHandler.readDatasetToContentValues("POOLS", contentValuesOf(Pair("Id", dbId)))!!

        poolInformations = PoolInformations(
            dbId = dataset.getAsInteger("Id"),
            name = dataset.getAsString("NAME"),
            categoryEnum =
            when(dataset.getAsInteger("CATEGORY")) {
                1 -> PoolCategoryEnum.INDOOR
                2 -> PoolCategoryEnum.OUTDOOR
                3 -> PoolCategoryEnum.OUTANDINDOOR
                4 -> PoolCategoryEnum.SPA
                else -> PoolCategoryEnum.LAKE
            },
            latitude = dataset.getAsDouble("LATITUDE"),
            longitude = dataset.getAsDouble("LONGITUDE"),
            imageUrl = dataset.getAsString("IMAGEURL"),
            subtext = dataset.getAsString("SUBTEXT"),
            description = dataset.getAsString("DESCRIPTION"),
            pools = dataset.getAsString("POOLS"),
            restaurant = dataset.getAsString("RESTAURANT"),
            sauna = dataset.getAsString("SAUNA"),
            other = dataset.getAsString("OTHER"),
            phoneNumber = dataset.getAsString("PHONENUMBER"),
            email = dataset.getAsString("EMAIL"),
            address = dataset.getAsString("ADDRESS"),
            mo1 = dataset.getAsString("MO1"),
            mo2 = dataset.getAsString("MO2"),
            di1 = dataset.getAsString("DI1"),
            di2 = dataset.getAsString("DI2"),
            mi1 = dataset.getAsString("MI1"),
            mi2 = dataset.getAsString("MI2"),
            do1 = dataset.getAsString("DO1"),
            do2 = dataset.getAsString("DO2"),
            fr1 = dataset.getAsString("FR1"),
            fr2 = dataset.getAsString("FR2"),
            sa1 = dataset.getAsString("SA1"),
            sa2 = dataset.getAsString("SA2"),
            so1 = dataset.getAsString("SO1"),
            so2 = dataset.getAsString("SO2"),
            prices = "",
            distance = 0.0f,
            publictransport = dataset.getAsString("PUBLICTRANSPORT"),
            saison = dataset.getAsString("SAISON")
            )
    }


    /* -------------------- Public methods -------------------- */

    /**
     * Offers the timetable of today
     *
     * @param   nr      1 = First open slot, 2 = Second open slot
     *
     * @return  String with the opening time in the form of HH:MM-HH:MM
     */
    fun getOpenTimesToday(nr : Int) : String {
        val calendar = Calendar.getInstance()

        return when(calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> { if(nr == 1) { poolInformations.mo1 } else { poolInformations.mo2 } }
            Calendar.TUESDAY -> { if(nr == 1) { poolInformations.di1 } else { poolInformations.di2 } }
            Calendar.WEDNESDAY -> { if(nr == 1) { poolInformations.mi1 } else { poolInformations.mi2 } }
            Calendar.THURSDAY -> { if(nr == 1) { poolInformations.do1 } else { poolInformations.do2 } }
            Calendar.FRIDAY -> { if(nr == 1) { poolInformations.fr1 } else { poolInformations.fr2 } }
            Calendar.SATURDAY -> { if(nr == 1) { poolInformations.sa1 } else { poolInformations.sa2 } }
            Calendar.SUNDAY -> { if(nr == 1) { poolInformations.so1 } else { poolInformations.so2 } }
            else -> ""
        }
    }


    /**
     * Method to get the current open state of the pool
     *
     * @return  OpenEnum with the state
     */
    fun getOpenState() : OpenEnum {
        if(poolInformations.categoryEnum == PoolCategoryEnum.LAKE) {
            return OpenEnum.NOOPENTIMES
        } else {
            try {
                // If there are no data about the opening time
                if(getOpenTimesToday(1).isEmpty()) {
                    return OpenEnum.CLOSED
                } else {
                    // Getting the times in minutes
                    val currentTime = Time(System.currentTimeMillis()).hours * 60 + Time(System.currentTimeMillis()).minutes
                    val open1 = getOpenTimesToday(1).substringBefore(":").toInt() * 60 +
                            getOpenTimesToday(1).substringAfter(":").substringBefore("-").toInt()
                    val close1 = getOpenTimesToday(1).substringAfter("-").substringBefore(":").toInt() * 60 +
                            getOpenTimesToday(1).substringAfterLast(":").toInt()


                    // Get saison
                    val formatter = DateTimeFormatter.ofPattern("d.MM.yyyy", Locale.GERMAN)
                    val saisonStart = LocalDate.parse(poolInformations.saison.substringBefore('-'), formatter)
                    val saisonEnd = LocalDate.parse(poolInformations.saison.substringAfter('-'), formatter)


                    // Calculate the Opening state
                    when {
                        LocalDate.now() < saisonStart || LocalDate.now() > saisonEnd -> {
                            return OpenEnum.OUTOFSAISON
                        }
                        close1 - 60 > currentTime && currentTime > open1 -> {
                            return OpenEnum.OPEN
                        }
                        currentTime in (open1 + 1) until close1 -> {
                            return OpenEnum.WILLBECLOSING
                        }
                        currentTime in (open1 - 60) until open1 -> {
                            return OpenEnum.OPENSOON
                        }
                        else -> {
                            try {
                                val open2 = getOpenTimesToday(2).substringBefore(":").toInt() * 60 +
                                        getOpenTimesToday(2).substringAfter(":").substringBefore("-").toInt()
                                val close2 = getOpenTimesToday(2).substringAfter("-").substringBefore(":").toInt() * 60 +
                                        getOpenTimesToday(2).substringAfterLast(":").toInt()

                                return when {
                                    close2 - 60 > currentTime && currentTime > open2 -> {
                                        OpenEnum.OPEN
                                    }
                                    currentTime in (open2 + 1) until close2 -> {
                                        OpenEnum.WILLBECLOSING
                                    }
                                    else -> OpenEnum.CLOSED
                                }
                            } catch (e: Exception) {
                                return OpenEnum.CLOSED
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                return if(getOpenTimesToday(1).isBlank()) {
                    OpenEnum.CLOSED
                } else {
                    OpenEnum.OUTOFSAISON
                }
            }
        }
    }


    /**
     * Calculate the distance between a location and the location of the pool
     *
     * @param   currentLatitude     Latitude of the user
     * @param   currentLongitude    Longitude of the user
     *
     * @return  Distance in km or 100er-Meter if < 1 km
     */
    fun calculateDistance(currentLatitude : Double, currentLongitude : Double) {
        try {
            val currentLocation = Location("Point A")
            currentLocation.latitude = currentLatitude
            currentLocation.longitude = currentLongitude

            val poolLocation = Location("Point B")
            poolLocation.latitude = poolInformations.latitude
            poolLocation.longitude = poolInformations.longitude


            poolInformations.distance = ((currentLocation.distanceTo(poolLocation) / 100).toInt()).toFloat() / 10

        } catch (e: Exception) {
            poolInformations.distance = 0.0f
        }
    }
}