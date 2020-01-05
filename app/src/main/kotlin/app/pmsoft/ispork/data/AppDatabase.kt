package app.pmsoft.ispork.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  entities = [
    Participant::class,
    Category::class,
    Transaction::class,
    SubTransaction::class,
    CategoryAnnotation::class,
    SavingGoal::class],
  version = 1
)
@TypeConverters(TimestampConverter::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun categoryDao(): CategoryDao

  abstract fun transactionDao(): TransactionDao

  abstract fun subTransactionDao(): SubTransactionDao

  abstract fun participantDao(): ParticipantDao

  abstract fun categoryAnnotationDao(): CategoryAnnotationDao

  abstract fun savingGoalDao(): SavingGoalDao
}
