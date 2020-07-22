package app.pmsoft.ispork.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import app.pmsoft.ispork.R
import app.pmsoft.ispork.util.CurrencyFormatter
import kotlin.math.absoluteValue

class AmountInputDialog : DialogFragment() {
  var dialogResultListener: (Long) -> Unit = {}

  private lateinit var amountField: TextView
  private lateinit var button1: Button
  private lateinit var button2: Button
  private lateinit var button3: Button
  private lateinit var button4: Button
  private lateinit var button5: Button
  private lateinit var button6: Button
  private lateinit var button7: Button
  private lateinit var button8: Button
  private lateinit var button9: Button
  private lateinit var button10: Button
  private lateinit var backspaceButton: ImageButton
  private lateinit var negativeButton: Button
  private lateinit var positiveButton: Button
  private var allowNegative: Boolean = true

  private var value: Long = 0
  private lateinit var currencyFormatter: CurrencyFormatter
  private var fresh = true

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    fresh = true
    val builder = AlertDialog.Builder(activity!!)
    val layoutInflater = requireActivity().layoutInflater
    val view = layoutInflater.inflate(
      R.layout.amount_input_dialog,
      null
    )
    amountField = view.findViewById(R.id.amount_input_field)
    button1 = setupNumberButton(
      view,
      R.id.amount_input_button_1
    )
    button2 = setupNumberButton(
      view,
      R.id.amount_input_button_2
    )
    button3 = setupNumberButton(
      view,
      R.id.amount_input_button_3
    )
    button4 = setupNumberButton(
      view,
      R.id.amount_input_button_4
    )
    button5 = setupNumberButton(
      view,
      R.id.amount_input_button_5
    )
    button6 = setupNumberButton(
      view,
      R.id.amount_input_button_6
    )
    button7 = setupNumberButton(
      view,
      R.id.amount_input_button_7
    )
    button8 = setupNumberButton(
      view,
      R.id.amount_input_button_8
    )
    button9 = setupNumberButton(
      view,
      R.id.amount_input_button_9
    )
    button10 = setupNumberButton(
      view,
      R.id.amount_input_button_10
    )
    backspaceButton = view.findViewById(R.id.amount_input_backspace)
    backspaceButton.setOnClickListener(this::backspace)
    backspaceButton.setOnLongClickListener(this::clear)
    negativeButton = view.findViewById(R.id.amount_input_negative_button)
    negativeButton.setOnClickListener(this::saveOutflow)
    positiveButton = view.findViewById(R.id.amount_input_positive_button)
    positiveButton.setOnClickListener(this::saveInflow)
    value = arguments?.getLong("amount") ?: 0L
    currencyFormatter = CurrencyFormatter.getInstanceFor(arguments?.getString("currency_code"))
    positiveButton.text = arguments
      ?.getString("positive_flow_string")
      ?: view.context.resources.getString(R.string.inflow)
    negativeButton.text = arguments
      ?.getString("negative_flow_string")
      ?: view.context.resources.getString(R.string.outflow)
    allowNegative = arguments
      ?.getBoolean("allow_negative")
      ?: true
    negativeButton.visibility = if (allowNegative) VISIBLE else INVISIBLE
    updateView()
    builder.setView(view)
    return builder.create()
  }

  private fun setupNumberButton(
    view: View,
    id: Int
  ): Button {
    val button: Button = view.findViewById(id)
    button.setOnClickListener(this::inputDigit)
    return button
  }

  @Suppress("UNUSED_PARAMETER")
  private fun backspace(view: View) {
    value /= 10L
    updateView()
  }

  @Suppress("UNUSED_PARAMETER")
  private fun clear(view: View): Boolean {
    value = 0
    updateView()
    return true
  }

  @Suppress("UNUSED_PARAMETER")
  private fun saveInflow(view: View) {
    value = value.absoluteValue
    returnResult()
  }

  @Suppress("UNUSED_PARAMETER")
  private fun saveOutflow(view: View) {
    value = -value.absoluteValue
    returnResult()
  }

  private fun returnResult() {
    dialogResultListener(value)
    dismiss()
  }

  private fun inputDigit(view: View) {
    if (fresh) {
      value = 0
      fresh = false
    }
    // this actually reads the text of the button, so don't change that
    // I don't know an easier way to map a button to a number
    val digit = (view as Button).text.toString().toLong()
    value *= 10L
    if (value >= 0) {
      value += digit
    } else {
      value -= digit
    }
    updateView()
  }

  private fun updateView() {
    amountField.text = currencyFormatter.format(value)
  }
}
