package app.pmsoft.ispork.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.pmsoft.ispork.R
import app.pmsoft.ispork.util.CurrencyHandler
import com.google.android.material.textfield.TextInputEditText

class AmountInputView(
  context: Context,
  attributes: AttributeSet
) : TextInputEditText(
  context,
  attributes
) {

  private var onAmountChangedListener: OnAmountChangedListener? = null

  private lateinit var dialog: AmountInputDialog

  var amount: Long? = null
    get() {
      val data = amountData
      return if (data != null) {
        data.value
      } else {
        field
      }
    }
    set(value) {
      val data = amountData
      field = value
      if (data != null) {
        data.value = value
      } else {
        onAmountChangedListener?.amountChanged(value)
      }
      updateView()
    }

  var suggestedAmount: Long? = null
    get() {
      val data = suggestedAmountData
      return if (data != null) {
        data.value
      } else {
        field
      }
    }
    set(value) {
      field = value
      updateView()
    }

  private val updateViewObserver: Observer<Long?> = Observer {
    updateView()
  }

  var amountData: MutableLiveData<Long?>? = null
    set(value) {
      field?.removeObserver(updateViewObserver)
      value?.observe(
        retrieveActivity(),
        updateViewObserver
      )
      field = value
      updateView()
    }
  var suggestedAmountData: LiveData<Long?>? = null
    set(value) {
      field?.removeObserver(updateViewObserver)
      value?.observe(
        retrieveActivity(),
        updateViewObserver
      )
      field = value
      updateView()
    }

  private fun retrieveActivity() = (context as ContextThemeWrapper).baseContext as AppCompatActivity

  var positiveFlowString: String? = null
  var negativeFlowString: String? = null
  var allowNegative: Boolean = true

  init {
    this.setOnClickListener {
      val bundle = Bundle()
      bundle.putLong(
        "amount",
        amount ?: 0L
      )
      bundle.putString(
        "positive_flow_string",
        positiveFlowString
      )
      bundle.putString(
        "negative_flow_string",
        negativeFlowString
      )
      bundle.putBoolean(
        "allow_negative",
        allowNegative
      )
      dialog = AmountInputDialog()
      dialog.dialogResultListener = {
        this.amount = it
      }
      dialog.arguments = bundle
      dialog.show(
        retrieveActivity().supportFragmentManager,
        "transaction_amount_input"
      )
    }
  }

  fun setOnAmountChangedListener(listener: OnAmountChangedListener?) {
    this.onAmountChangedListener = listener
  }

  fun setOnAmountChangedListener(listener: (Long?) -> Unit) {
    this.onAmountChangedListener = object : OnAmountChangedListener {
      override fun amountChanged(amount: Long?) {
        listener(amount)
      }
    }
  }

  private fun updateView() {
    val value = amount
    val suggested = suggestedAmount
    when {
      value != null -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          this.setTextAppearance(R.style.AmountInputViewText)
        } else {
          this.setTypeface(
            typeface,
            Typeface.NORMAL
          )
          this.setTextColor(R.attr.text_color)
        }
        this.text = SpannableStringBuilder(CurrencyHandler.format(value))
      }
      suggested != null -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          this.setTextAppearance(R.style.AmountInputViewText_Hint)
        } else {
          this.setTypeface(
            typeface,
            Typeface.ITALIC
          )
          this.setTextColor(R.attr.hint_color)
        }
        this.text = SpannableStringBuilder(CurrencyHandler.format(suggested))
      }
      else -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          this.setTextAppearance(R.style.AmountInputViewText_Hint)
        } else {
          this.setTypeface(
            typeface,
            Typeface.ITALIC
          )
          this.setTextColor(R.attr.hint_color)
        }
        this.text = null
      }
    }
  }

  interface OnAmountChangedListener {
    fun amountChanged(amount: Long?)
  }
}
