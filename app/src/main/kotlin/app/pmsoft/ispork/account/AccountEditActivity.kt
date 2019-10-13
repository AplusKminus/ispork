package app.pmsoft.ispork.account

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.R
import app.pmsoft.ispork.RequestCodes
import app.pmsoft.ispork.data.FullParticipant
import app.pmsoft.ispork.data.Participant
import app.pmsoft.ispork.util.CurrencyHandler
import app.pmsoft.ispork.view.AmountInputView

class AccountEditActivity : AppCompatActivity() {

  private lateinit var nameField: EditText
  private lateinit var amountField: AmountInputView

  private lateinit var account: FullParticipant

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_account_edit)
    nameField = findViewById(R.id.account_edit_name_field)
    amountField = findViewById(R.id.account_edit_amount_field)
    title = intent.getStringExtra("title")

    when (intent.getIntExtra(
      "request_code",
      0
    )) {
      RequestCodes.ACCOUNT_CREATION_REQUEST_CODE -> account = FullParticipant(Participant.Type.ACCOUNT)
      RequestCodes.ACCOUNT_EDITING_REQUEST_CODE -> account = intent.getParcelableExtra("account")
    }
    nameField.text.insert(
      0,
      account.name
    )
    CurrencyHandler.locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      resources.configuration.locales[0]
    } else {
      @Suppress("DEPRECATION")
      resources.configuration.locale
    }
    amountField.amount = account.startingBalance
    amountField.setOnAmountChangedListener {
      account.startingBalance = it
    }
    amountField.positiveFlowString = resources.getString(R.string.credit)
    amountField.negativeFlowString = resources.getString(R.string.debit)
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_save_account -> {
      account.name = nameField.text.toString().trim()
      intent.putExtra(
        "account",
        account
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
      R.menu.account_edit_menu,
      menu
    )
    return true
  }
}
