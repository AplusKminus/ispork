package app.pmsoft.ispork.participant

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import app.pmsoft.ispork.R
import app.pmsoft.ispork.data.Participant

class ParticipantTypeIcon(context: Context, attributeSet: AttributeSet) : AppCompatImageView(context, attributeSet) {
  fun setType(participantType: Participant.Type) {
    val drawableId: Int
    val description: String
    when (participantType) {
      Participant.Type.ACCOUNT -> {
        drawableId = R.drawable.ic_account_white_24dp
        description = context.getString(R.string.account_icon)
      }
      Participant.Type.PERSON -> {
        drawableId = R.drawable.ic_person_white_24dp
        description = context.getString(R.string.person_icon)
      }
      Participant.Type.PAYEE -> {
        drawableId = R.drawable.ic_payee_white_24dp
        description = context.getString(R.string.payee_icon)
      }
    }
    setImageDrawable(context.resources.getDrawable(
      drawableId,
      context.theme
    ))
    contentDescription = description
  }
}
