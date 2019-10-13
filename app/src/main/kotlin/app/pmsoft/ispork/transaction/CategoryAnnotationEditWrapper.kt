package app.pmsoft.ispork.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.data.FullCategoryAnnotation
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.ZeroIsNullLongLiveData
import app.pmsoft.ispork.util.getValue
import app.pmsoft.ispork.util.setValue

class CategoryAnnotationEditWrapper(
  val subTransactionEditWrapper: SubTransactionEditWrapper,
  val originalData: FullCategoryAnnotation
) {

  val amountData = ZeroIsNullLongLiveData(originalData.amount)
  var amount: Long? by amountData

  val categoryData = MutableLiveData(originalData.category)
  var category: Category? by categoryData

  val notesData = MutableLiveData(originalData.notes)
  var notes: String? by notesData

  val participantData: LiveData<Participant> = subTransactionEditWrapper.participantData
  val participant: Participant by subTransactionEditWrapper.participantData

  private val _suggestedAmount = ZeroIsNullLongLiveData()
  val suggestedAmountData: LiveData<Long?> = _suggestedAmount
  val suggestedAmount: Long? by suggestedAmountData

  init {
    updateSuggestedAmount()
  }

  fun updateSuggestedAmount() {
    val newValue = if (amount == null) {
      getSuggestedAmountThroughSubTransaction()
    } else {
      null
    }
    if (newValue != suggestedAmount) {
      _suggestedAmount.value = newValue
    }
  }

  private fun getSuggestedAmountThroughSubTransaction(): Long? {
    val amountToBaseSuggestionOn = subTransactionEditWrapper.amount
      ?: subTransactionEditWrapper.getSuggestedAmountThroughTransaction()
      ?: return null
    return amountToBaseSuggestionOn - subTransactionEditWrapper.categoryAnnotations
      .filter { it !== this }
      .map { it.amount ?: return null }
      .sum()
  }

  fun delete() {
    subTransactionEditWrapper.deleteCategoryAnnotation(this)
  }

  fun addSibling() {
    subTransactionEditWrapper.addNewCategoryAnnotation()
  }

  fun extractCategoryAnnotation(): FullCategoryAnnotation {
    return FullCategoryAnnotation(
      originalData.id,
      subTransactionEditWrapper.originalData.id,
      amount ?: suggestedAmount ?: 0L,
      category,
      notes
    )
  }
}
