package app.pmsoft.ispork.account

import androidx.lifecycle.ViewModelProvider
import app.pmsoft.ispork.*
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.participant.ParticipantListViewAdapter
import app.pmsoft.ispork.participant.ParticipantListViewHolder
import app.pmsoft.ispork.participant.ParticipantListViewModel

class AccountListActivity
  : AbstractListActivity<FullParticipant, ParticipantListViewHolder, ParticipantListViewAdapter>() {

  override val activityActions: List<ActivityAction<FullParticipant>> = listOf(
    GenericCreateAction(
      AccountEditActivity::class.java,
      R.string.account_create_activity_title,
      RequestCodes.ACCOUNT_CREATION_REQUEST_CODE,
      "account"
    )
  )
  override val selectedActions: List<SelectedListItemAction<FullParticipant>> = listOf(
    GenericEditAction(
      AccountEditActivity::class.java,
      R.string.account_edit_activity_title,
      RequestCodes.ACCOUNT_EDITING_REQUEST_CODE,
      "account"
    ),
    GenericDeleteAction(
      R.plurals.title_dialog_account_deletion,
      R.plurals.account_deletion_message
    )
  )

  override fun createViewAdapter(): ParticipantListViewAdapter {
    return ParticipantListViewAdapter(this)
  }

  override fun createViewModel(): ListViewModel<FullParticipant> {
    val participantListViewModel = ViewModelProvider(this).get(ParticipantListViewModel::class.java)
    return AccountListViewModel(participantListViewModel)
  }
}
