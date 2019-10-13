package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(Participant.Type.Converter::class)
class FullParticipant(
  @Ignore
  override val type: Type,
  @Ignore
  override var id: Long,
  @Ignore
  override var name: String,
  @Ignore
  override var startingBalance: Long?,
  var balance: Long?
) : Participant(type),
  Parcelable {

  @Ignore
  constructor(type: Type) : this(
    type,
    0,
    "",
    if (type.internal) null else 0L,
    if (type.internal) null else 0L
  )
}
