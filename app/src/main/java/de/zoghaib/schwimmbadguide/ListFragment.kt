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
			adapter = PoolAdapter(recyclerViewEntries, 52.0, 9.5) { partItem: SwimmingPool -> partItemClicked(partItem) }
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
	private fun partItemClicked(partItem: SwimmingPool) {
		val intent = Intent(requireContext(), PoolDetailViewActivity::class.java)
		intent.putExtra("name", partItem.poolInformations.name)
		intent.putExtra("imageUrl", partItem.poolInformations.imageUrl)
		intent.putExtra("subtext", partItem.poolInformations.subtext)
		intent.putExtra("description", partItem.poolInformations.description)
		intent.putExtra("pools", partItem.poolInformations.pools)
		intent.putExtra("restaurant", partItem.poolInformations.restaurant)
		intent.putExtra("phoneNumber", partItem.poolInformations.phoneNumber)
		intent.putExtra("email", partItem.poolInformations.email)
		intent.putExtra("address", partItem.poolInformations.address)
		intent.putExtra("open1", partItem.getOpenTimesToday(1))
		intent.putExtra("open2", partItem.getOpenTimesToday(2))
		intent.putExtra("prices", partItem.poolInformations.prices)

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

		val m = context!!.getSystemService(LocationManager::class.java)
		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

		for(pool in pools) {
			recyclerViewEntries.add(pool)
		}

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}