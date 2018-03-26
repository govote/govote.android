package com.example.android.utils.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.example.android.utils.DimensionUtils;
import com.example.android.R;

import java.math.BigDecimal;

public class RangeSeekBar<T extends Number> extends android.support.v7.widget.AppCompatImageView {
	public static final Integer DEFAULT_MINIMUM = 0;
	public static final Integer DEFAULT_MAXIMUM = 100;
	public static final int HEIGHT_IN_DP = 30;
	public static final int TEXT_LATERAL_PADDING_IN_DP = 3;
	public static final int INVALID_POINTER_ID = 255;

	// Localized constants from MotionEvent for compatibility
	// with API < 8 "Froyo".
	public static final int ACTION_POINTER_INDEX_MASK = 0x0000ff00;
	public static final int ACTION_POINTER_INDEX_SHIFT = 8;

	private static final int INITIAL_PADDING_IN_DP = 8;
	private static final int DEFAULT_TEXT_SIZE_IN_DP = 9;
	private static final int DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 8;
	private static final int DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 0;
	private static final int LINE_HEIGHT_IN_DP = 2;

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public int mDefaultColor; // Color.argb(0xFF, 0x33, 0xB5, 0xE5);
	private Bitmap mThumbImage;
	private float thumbHalfHeight;
	private float thumbHalfWidth;
	private float initialPadding;
	private float padding;
	private T absoluteMinValue;
	private T absoluteMaxValue;
	private NumberType numberType;
	private double absoluteMinValuePrim;
	private double absoluteMaxValuePrim;
	private double normalizedMinValue = 0d;
	private double normalizedMaxValue = 1d;
	@Nullable
	private Thumb pressedThumb = null;
	private boolean notifyWhileDragging = false;
	private OnRangeSeekBarChangeListener<T> listener;
	private float mDownMotionX;
	private int mActivePointerId = INVALID_POINTER_ID;
	private int mScaledTouchSlop;
	private boolean mIsDragging;
	private int mTextOffset;
	private int mTextSize;
	private int mDistanceToTop;
	private RectF mRect;
	private boolean mSingleThumb;

	private int mActiveSeekBarColor;
	private int mDefaultSeekBarColor;

	public RangeSeekBar(@NonNull Context context) {
		super(context);
		init(context, null);
	}

	public RangeSeekBar(@NonNull Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RangeSeekBar(@NonNull Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	@NonNull
	@SuppressWarnings("unchecked")
	private T extractNumericValueFromAttributes(@NonNull TypedArray a, int attribute, int defaultValue) {
		TypedValue tv = a.peekValue(attribute);
		if (tv == null) {
			return (T) Integer.valueOf(defaultValue);
		}

		int type = tv.type;
		if (type == TypedValue.TYPE_FLOAT) {
			return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
		} else {
			return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
		}
	}

	private void init(Context context, @Nullable AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);

		mDefaultColor = attributes.getColor(R.styleable.RangeSeekBar_defaultColor, ContextCompat.getColor(getContext(), R.color.thumbDark));
		mActiveSeekBarColor = attributes.getColor(R.styleable.RangeSeekBar_activeSeekColor, ContextCompat.getColor(getContext(), R.color.thumbAccent));
		mDefaultSeekBarColor = attributes.getColor(R.styleable.RangeSeekBar_defaultSeekColor, ContextCompat.getColor(getContext(), R.color.seekBar));

		Drawable drawable = attributes.getDrawable(R.styleable.RangeSeekBar_thumb);

		if (drawable == null) {
			throw new IllegalStateException("Thumb was not specified. Specify one with \"thumb\" attribute");
		}

		mThumbImage = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(mThumbImage);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		float thumbWidth = mThumbImage.getWidth();

		thumbHalfHeight = 0.5f * mThumbImage.getHeight();
		thumbHalfWidth = 0.5f * thumbWidth;

		if (attrs == null) {
			setRangeToDefaultValues();
		} else {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);

			setRangeValues(
				extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM),
				extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM));

			mSingleThumb = a.getBoolean(R.styleable.RangeSeekBar_singleThumb, false);

