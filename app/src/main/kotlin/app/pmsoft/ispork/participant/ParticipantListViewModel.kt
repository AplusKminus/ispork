package app.pmsoft.ispork.participant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.*
import app.pmsoft.ispork.util.AsyncDataLoader

class ParticipantListViewModel(application: Application) : AndroidViewModel(application) {

  private val participantDao: ParticipantDao = ParticipantDao_Impl(provideDatabase(application))

  private val unfilteredParticipants: MutableLiveData<List<FullParticipant>> = MutableLiveData<List<FullParticipant>>().also {
    AsyncDataLoader(
      participantDao::getAll,
      it
    ).execute()
  }

  val participants: LiveData<List<FullParticipant>> = MediatorLiveData<List<FullParticipant>>().also {
    it.addSource(unfilteredParticipants) { value -> it.value = filter(value) }
  }

  val accounts: LiveData<List<FullParticipant>> by lazy {
    MediatorLiveData<List<FullParticipant>>().also { accounts ->
      accounts.addSource(participants) { value ->
        accounts.value = value.filter { it.type == Participant.Type.ACCOUNT }
      }
    }
  }

  val payees: LiveData<List<FullParticipant>> by lazy {
    MediatorLiveData<List<FullParticipant>>().also { payees ->
      payees.addSource(participants) { value ->
        payees.value = value.filter { it.type == Participant.Type.PAYEE }
      }
    }
  }

  var filterString: String = ""
    set(value) {
      field = value
      refresh()
    }

  private fun filter(input: List<FullParticipant>): List<FullParticipant> {
    return input.filter {
      it.name.contains(
        filterString,
        true
      )
    }.sortedBy {
      it.name.indexOf(
        filterString,
        0,
        true
      )
    }
  }

  fun delete(participants: List<FullParticipant>) {
    participantDao.delete(participants)
    refresh()
  }

  fun delete(participant: FullParticipant) {
    participantDao.delete(participant)
    refresh()
  }

  fun persist(participant: FullParticipant): Long {
    if (participant.id == 0L) {
      participant.id = participantDao.insert(participant)
    } else {
      participantDao.update(
        participant.id,
        participant.name,
        participant.startingBalance
      )
    }
    refresh()
    return participant.id
  }

  fun insert(participant: FullParticipant): Long {
    return participantDao.insert(participant).also {
      refresh()
    }
  }

  fun update(participant: FullParticipant) {
    participantDao.update(
      participant.id,
      participant.name,
      participant.startingBalance
    )
    refresh()
  }

  private fun refresh() {
    AsyncDataLoader(
      participantDao::getAll,
      unfilteredParticipants
    ).execute()
  }
}
