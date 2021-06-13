package de.zoghaib.schwimmbadguide.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.adapter.PoolAdapter
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.databinding.FragmentListBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool

/**
 * Fragment to list the pools as CardViews in a RecyclerView
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-01
 */
class ListFragment(

	/** List of pools, overgiven from MainActivity */
	val pools : ArrayList<SwimmingPool>

	) : Fragment(R.layout.fragment_list) {

	/* -------------------- Member Variables -------------------- */

	/** Array for RecyclerView items */
	private val recyclerViewEntries = ArrayList<SwimmingPool>()

	/** View binding object to access items in the view */
	private lateinit var binding: FragmentListBinding

	/** Database handler object */
	private lateinit var dbHandler : DatabaseHandler

	/** Shared preference object */
	private lateinit var sharedPreferences: SharedPreferences


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


		// Shared preferences
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())


		// Initialize the RecyclerView
		binding.rvPools.apply {
			// Set a LinearLayout in the RecyclerView
			layoutManager = LinearLayoutManager(activity)

			// Set the custom adapter to the RecyclerView
			adapter = PoolAdapter(recyclerViewEntries, requireActivity())

			updateRecyclerView()
		}
	}


	/**
	 * todo
	 */
	override fun setUserVisibleHint(isVisibleToUser: Boolean) {
		super.setUserVisibleHint(isVisibleToUser)
		if (isVisibleToUser) {
			updateRecyclerView()
		}
	}




	/* -------------------- Local methods -------------------- */

	/**
	 * Method to load all datasets from the database to the RecyclerView
	 */
	@SuppressLint("MissingPermission")
	private fun updateRecyclerView() {
		// Clear the RecyclerView
		recyclerViewEntries.clear()

		val m = requireContext().getSystemService(LocationManager::class.java)
		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

		for(pool in pools) {
			val filter = sharedPreferences.getStringSet("poolTypes", setOf(""))

			if(!filter?.contains(pool.poolInformations.categoryEnum.toString())!!) {
				continue
			}

			pool.calculateDistance(loc!!.latitude, loc.longitude)
			recyclerViewEntries.add(pool)
		}

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}