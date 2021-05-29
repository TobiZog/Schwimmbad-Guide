package de.zoghaib.schwimmbadguide

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.contentValuesOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.zoghaib.schwimmbadguide.adapter.PoolAdapter
import de.zoghaib.schwimmbadguide.data.PoolCategory
import de.zoghaib.schwimmbadguide.data.PoolInformations
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.databinding.FragmentListBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import de.zoghaib.schwimmbadguide.parser.RegionHannoverPoolParser

/**
 * Fragment to list the pools as CardViews in a RecyclerView
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-01
 */
class ListFragment(
	/** todo */
	val pools : ArrayList<SwimmingPool>
	) : Fragment(R.layout.fragment_list) {

	/* -------------------- Member Variables -------------------- */

	/** Array for RecyclerView items */
	private val recyclerViewEntries = ArrayList<SwimmingPool>()

	/** View binding object to access items in the view */
	private lateinit var binding: FragmentListBinding

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
			adapter = PoolAdapter(recyclerViewEntries, requireActivity())
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
	 * Method to load all datasets from the database to the RecyclerView
	 */
	@SuppressLint("MissingPermission")
	private fun updateRecyclerView() {
		// Clear the RecyclerView
		recyclerViewEntries.clear()

		val m = context!!.getSystemService(LocationManager::class.java)
		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

		for(pool in pools) {
			pool.calculateDistance(loc!!.latitude, loc.longitude)
			recyclerViewEntries.add(pool)
		}

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}