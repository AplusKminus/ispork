package app.pmsoft.ispork.payee

import androidx.lifecycle.LiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.participant.ParticipantListViewModel

class PayeeListViewModel(private val delegate: ParticipantListViewModel) : ListViewModel<FullParticipant> {

  override val data: LiveData<List<FullParticipant>>
    get() = delegate.payees

  override fun delete(elements: List<FullParticipant>) {
    delegate.delete(elements)
  }

  override fun persist(element: FullParticipant): Long {
    return delegate.persist(element)
  }
}
