package de.zoghaib.schwimmbadguide.data

/**
 * Data class for all informations about one swimming pool
 */
data class PoolInformations (
    val name : String,
    val imageUrl : String,
    val subtext : String,
    val description : String,
    val equipment : String,
    val phoneNumber : String,
    val email : String,
    val address : String,
    val openingTimes : String,
    val prices : String
)