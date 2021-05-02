package de.zoghaib.schwimmbadguide

import android.os.Bundle
import android.view.View
import androidx.core.util.Pools
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.zoghaib.schwimmbadguide.adapter.PoolAdapter
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.data.PoolAdapterData
import de.zoghaib.schwimmbadguide.databinding.FragmentListBinding

/**
 * Fragment to list the pools as CardViews in a RecyclerView
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-01
 */
class ListFragment : Fragment(R.layout.fragment_list) {

	/* -------------------- Member Variables -------------------- */

	/** Array for RecyclerView items */
	private val recyclerViewEntries = ArrayList<PoolAdapterData>()

	/** View binding object to access items in the view */
	private lateinit var binding: FragmentListBinding


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


		// Initialize the RecyclerView
		binding.rvPools.apply {
			// Set a LinearLayout in the RecyclerView
			layoutManager = LinearLayoutManager(activity)

			// Set the custom adapter to the RecyclerView
			adapter = PoolAdapter(recyclerViewEntries) { partItem: PoolAdapterData -> partItemClicked(partItem) }
		}
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
	private fun partItemClicked(partItem: PoolAdapterData) {
		// todo: Implement onClickListener
	}


	/**
	 * Method to load all datasets from the database to the RecyclerView
	 */
	private fun updateRecyclerView() {
		// Clear the RecyclerView
		recyclerViewEntries.clear()

		// todo: Dummy data, replace it!
		recyclerViewEntries.add(
			PoolAdapterData(
				image = R.drawable.stadionad_klein,
				title = "Stadionbad",
				subtext = "1972 im Olympiajahr erbaut, ist das Stadionbad das Highlight im Sportpark Hannover.",
				open = OpenEnum.OPEN,
				openText = "Geöffnet bis 19:00",
				distance = "1.0 km"
			)
		)

		recyclerViewEntries.add(
			PoolAdapterData(
				image = R.drawable.ricklinger_kiesteiche,
				title = "Ricklinger Kiesteiche",
				subtext = "Die Costa Kiesa: Seit vielen Jahren eines der beliebtesten Urlaubsziele des Hannoveraners.",
				distance = "2.1 km"
			)
		)

		recyclerViewEntries.add(
			PoolAdapterData(
				image = R.drawable.ricklinger_freibad,
				title = "Ricklinger Bad",
				subtext = "Paradiesisch ruhig liegt das beheizte Freibad (22°C) am Ricklinger Waldrand mit Zugang zu einem der Ricklinger Kiesteiche.",
				open = OpenEnum.OPEN,
				openText = "Geöffnet bis 20:00",
				distance = "2.4 km"
			)
		)

		binding.rvPools.adapter!!.notifyDataSetChanged()
	}
}