package de.zoghaib.schwimmbadguide

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
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

        // Set up map view -> todo!
        // setPins()
        // setViews()
    }
}