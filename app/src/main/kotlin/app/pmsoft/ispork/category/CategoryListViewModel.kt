package app.pmsoft.ispork.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.pmsoft.ispork.ListViewModel
import app.pmsoft.ispork.data.Category
import app.pmsoft.ispork.data.CategoryDao
import app.pmsoft.ispork.data.CategoryDao_Impl
import app.pmsoft.ispork.data.provideDatabase
import app.pmsoft.ispork.util.AsyncDataLoader

class CategoryListViewModel(application: Application) : AndroidViewModel(application),
  ListViewModel<Category> {

  private val categoryDao: CategoryDao = CategoryDao_Impl(provideDatabase(application))

  private val unfilteredCategories: MutableLiveData<List<Category>> by lazy {
    MutableLiveData<List<Category>>().also {
      AsyncDataLoader(
        categoryDao::getAll,
        it
      ).execute()
    }
  }

  val categories: LiveData<List<Category>> = MediatorLiveData<List<Category>>().also {
    it.addSource(unfilteredCategories) { value -> it.value = filter(value) }
  }

  override val data: LiveData<List<Category>>
    get() = categories

  var filterString: String = ""
    set(value) {
      field = value
      refresh()
    }

  private fun filter(input: List<Category>): List<Category> {
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

  override fun persist(element: Category): Long {
    if (element.id == 0L) {
      element.id = categoryDao.insert(element)
    } else {
      categoryDao.update(
        element.id,
        element.name
      )
    }
    refresh()
    return element.id
  }

  override fun delete(elements: List<Category>) {
    categoryDao.delete(elements)
    refresh()
  }

  private fun refresh() {
    AsyncDataLoader(
      categoryDao::getAll,
      unfilteredCategories
    ).execute()
  }
}
