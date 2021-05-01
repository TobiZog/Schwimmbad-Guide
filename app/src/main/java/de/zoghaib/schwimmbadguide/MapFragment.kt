package de.zoghaib.schwimmbadguide

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import de.zoghaib.schwimmbadguide.databinding.FragmentMapBinding

/**
 * Fragment which shows a Google Maps view with location of the pools
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-01
 */
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

	/* -------------------- Member Variables -------------------- */

	/** View binding object to access items in the view */
	private lateinit var binding : FragmentMapBinding

	/** The map object in the view */
	private lateinit var mMap : GoogleMap


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method after the view was created
	 *
	 * @param   savedInstanceState      Save state of the view
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Set up binding
		binding = FragmentMapBinding.bind(view)


		// Set up map view
		binding.mvMap.onCreate(savedInstanceState)
		binding.mvMap.onResume()
		binding.mvMap.getMapAsync(this)
	}


	/**
	 * Lifecycle method after the map is ready in the view
	 *
	 * @param   googleMap       Google Map object to interact with
	 */
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap

		// Request permissions
		if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
			requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 42)
		} else {
			moveCameraToPosition()
		}

		// Set up map view
		mMap.uiSettings.isZoomControlsEnabled = true
		mMap.uiSettings.isMapToolbarEnabled = true

	}


	/**
	 * Get the position and move the map camera to this
	 */
	@Throws(SecurityException::class)
	private fun moveCameraToPosition() {
		val m = context!!.getSystemService(LocationManager::class.java)

		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
		loc?.run {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))
		}

		mMap.isMyLocationEnabled = true
	}


	/**
	 * Lifecycle method if the user allow or disallow permissions
	 *
	 * @param   requestCode     Code which are send on requestPermissions
	 * @param   permissions     Array of requested permissions
	 * @param   grantResults    Array of results
	 */
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
		if (requestCode == 42 && grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
			moveCameraToPosition()
		}
	}
}