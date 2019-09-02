package br.com.govote.android.data.projects

import androidx.annotation.WorkerThread
import br.com.govote.android.api.bff.BffApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ProjectRepository constructor(
  private val bffApi: BffApi,
  private val projectDao: ProjectDao
) {

  @WorkerThread
  fun shuffledIdentifiers(year: Int): Observable<List<String>> =
    projectDao
      .idsByYear(year)
      .switchIfEmpty {
        bffApi.projectIdentifiers()
          .doOnNext { projectDao.insertIds(ProjectIds(year, it.body.orEmpty())) }
      }
      .map { it.ids }
      .toObservable()
      .subscribeOn(Schedulers.io())
}
