package app.pmsoft.ispork

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.data.ISporkEntry
import app.pmsoft.ispork.util.CurrencyHandler

abstract class AbstractListActivity<E, VH, VA>() : AppCompatActivity(),
  SelectionHandler<E> where
E : ISporkEntry,
E : Parcelable,
VH : AbstractViewHolder<E>,
VA : AbstractListViewAdapter<E, VH> {

  protected abstract val activityActions: List<ActivityAction<E>>
  protected abstract val selectedActions: List<SelectedListItemAction<E>>

  private lateinit var recyclerView: RecyclerView
  private lateinit var viewAdapter: VA
  private lateinit var viewManager: RecyclerView.LayoutManager
  lateinit var viewModel: ListViewModel<E>

  private var actionMode: ActionMode? = null

  open var selectionMode = SelectionHandler.Mode.MULTI
  private val selectedEntries = mutableListOf<E>()

  private val actionModeCallback = object : ActionMode.Callback {
    // Called when the action mode is created; startActionMode() was called
    override fun onCreateActionMode(
      mode: ActionMode,
      menu: Menu
    ): Boolean {
      selectedActions.forEach {
        it.addMenuItem(
          menu,
          this@AbstractListActivity
        )
      }
      actionMode = mode
      return true
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    override fun onPrepareActionMode(
      mode: ActionMode,
      menu: Menu
    ): Boolean {
      var changed = false
      selectedActions.forEach {
        val button = menu.findItem(it.menuItemId)
        val previousState = button.isVisible
        button.isVisible = it.appliesToNItems(selectedEntries.size)
        changed = changed || (previousState != button.isVisible)
      }
      return changed
    }

    // Called when the user selects a contextual menu item
    override fun onActionItemClicked(
      mode: ActionMode,
      item: MenuItem
    ): Boolean {
      for (action in selectedActions) {
        if (action.menuItemId == item.itemId) {
          action.apply(
            selectedEntries,
            this@AbstractListActivity
          )
          if (action.finishesActionMode) {
            mode.finish()
          }
          return true
        }
      }
      return false
    }

    // Called when the user exits the action mode
    override fun onDestroyActionMode(mode: ActionMode) {
      actionMode = null
      selectedEntries.clear()
      viewAdapter.notifyDataSetChanged()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.generic_list_layout)

    viewManager = LinearLayoutManager(this)

    viewModel = createViewModel()
    viewAdapter = createViewAdapter()
    viewModel.data.observe(
      this,
      viewAdapter
    )

    recyclerView = findViewById<RecyclerView>(R.id.generic_list_recycler_view).apply {
      setHasFixedSize(true)
      layoutManager = viewManager
      adapter = viewAdapter
    }
    CurrencyHandler.locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales[0]
    } else {
      @Suppress("DEPRECATION")
      resources.configuration.locale
    }
  }

  abstract fun createViewAdapter(): VA

  abstract fun createViewModel(): ListViewModel<E>

  override fun startActivityForResult(
    intent: Intent?,
    requestCode: Int
  ) {
    intent?.putExtra(
      "request_code",
      requestCode
    )
    super.startActivityForResult(
      intent,
      requestCode
    )
  }

  override fun onClick(element: E) {
    if (actionMode != null) {
      selectEntry(element)
    }
  }

  override fun onLongClick(element: E) {
    if (selectionMode != SelectionHandler.Mode.NONE) {
      startActionMode(actionModeCallback)
      selectEntry(element)
    }
  }

  override fun isSelected(element: E): Boolean {
    return selectedEntries.any { it.id == element.id }
  }

  private fun selectEntry(element: E) {
    when (selectionMode) {
      SelectionHandler.Mode.SINGLE -> {
        if (isSelected(element)) {
          selectedEntries.clear()
          actionMode?.finish()
        } else {
          if (selectedEntries.isNotEmpty()) {
            val currentlySelected = selectedEntries[0]
            selectedEntries.clear()
            notifySelectionChange(currentlySelected)
          }
          selectedEntries.add(element)
          notifySelectionChange(element)
        }
      }
      SelectionHandler.Mode.MULTI -> {
        val previousApplicableActions = selectedActions.filter { it.appliesToNItems(selectedEntries.size) }
        if (isSelected(element)) {
          deselectEntry(element)
          if (selectedEntries.isEmpty()) {
            actionMode?.finish()
          }
        } else {
          selectedEntries.add(element)
        }
        val applicableActions = selectedActions.filter { it.appliesToNItems(selectedEntries.size) }
        if (previousApplicableActions != applicableActions) {
          actionMode?.invalidate()
        }
        notifySelectionChange(element)
      }
      SelectionHandler.Mode.NONE -> {
        throw IllegalStateException("selectEntry() should not be called in selectionMode NONE.")
      }
    }
  }

  private fun deselectEntry(element: E) {
    val iterator = selectedEntries.iterator()
    while (iterator.hasNext()) {
      val selected = iterator.next()
      if (selected.id == element.id) {
        iterator.remove()
      }
    }
  }

  private fun notifySelectionChange(element: E) {
    viewAdapter.notifyItemChanged(viewModel.data.value?.indexOfFirst { it.id == element.id }
      ?: -1)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    for (listAction in activityActions) {
      if (listAction.menuItemId == item.itemId) {
        listAction.apply(this)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    activityActions.forEach {
      it.addMenuItem(
        menu,
        this@AbstractListActivity
      )
    }
    return true
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(
      requestCode,
      resultCode,
      data
    )
    for (listAction in activityActions) {
      if (listAction.requestCode == requestCode) {
        listAction.handleActivityResult(
          data,
          resultCode,
          viewModel
        )
        return
      }
    }
    for (action in selectedActions) {
      if (action.requestCode == requestCode) {
        action.handleActivityResult(
          data,
          resultCode,
          viewModel
        )
        return
      }
    }
    val toast = Toast.makeText(
      this,
      resources.getString(R.string.no_data_received),
      Toast.LENGTH_SHORT
    )
    toast.show()
  }
}
