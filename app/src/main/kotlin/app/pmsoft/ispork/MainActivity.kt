package app.pmsoft.ispork

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.pmsoft.ispork.account.AccountListActivity
import app.pmsoft.ispork.category.CategoryListActivity
import app.pmsoft.ispork.payee.PayeeListActivity
import app.pmsoft.ispork.transaction.TransactionListActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun viewCategories(@Suppress("UNUSED_PARAMETER") view: View) {
    val intent = Intent(
      this,
      CategoryListActivity::class.java
    )
    startActivity(intent)
  }

  fun viewAccounts(@Suppress("UNUSED_PARAMETER") view: View) {
    val intent = Intent(
      this,
      AccountListActivity::class.java
    )
    startActivity(intent)
  }

  fun viewPayees(@Suppress("UNUSED_PARAMETER") view: View) {
    val intent = Intent(
      this,
      PayeeListActivity::class.java
    )
    startActivity(intent)
  }

  fun viewTransactions(@Suppress("UNUSED_PARAMETER") view: View) {
    val intent = Intent(
      this,
      TransactionListActivity::class.java
    )
    startActivity(intent)
  }
}
