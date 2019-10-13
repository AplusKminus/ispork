package app.pmsoft.ispork.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import app.pmsoft.ispork.AbstractListViewAdapter
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullParticipant

class ParticipantListViewAdapter(private val selectionHandler: SelectionHandler<FullParticipant>) : AbstractListViewAdapter<FullParticipant, ParticipantListViewHolder>() {
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
      selectionHandler
    )
  }
}
