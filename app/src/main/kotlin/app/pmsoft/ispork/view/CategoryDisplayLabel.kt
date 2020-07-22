package app.pmsoft.ispork.view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.FullSubTransaction

class CategoryDisplayLabel(
  context: Context,
  attributeSet: AttributeSet
) : AppCompatTextView(
  context,
  attributeSet
) {

  fun displayCategoryFor(subTransactions: List<FullSubTransaction>) {
    val categories = subTransactions
      .flatMap { it.budgetPotAnnotations }
      .mapNotNull { it.budgetPot?.category }
      .distinct()
    val displayType: DisplayType
    when {
      categories.size == 1 -> {
        text = categories[0].name
        displayType = DisplayType.SINGLE
      }
      categories.size > 1 -> {
        displayType = DisplayType.SPLIT
        text = context.resources.getString(R.string.category_split)
      }
      subTransactions.all { it.moneyBag.participant.type.internal } -> {
        displayType = DisplayType.TRANSFER
        text = context.resources.getString(R.string.transfer)
      }
      else -> {
        displayType = DisplayType.MISSING
        text = context.resources.getString(R.string.missing_category)
      }
    }
    configureCategoryLabel(displayType)
  }

  private fun configureCategoryLabel(displayType: DisplayType) {
    val textAppearance: Int
    val background: Drawable?
    val typefaceStyle: Int
    val textColor: Int
    when (displayType) {
      DisplayType.SINGLE -> {
        textAppearance = R.style.CategoryViewText
        background = null
        typefaceStyle = Typeface.NORMAL
        textColor = R.attr.text_color
      }
      DisplayType.SPLIT, DisplayType.TRANSFER -> {
        textAppearance = R.style.CategoryViewText_Special
        background = null
        typefaceStyle = Typeface.ITALIC
        textColor = R.attr.text_color
      }
      DisplayType.MISSING -> {
        textAppearance = R.style.CategoryViewText_Warning
        background = context.getDrawable(R.drawable.text_view_warning_background)
        typefaceStyle = Typeface.BOLD_ITALIC
        textColor = R.attr.text_view_warning_text_color
      }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      this.setTextAppearance(textAppearance)
      this.background = background
    } else {
      this.setTypeface(
        this.typeface,
        typefaceStyle
      )
      this.setTextColor(textColor)
    }
  }

  private enum class DisplayType {
    SINGLE, SPLIT, TRANSFER, MISSING
  }
}
