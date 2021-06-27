package de.zoghaib.schwimmbadguide.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Pair as UtilPair
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.zoghaib.schwimmbadguide.PoolDetailViewActivity
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.databinding.ItemPoolBinding
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import java.util.*

/**
 * Adapter class for the RecyclerViews which shows a cardview for every pool
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-02
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PoolAdapter(
	/** todo */
	private val dataSet: ArrayList<SwimmingPool>,

	/** todo */
	private val context : Context
) : RecyclerView.Adapter<PoolAdapter.MyViewHolder>() {

	/* -------------------- Member Variables -------------------- */

	/** View binding class to access items in the view */
	class MyViewHolder(val binding : ItemPoolBinding) : RecyclerView.ViewHolder(binding.root)


	/* -------------------- Lifecycle -------------------- */

	/**
	 * Lifecycle method on creation
	 *
	 * @param   parent      The recyclerview
	 * @param   viewType    The view type of the new view
	 *
	 * @return  A PartView item to display in the recyclerview
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		// Set up binding
		return MyViewHolder(ItemPoolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}


	/**
	 * Lifecycle method on view binding
	 *
	 * @param   holder      The ViewHolder which should be updated to represent the contents of
	 *                      the item at the given position in the data set
	 * @param   position    The position of the item within the adapter's data set
	 */
	@SuppressLint("SimpleDateFormat")
	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		// Pick the right dataSet
		val data = dataSet[position]

		// Download and insert image
		try { Picasso.get().load(data.poolInformations.imageUrl).into(holder.binding.imgPool) } catch (e: Exception) {}


		//if(data.heart) { holder.binding.imgHeart.setImageResource(R.drawable.ic_heart) }
		holder.binding.txtTitle.text = data.poolInformations.name
		holder.binding.txtSubtext.text = data.poolInformations.subtext


		when(data.getOpenState()) {
			OpenEnum.OPEN -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_green)
				holder.binding.txtOpenText.text = "Jetzt geöffnet: ${data.getOpenTimesToday(1)}"
			}
			OpenEnum.WILLBECLOSING -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_orange)
				holder.binding.txtOpenText.text = "Schließt bald: ${data.getOpenTimesToday(1)}"
			}
			OpenEnum.OPENSOON -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_yellow)
				holder.binding.txtOpenText.text = context.getString(R.string.open_soon) + " " + data.getOpenTimesToday(1)
			}
			OpenEnum.CLOSED -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_red)
				holder.binding.txtOpenText.text = context.getString(R.string.closed)
			}
			OpenEnum.OUTOFSAISON -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_black)
				holder.binding.txtOpenText.text = context.getString(R.string.out_of_saison)
			}
			OpenEnum.NOOPENTIMES -> {
				holder.binding.imgOpen.setImageResource(R.drawable.ic_circle_grey)
				holder.binding.txtOpenText.text = context.getString(R.string.no_opening_times)
			}
		}


		// Distance
		holder.binding.txtDistance.text = "${data.poolInformations.distance} km"


		// OnClickListener for the item
		holder.binding.cvItem.setOnClickListener {
			val intent = Intent(context, PoolDetailViewActivity::class.java)
			intent.putExtra("dbId", data.poolInformations.dbId)


			val options = ActivityOptions.makeSceneTransitionAnimation(
				context as Activity,
				UtilPair.create(holder.binding.imgPool, holder.binding.imgPool.transitionName),
				UtilPair.create(holder.binding.txtTitle, holder.binding.txtTitle.transitionName),
				UtilPair.create(holder.binding.txtSubtext, holder.binding.txtSubtext.transitionName))

			// todo: Implement transitions!

			ActivityCompat.startActivity(context, intent, options.toBundle())
		}


		if(data.poolInformations.favorite) {
			holder.binding.imgHeart.setImageResource(R.drawable.ic_heart)
		} else {
			holder.binding.imgHeart.setImageResource(R.drawable.ic_heart_outline)
		}

		// OnClickListener for the heart icon
		holder.binding.imgHeart.setOnClickListener {
			if(data.poolInformations.favorite) {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart_outline)
				data.setFavorite(false)
			} else {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart)
				data.setFavorite(true)
			}
		}
	}




	/**
	 * Get the number of items in the recyclerview
	 *
	 * @return  Number of items
	 */
	override fun getItemCount(): Int {
		return dataSet.size
	}
}