package app.pmsoft.ispork.participant

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.SelectionHandler
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.TextWatcherAdapter
import com.google.android.material.tabs.TabLayout

class ParticipantPickingActivity : AppCompatActivity(),
  TabLayout.OnTabSelectedListener,
  SelectionHandler<FullParticipant> {

  companion object {
    private const val ALL_TAB_POSITION = 0
    private const val ACCOUNT_TAB_POSITION = 1
    private const val PAYEE_TAB_POSITION = 2
  }

  private lateinit var recyclerView: RecyclerView
  private lateinit var viewModel: ParticipantListViewModel
  private lateinit var viewManager: RecyclerView.LayoutManager
  private lateinit var viewAdapter: ParticipantViewAdapter

  private lateinit var searchField: EditText
  private lateinit var tabLayout: TabLayout
  private var selectedTab: Int = ALL_TAB_POSITION

  private val selectedParticipants: ArrayList<Participant> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_participant_picking)

    viewManager = LinearLayoutManager(this)

    viewModel = ViewModelProvider(this).get(ParticipantListViewModel::class.java)
    viewAdapter = ParticipantViewAdapter(this)
    viewModel.participants.observe(
      this,
      viewAdapter
    )

    recyclerView = findViewById<RecyclerView>(R.id.participant_recycler_view).apply {
      setHasFixedSize(true)
      layoutManager = viewManager
      adapter = viewAdapter
    }

    tabLayout = findViewById(R.id.participant_selection_filter_tab_layout)
    searchField = findViewById(R.id.participant_selection_search_field)
    searchField.addTextChangedListener(object : TextWatcherAdapter() {
      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
      ) {
        viewModel.filterString = s?.toString() ?: ""
      }
    })

    tabLayout.addOnTabSelectedListener(this)
  }

  override fun onTabReselected(tab: TabLayout.Tab?) {
  }

  override fun onTabUnselected(tab: TabLayout.Tab?) {
    when (tab?.position) {
      ALL_TAB_POSITION -> {
        viewModel.participants.removeObserver(viewAdapter)
      }
      ACCOUNT_TAB_POSITION -> {
        viewModel.accounts.removeObserver(viewAdapter)
      }
      PAYEE_TAB_POSITION -> {
        viewModel.payees.removeObserver(viewAdapter)
      }
      else -> {

      }
    }
    selectedTab = -1
  }

  override fun onTabSelected(tab: TabLayout.Tab?) {
    when (tab?.position) {
      ALL_TAB_POSITION -> {
        viewModel.participants.observe(
          this,
          viewAdapter
        )
      }
      ACCOUNT_TAB_POSITION -> {
        viewModel.accounts.observe(
          this,
          viewAdapter
        )
      }
      PAYEE_TAB_POSITION -> {
        viewModel.payees.observe(
          this,
          viewAdapter
        )
      }
      else -> {

      }
    }
    selectedTab = tab?.position ?: -1
  }

  override fun onClick(element: FullParticipant) {
    if (isSelected(element)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        selectedParticipants.removeIf { it.id == element.id }
      } else {
        for ((index, selectedParticipant) in selectedParticipants.withIndex()) {
          if (selectedParticipant.id == element.id) {
            selectedParticipants.removeAt(index)
            break
          }
        }
      }
    } else {
      if (element.type == Participant.Type.PAYEE && selectedParticipants.any { it.type == Participant.Type.PAYEE }) {
        for ((index, selectedParticipant) in selectedParticipants.withIndex()) {
          if (selectedParticipant.type == Participant.Type.PAYEE) {
            selectedParticipants.removeAt(index)
            viewAdapter.notifyItemChanged(getIndexOf(selectedParticipant))
            break
          }
        }
      }
      selectedParticipants.add(element)
    }
    updateSelectionBasedView()
  }

  override fun onLongClick(element: FullParticipant) {
    // do nothing
  }

  override fun isSelected(element: FullParticipant): Boolean {
    return selectedParticipants.any { it.id == element.id }
  }

  private fun getIndexOf(participant: Participant): Int {
    val observable: LiveData<List<FullParticipant>> = when (selectedTab) {
      ALL_TAB_POSITION -> viewModel.participants
      ACCOUNT_TAB_POSITION -> viewModel.accounts
      PAYEE_TAB_POSITION -> viewModel.payees
      else -> return -1
    }
    return observable.value?.indexOfFirst { it.id == participant.id } ?: -1
  }

  private fun updateSelectionBasedView() {
    val accounts = selectedParticipants.count { it.type == Participant.Type.ACCOUNT }
    val payees = selectedParticipants.count { it.type == Participant.Type.PAYEE }
    var allString = resources.getString(R.string.all)
    if (accounts + payees > 0) {
      allString += " (${accounts + payees})"
    }
    var accountsString = resources.getString(R.string.accounts)
    if (accounts > 0) {
      accountsString += " ($accounts)"
    }
    var payeesString = resources.getString(R.string.payees)
    if (payees > 0) {
      payeesString += " ($payees)"
    }
    tabLayout.getTabAt(ALL_TAB_POSITION)!!.text = allString
    tabLayout.getTabAt(ACCOUNT_TAB_POSITION)!!.text = accountsString
    tabLayout.getTabAt(PAYEE_TAB_POSITION)!!.text = payeesString
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_accept_participant_selection -> {
        if (selectedParticipants.isNotEmpty()) {
          intent.putParcelableArrayListExtra(
            "participants",
            selectedParticipants
          )
          setResult(
            Activity.RESULT_OK,
            intent
          )
        } else {
          setResult(Activity.RESULT_CANCELED)
        }
        finish()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(
      R.menu.participant_picking_menu,
      menu
    )
    return true
  }
}
