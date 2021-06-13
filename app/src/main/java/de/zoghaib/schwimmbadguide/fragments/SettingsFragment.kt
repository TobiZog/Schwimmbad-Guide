package de.zoghaib.schwimmbadguide.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import de.zoghaib.schwimmbadguide.R

/**
 * Fragment to adjust settings in the app
 *
 * @author	Tobias Zoghaib
 * @since	2021-06-13
 */
class SettingsFragment : PreferenceFragmentCompat() {

	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on create
	 *
	 * @param	savedInstanceState	todo
	 * @param	rootKey				todo
	 */
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.preferences)
	}
}