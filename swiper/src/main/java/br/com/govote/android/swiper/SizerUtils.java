package br.com.govote.android.swiper;

import android.content.Context;
import android.util.DisplayMetrics;

final class SizerUtils {
  static int dpToPx(final Context context, final int dp) {
    return Math.round(dp * getPixelScaleFactor(context));
  }

  private static float getPixelScaleFactor(final Context context) {
    final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}
