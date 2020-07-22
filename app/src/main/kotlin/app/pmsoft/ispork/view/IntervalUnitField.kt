package app.pmsoft.ispork.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Interval

/**
 * This specialized view is for displaying the different [IntervalUnits][Interval.Unit] with localized
 * quantity strings.
 *
 * Its [unit] and [quantity] can be changed by the appropriate setters. The quantity has an effect on the displayed
 * string, but will itself not be displayed.
 */
class IntervalUnitField(
  context: Context,
  attributes: AttributeSet
) : LinearLayout(context, attributes) {

  private lateinit var decrementButton: ImageView
  private lateinit var incrementButton: ImageView
  private lateinit var label: TextView

  /**
   * The quantity of interval units. This number is not displayed by this component, but it is used to determine the
   * appropriate quantity string. Defaults to 1.
   */
  // has to be initialized before data in order for data to be initialized with quantity 1
  var quantity: Int = 1
    set(value) {
      val old = field
      field = value
      if (old != value) {
        data = Interval.Unit.values()
          .map(::valueToString)
        updateText()
      }
    }

  private var data: List<String>

  private var index: Int = Interval.Unit.MONTH.ordinal
  private var _unit: Interval.Unit = Interval.Unit.MONTH

  /**
   * The unit that should be displayed by this component. Setting this property sets the text to the appropriate
   * localized quantity string. See also the [quantity] property.
   */
  var unit: Interval.Unit
    get() = _unit
    set(value) {
      _unit = value
      index = value.ordinal
      updateText()
      updateButtonVisibility()
    }

  init {
    View.inflate(context, R.layout.interval_unit_field, this)
    data = Interval.Unit.values()
      .map(::valueToString)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    decrementButton = findViewById(R.id.decrement_button)
    incrementButton = findViewById(R.id.increment_button)
    label = findViewById(R.id.unit_label)
    decrementButton.setOnClickListener {
      unit = Interval.Unit.values()[index - 1]
    }
    incrementButton.setOnClickListener {
      unit = Interval.Unit.values()[index + 1]
    }
  }

  private fun updateText() {
    label.text = data[index]
  }

  private fun updateButtonVisibility() {
    when (index) {
      0 -> {
        decrementButton.visibility = View.INVISIBLE
        incrementButton.visibility = View.VISIBLE
      }
      data.size - 1 -> {
        decrementButton.visibility = View.VISIBLE
        incrementButton.visibility = View.INVISIBLE
      }
      else -> {
        decrementButton.visibility = View.VISIBLE
        incrementButton.visibility = View.VISIBLE
      }
    }
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

