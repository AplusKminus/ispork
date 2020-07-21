package app.pmsoft.ispork.transaction.scheduled

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.ScheduledRhythm
import app.pmsoft.ispork.util.DateHandler

class RhythmListViewHolder(
  view: View
) : RecyclerView.ViewHolder(view) {

  private val descriptionField: TextView = view.findViewById(R.id.rhythm_list_item_description_field)
  private val startDateField: TextView = view.findViewById(R.id.rhythm_list_item_start_date_field)
  private val endDateField: TextView = view.findViewById(R.id.rhythm_list_item_end_date_field)
  private val totalOccurrencesField: TextView = view.findViewById(R.id.rhythm_list_item_occurrences_field)

  private lateinit var data: ScheduledRhythm

  fun setData(rhythm: ScheduledRhythm) {
    data = rhythm
    updateViewFromData()
  }

  private fun updateViewFromData() {
    descriptionField.text = RhythmHelper.describe(data)
    startDateField.text = DateHandler.format(data.startDate)
    endDateField.text = data.endDate?.let { DateHandler.format(it) } ?: "∞"
    totalOccurrencesField.text = RhythmHelper.countOccurrences(data)
      ?.toString() ?: "∞"
  }
}
