package app.pmsoft.ispork.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
  tableName = "category"
)
@Parcelize
class Category(

  @PrimaryKey(autoGenerate = true)
  override var id: Long,

  @ColumnInfo(name = "name")
  var name: String
) : Parcelable,
  ISporkEntry {

  constructor() : this(
    0,
    ""
  )
}
