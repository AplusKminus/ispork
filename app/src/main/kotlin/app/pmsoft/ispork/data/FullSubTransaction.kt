package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class FullSubTransaction(
  @Ignore
  override var id: Long,
  @Ignore
  override var amount: Long,
  @Ignore
  override var transactionId: Long,
  @Relation(
    parentColumn = "participant_id",
    entityColumn = "id",
    entity = Participant::class
  )
  var participant: Participant,
  @Ignore
  override var bookingDate: Date?,
  @Ignore
  override var notes: String?,
  @Relation(
    parentColumn = "id",
    entityColumn = "sub_transaction_id",
    entity = CategoryAnnotation::class
  )
  var categoryAnnotations: List<FullCategoryAnnotation>
) : SubTransaction(),
  Parcelable {

  @Ignore
  constructor(participant: Participant) : this(
    0,
    0,
    0,
    participant,
    null,
    null,
    emptyList()
  )

  override var participantId: Long
    get() = participant.id
    set(value) {
      super.participantId = value
    }
}
