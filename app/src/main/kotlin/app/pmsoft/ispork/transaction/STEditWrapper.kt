package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.BaseBudgetPotAnnotation
import app.pmsoft.ispork.data.BaseSubTransaction
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.NonNullLiveData

interface STEditWrapper<ST : BaseSubTransaction<BPA>, BPA : BaseBudgetPotAnnotation> {

  val id: Long

  val amountData: MutableLiveData<Long?>
  var amount: Long?

  val participantData: NonNullLiveData<Participant>
  var participant: Participant

  var notes: String?

  val budgetPotAnnotationsData: LiveData<List<BPAEditWrapper<BPA>>>
  var budgetPotAnnotations: List<BPAEditWrapper<BPA>>

  val suggestedAmountData: LiveData<Long?>
  val suggestedAmount: Long?

  fun extractSubTransaction(): ST

  fun updateSuggestedAmount()

  fun destroy()

  fun addNewBudgetPotAnnotation()

  fun deleteBudgetPotAnnotation(bpaEditWrapper: BPAEditWrapper<BPA>)

  fun getSiblingAnnotations(target: BPAEditWrapper<BPA>): List<BPAEditWrapper<BPA>>

  fun getSuggestedAmountThroughBudgetPots(): Long?
}
