package de.zoghaib.schwimmbadguide.parser

import androidx.core.content.contentValuesOf
import de.zoghaib.schwimmbadguide.data.PoolInformations

/**
 * Parser to get data from www.hannover.de about pool in the Region Hannover
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-25
 */
class regionHannoverPoolParser {

    private val urls = contentValuesOf(
        Pair("Deisterbad Barsinghausen")
        Pair("Kleefelder Bad (Annabad)", "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Freib%C3%A4der/Kleefelder-Bad-Annabad"),
        Pair("Hainhölzer Naturbad", "https://www.hannover.de/Kultur-Freizeit/Freizeit-Sport/Sport/B%C3%A4derf%C3%BChrer/Freib%C3%A4der/Hainh%C3%B6lzer-Naturbad")
    )

    fun getListOfPools() {

    }


    /**
     * Returns informations about one pool
     *
     * @param   name    Name of the pool
     *
     * @return  All informations as a PoolInformations or null if there is no one
     */
    fun getPoolDetails(name : String) : PoolInformations? {
        // todo: Implement
        return null
    }
}