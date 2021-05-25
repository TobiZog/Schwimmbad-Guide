package de.zoghaib.schwimmbadguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.zoghaib.schwimmbadguide.adapter.PoolAdapter
import de.zoghaib.schwimmbadguide.data.PoolInformations
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


		// Parser
		hannoverParser = RegionHannoverPoolParser()


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
		//updateRecyclerView() todo: Change only dynamic informations like "Is open now?"
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
		intent.putExtra("equipment", partItem.equipment)
		intent.putExtra("phoneNumber", partItem.phoneNumber)
		intent.putExtra("email", partItem.email)
		intent.putExtra("address", partItem.address)
		intent.putExtra("openingTImes", partItem.openingTimes)
		intent.putExtra("prices", partItem.prices)

		startActivity(intent)
	}


	/**
	 * Method to load all datasets from the database to the RecyclerView
	 */
	private fun updateRecyclerView() {
		// Clear the RecyclerView
		recyclerViewEntries.clear()

		val listOfPools = hannoverParser.getListOfPools()

		for(pool in listOfPools) {
			recyclerViewEntries.add(pool)
		}

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}