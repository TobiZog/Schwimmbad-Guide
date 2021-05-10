package de.zoghaib.schwimmbadguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.zoghaib.schwimmbadguide.databinding.ActivityPoolDetailViewBinding

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

		binding.txtTitle.text = intent.getStringExtra("title")
		binding.txtSubtext.text = intent.getStringExtra("subtext")
		binding.txtDistance.text = intent.getStringExtra("distance")
		binding.txtOpenText.text = intent.getStringExtra("opentext")
		binding.imageView.setImageResource(R.drawable.ricklinger_freibad)
	}
}