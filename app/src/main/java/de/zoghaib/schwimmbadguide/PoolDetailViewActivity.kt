package de.zoghaib.schwimmbadguide

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.zoghaib.schwimmbadguide.databinding.ActivityPoolDetailViewBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import org.jetbrains.anko.displayMetrics

/**
 * Activity to show the details of a pool
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-10
 */
class PoolDetailViewActivity : AppCompatActivity() {

	/* -------------------- Member Variables -------------------- */

	/** View binding object to access items in the view */
	private lateinit var binding : ActivityPoolDetailViewBinding

	/** todo */
	private lateinit var swimmingPool: SwimmingPool


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on activity creation
	 *
	 * @param   savedInstanceState      Save state of the view
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Set up binding
		binding = ActivityPoolDetailViewBinding.inflate(layoutInflater)
		setContentView(binding.root)


		// Creating a swimming pool object
		swimmingPool = SwimmingPool(this, intent.getIntExtra("dbId", -1))


		// Filling the textViews
		binding.txtTitle.text = swimmingPool.poolInformations.name
		binding.txtSubtext.text = swimmingPool.poolInformations.subtext
		binding.txtDescription.text = swimmingPool.poolInformations.description

		var poolsText = swimmingPool.poolInformations.pools
		poolsText = poolsText.replace(" * ", "\n\n")
		poolsText = poolsText.replace("* ","")
		binding.txtPools.text = poolsText

		binding.txtFood.text = swimmingPool.poolInformations.restaurant
		binding.txtPhonenumber.text = swimmingPool.poolInformations.phoneNumber
		binding.txtMail.text = swimmingPool.poolInformations.email

		try{ Picasso.get().load(swimmingPool.poolInformations.imageUrl).into(binding.imgPool) } catch (e: Exception) {}


		// Resize the image in the header with the scrolling
		val imgPoolOriginalHeight = 300 * displayMetrics.density

		binding.svPoolDetails.viewTreeObserver.addOnScrollChangedListener {
			val txtTitleLocation = IntArray(2)
			binding.txtTitle.getLocationOnScreen(txtTitleLocation)

			val tbPoolDetailsLocation = IntArray(2)
			binding.tbPoolDetails.getLocationOnScreen(tbPoolDetailsLocation)

			// Calcualte the height between notification bar and title
			val imageHeight = txtTitleLocation[1] - tbPoolDetailsLocation[1]

			when {
				// Image is bigger than toolbar
				imageHeight > binding.tbPoolDetails.height -> {
					binding.imgPool.layoutParams.height = imageHeight
					binding.imgPool.visibility = View.VISIBLE

					// Calculate the transparence of the toolbar
					binding.tbPoolDetails.background.alpha = (255 - (100f / imgPoolOriginalHeight * (imageHeight  - binding.tbPoolDetails.height) / 100 * 255)).toInt()
				}
				// Image is disappeared under the toolbar
				else -> {
					binding.imgPool.visibility = View.GONE

					binding.tbPoolDetails.background.alpha = 255
				}
			}

			binding.imgPool.requestLayout()


			// Show title and subtitle, if they disappear
			if(txtTitleLocation[1] < binding.tbPoolDetails.height + tbPoolDetailsLocation[1]) {
				binding.tbPoolDetails.title = intent.getStringExtra("name")
				binding.tbPoolDetails.subtitle = intent.getStringExtra("category")
			} else {
				binding.tbPoolDetails.title = ""
				binding.tbPoolDetails.subtitle = ""
			}
		}
	}


	/**
	 * todo
	 */
	override fun onBackPressed() {
		ActivityCompat.finishAfterTransition(this)
	}
}