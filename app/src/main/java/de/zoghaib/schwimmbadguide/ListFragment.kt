package de.zoghaib.schwimmbadguide

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.zoghaib.schwimmbadguide.adapter.PoolAdapter
import de.zoghaib.schwimmbadguide.data.PoolCategory
import de.zoghaib.schwimmbadguide.data.PoolInformations
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.databinding.FragmentListBinding
import de.zoghaib.schwimmbadguide.parser.RegionHannoverPoolParser

/**
 * Fragment to list the pools as CardViews in a RecyclerView
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-01
 */
class ListFragment : Fragment(R.layout.fragment_list) {

	/* -------------------- Member Variables -------------------- */

	/** Array for RecyclerView items */
	private val recyclerViewEntries = ArrayList<PoolInformations>()

	/** View binding object to access items in the view */
	private lateinit var binding: FragmentListBinding

	/** Parser object */
	private lateinit var hannoverParser : RegionHannoverPoolParser

	/** Database handler object */
	private lateinit var dbHandler : DatabaseHandler


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method after the creation of the view
	 *
	 * @param   view                View where of the fragment
	 * @param   savedInstanceState  Save state of the view
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set up binding
		binding = FragmentListBinding.bind(view)


		// Database
		dbHandler = DatabaseHandler(requireContext())


		// Initialize the RecyclerView
		binding.rvPools.apply {
			// Set a LinearLayout in the RecyclerView
			layoutManager = LinearLayoutManager(activity)

			// Set the custom adapter to the RecyclerView
			adapter = PoolAdapter(recyclerViewEntries) { partItem: PoolInformations -> partItemClicked(partItem) }
		}

		updateRecyclerView()
	}


	/**
	 * Lifecycle method if the view will reload
	 */
	override fun onResume() {
		super.onResume()

		// Starting the RecyclerView entrys thread
		updateRecyclerView()
	}


	/* -------------------- Local methods -------------------- */

	/**
	 * Method to handle actions if the user clicks on one item in the RecyclerView
	 *
	 * @param   partItem    The item, which was clicked
	 */
	private fun partItemClicked(partItem: PoolInformations) {
		val intent = Intent(requireContext(), PoolDetailViewActivity::class.java)
		intent.putExtra("name", partItem.name)
		intent.putExtra("imageUrl", partItem.imageUrl)
		intent.putExtra("subtext", partItem.subtext)
		intent.putExtra("description", partItem.description)
		intent.putExtra("pools", partItem.pools)
		intent.putExtra("restaurant", partItem.restaurant)
		intent.putExtra("phoneNumber", partItem.phoneNumber)
		intent.putExtra("email", partItem.email)
		intent.putExtra("address", partItem.address)
		intent.putExtra("do1", partItem.do1)
		intent.putExtra("do2", partItem.do2)
		intent.putExtra("prices", partItem.prices)

		// todo: Implement transitions!

		startActivity(intent)
	}


	/**
	 * Method to load all datasets from the database to the RecyclerView
	 */
	@SuppressLint("MissingPermission")
	private fun updateRecyclerView() {
		// Clear the RecyclerView
		recyclerViewEntries.clear()

		val datasets = dbHandler.readTableToArrayList("POOLS")

		val m = context!!.getSystemService(LocationManager::class.java)
		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

		if (datasets != null) {
			for(pool in datasets) {
				recyclerViewEntries.add(
					PoolInformations(
						name = pool.getAsString("NAME"),
						category =
							when(pool.getAsInteger("CATEGORY")) {
								1 -> PoolCategory.INDOOR
								2 -> PoolCategory.OUTDOOR
								3 -> PoolCategory.OUTANDINDOOR
								else -> PoolCategory.SPA
							},
						latitude = pool.getAsDouble("LATITUDE"),
						longitude = pool.getAsDouble("LONGITUDE"),
						imageUrl = pool.getAsString("IMAGEURL"),
						subtext = pool.getAsString("SUBTEXT"),
						description = pool.getAsString("DESCRIPTION"),
						pools = pool.getAsString("POOLS"),
						restaurant = pool.getAsString("RESTAURANT"),
						sauna = pool.getAsString("SAUNA"),
						other = pool.getAsString("OTHER"),
						phoneNumber = pool.getAsString("PHONENUMBER"),
						email = pool.getAsString("EMAIL"),
						address = pool.getAsString("ADDRESS"),
						mo1 = pool.getAsString("MO1"),
						mo2 = pool.getAsString("MO2"),
						di1 = pool.getAsString("DI1"),
						di2 = pool.getAsString("DI2"),
						mi1 = pool.getAsString("MI1"),
						mi2 = pool.getAsString("MI2"),
						do1 = pool.getAsString("DO1"),
						do2 = pool.getAsString("DO2"),
						fr1 = pool.getAsString("FR1"),
						fr2 = pool.getAsString("FR2"),
						sa1 = pool.getAsString("SA1"),
						sa2 = pool.getAsString("SA2"),
						so1 = pool.getAsString("SO1"),
						so2 = pool.getAsString("SO2"),
						prices = "",
						currentLatitude = loc?.latitude,
						currentLongitude = loc?.longitude
					)
				)
			}
		}

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}