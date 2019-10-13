package app.pmsoft.ispork.payee

import androidx.lifecycle.ViewModelProvider
import app.pmsoft.ispork.*
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.participant.ParticipantListViewAdapter
import app.pmsoft.ispork.participant.ParticipantListViewHolder
import app.pmsoft.ispork.participant.ParticipantListViewModel

class PayeeListActivity
  : AbstractListActivity<FullParticipant, ParticipantListViewHolder, ParticipantListViewAdapter>() {

  override val activityActions: List<ActivityAction<FullParticipant>> = listOf(
    GenericCreateAction(
      PayeeEditActivity::class.java,
      R.string.payee_create_activity_title,
      RequestCodes.PAYEE_CREATION_REQUEST_CODE,
      "payee"
    )
  )
  override val selectedActions: List<SelectedListItemAction<FullParticipant>> = listOf(
    GenericEditAction(
      PayeeEditActivity::class.java,
      R.string.payee_edit_activity_title,
      RequestCodes.PAYEE_EDITING_REQUEST_CODE,
      "payee"
    ),
    GenericDeleteAction(
      R.plurals.title_dialog_payee_deletion,
      R.plurals.payee_deletion_message
    )
  )

  override fun createViewAdapter(): ParticipantListViewAdapter {
    return ParticipantListViewAdapter(this)
  }

  override fun createViewModel(): ListViewModel<FullParticipant> {
    val participantListViewModel = ViewModelProvider(this).get(ParticipantListViewModel::class.java)
    return PayeeListViewModel(participantListViewModel)
  }
}
