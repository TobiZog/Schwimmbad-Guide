package de.zoghaib.schwimmbadguide

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.data.PoolCategoryEnum
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.databinding.FragmentMapBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import java.util.*
import kotlin.collections.ArrayList

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

	/** Database handler object */
	private lateinit var dbHandler: DatabaseHandler

	/** Dialog items */
	private val dialogItems = arrayOf("Hallenbäder", "Freibäder", "Spa", "Badeseen")

	/** Checked items in the filter dialog */
	private var checkedItems = booleanArrayOf(true, true, true, true)

	/** Array list with all pools */
	private val pools = ArrayList<SwimmingPool>()


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


		// Image button to filter the points on the map
		binding.ibFilter.setOnClickListener {
			val builder = AlertDialog.Builder(context)

			builder.setTitle("Filtern")
				.setMultiChoiceItems(dialogItems, checkedItems) { _, which, isChecked ->
					checkedItems[which] = isChecked
				}
				.setPositiveButton("Ok") { _, _ ->
					addMarkerToMap()
				}
				.create()
				.show()
		}
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
			MapStyleOptions.loadRawResourceStyle(context!!, R.raw.map_style)
		)

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
		val m = context!!.getSystemService(LocationManager::class.java)

		val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
		loc?.run {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 13f))
		}

		mMap.isMyLocationEnabled = true
	}


	/**
	 * todo
	 */
	private fun addMarkerToMap() {
		mMap.clear()

		// Load the pools
		if(pools.isEmpty()) {
			val rawDatasets = dbHandler.readTableToArrayList("POOLS")

			if (rawDatasets != null) {
				for(pool in rawDatasets) {
					pools.add(
						SwimmingPool(requireContext(), pool.getAsInteger("Id"))
					)
				}
			}
		}


		// Add marker
		val markerArray = ArrayList<MarkerOptions>()
		val datasets = dbHandler.readTableToArrayList("POOLS")
		val coordinates = ArrayList<ContentValues>()

		if (datasets != null) {
			for(pool in pools) {
				// Check the filter
				when(pool.poolInformations.categoryEnum) {
					PoolCategoryEnum.INDOOR -> if (!checkedItems[0]) { continue }
					PoolCategoryEnum.OUTDOOR -> if(!checkedItems[1]) { continue }
					PoolCategoryEnum.OUTANDINDOOR -> if(!checkedItems[0] || !checkedItems[1]) { continue }
					PoolCategoryEnum.SPA -> if(!checkedItems[2]) { continue }
					PoolCategoryEnum.LAKE -> if(!checkedItems[3]) { continue }
				}

				// Store all markers
				val markerData = ContentValues()

				markerData.put("name", pool.poolInformations.name)
				markerData.put("latitude", pool.poolInformations.latitude)
				markerData.put("longitude", pool.poolInformations.longitude)

				when(pool.getopenState()) {
					OpenEnum.OPEN -> markerData.put("marker", R.drawable.ic_map_marker_green)
					OpenEnum.WILLBECLOSING -> markerData.put("marker", R.drawable.ic_map_marker_yellow)
					OpenEnum.CLOSED -> markerData.put("marker", R.drawable.ic_map_marker_red)
					OpenEnum.OUTOFSAISON -> markerData.put("marker", R.drawable.ic_map_marker_black)
					OpenEnum.NOOPENTIMES -> markerData.put("marker", R.drawable.ic_map_marker_blue)
				}

				coordinates.add(markerData)
			}
		}

		for(i in coordinates) {
			val marker = MarkerOptions()
			marker.title(i.getAsString("name"))
			marker.position(LatLng(i.getAsDouble("latitude"), i.getAsDouble("longitude")))

			marker.icon(BitmapDescriptorFactory.fromResource(i.getAsInteger("marker")))
			markerArray.add(marker)
		}

		for(i in markerArray) {
			mMap.addMarker(i)
		}
	}
}