package de.zoghaib.schwimmbadguide.adapter

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.data.PoolInformations
import de.zoghaib.schwimmbadguide.databinding.ItemPoolBinding
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter class for the RecyclerViews which shows a cardview for every pool
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-02
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PoolAdapter(
	/** todo */
	private val dataSet: ArrayList<PoolInformations>,

	/** todo */
	private val clickListener: (PoolInformations) -> Unit
) : RecyclerView.Adapter<PoolAdapter.MyViewHolder>() {

	/* -------------------- Member Variables -------------------- */

	/** View binding class to access items in the view */
	class MyViewHolder(val binding : ItemPoolBinding) : RecyclerView.ViewHolder(binding.root)


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on creation
	 *
	 * @param   parent      The recyclerview
	 * @param   viewType    The view type of the new view
	 *
	 * @return  A PartView item to display in the recyclerview
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		// Set up binding
		return MyViewHolder(ItemPoolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}


	/**
	 * Lifecycle method on view binding
	 *
	 * @param   holder      The ViewHolder which should be updated to represent the contents of
	 *                      the item at the given position in the data set
	 * @param   position    The position of the item within the adapter's data set
	 */
	@SuppressLint("SimpleDateFormat")
	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		// Pick the right dataSet
		val data = dataSet[position]

		// Download and insert image
		try { Picasso.get().load(data.imageUrl).into(holder.binding.imgPool) } catch (e: Exception) {}


		//if(data.heart) { holder.binding.imgHeart.setImageResource(R.drawable.ic_heart) }
		holder.binding.txtTitle.text = data.name
		holder.binding.txtSubtext.text = data.subtext


		// Calculate the opening state
		val calendar = Calendar.getInstance()


		/**
		 * todo
		 */
		fun getopenToday(nr : Int) : String {
			return when(calendar.get(Calendar.DAY_OF_WEEK)) {
				Calendar.MONDAY -> { if(nr == 1) { data.mo1!! } else { data.mo2!! } }
				Calendar.TUESDAY -> { if(nr == 1) { data.di1!! } else { data.di2!! } }
				Calendar.WEDNESDAY -> { if(nr == 1) { data.mi1!! } else { data.mi2!! } }
				Calendar.THURSDAY -> { if(nr == 1) { data.do1!! } else { data.do2!! } }
				Calendar.FRIDAY -> { if(nr == 1) { data.fr1!! } else { data.fr2!! } }
				Calendar.SATURDAY -> { if(nr == 1) { data.sa1!! } else { data.sa2!! } }
				Calendar.SUNDAY -> { if(nr == 1) { data.so1!! } else { data.so2!! } }
				else -> ""
			}
		}

		try {
			if(getopenToday(1).isEmpty()) {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_black)
			} else {
				val currentTime = Time(System.currentTimeMillis()).hours * 60 + Time(System.currentTimeMillis()).minutes
				val open1 = getopenToday(1).substringBefore(":").toInt() * 60 + getopenToday(1).substringAfter(":").substringBefore("-").toInt()
				val close1 = getopenToday(1).substringAfter("-").substringBefore(":").toInt() * 60 + getopenToday(1).substringAfterLast(":").toInt()

				/*try { todo: Time 2
					val open2 = dateFormat.format(SimpleDateFormat("HH:mm").parse(getopenToday(2).substringBefore("-")))
					val close2 = dateFormat.format(SimpleDateFormat("HH:mm").parse(getopenToday(2).substringAfter("-")))
				} catch (e: Exception) {}*/

				when {
					close1 - 60 > currentTime && currentTime > open1 -> {
						holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_green)
						holder.binding.txtOpenText.text = "Jetzt geöffnet: ${getopenToday(1)}"
					}
					currentTime in (open1 + 1) until close1 -> {
						holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_orange)
						holder.binding.txtOpenText.text = "Schließt bald: ${getopenToday(1)}"
					}
					else -> {
						//todo: Time 2
						holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_red)
						holder.binding.txtOpenText.text = "Geschlossen"
					}
				}
			}
		} catch (e: Exception) {}


		// Calculate the distance
		try {
			val currentLocation = Location("Point A")
			currentLocation.latitude = data.currentLatitude!!
			currentLocation.longitude = data.currentLongitude!!

			val poolLocation = Location("Point B")
			poolLocation.latitude = data.latitude
			poolLocation.longitude = data.longitude

			val distance = (currentLocation.distanceTo(poolLocation) / 1000).toInt()
			holder.binding.txtDistance.text = "$distance km"
		} catch (e: Exception) {}





		//holder.binding.txtOpenText.text = data.openText
		//holder.binding.txtDistance.text = data.distance

		/*if(data.open != null) {
			holder.binding.imgOpen.setImageResource(
				when(data.open) {
					OpenEnum.OPEN -> R.drawable.ic_circle_green
					OpenEnum.CLOSED -> R.drawable.ic_circle_red
					OpenEnum.OUTOFSAISON -> R.drawable.ic_circle_black
					OpenEnum.WILLBECLOSING -> R.drawable.ic_circle_orange
				}
			) todo
		}*/

		// OnClickListener for the item
		holder.binding.cvItem.setOnClickListener { clickListener(data) }

		// OnClickListener for the heart icon todo
		/*holder.binding.imgHeart.setOnClickListener {
			if(data.heart) {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart_outline)
				data.heart = false
			} else {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart)
				data.heart = true
			}
		}*/
	}


	/**
	 * Get the number of items in the recyclerview
	 *
	 * @return  Number of items
	 */
	override fun getItemCount(): Int {
		return dataSet.size
	}
}