package de.zoghaib.schwimmbadguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
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

		binding.txtTitle.text = intent.getStringExtra("name")
		binding.txtSubtext.text = intent.getStringExtra("subtext")
		binding.txtDescription.text = intent.getStringExtra("description")
		binding.txtEquipment.text = intent.getStringExtra("equipment")
		//binding.txtDistance.text = intent.getStringExtra("distance")
		//binding.txtOpenText.text = intent.getStringExtra("opentext")
		try{ Picasso.get().load(intent.getStringExtra("imageUrl")).into(binding.imageView) } catch (e: Exception) {}
	}
}