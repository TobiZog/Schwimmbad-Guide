package de.zoghaib.schwimmbadguide.data

/**
 * Data class for a CardView item in die ListFragment
 */
data class PoolAdapterData(
	val image : Int,
	var heart : Boolean = false,
	val title : String,
	val subtext : String,
	val open : OpenEnum? = null,
	val openText : String? = null,
	val distance : String
)