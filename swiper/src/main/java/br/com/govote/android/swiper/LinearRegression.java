package br.com.govote.android.swiper;

import java.util.Locale;

final class LinearRegression {
  private final double alpha;
  private final double beta;
  private final double r2;

  LinearRegression(final float[] x, final float[] y) {
    if (x.length != y.length) {
      throw new IllegalArgumentException("array lengths are not equal");
    }

    final int n = x.length;

    // first pass
    double sumx = 0.0;
    double sumy = 0.0;

    for (float aX : x) {
      sumx += aX;
    }

    for (int i = 0; i < n; i++) {
      sumy += y[i];
    }

    final double xbar = sumx / n;
    final double ybar = sumy / n;

    // second pass: compute summary statistics
    double xxbar = 0.0;
    double yybar = 0.0;
    double xybar = 0.0;

    for (int i = 0; i < n; i++) {
      xxbar += (x[i] - xbar) * (x[i] - xbar);
      yybar += (y[i] - ybar) * (y[i] - ybar);
      xybar += (x[i] - xbar) * (y[i] - ybar);
    }

    beta = xybar / xxbar;
    alpha = ybar - beta * xbar;

    // more statistical analysis
    double ssr = 0.0;      // regression sum of squares

    for (float aX : x) {
      double fit = beta * aX + alpha;
      ssr += (fit - ybar) * (fit - ybar);
    }

    r2 = ssr / yybar;
  }

  double intercept() {
    return alpha;
  }

  double slope() {
    return beta;
  }

  private double r2() {
    return r2;
  }

  @Override
  public String toString() {
    final Locale locale = Locale.getDefault();
    final String s = String.format(locale, "%.2f mN + %.2f", slope(), intercept());

    return s + "  (R^2 = " + String.format(locale, "%.3f", r2()) + ")";
  }

}
