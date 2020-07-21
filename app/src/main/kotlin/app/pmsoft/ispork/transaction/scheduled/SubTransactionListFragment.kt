package app.pmsoft.ispork.transaction.scheduled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.FullSubTransactionTemplate

class SubTransactionListFragment : Fragment() {

  private lateinit var pickParticipantsButton: Button

  var data: List<FullSubTransactionTemplate> = emptyList()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.sub_transaction_template_list, container, false)
    pickParticipantsButton = view.findViewById(R.id.sub_transaction_templates_list_pick_participants_button)
    return view
  }
}
