package app.pmsoft.ispork.participant

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import app.pmsoft.ispork.AbstractViewHolder
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.CurrencyHandler

class ParticipantListViewHolder(
  view: View,
  selectionHandler: SelectionHandler<FullParticipant>
) : AbstractViewHolder<FullParticipant>(
  view,
  selectionHandler
) {
  private val layout: FrameLayout = view.findViewById(R.id.participant_list_item_layout)
  private val amountLabel: TextView = view.findViewById(R.id.participant_list_item_amount_label)
  private val nameLabel: TextView = view.findViewById(R.id.participant_list_item_name_label)
  private val participantTypeIcon: ImageView = view.findViewById(R.id.participant_list_item_participant_type_icon)

  override val backgroundView: View
    get() = layout

  override fun updateViewFromData(e: FullParticipant) {
    nameLabel.text = e.name
    if (e.type.internal) {
      amountLabel.visibility = View.VISIBLE
      amountLabel.text = CurrencyHandler.format((e.balance ?: 0L) + (e.startingBalance ?: 0L))
    } else {
      amountLabel.visibility = View.GONE
    }
    participantTypeIcon.setImageDrawable(itemView.context.resources.getDrawable(
      when (e.type){
        Participant.Type.ACCOUNT -> R.drawable.ic_account_white_24dp
        Participant.Type.PERSON -> R.drawable.ic_person_white_24dp
        Participant.Type.PAYEE -> R.drawable.ic_payee_white_24dp
      },
      itemView.context.theme
    ))
  }
}
