package app.pmsoft.ispork.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullParticipant

class ParticipantViewAdapter(private val participantPickingHandler: SelectionHandler<FullParticipant>) :
  RecyclerView.Adapter<ParticipantListViewHolder>(),
  Observer<List<FullParticipant>> {

  private var dataSet: List<FullParticipant> = emptyList()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ParticipantListViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
      R.layout.list_item_participant,
      parent,
      false
    )
    return ParticipantListViewHolder(
      view,
      participantPickingHandler
    )
  }

  override fun onBindViewHolder(
    viewHolder: ParticipantListViewHolder,
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
