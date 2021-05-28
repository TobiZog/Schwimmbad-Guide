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

		//val pool = intent.getSerializableExtra("swimmingPool") as SwimmingPool

		binding.txtTitle.text = intent.getStringExtra("name")
		binding.txtSubtext.text = intent.getStringExtra("subtext")
		binding.txtDescription.text = intent.getStringExtra("description")

		var poolsText = intent.getStringExtra("pools")
		poolsText = poolsText!!.replace(" * ", "\n\n")
		poolsText = poolsText.replace("* ","")

		binding.txtPools.text = poolsText
		binding.txtFood.text = intent.getStringExtra("restaurant")
		binding.txtPhonenumber.text = intent.getStringExtra("phoneNumber")
		binding.txtMail.text = intent.getStringExtra("email")

		//binding.imageView.transitionName = intent.extras!!.getString(RecyclerView.)

		//binding.txtDistance.text = intent.getStringExtra("distance")
		//binding.txtOpenText.text = intent.getStringExtra("opentext")
		try{ Picasso.get().load(intent.getStringExtra("imageUrl")).into(binding.imgPool) } catch (e: Exception) {}


		val imgPoolOriginalHeight = 300 * displayMetrics.density


		// Resize the image in the header with the scrolling
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