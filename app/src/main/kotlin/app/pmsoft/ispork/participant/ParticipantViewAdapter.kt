package app.pmsoft.ispork.participant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullParticipant

class ParticipantViewAdapter(private val participantPickingHandler: SelectionHandler<FullParticipant>) :
  RecyclerView.Adapter<ParticipantViewAdapter.ViewHolder>(),
  Observer<List<FullParticipant>> {

  private var dataSet: List<FullParticipant> = emptyList()

  class ViewHolder(
    view: View,
    private val participantPickingHandler: SelectionHandler<FullParticipant>
  ) : RecyclerView.ViewHolder(view) {
    private val nameView: TextView = view.findViewById(R.id.participant_list_item_name_label)
    private val layout: FrameLayout = view.findViewById(R.id.participant_list_item_layout)

    private lateinit var data: FullParticipant

    init {
      itemView.setOnClickListener {
        participantPickingHandler.onClick(data)
        updateBackgroundColor()
      }
    }

    fun setData(participant: FullParticipant) {
      this.data = participant
      updateViewFromData()
    }

    private fun updateViewFromData() {
      nameView.text = data.name
      updateBackgroundColor()
    }

    private fun updateBackgroundColor() {
      if (participantPickingHandler.isSelected(data)) {
        layout.background = itemView.context.getDrawable(R.drawable.list_item_selected_background)
      } else {
        layout.background = itemView.context.getDrawable(R.drawable.list_item_background)
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
      R.layout.list_item_participant,
      parent,
      false
    )
    return ViewHolder(
      view,
      participantPickingHandler
    )
  }

  override fun onBindViewHolder(
    viewHolder: ViewHolder,
    position: Int
  ) {
    viewHolder.setData(dataSet[position])
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onChanged(newData: List<FullParticipant>?) {
    dataSet = newData ?: emptyList()
    notifyDataSetChanged()
  }
}
