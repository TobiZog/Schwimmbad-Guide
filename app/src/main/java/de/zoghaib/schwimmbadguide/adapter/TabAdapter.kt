package de.zoghaib.schwimmbadguide.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Adapter class for tab views
 *
 * @param   fm          FragmentManager
 * @param   behavior    Position in the TabLayout
 *
 * @author  Tobias Zoghaib
 * @since   2021-06-13
 */
class TabAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

	/* -------------------- Member variables -------------------- */

	/** ArrayList of the fragments */
	private val mFragmentList = ArrayList<Fragment>()

	/** ArrayList of the fragment titles */
	private val mFragmentTitleList = ArrayList<String>()


	/* -------------------- Public methods -------------------- */

	/**
	 * Adding a fragment to the adapter
	 *
	 * @param   fragment    The fragment to add
	 * @param   title       The title for the tabs
	 */
	fun addFragment(fragment: Fragment, title: String) {
		mFragmentList.add(fragment)
		mFragmentTitleList.add(title)
	}


	/**
	 * Removing a fragment from the adapter
	 *
	 * @param   position    Position in the tablayout
	 */
	fun removeFragment(position: Int) {
		mFragmentList.removeAt(position)
		mFragmentTitleList.removeAt(position)
	}


	/**
	 * Getter for an item
	 *
	 * @param   position    Id of the wanted fragment
	 *
	 * @return  The wanted fragment
	 */
	override fun getItem(position: Int): Fragment {
		return mFragmentList[position]
	}


	/**
	 * Getter for number of tabs
	 *
	 * @return  Number of tabs
	 */
	override fun getCount(): Int {
		return mFragmentList.size
	}


	/**
	 * Getter for the title of a fragment
	 *
	 * @param   position    Id of the wanted title
	 *
	 * @return  Title of the fragment
	 */
	override fun getPageTitle(position: Int): CharSequence {
		return mFragmentTitleList[position]
	}
}