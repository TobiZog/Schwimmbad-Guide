package de.zoghaib.schwimmbadguide.fragments

import android.content.Intent
import android.os.Bundle
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.services.NotificationService

/**
 * Fragment to adjust settings in the app
 *
 * @author	Tobias Zoghaib
 * @since	2021-06-13
 */
class SettingsFragment : PreferenceFragmentCompat() {

	/* -------------------- Member Variables -------------------- */

	/** Notification service object */
	private lateinit var intent : Intent


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on create
	 *
	 * @param	savedInstanceState	todo
	 * @param	rootKey				todo
	 */
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.preferences)


		/*intent = Intent(requireContext(), NotificationService::class.java)


		// Start service, if the switch is on (on startup)
		controlNotificationService()


		// Start or stop service via Switch
		findPreference<SwitchPreference>("notificationsEnabled")?.onPreferenceClickListener =
			Preference.OnPreferenceClickListener {
				controlNotificationService()
				true
			}


		// Restart if the events for notifications changed
		findPreference<MultiSelectListPreference>("notificationActions")?.onPreferenceChangeListener =
			Preference.OnPreferenceChangeListener { _, _ ->
				controlNotificationService()
				true
			}*/
	}


	/**
	 * todo
	 */
	private fun controlNotificationService() {
		requireActivity().stopService(intent)

		if(findPreference<SwitchPreference>("notificationsEnabled")!!.isChecked){
			requireActivity().startForegroundService(intent)
			//startForegroundService(requireContext(), NotificationService::class.java)
		} else {
			requireActivity().stopService(intent)
		}
	}
}