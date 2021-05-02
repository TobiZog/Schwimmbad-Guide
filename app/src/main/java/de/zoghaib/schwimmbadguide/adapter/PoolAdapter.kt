package de.zoghaib.schwimmbadguide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.data.PoolAdapterData
import de.zoghaib.schwimmbadguide.databinding.ItemPoolBinding

/**
 * Adapter class for the RecyclerViews which shows a cardview for every pool
 *
 * @author	Tobias Zoghaib
 * @since	2021-05-02
 */
class PoolAdapter(
	/** todo */
	private val dataSet: ArrayList<PoolAdapterData>,

	/** todo */
	private val clickListener: (PoolAdapterData) -> Unit
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
	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		// Pick the right dataSet
		val data = dataSet[position]

		holder.binding.imgPool.setImageResource(data.image)
		if(data.heart) { holder.binding.imgHeart.setImageResource(R.drawable.ic_heart) }
		holder.binding.txtTitle.text = data.title
		holder.binding.txtSubtext.text = data.subtext
		holder.binding.txtOpenText.text = data.openText
		holder.binding.txtDistance.text = data.distance

		if(data.open != null) {
			holder.binding.imgOpen.setImageResource(
				when(data.open) {
					OpenEnum.OPEN -> R.drawable.ic_circle_green
					OpenEnum.CLOSED -> R.drawable.ic_circle_red
					OpenEnum.OUTOFSAISON -> R.drawable.ic_circle_black
					OpenEnum.WILLBECLOSING -> R.drawable.ic_circle_orange
				}
			)
		}

		// OnClickListener for the item
		holder.binding.cvItem.setOnClickListener { clickListener(data) }

		// OnClickListener for the heart icon
		holder.binding.imgHeart.setOnClickListener {
			if(data.heart) {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart_outline)
				data.heart = false
			} else {
				holder.binding.imgHeart.setImageResource(R.drawable.ic_heart)
				data.heart = true
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