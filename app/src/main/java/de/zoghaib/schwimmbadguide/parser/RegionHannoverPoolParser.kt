package de.zoghaib.schwimmbadguide.parser

import androidx.core.content.contentValuesOf
import de.zoghaib.schwimmbadguide.data.PoolInformations
import de.zoghaib.schwimmbadguide.data.PoolNamesEnum
import com.dezlum.codelabs.getjson.GetJson

/**
 * Parser to get data from www.hannover.de about pool in the Region Hannover
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-25
 */
class RegionHannoverPoolParser {

    /**
     * todo: Description
     */
    private val urls = contentValuesOf(
        Pair(PoolNamesEnum.ANDERTERBAD.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/Anderter-Bad"),
        Pair(PoolNamesEnum.AQUALAATZIUM.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/aquaLaatzium"),
        Pair(PoolNamesEnum.ASPRIASPASPORTCLUB.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/Aspria-Spa-Sport-Club"),
        Pair(PoolNamesEnum.BALNEON.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/Balneon"),
        Pair(PoolNamesEnum.BUENTEBADHEMMINGEN.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/B%C3%BCntebad-Hemmingen"),
        Pair(PoolNamesEnum.DEISTERBADBARSINGHAUSEN.toString(), "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Hallenb%C3%A4der/Deisterbad-Barsinghausen")
    )

    /**
     * todo: Description
     */
    fun getListOfPools() : ArrayList<PoolInformations> {
        val poolArray = ArrayList<PoolInformations>()

        // Convert the enumeration to a list
        val poolNames = PoolNamesEnum.values().toList()

        // Request details for every pool


        return poolArray
    }

}