package app.pmsoft.ispork.account

import androidx.lifecycle.LiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.participant.ParticipantListViewModel

class AccountListViewModel(private val delegate: ParticipantListViewModel) : ListViewModel<FullParticipant> {

  override val data: LiveData<List<FullParticipant>>
    get() = delegate.accounts

  override fun delete(elements: List<FullParticipant>) {
    delegate.delete(elements)
  }

  override fun persist(element: FullParticipant): Long {
    return delegate.persist(element)
  }
}
