package de.zoghaib.schwimmbadguide

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.data.PoolCategoryEnum
import de.zoghaib.schwimmbadguide.databinding.ActivityPoolDetailViewBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import java.util.*

/**
 * Activity to show the details of a pool
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-10
 */
class PoolDetailViewActivity : AppCompatActivity(), OnMapReadyCallback {

	/* -------------------- Member Variables -------------------- */

	/** View binding object to access items in the view */
	private lateinit var binding : ActivityPoolDetailViewBinding

	/** The swimming pool object to extract data from */
	private lateinit var swimmingPool: SwimmingPool

	/** The map object in the view */
	private lateinit var mMap : GoogleMap


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on activity creation
	 *
	 * @param   savedInstanceState      Save state of the view
	 */
	@SuppressLint("ResourceAsColor", "MissingPermission", "SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Set up binding
		binding = ActivityPoolDetailViewBinding.inflate(layoutInflater)
		setContentView(binding.root)


		// Creating a swimming pool object
		swimmingPool = SwimmingPool(this, intent.getIntExtra("dbId", -1))


		// Open State image
		when(swimmingPool.getOpenState()) {
			OpenEnum.OPEN -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.open))
			}
			OpenEnum.WILLBECLOSING -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.willBeClosing))
			}
			OpenEnum.OPENSOON -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.openSoon))
			}
			OpenEnum.CLOSED -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.closed))
			}
			OpenEnum.OUTOFSAISON -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.outOfSaison))
			}
			OpenEnum.NOOPENTIMES -> {
				binding.imgOpenToday.setColorFilter(getColor(R.color.noTimes))
			}
		}


		// Toolbar
		binding.tbPoolDetails.title = swimmingPool.poolInformations.name
		binding.tbPoolDetails.subtitle =
			when(swimmingPool.poolInformations.categoryEnum) {
				PoolCategoryEnum.INDOOR -> "Hallenbad"
				PoolCategoryEnum.LAKE -> "See"
				PoolCategoryEnum.SPA -> "Spa"
				PoolCategoryEnum.OUTANDINDOOR -> "Frei- und Hallenbad"
				PoolCategoryEnum.OUTDOOR -> "Freibad"
			}

		if(swimmingPool.poolInformations.favorite) {
			binding.imgHeart.setImageResource(R.drawable.ic_heart_white)
		}

		binding.imgHeart.setOnClickListener {
			swimmingPool.setFavorite(!swimmingPool.poolInformations.favorite)

			if(swimmingPool.poolInformations.favorite) {
				binding.imgHeart.setImageResource(R.drawable.ic_heart_white)
			} else {
				binding.imgHeart.setImageResource(R.drawable.ic_heart_outline_white)
			}
		}


		// Filling the textViews
		// Quick info
		binding.txtOpenToday.text = getOpenTimes(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))

		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
			val m = getSystemService(LocationManager::class.java)
			val loc = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

			swimmingPool.calculateDistance(loc!!.latitude, loc.longitude)
			binding.txtDistance.text = swimmingPool.poolInformations.distance.toString() + resources.getString(R.string.km)
		} else {
			binding.txtDistance.text = resources.getString(R.string.cantGetDistance)
		}



		when {
			swimmingPool.poolInformations.distance < 1 -> binding.imgDistance.setColorFilter(getColor(R.color.nearDistance))
			swimmingPool.poolInformations.distance < 5 -> binding.imgDistance.setColorFilter(getColor(R.color.midDistance))
			else -> binding.imgDistance.setColorFilter(getColor(R.color.farDistance))
		}

		binding.txtPublicTransport.text = swimmingPool.poolInformations.publictransport


		binding.txtDescription.text = swimmingPool.poolInformations.description

		swimmingPool.poolInformations.sauna.let {
			if(it.isBlank()) {
				binding.cvSauna.visibility = View.GONE
			} else {
				binding.txtSauna.text = it
			}
		}


		swimmingPool.poolInformations.other.let {
			if(it.length > 1) {
				binding.txtOther.text = it
			} else {
				binding.cvOther.visibility = View.GONE
			}
		}


		var poolsText = swimmingPool.poolInformations.pools
		poolsText = poolsText.replace(" * ", "\n\n")
		poolsText = poolsText.replace("* ","")
		binding.txtPools.text = poolsText


		var saunaText = swimmingPool.poolInformations.sauna
		saunaText = saunaText.replace(" * ", "\n\n")
		saunaText = saunaText.replace("* ","")
		binding.txtSauna.text = saunaText

		binding.txtFood.text = swimmingPool.poolInformations.restaurant
		binding.txtPhonenumber.text = swimmingPool.poolInformations.phoneNumber
		binding.txtMail.text = swimmingPool.poolInformations.email

		try{ Picasso.get().load(swimmingPool.poolInformations.imageUrl).into(binding.imgPool) } catch (e: Exception) {}

		// Opening time table
		if(swimmingPool.poolInformations.categoryEnum == PoolCategoryEnum.LAKE) {
			binding.cvTimetable.visibility = View.GONE
			binding.txtOpenToday.text = resources.getString(R.string.no_opening_times)
		} else {
			binding.txtOpeningMonday.text = getOpenTimes(Calendar.MONDAY)
			binding.txtOpeningTuesday.text = getOpenTimes(Calendar.TUESDAY)
			binding.txtOpeningWednesday.text = getOpenTimes(Calendar.WEDNESDAY)
			binding.txtOpeningThursday.text = getOpenTimes(Calendar.THURSDAY)
			binding.txtOpeningFriday.text = getOpenTimes(Calendar.FRIDAY)
			binding.txtOpeningSaturday.text = getOpenTimes(Calendar.SATURDAY)
			binding.txtOpeningSunday.text = getOpenTimes(Calendar.SUNDAY)

			when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
				Calendar.MONDAY -> { binding.txtOpeningMonday.setTextColor(Color.YELLOW) }
				Calendar.TUESDAY -> { binding.txtOpeningTuesday.setTextColor(Color.YELLOW) }
				Calendar.WEDNESDAY -> { binding.txtOpeningWednesday.setTextColor(Color.YELLOW) }
				Calendar.THURSDAY -> { binding.txtOpeningThursday.setTextColor(Color.YELLOW) }
				Calendar.FRIDAY -> { binding.txtOpeningFriday.setTextColor(Color.YELLOW) }
				Calendar.SATURDAY -> { binding.txtOpeningSaturday.setTextColor(Color.YELLOW) }
				Calendar.SUNDAY -> { binding.txtOpeningSunday.setTextColor(Color.YELLOW) }
			}
		}



		// Phone number listener
		if(swimmingPool.poolInformations.phoneNumber.length > 1) {
			binding.txtPhonenumber.setOnClickListener {
				try {
					val intent = Intent(Intent.ACTION_DIAL).apply {
						data = Uri.parse("tel:${swimmingPool.poolInformations.phoneNumber}")
					}

					startActivity(intent)
				} catch (e : Exception) {
					Toast.makeText(this, "Konnte das Telefonprogramm nicht starten", Toast.LENGTH_SHORT).show()
				}
			}
		} else {
			binding.cvPhone.visibility = View.GONE
		}


		// Mail listener
		if(swimmingPool.poolInformations.email.length > 1) {
			binding.txtMail.setOnClickListener {
				try {
					val intent = Intent(Intent.ACTION_SEND).apply {
						type = "text/plain"
						putExtra(Intent.EXTRA_EMAIL, swimmingPool.poolInformations.email)
					}

					startActivity(intent)
				} catch (e : Exception) {
					Toast.makeText(this, "Konnte das Mailprogramm nicht starten", Toast.LENGTH_SHORT).show()
				}
			}
		} else {
			binding.cvMail.visibility = View.GONE
		}


		// Approach
		binding.txtApproach.text = swimmingPool.poolInformations.address


		// Google Maps View
		binding.mvMap.onCreate(savedInstanceState)
		binding.mvMap.onResume()
		binding.mvMap.getMapAsync(this)


		// Resize the image in the header with the scrolling
		binding.svPoolDetails.viewTreeObserver.addOnScrollChangedListener {
			val cvShortsLocation = IntArray(2)
			binding.cvShorts.getLocationOnScreen(cvShortsLocation)

			val tbPoolDetailsLocation = IntArray(2)
			binding.tbPoolDetails.getLocationOnScreen(tbPoolDetailsLocation)

			// Calcualte the height between notification bar and title
			val imageHeight = cvShortsLocation[1] - tbPoolDetailsLocation[1]

			when {
				// Image is bigger than toolbar
				imageHeight > binding.tbPoolDetails.height -> {
					binding.imgPool.layoutParams.height = imageHeight
					binding.imgPool.visibility = View.VISIBLE
				}
				// Image is disappeared under the toolbar
				else -> {
					binding.imgPool.visibility = View.GONE

					binding.tbPoolDetails.background.alpha = 255
				}
			}

			binding.imgPool.requestLayout()
		}
	}


	/**
	 * Lifecycle method when the user press the back button
	 */
	override fun onBackPressed() {
		ActivityCompat.finishAfterTransition(this)
	}


	/**
	 * Lifecycle method after the map is ready in the view
	 *
	 * @param   googleMap       Google Map object to interact with
	 */
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap


		// Specific map style to remove all POIs for a clearer map
		mMap.setMapStyle(
			MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
		)


		// Add marker
		mMap.clear()
		mMap.addMarker(MarkerOptions().position(LatLng(swimmingPool.poolInformations.latitude, swimmingPool.poolInformations.longitude)))


		// Move camera to pool
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(swimmingPool.poolInformations.latitude, swimmingPool.poolInformations.longitude), 15f))
	}


	/**
	 * Get method for the opening times
	 *
	 * @param	day		Calender day as integer
	 *
	 * @return	String with the pool opening times in format like 06:00-18:00
	 */
	private fun getOpenTimes(day : Int) : String {
		return when(day) {
			Calendar.MONDAY -> swimmingPool.poolInformations.mo1
			Calendar.TUESDAY -> swimmingPool.poolInformations.di1
			Calendar.WEDNESDAY -> swimmingPool.poolInformations.mi1
			Calendar.THURSDAY -> swimmingPool.poolInformations.do1
			Calendar.FRIDAY -> swimmingPool.poolInformations.fr1
			Calendar.SATURDAY -> swimmingPool.poolInformations.sa1
			Calendar.SUNDAY -> swimmingPool.poolInformations.so1
			else -> ""
		}
	}
}