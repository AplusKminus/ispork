package app.pmsoft.ispork

import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractListViewAdapter<E : Parcelable, VH : AbstractViewHolder<E>>
  : RecyclerView.Adapter<VH>(),
  Observer<List<E>> {

  private var dataSet: List<E> = emptyList()

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(
    holder: VH,
    position: Int
  ) {
    holder.setData(dataSet[position])
  }

  override fun onChanged(t: List<E>?) {
    dataSet = t ?: emptyList()
    notifyDataSetChanged()
  }
}
