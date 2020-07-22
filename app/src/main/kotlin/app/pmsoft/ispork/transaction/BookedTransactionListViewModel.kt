package app.pmsoft.ispork.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.*
import app.pmsoft.ispork.util.AsyncDataLoader

class BookedTransactionListViewModel(application: Application) : AndroidViewModel(application),
  ListViewModel<FullTransactionDefinition> {

  private val database: AppDatabase = provideDatabase(application)
  private val transactionDao: TransactionDefinitionDao = database.transactionDao()
  private val subTransactionDao: SubTransactionDao = database.subTransactionDao()
  private val budgetPotAnnotationDao: BudgetPotAnnotationDao = database.budgetPotAnnotationDao()

  private val transactions: MutableLiveData<List<FullTransactionDefinition>> by lazy {
    MutableLiveData<List<FullTransactionDefinition>>().also {
      AsyncDataLoader(
        transactionDao::getBooked,
        it
      ).execute()
    }
  }

  override val data: LiveData<List<FullTransactionDefinition>>
    get() = transactions

  private fun refresh() {
    AsyncDataLoader(
      transactionDao::getBooked,
      transactions
    ).execute()
  }

  override fun delete(elements: List<FullTransactionDefinition>) {
    transactionDao.delete(elements)
    refresh()
  }

  override fun persist(element: FullTransactionDefinition): Long {
    if (element.id == 0L) {
      element.id = transactionDao.insert(element)
    } else {
      transactionDao.update(element)
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
      subTransactionDao.update(subTransaction)
    }
    subTransaction.budgetPotAnnotations.forEach {
      it.subTransactionId = id
      persistBudgetPotAnnotation(it)
    }
    budgetPotAnnotationDao.deleteUnused(
      id,
      subTransaction.budgetPotAnnotations.map { it.id })
  }

  private fun persistBudgetPotAnnotation(budgetPotAnnotation: FullBudgetPotAnnotation) {
    if (budgetPotAnnotation.id == 0L) {
      val id = budgetPotAnnotationDao.insert(budgetPotAnnotation)
      budgetPotAnnotation.id = id
    } else {
      budgetPotAnnotationDao.update(budgetPotAnnotation)
    }
  }
}
