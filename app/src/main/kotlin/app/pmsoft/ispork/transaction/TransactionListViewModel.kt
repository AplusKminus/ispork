package app.pmsoft.ispork.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.*
import app.pmsoft.ispork.util.AsyncDataLoader

class TransactionListViewModel(application: Application) : AndroidViewModel(application),
  ListViewModel<FullTransaction> {

  private val database: AppDatabase = provideDatabase(application)
  private val transactionDao: TransactionDao = TransactionDao_Impl(database)
  private val subTransactionDao: SubTransactionDao = SubTransactionDao_Impl(database)
  private val categoryAnnotationDao: CategoryAnnotationDao = CategoryAnnotationDao_Impl(database)

  private val transactions: MutableLiveData<List<FullTransaction>> by lazy {
    MutableLiveData<List<FullTransaction>>().also {
      AsyncDataLoader(
        transactionDao::getAll,
        it
      ).execute()
    }
  }

  override val data: LiveData<List<FullTransaction>>
    get() = transactions

  private fun refresh() {
    AsyncDataLoader(
      transactionDao::getAll,
      transactions
    ).execute()
  }

  override fun delete(elements: List<FullTransaction>) {
    transactionDao.delete(elements)
    refresh()
  }

  override fun persist(element: FullTransaction): Long {
    if (element.id == 0L) {
      element.id = transactionDao.insert(element)
    } else {
      transactionDao.update(
        element.id,
        element.entryDate,
        element.notes
      )
    }
    element.subTransactions.forEach {
      it.transactionId = element.id
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
        subTransaction.transactionId,
        subTransaction.participantId,
        subTransaction.bookingDate,
        subTransaction.notes
      )
    }
    subTransaction.categoryAnnotations.forEach {
      it.subTransactionId = id
      persistCategoryAnnotation(it)
    }
    categoryAnnotationDao.deleteUnused(
      id,
      subTransaction.categoryAnnotations.map { it.id })
  }

  private fun persistCategoryAnnotation(categoryAnnotation: FullCategoryAnnotation) {
    if (categoryAnnotation.id == 0L) {
      val id = categoryAnnotationDao.insert(categoryAnnotation)
      categoryAnnotation.id = id
    } else {
      categoryAnnotationDao.update(
        categoryAnnotation.id,
        categoryAnnotation.subTransactionId,
        categoryAnnotation.amount,
        categoryAnnotation.categoryId,
        categoryAnnotation.notes
      )
    }
  }
}
