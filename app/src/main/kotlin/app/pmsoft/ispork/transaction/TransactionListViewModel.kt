package app.pmsoft.ispork.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.*
import app.pmsoft.ispork.util.AsyncDataLoader

class TransactionListViewModel(application: Application) : AndroidViewModel(application),
  ListViewModel<FullTransactionDefinition> {

  private val database: AppDatabase = provideDatabase(application)
  private val transactionDefinitionDao: TransactionDefinitionDao = database.transactionDao()
  private val subTransactionDao: SubTransactionDao = database.subTransactionDao()
  private val categoryAnnotationDao: BudgetPotAnnotationDao = database.budgetPotAnnotationDao()

  private val transactions: MutableLiveData<List<FullTransactionDefinition>> by lazy {
    MutableLiveData<List<FullTransactionDefinition>>().also {
      AsyncDataLoader(
        transactionDefinitionDao::getBooked,
        it
      ).execute()
    }
  }

  override val data: LiveData<List<FullTransactionDefinition>>
    get() = transactions

  private fun refresh() {
    AsyncDataLoader(
      transactionDefinitionDao::getAll,
      transactions
    ).execute()
  }

  override fun delete(elements: List<FullTransactionDefinition>) {
    transactionDefinitionDao.delete(elements)
    refresh()
  }

  override fun persist(element: FullTransactionDefinition): Long {
    if (element.id == 0L) {
      element.id = transactionDefinitionDao.insert(element)
    } else {
      transactionDefinitionDao.update(
        element.id,
        element.booked
      )
    }
    element.subTransactions.forEach {
      it.transactionDefinitionId = element.id
      persistSubTransaction(it)
    }
    subTransactionDao.deleteUnused(
      element.id,
      element.subTransactions.map { it.id })
    refresh()
    return element.id
  }

  private fun persistSubTransaction(subTransaction: FullSubTransaction) {
    val id: Long
    if (subTransaction.id == 0L) {
      id = subTransactionDao.insert(subTransaction)
      subTransaction.id = id
    } else {
      id = subTransaction.id
      subTransactionDao.update(
        subTransaction.id,
        subTransaction.amount,
        subTransaction.transactionDefinitionId,
        subTransaction.participantId,
        subTransaction.bookingDate,
        subTransaction.notes
      )
    }
    subTransaction.budgetPotAnnotations.forEach {
      it.subTransactionId = id
      persistCategoryAnnotation(it)
    }
    categoryAnnotationDao.deleteUnused(
      id,
      subTransaction.budgetPotAnnotations.map { it.id })
  }

  private fun persistCategoryAnnotation(budgetPotAnnotation: FullBudgetPotAnnotation) {
    if (budgetPotAnnotation.id == 0L) {
      val id = categoryAnnotationDao.insert(budgetPotAnnotation)
      budgetPotAnnotation.id = id
    } else {
      categoryAnnotationDao.update(
        budgetPotAnnotation.id,
        budgetPotAnnotation.subTransactionId,
        budgetPotAnnotation.amountInTransaction,
        budgetPotAnnotation.amountInBudget,
        budgetPotAnnotation.budgetPotId,
        budgetPotAnnotation.notes
      )
    }
  }
}
