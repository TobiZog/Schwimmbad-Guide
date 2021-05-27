package de.zoghaib.schwimmbadguide.data

/**
 * Data class for all informations about one swimming pool
 */
data class PoolInformations (
    val name : String,
    val category : PoolCategory,
    val latitude : Double,
    val longitude : Double,
    val imageUrl : String,
    val subtext : String,
    val description : String,
    val pools : String,
    val restaurant : String,
    val sauna : String,
    val other : String,
    val phoneNumber : String,
    val email : String,
    val address : String,
    val mo1 : String,
    val mo2 : String,
    val di1 : String,
    val di2 : String,
    val mi1 : String,
    val mi2 : String,
    val do1 : String,
    val do2 : String,
    val fr1 : String,
    val fr2 : String,
    val sa1 : String,
    val sa2 : String,
    val so1 : String,
    val so2 : String,
    val prices : String
)