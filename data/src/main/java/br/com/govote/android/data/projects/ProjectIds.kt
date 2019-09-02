package br.com.govote.android.data.projects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_ids")
data class ProjectIds(
  @PrimaryKey
  val year: Int,
  val ids: List<String>
)