			a.recycle();
		}

		setValuePrimAndNumberType();

		initialPadding = DimensionUtils.dpToPx(context, INITIAL_PADDING_IN_DP);

		mTextSize = DimensionUtils.dpToPx(context, DEFAULT_TEXT_SIZE_IN_DP);
		mDistanceToTop = DimensionUtils.dpToPx(context, DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP);
		mTextOffset = this.mTextSize + DimensionUtils.dpToPx(context,
			DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP) + this.mDistanceToTop;

		float lineHeight = DimensionUtils.dpToPx(context, LINE_HEIGHT_IN_DP);

		mRect = new RectF(padding,
			mTextOffset + thumbHalfHeight - lineHeight / 2,
			getWidth() - padding,
			mTextOffset + thumbHalfHeight + lineHeight / 2);

		// make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
		setFocusable(true);
		setFocusableInTouchMode(true);
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		attributes.recycle();
	}

	public void setRangeValues(T minValue, T maxValue) {
		this.absoluteMinValue = minValue;
		this.absoluteMaxValue = maxValue;
		setValuePrimAndNumberType();
	}

	@SuppressWarnings("unchecked")
	// only used to set default values when initialised from XML without any values specified
	private void setRangeToDefaultValues() {
		this.absoluteMinValue = (T) DEFAULT_MINIMUM;
		this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
		setValuePrimAndNumberType();
	}

	private void setValuePrimAndNumberType() {
		absoluteMinValuePrim = absoluteMinValue.doubleValue();
		absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
		numberType = NumberType.fromNumber(absoluteMinValue);
	}

	public void resetSelectedValues() {
		setSelectedMinValue(absoluteMinValue);
		setSelectedMaxValue(absoluteMaxValue);
	}

	public boolean isNotifyWhileDragging() {
		return notifyWhileDragging;
	}

	public void setNotifyWhileDragging(boolean flag) {
		this.notifyWhileDragging = flag;
	}

	/**
	 * Returns the absolute minimum value of the range that has been set at construction time.
	 *
	 * @return The absolute minimum value of the range.
	 */
	public T getAbsoluteMinValue() {
		return absoluteMinValue;
	}

	/**
	 * Returns the absolute maximum value of the range that has been set at construction time.
	 *
	 * @return The absolute maximum value of the range.
	 */
	public T getAbsoluteMaxValue() {
		return absoluteMaxValue;
	}

	/**
	 * Returns the currently selected min value.
	 *
	 * @return The currently selected min value.
	 */
	@NonNull
	public T getSelectedMinValue() {
		return normalizedToValue(normalizedMinValue);
	}

	/**
	 * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
	 *
	 * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMinValue(@NonNull T value) {
		// in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
			setNormalizedMinValue(0d);
		} else {
			setNormalizedMinValue(valueToNormalized(value));
		}
	}

	/**
	 * Returns the currently selected max value.
	 *
	 * @return The currently selected max value.
	 */
	@NonNull
	public T getSelectedMaxValue() {
		return normalizedToValue(normalizedMaxValue);
	}

	/**
	 * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
	 *
	 * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMaxValue(@NonNull T value) {
		// in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
			setNormalizedMaxValue(1d);
		} else {
			setNormalizedMaxValue(valueToNormalized(value));
		}
	}

	/**
	 * Registers given listener callback to notify about changed selected values.
	 *
	 * @param listener The listener to notify about changed selected values.
	 */
	public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
		this.listener = listener;
	}

	/**
	 * Handles thumb selection and movement. Notifies listener callback on certain events.
	 */
	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {

		if (!isEnabled()) {
			return false;
		}

		int pointerIndex;

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN:
				// Remember where the motion event started
				mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
				pointerIndex = event.findPointerIndex(mActivePointerId);
				mDownMotionX = event.getX(pointerIndex);

				pressedThumb = evalPressedThumb(mDownMotionX);

				// Only handle thumb presses.
				if (pressedThumb == null) {
					return super.onTouchEvent(event);
				}

				setPressed(true);
				invalidate();
				onStartTrackingTouch();
				trackTouchEvent(event);
				attemptClaimDrag();

				break;
			case MotionEvent.ACTION_MOVE:
				if (pressedThumb != null) {

					if (mIsDragging) {
						trackTouchEvent(event);
					} else {
						// Scroll to follow the motion event
						pointerIndex = event.findPointerIndex(mActivePointerId);
						final float x = event.getX(pointerIndex);

						if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
							setPressed(true);
							invalidate();
							onStartTrackingTouch();
							trackTouchEvent(event);
							attemptClaimDrag();
						}
					}

					if (notifyWhileDragging && listener != null) {
						listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsDragging) {
					trackTouchEvent(event);
					onStopTrackingTouch();
					setPressed(false);
				} else {
					// Touch up when we never crossed the touch slop threshold
					// should be interpreted as a tap-seek to that location.
					performClick();
					onStartTrackingTouch();
					trackTouchEvent(event);
					onStopTrackingTouch();
				}

				pressedThumb = null;
				invalidate();
				if (listener != null) {
					listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN: {
				final int index = event.getPointerCount() - 1;
				// final int index = ev.getActionIndex();
				mDownMotionX = event.getX(index);
				mActivePointerId = event.getPointerId(index);
				invalidate();
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
				onSecondaryPointerUp(event);
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
				if (mIsDragging) {
					onStopTrackingTouch();
					setPressed(false);
				}
				invalidate(); // see above explanation
				break;
		}
		return true;
	}

	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	private void onSecondaryPointerUp(@NonNull MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose
			// a new active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mDownMotionX = ev.getX(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	private void trackTouchEvent(@NonNull MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mActivePointerId);
		final float x = event.getX(pointerIndex);

		if (Thumb.MIN.equals(pressedThumb) && !mSingleThumb) {
			setNormalizedMinValue(screenToNormalized(x));
		} else if (Thumb.MAX.equals(pressedThumb)) {
			setNormalizedMaxValue(screenToNormalized(x));
		}
	}

	/**
	 * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
	 */
	private void attemptClaimDrag() {
		if (getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
	}

	/**
	 * This is called when the user has started touching this widget.
	 */
	void onStartTrackingTouch() {
		mIsDragging = true;
	}

	/**
	 * This is called when the user either releases his touch or the touch is canceled.
	 */
	void onStopTrackingTouch() {
		mIsDragging = false;
	}

	/**
	 * Ensures correct size of the widget.
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 200;
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
			width = MeasureSpec.getSize(widthMeasureSpec);
		}

		int height = mThumbImage.getHeight() + DimensionUtils.dpToPx(getContext(), HEIGHT_IN_DP);
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
			height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
		}
		setMeasuredDimension(width, height);
	}

	/**
	 * Draws the widget on the given canvas.
	 */
	@Override
	protected synchronized void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);

		paint.setTextSize(mTextSize);
		paint.setStyle(Style.FILL);
		paint.setColor(mDefaultSeekBarColor);
		paint.setAntiAlias(true);

		// draw min and max labels
		String minLabel = "";
		String maxLabel = "";

		float minMaxLabelSize = Math.max(paint.measureText(minLabel), paint.measureText(maxLabel));
		float minMaxHeight = mTextOffset + thumbHalfHeight + mTextSize / 3;
		canvas.drawText(minLabel, 0, minMaxHeight, paint);
		canvas.drawText(maxLabel, getWidth() - minMaxLabelSize, minMaxHeight, paint);
		padding = initialPadding + minMaxLabelSize + thumbHalfWidth;

		// draw seek bar background line
		mRect.left = padding;
		mRect.right = getWidth() - padding;
		canvas.drawRect(mRect, paint);

		boolean selectedValuesAreDefault = (getSelectedMinValue().equals(getAbsoluteMinValue()) &&
			getSelectedMaxValue().equals(getAbsoluteMaxValue()));

		int colorToUseForButtonsAndHighlightedLine = selectedValuesAreDefault ?
			mDefaultSeekBarColor :    // default values
			mActiveSeekBarColor; //non default, filter is active

		// draw seek bar active range line
		mRect.left = normalizedToScreen(normalizedMinValue);
		mRect.right = normalizedToScreen(normalizedMaxValue);

		paint.setColor(colorToUseForButtonsAndHighlightedLine);
		canvas.drawRect(mRect, paint);

		// draw minimum thumb if not a single thumb control
		if (!mSingleThumb) {
			drawThumb(normalizedToScreen(normalizedMinValue), Thumb.MIN.equals(pressedThumb), canvas,
				selectedValuesAreDefault);
		}

		// draw maximum thumb
		drawThumb(normalizedToScreen(normalizedMaxValue), Thumb.MAX.equals(pressedThumb), canvas,
			selectedValuesAreDefault);

		// draw the text if sliders have moved from default edges
		if (!selectedValuesAreDefault) {
			paint.setTextSize(mTextSize);
			paint.setColor(Color.WHITE);
			// give text a bit more space here so it doesn't getSettings cut off
			int offset = DimensionUtils.dpToPx(getContext(), TEXT_LATERAL_PADDING_IN_DP);

			String minText = String.valueOf(getSelectedMinValue());
			String maxText = String.valueOf(getSelectedMaxValue());
			float minTextWidth = paint.measureText(minText) + offset;
			float maxTextWidth = paint.measureText(maxText) + offset;

			if (!mSingleThumb) {
				canvas.drawText(minText,
					normalizedToScreen(normalizedMinValue) - minTextWidth * 0.5f,
					mDistanceToTop + mTextSize,
					paint);

			}

			canvas.drawText(maxText,
				normalizedToScreen(normalizedMaxValue) - maxTextWidth * 0.5f,
				mDistanceToTop + mTextSize,
				paint);
		}

	}

	/**
	 * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		final Bundle bundle = new Bundle();
		bundle.putParcelable("SUPER", super.onSaveInstanceState());
		bundle.putDouble("MIN", normalizedMinValue);
		bundle.putDouble("MAX", normalizedMaxValue);
		return bundle;
	}

	/**
	 * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable parcel) {
		final Bundle bundle = (Bundle) parcel;
		super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
		normalizedMinValue = bundle.getDouble("MIN");
		normalizedMaxValue = bundle.getDouble("MAX");
	}

	/**
	 * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
	 *
	 * @param screenCoord The x-coordinate in screen space where to draw the image.
	 * @param pressed     Is the thumb currently in "pressed" state?
	 * @param canvas      The canvas to draw upon.
	 */
	private void drawThumb(float screenCoord, boolean pressed, @NonNull Canvas canvas, boolean areSelectedValuesDefault) {
//        Bitmap buttonToDraw;
//        if (areSelectedValuesDefault) {
//            buttonToDraw = thumbDisabledImage;
//        } else {
//            buttonToDraw = pressed ? thumbPressedImage : thumbImage;
//        }
//
		canvas.drawBitmap(mThumbImage, screenCoord - thumbHalfWidth,
			mTextOffset,
			paint);
	}

	/**
	 * Decides which (if any) thumb is touched by the given x-coordinate.
	 *
	 * @param touchX The x-coordinate of a touch event in screen space.
	 * @return The pressed thumb or null if none has been touched.
	 */
	@Nullable
	private Thumb evalPressedThumb(float touchX) {
		Thumb result = null;
		boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
		boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
		if (minThumbPressed && maxThumbPressed) {
			// if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
			result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
		} else if (minThumbPressed) {
			result = Thumb.MIN;
		} else if (maxThumbPressed) {
			result = Thumb.MAX;
		}
		return result;
	}

	/**
	 * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
	 *
	 * @param touchX               The x-coordinate in screen space to check.
	 * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
	 * @return true if x-coordinate is in thumb range, false otherwise.
	 */
	private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
		return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth;
	}

	/**
	 * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will getSettings invalidated when calling this method.
	 *
	 * @param value The new normalized min value to set.
	 */
	private void setNormalizedMinValue(double value) {
		normalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, normalizedMaxValue)));
		invalidate();
	}

	/**
	 * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will getSettings invalidated when calling this method.
	 *
	 * @param value The new normalized max value to set.
	 */
	private void setNormalizedMaxValue(double value) {
		normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, normalizedMinValue)));
		invalidate();
	}

	/**
	 * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
	 *
	 * @param normalized double value
	 * @return T
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	private T normalizedToValue(double normalized) {
		double v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim);
		// TODO parameterize this rounding to allow variable decimal points
		return (T) numberType.toNumber(Math.round(v * 100) / 100d);
	}

	/**
	 * Converts the given Number value to a normalized double.
	 *
	 * @param value The Number value to normalize.
	 * @return The normalized double.
	 */
	private double valueToNormalized(@NonNull T value) {
		if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
			// prevent division by zero, simply return 0.
			return 0d;
		}
		return (value.doubleValue() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim);
	}

	/**
	 * Converts a normalized value into screen space.
	 *
	 * @param normalizedCoord The normalized value to convert.
	 * @return The converted value in screen space.
	 */
	private float normalizedToScreen(double normalizedCoord) {
		return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
	}

	/**
	 * Converts screen space x-coordinates into normalized values.
	 *
	 * @param screenCoord The x-coordinate in screen space to convert.
	 * @return The normalized value.
	 */
	private double screenToNormalized(float screenCoord) {
		int width = getWidth();
		if (width <= 2 * padding) {
			// prevent division by zero, simply return 0.
			return 0d;
		} else {
			double result = (screenCoord - padding) / (width - 2 * padding);
			return Math.min(1d, Math.max(0d, result));
		}
	}

	/**
	 * Thumb constants (min and max).
	 */
	private enum Thumb {
		MIN, MAX
	}

	/**
	 * Utility enumeration used to convert between Numbers and doubles.
	 *
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 */
	private enum NumberType {
		LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

		@NonNull
		public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
			if (value instanceof Long) {
				return LONG;
			}
			if (value instanceof Double) {
				return DOUBLE;
			}
			if (value instanceof Integer) {
				return INTEGER;
			}
			if (value instanceof Float) {
				return FLOAT;
			}
			if (value instanceof Short) {
				return SHORT;
			}
			if (value instanceof Byte) {
				return BYTE;
			}
			if (value instanceof BigDecimal) {
				return BIG_DECIMAL;
			}
			throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
		}

		public Number toNumber(double value) {
			switch (this) {
				case LONG:
					return (long) value;
				case DOUBLE:
					return value;
				case INTEGER:
					return (int) value;
				case FLOAT:
					return (float) value;
				case SHORT:
					return (short) value;
				case BYTE:
					return (byte) value;
				case BIG_DECIMAL:
					return BigDecimal.valueOf(value);
			}
			throw new InstantiationError("can't convert " + this + " to a Number object");
		}
	}

	/**
	 * Callback listener interface to notify about changed range values.
	 *
	 * @param <T> The Number type the RangeSeekBar has been declared with.
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 */
	@FunctionalInterface
	public interface OnRangeSeekBarChangeListener<T> {
		void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, T minValue, T maxValue);
	}

}
