package app.pmsoft.ispork.data

import android.content.Context
import androidx.room.Room
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Provides
fun provideDatabase(context: Context): AppDatabase {
  return Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "iSpork.db"
  )
    .allowMainThreadQueries()
    .build()
}
