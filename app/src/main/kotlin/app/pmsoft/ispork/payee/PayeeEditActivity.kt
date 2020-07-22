package app.pmsoft.ispork.payee

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.LocaleHandler
import java.util.*

class PayeeEditActivity : AppCompatActivity() {

  private lateinit var nameField: EditText
  private lateinit var payee: FullParticipant

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_payee_edit)
    nameField = findViewById(R.id.payee_edit_name_field)
    title = intent.getStringExtra("title")

    when (intent.getIntExtra(
      "request_code",
      0
    )) {
      RequestCodes.PAYEE_CREATION_REQUEST_CODE -> payee = FullParticipant(
        Participant.Type.PAYEE,
        Currency.getInstance(LocaleHandler.locale)
      )
      RequestCodes.PAYEE_EDITING_REQUEST_CODE -> payee = intent.getParcelableExtra("payee")
    }
    nameField.text.insert(
      0,
      payee.name
    )
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_payee -> {
      payee.name = nameField.text.toString().trim()
      intent.putExtra(
        "payee",
        payee
      )
      setResult(
        RESULT_OK,
        intent
      )
      finish()
      true
    }
    else -> {
      super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(
      R.menu.payee_edit_menu,
      menu
    )
    return true
  }
}
