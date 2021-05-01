package de.zoghaib.schwimmbadguide

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
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

		// Specific map style to remove all POIs for a clearer map
		mMap.setMapStyle(
			MapStyleOptions.loadRawResourceStyle(context!!, R.raw.map_style)
		)

		// todo: This section is only for test marker - remove it!
		val markerArray = ArrayList<MarkerOptions>()
		val coordinates = arrayListOf(
			contentValuesOf(Pair("name", "Stadionbad"), Pair("latitude", 52.35916), Pair("longitude", 9.73406), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Vahrenwalder Bad"), Pair("latitude", 52.39412), Pair("longitude", 9.73648), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Nord-Ost-Bad"), Pair("latitude", 52.40515), Pair("longitude", 9.7963), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Fössebad"), Pair("latitude", 52.37124), Pair("longitude", 9.69645), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Anderter Bad"), Pair("latitude", 52.36372), Pair("longitude", 9.85152), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Hallenbad Isernhaben"), Pair("latitude", 52.4336), Pair("longitude", 9.8561), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Wasserwelt Langenhagen"), Pair("latitude", 52.44695), Pair("longitude", 9.75353), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "RSV-Bad Leinhausen"), Pair("latitude", 52.40019), Pair("longitude", 9.67843), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Stöckener Bad"), Pair("latitude", 52.41463), Pair("longitude", 9.66553), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Hallenbad Höver"), Pair("latitude", 52.35013), Pair("longitude", 9.8956), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Büntebad Hemmingen"), Pair("latitude", 52.32699), Pair("longitude", 9.74238), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Hallenbad Letter"), Pair("latitude", 52.40498), Pair("longitude", 9.64202), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Hallenbad Planetenring"), Pair("latitude", 52.41733), Pair("longitude", 9.60292), Pair("marker", R.drawable.ic_greendot)),
			contentValuesOf(Pair("name", "Königliche Kristall Saunatherme Seelze"), Pair("latitude", 52.39761), Pair("longitude", 9.60118), Pair("marker", R.drawable.ic_greendot)),

			contentValuesOf(Pair("name", "Misburger Bad"), Pair("latitude", 52.3924), Pair("longitude", 9.8611), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Hainhölzer Naturbad"), Pair("latitude", 52.40208), Pair("longitude", 9.71731), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Ricklinger Bad"), Pair("latitude", 52.33901), Pair("longitude", 9.73522), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Lister Bad"), Pair("latitude", 52.4059), Pair("longitude", 9.7506), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Volksbad Limmer"), Pair("latitude", 52.3875), Pair("longitude", 9.6775), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Freibad Empelde"), Pair("latitude", 52.3419), Pair("longitude", 9.65073), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Freibad Arnum"), Pair("latitude", 52.29623), Pair("longitude", 9.73399), Pair("marker", R.drawable.ic_reddot)),
			contentValuesOf(Pair("name", "Kleefelder Bad (Annabad)"), Pair("latitude", 52.3724), Pair("longitude", 9.8131), Pair("marker", R.drawable.ic_reddot)),

			contentValuesOf(Pair("name", "Strandbad Blauer See"), Pair("latitude", 52.4199), Pair("longitude", 9.5504), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Kiesteich Lohnde"), Pair("latitude", 52.4054), Pair("longitude", 9.5489), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Waldsee Krähenwinkel"), Pair("latitude", 52.4736), Pair("longitude", 9.7573), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Hufeisensee"), Pair("latitude", 52.4556), Pair("longitude", 9.7747), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Silbersee"), Pair("latitude", 52.4304), Pair("longitude", 9.7596), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Kirchhorster See"), Pair("latitude", 52.4355), Pair("longitude", 9.8841), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Altwarmbüchener See"), Pair("latitude", 52.4232), Pair("longitude", 9.8543), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Sonnensee"), Pair("latitude", 52.4066), Pair("longitude", 9.8741), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Ricklinger Kiesteiche"), Pair("latitude", 52.3394), Pair("longitude", 9.7387), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Strandbad Maschsee"), Pair("latitude", 52.34349), Pair("longitude", 9.7534), Pair("marker", R.drawable.ic_orangedot)),
			contentValuesOf(Pair("name", "Birkensee"), Pair("latitude", 52.30334), Pair("longitude", 9.86185), Pair("marker", R.drawable.ic_orangedot)),
		)

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