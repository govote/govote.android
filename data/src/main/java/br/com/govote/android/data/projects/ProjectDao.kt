package br.com.govote.android.data.projects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ProjectDao {

  @Query("SELECT * FROM project_ids WHERE year = :year")
  fun idsByYear(year: Int): Flowable<ProjectIds>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertIds(projectIds: ProjectIds): Completable
}
