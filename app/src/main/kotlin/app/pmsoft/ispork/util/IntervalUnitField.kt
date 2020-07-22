package app.pmsoft.ispork.util

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Interval

/**
 * This specialized drop down text field is for displaying the different [IntervalUnits][Interval.Unit] with localized
 * quantity strings.
 *
 * Do not use [AppCompatAutoCompleteTextView.getText] to change its value, use the [unit] property instead.
 */
class IntervalUnitField(
  context: Context,
  attributes: AttributeSet
) : AppCompatAutoCompleteTextView(context, attributes) {

  private var data: List<String> = Interval.Unit.values()
    .map(::valueToString)

  private val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(context, R.layout.dropdown_item) {
    /**
     * This filter implementation is a no-op-filter.
     */
    override fun getFilter(): Filter {
      return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
          val results = FilterResults()
          results.count = data.size
          results.values = data
          return results
        }

        override fun publishResults(
          constraint: CharSequence?,
          results: FilterResults?
        ) {
          notifyDataSetChanged()
        }
      }
    }
  }

  private var _unit: Interval.Unit = Interval.Unit.MONTH

  /**
   * The unit that should be displayed by this component. Setting this property sets the text to the appropriate
   * localized quantity string. See also the [quantity] property.
   */
  var unit: Interval.Unit
    get() = _unit
    set(value) {
      _unit = value
      text.clear()
      text.insert(0, valueToString(value))
    }

  /**
   * The quantity of interval units. This number is not displayed by this component, but it is used to determine the
   * appropriate quantity string. Defaults to 1.
   */
  var quantity: Int = 1
    set(value) {
      val old = field
      field = value
      if (old != value) {
        data = Interval.Unit.values()
          .map(::valueToString)
        refresh()
      }
    }

  init {
    setAdapter(adapter)
    addTextChangedListener(object : TextWatcherAdapter() {
      override fun afterTextChanged(s: Editable?) {
        adapter.getPosition(s.toString())
          .takeIf { it in Interval.Unit.values().indices }
          ?.let {
            _unit = Interval.Unit.values()[it]
          }
      }
    })
  }

  private fun refresh() {
    adapter.clear()
    adapter.addAll(data)
    text.clear()
    text.insert(0, valueToString(_unit))
  }

  private fun valueToString(
    unit: Interval.Unit
  ): String {
    return context.resources.getQuantityString(
      when (unit) {
        Interval.Unit.DAY -> R.plurals.day
        Interval.Unit.WEEK -> R.plurals.week
        Interval.Unit.MONTH -> R.plurals.month
        Interval.Unit.YEAR -> R.plurals.year
      }, quantity
    )
  }
}

