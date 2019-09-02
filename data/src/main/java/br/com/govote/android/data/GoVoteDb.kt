package br.com.govote.android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.govote.android.data.projects.ProjectDao
import br.com.govote.android.data.projects.ProjectIds

@Database(entities = [ProjectIds::class], version = 1)
abstract class GoVoteDb : RoomDatabase() {

  abstract fun projectDao(): ProjectDao

  companion object {
    @Volatile private var INSTANCE: GoVoteDb? = null

    fun getInstance(context: Context): GoVoteDb =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
      }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(context.applicationContext,
        GoVoteDb::class.java, "govote.db")
        .build()
  }
}
