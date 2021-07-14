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
	 * @param	savedInstanceState	If there is a saveState, it will offer here
	 * @param	rootKey				If there is a rootKey for the setting, they will offer here
	 */
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.preferences)
	}
}