package app.pmsoft.ispork.transaction.scheduled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.ScheduledRhythm
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScheduleFragment : Fragment() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var fab: FloatingActionButton
  private val listViewAdapter = RhythmListViewAdapter()

  var schedule: List<ScheduledRhythm> = emptyList()
    set(value) {
      field = value
      listViewAdapter.setData(value)
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.schedule_fragment, container, false)
    recyclerView = view.findViewById(R.id.schedule_recycler_view)
    fab = view.findViewById(R.id.schedule_add_rhythm_button)

    recyclerView.adapter = listViewAdapter
    recyclerView.layoutManager = LinearLayoutManager(this.context)
    return view
  }

  fun addRhythm() {

  }
}
