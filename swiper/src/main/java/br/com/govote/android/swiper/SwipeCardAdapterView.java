package br.com.govote.android.swiper;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;

@SuppressWarnings("unchecked")
public class SwipeCardAdapterView extends AdapterView {
  private int maxVisible = 2;
  private int minAdapterStack = 6;
  private float rotationDegrees = 15.f;

  private Adapter adapter;
  private int firstObjectInStack = 0;
  private SwipeListener swipeListener;
  private AdapterDataSetObserver dataSetObserver;
  private boolean inLayout = false;
  private View activeCard = null;
  private SwipeCardListener flingCardListener;
  private PointF lastTouchPoint;

  private int heightMeasureSpec;
  private int widthMeasureSpec;

  public SwipeCardAdapterView(final Context context, final AttributeSet attrs) {
    this(context, attrs, attrs.getStyleAttribute());
  }

  public SwipeCardAdapterView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

    final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SwipeCardAdapterView, defStyle, 0);

    maxVisible = attributes.getInt(R.styleable.SwipeCardAdapterView_max_visible, maxVisible);
    minAdapterStack = attributes.getInt(R.styleable.SwipeCardAdapterView_min_adapter_stack, minAdapterStack);
    rotationDegrees = attributes.getFloat(R.styleable.SwipeCardAdapterView_rotation_degrees, rotationDegrees);

    attributes.recycle();
  }

  @Override
  public View getSelectedView() {
    return activeCard;
  }

  @Override
  public void requestLayout() {
    if (!inLayout) {
      super.requestLayout();
    }
  }

  @Override
  protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    if (adapter == null) {
      return;
    }

    inLayout = true;
    final int adapterCount = adapter.getCount();

    if (adapterCount == 0) {
      removeAllViewsInLayout();
    } else {
      final View topCard = getChildAt(firstObjectInStack);
      if (activeCard != null && topCard != null && topCard == activeCard) {
        if (flingCardListener.isTouching()) {
          final PointF lastPoint = flingCardListener.getLastPoint();
          if (lastTouchPoint == null || !lastTouchPoint.equals(lastPoint)) {
            lastTouchPoint = lastPoint;
            removeViewsInLayout(0, firstObjectInStack);
            layoutChildren(1, adapterCount);
          }
        }
      } else {
        // Reset the UI and set top view listener
        removeAllViewsInLayout();
        layoutChildren(0, adapterCount);
        setTopView();
      }
    }

    inLayout = false;

    if (adapterCount <= minAdapterStack) {
      swipeListener.onAdapterAboutToEmpty(adapterCount);
    }
  }

  private void layoutChildren(int startingIndex, int adapterCount) {
    while (startingIndex < Math.min(adapterCount, maxVisible)) {
      final View newUnderChild = adapter.getView(startingIndex, null, this);

      if (newUnderChild.getVisibility() != GONE) {
        makeAndAddView(newUnderChild);
        firstObjectInStack = startingIndex;
      }

      startingIndex++;
    }
  }

  private void makeAndAddView(View child) {
    final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
    addViewInLayout(child, 0, lp, true);

    final boolean needToMeasure = child.isLayoutRequested();

    if (needToMeasure) {
      final int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
        getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
        lp.width);
      final int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
        getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
        lp.height);
      child.measure(childWidthSpec, childHeightSpec);
    } else {
      cleanupLayoutState(child);
    }

    final int w = child.getMeasuredWidth();
    final int h = child.getMeasuredHeight();

    final int gravity = lp.gravity == -1
      ? Gravity.TOP | Gravity.START
      : lp.gravity;

    final int layoutDirection = getLayoutDirection();
    final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
    final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

    int childLeft;
    int childTop;

    switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
      case Gravity.CENTER_HORIZONTAL:
        childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 +
          lp.leftMargin - lp.rightMargin;
        break;
      case Gravity.END:
        childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
        break;
      case Gravity.START:
      default:
        childLeft = getPaddingLeft() + lp.leftMargin;
        break;
    }

    switch (verticalGravity) {
      case Gravity.CENTER_VERTICAL:
        childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 +
          lp.topMargin - lp.bottomMargin;
        break;
      case Gravity.BOTTOM:
        childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
        break;
      case Gravity.TOP:
      default:
        childTop = getPaddingTop() + lp.topMargin;
        break;
    }

    final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

    child.setLayerType(View.LAYER_TYPE_HARDWARE, p);
    child.layout(childLeft, childTop, childLeft + w, childTop + h);
  }

  private void setTopView() {
    if (getChildCount() > 0) {
      activeCard = getChildAt(firstObjectInStack);

      if (activeCard != null) {
        flingCardListener = new SwipeCardListener(activeCard, adapter.getItem(0),
          rotationDegrees, new SwipeActionsListener() {

          @Override
          public void onTouchDown() {
            swipeListener.onTouchDown();
          }

          @Override
          public void onTouchUp() {
            swipeListener.onTouchUp();
          }

          @Override
          public void onCardExited() {
            activeCard = null;
            swipeListener.removeFirstObjectInAdapter();
          }

          @Override
          public void onBeforeLeftCardExit(ActionCallback done) {
            swipeListener.onBeforeLeftCardExit(done);
          }

          @Override
          public void onBeforeRightCardExit(ActionCallback done) {
            swipeListener.onBeforeRightCardExit(done);
          }

          @Override
          public void leftExit(Object dataObject) {
            swipeListener.onLeftCardExit(dataObject);
          }

          @Override
          public void rightExit(Object dataObject) {
            swipeListener.onRightCardExit(dataObject);
          }

          @Override
          public void onClick(Object dataObject) {
            swipeListener.onClick(dataObject);
          }

          @Override
          public void onScroll(float scrollProgressPercent) {
            swipeListener.onScroll(scrollProgressPercent);
          }
        });

        activeCard.setOnTouchListener(flingCardListener);
      }
    }
  }

  public SwipeCardListener getTopCardListener() {
    if (flingCardListener == null) {
      throw new NullPointerException();
    }

    return flingCardListener;
  }

  @Override
  public Adapter getAdapter() {
    return adapter;
  }

  @Override
  public void setAdapter(Adapter adapter) {
    if (this.adapter != null && dataSetObserver != null) {
      this.adapter.unregisterDataSetObserver(dataSetObserver);
      dataSetObserver = null;
    }

    this.adapter = adapter;

    if (this.adapter != null && dataSetObserver == null) {
      dataSetObserver = new AdapterDataSetObserver();
      this.adapter.registerDataSetObserver(dataSetObserver);
    }
  }

  public void setSwipeListener(SwipeListener onSwipeListener) {
    this.swipeListener = onSwipeListener;
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new FrameLayout.LayoutParams(getContext(), attrs);
  }

  @Override
  public void setSelection(int i) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    this.widthMeasureSpec = widthMeasureSpec;
    this.heightMeasureSpec = heightMeasureSpec;
  }

  private class AdapterDataSetObserver extends DataSetObserver {
    @Override
    public void onChanged() {
      requestLayout();
    }

    @Override
    public void onInvalidated() {
      requestLayout();
    }
  }
}
