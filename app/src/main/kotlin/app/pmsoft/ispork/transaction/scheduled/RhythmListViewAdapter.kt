package app.pmsoft.ispork.transaction.scheduled

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.ScheduledRhythm

class RhythmListViewAdapter : RecyclerView.Adapter<RhythmListViewHolder>() {

  private var dataSet: List<ScheduledRhythm> = emptyList()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): RhythmListViewHolder {
    return RhythmListViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(
          R.layout.rhythm_list_item,
          parent,
          false
        )
    )
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(
    holder: RhythmListViewHolder,
    position: Int
  ) {
    holder.setData(dataSet[position])
  }

  fun setData(newData: List<ScheduledRhythm>) {
    dataSet = newData
    notifyDataSetChanged()
  }
}
