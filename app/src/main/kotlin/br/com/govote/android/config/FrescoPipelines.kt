package br.com.govote.android.config

import android.content.Context
import android.graphics.Bitmap

import br.com.govote.android.BuildConfig
import br.com.govote.android.libs.logger.LogUtility
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig

object FrescoPipelines {
  fun setup(context: Context, imageCacheStatsTracker: ImageCacheStatsTracker) =
    Fresco.initialize(context,
      ImagePipelineConfig.newBuilder(context)
        .setBitmapsConfig(Bitmap.Config.RGB_565)
        .setImageCacheStatsTracker(imageCacheStatsTracker)
        .setMainDiskCacheConfig(
          DiskCacheConfig.newBuilder(context)
            .setCacheErrorLogger { _, _, message, throwable -> LogUtility.e(message, throwable!!) }
            .setMaxCacheSize((100 * 1000 * 1000).toLong())
            .setVersion(BuildConfig.VERSION_CODE)
            .build()
        )
        .setBitmapMemoryCacheParamsSupplier {
          MemoryCacheParams(
            32 * 1000 * 1000,
            100,
            5 * 1000 * 1000,
            10,
            250 * 1000
          )
        }
        .build())
}
