package de.zoghaib.schwimmbadguide.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import de.zoghaib.schwimmbadguide.PoolDetailViewActivity
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.databinding.FragmentMapBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import kotlin.collections.ArrayList


/**
 * Fragment which shows a Google Maps view with location of the pools
 *
 * @author  Tobias Zoghaib
 * @since   2021-05-01
 */
class MapFragment(

	/** List of pools, overgiven from MainActivity */
	val pools : ArrayList<SwimmingPool>

) : Fragment(R.layout.fragment_map), OnMapReadyCallback {

	/* -------------------- Member Variables -------------------- */

	/** View binding object to access items in the view */
	private lateinit var binding : FragmentMapBinding

	/** The map object in the view */
	private lateinit var mMap : GoogleMap

	/** Database handler object */
	private lateinit var dbHandler: DatabaseHandler

	/** Shared preference object */
	private lateinit var sharedPreferences: SharedPreferences


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


		// Set up database
		dbHandler = DatabaseHandler(requireContext())


		// Set up map view
		binding.mvMap.onCreate(savedInstanceState)
		binding.mvMap.onResume()
		binding.mvMap.getMapAsync(this)


		// Shared preferences
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
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

		// Specific map style to remove all POIs for a clearer map
		mMap.setMapStyle(
			MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
		)


		// Open the pool details, if the user clicks on the info window
		mMap.setOnInfoWindowClickListener { marker ->
			for(i in pools) {
				if(i.poolInformations.name == marker.title) {
					val intent = Intent(requireContext(), PoolDetailViewActivity::class.java)
					intent.putExtra("dbId", i.poolInformations.dbId)

					startActivity(intent)

					break
				}
			}
		}

		addMarkerToMap()
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


	/* -------------------- Private methods -------------------- */

	/**
	 * Get the position and move the map camera to this
	 */
	@Throws(SecurityException::class)
	private fun moveCameraToPosition() {
		val m = requireContext().getSystemService(LocationManager::class.java)

		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
		loc?.run {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 13f))
		}

		mMap.isMyLocationEnabled = true
	}


	/**
	 * Method to add the pools from the ArrayList to the MapView
	 * Adding the pools, if the filter included them
	 */
	private fun addMarkerToMap() {
		mMap.clear()


		// Add marker
		val markerArray = ArrayList<MarkerOptions>()
		val coordinates = ArrayList<ContentValues>()

		for(pool in pools) {
			// Check the filter
			val filter = sharedPreferences.getStringSet("poolTypes", setOf(""))

			if(!filter?.contains(pool.poolInformations.categoryEnum.toString())!!) {
				continue
			}


			// Store all markers
			val markerData = ContentValues()

			markerData.put("name", pool.poolInformations.name)
			markerData.put("latitude", pool.poolInformations.latitude)
			markerData.put("longitude", pool.poolInformations.longitude)

			when(pool.getOpenState()) {
				OpenEnum.OPEN -> markerData.put("marker", R.drawable.ic_map_marker_green)
				OpenEnum.WILLBECLOSING -> markerData.put("marker", R.drawable.ic_map_marker_yellow)
				OpenEnum.CLOSED -> markerData.put("marker", R.drawable.ic_map_marker_red)
				OpenEnum.OUTOFSAISON -> markerData.put("marker", R.drawable.ic_map_marker_black)
				OpenEnum.NOOPENTIMES -> markerData.put("marker", R.drawable.ic_map_marker_blue)
			}

			coordinates.add(markerData)
		}



		// Converting coordinates in markers
		for(i in coordinates) {
			val marker = MarkerOptions()
			marker.title(i.getAsString("name"))
			marker.position(LatLng(i.getAsDouble("latitude"), i.getAsDouble("longitude")))

			marker.icon(BitmapDescriptorFactory.fromResource(i.getAsInteger("marker")))
			markerArray.add(marker)
		}


		// Adding markers to the map
		for(i in markerArray) {
			mMap.addMarker(i)
		}
	}
}