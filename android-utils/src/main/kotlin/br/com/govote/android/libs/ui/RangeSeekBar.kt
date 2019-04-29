package br.com.govote.android.libs.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import br.com.govote.android.libs.R
import br.com.govote.android.libs.utils.DimensionUtils
import java.math.BigDecimal

@Suppress("UNCHECKED_CAST")
class RangeSeekBar<T : Number> : AppCompatImageView {

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var defaultColor: Int = 0
  private var thumbImage: Bitmap? = null
  private var thumbHalfHeight: Float = 0.toFloat()
  private var thumbHalfWidth: Float = 0.toFloat()
  private var initialPadding: Float = 0.toFloat()
  private var padding: Float = 0.toFloat()
  /**
   * Returns the absolute minimum value of the range that has been set at construction time.
   *
   * @return The absolute minimum value of the range.
   */
  var absoluteMinValue: T? = null
    private set
  /**
   * Returns the absolute maximum value of the range that has been set at construction time.
   *
   * @return The absolute maximum value of the range.
   */
  var absoluteMaxValue: T? = null
    private set
  private var numberType: NumberType? = null
  private var absoluteMinValuePrim: Double = 0.toDouble()
  private var absoluteMaxValuePrim: Double = 0.toDouble()
  private var normalizedMinValue = 0.0
  private var normalizedMaxValue = 1.0
  private var pressedThumb: Thumb? = null
  private var isNotifyWhileDragging = false
  private var listener: OnRangeSeekBarChangeListener<T>? = null
  private var downMotionX: Float = 0.toFloat()
  private var activePointerId = INVALID_POINTER_ID
  private var scaledTouchSlop: Int = 0
  private var isDragging: Boolean = false
  private var textOffset: Int = 0
  private var textSize: Int = 0
  private var distanceToTop: Int = 0
  private var rect: RectF? = null
  private var singleThumb: Boolean = false

  private var activeSeekBarColor: Int = 0
  private var defaultSeekBarColor: Int = 0

  // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
  private var selectedMinValue: T
    /**
     * Returns the currently selected min value.
     *
     * @return The currently selected min value.
     */
    get() = normalizedToValue(normalizedMinValue)
    /**
     * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
     */
    set(value) = if (0.0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
      setNormalizedMinValue(0.0)
    } else {
      setNormalizedMinValue(valueToNormalized(value))
    }

  // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
  private var selectedMaxValue: T
    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    get() = normalizedToValue(normalizedMaxValue)
    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */
    set(value) = if (0.0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
      setNormalizedMaxValue(1.0)
    } else {
      setNormalizedMaxValue(valueToNormalized(value))
    }

  constructor(context: Context) : super(context) {
    init(context, null)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet,
              defStyle: Int) : super(context, attrs, defStyle) {
    init(context, attrs)
  }

  private fun extractNumericValueFromAttributes(a: TypedArray, attribute: Int,
                                                defaultValue: Int): T {
    val tv = a.peekValue(attribute) ?: return Integer.valueOf(defaultValue) as T
    val type = tv.type

    return if (type == TypedValue.TYPE_FLOAT) {
      java.lang.Float.valueOf(a.getFloat(attribute, defaultValue.toFloat())) as T
    } else {
      Integer.valueOf(a.getInteger(attribute, defaultValue)) as T
    }
  }

  private fun init(context: Context, attrs: AttributeSet?) {
    val attributes = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar)

    defaultColor = attributes
      .getColor(R.styleable.RangeSeekBar_defaultColor, ContextCompat.getColor(getContext(), R.color.thumbDark))
    activeSeekBarColor = attributes
      .getColor(R.styleable.RangeSeekBar_activeSeekColor, ContextCompat.getColor(getContext(), R.color.thumbAccent))
    defaultSeekBarColor = attributes
      .getColor(R.styleable.RangeSeekBar_defaultSeekColor, ContextCompat.getColor(getContext(), R.color.seekBar))

    val drawable = attributes.getDrawable(R.styleable.RangeSeekBar_thumb)
      ?: throw IllegalStateException("Thumb was not specified. Specify one with \"thumb\" attribute")

    thumbImage = Bitmap
      .createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(thumbImage!!)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    val thumbWidth = thumbImage!!.width.toFloat()

    thumbHalfHeight = 0.5f * thumbImage!!.height
    thumbHalfWidth = 0.5f * thumbWidth

    if (attrs == null) {
      setRangeToDefaultValues()
    } else {
      val a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0)

      setRangeValues(
        extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM),
        extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM))

      singleThumb = a.getBoolean(R.styleable.RangeSeekBar_singleThumb, false)

      a.recycle()
    }

    setValuePrimAndNumberType()

    initialPadding = DimensionUtils.dpToPx(context, INITIAL_PADDING_IN_DP).toFloat()

    textSize = DimensionUtils.dpToPx(context, DEFAULT_TEXT_SIZE_IN_DP)
    distanceToTop = DimensionUtils.dpToPx(context, DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP)
    textOffset = this.textSize + DimensionUtils.dpToPx(context,
      DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP
    ) + this.distanceToTop

    val lineHeight = DimensionUtils.dpToPx(context, LINE_HEIGHT_IN_DP).toFloat()

    rect = RectF(padding,
      textOffset + thumbHalfHeight - lineHeight / 2,
      width - padding,
      textOffset.toFloat() + thumbHalfHeight + lineHeight / 2)

    // make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
    isFocusable = true
    isFocusableInTouchMode = true
    scaledTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop

    attributes.recycle()
  }

  private fun setRangeValues(minValue: T, maxValue: T) {
    this.absoluteMinValue = minValue
    this.absoluteMaxValue = maxValue
    setValuePrimAndNumberType()
  }

  private // only used to set default values when initialised from XML without any values specified
  fun setRangeToDefaultValues() {
    this.absoluteMinValue = DEFAULT_MINIMUM as T
    this.absoluteMaxValue = DEFAULT_MAXIMUM as T
    setValuePrimAndNumberType()
  }

  private fun setValuePrimAndNumberType() {
    absoluteMinValuePrim = absoluteMinValue!!.toDouble()
    absoluteMaxValuePrim = absoluteMaxValue!!.toDouble()
    numberType = NumberType.fromNumber(absoluteMinValue!!)
  }

  fun resetSelectedValues() {
    selectedMinValue = this.absoluteMinValue!!
    selectedMaxValue = this.absoluteMaxValue!!
  }

  /**
   * Registers given listener callback to notify about changed selected values.
   *
   * @param listener The listener to notify about changed selected values.
   */
  fun setOnRangeSeekBarChangeListener(listener: OnRangeSeekBarChangeListener<T>) {
    this.listener = listener
  }

  /**
   * Handles thumb selection and movement. Notifies listener callback on certain events.
   */
  override fun onTouchEvent(event: MotionEvent): Boolean {

    if (!isEnabled) {
      return false
    }

    val pointerIndex: Int

    val action = event.action
    when (action and MotionEvent.ACTION_MASK) {

      MotionEvent.ACTION_DOWN -> {
        // Remember where the motion event started
        activePointerId = event.getPointerId(event.pointerCount - 1)
        pointerIndex = event.findPointerIndex(activePointerId)
        downMotionX = event.getX(pointerIndex)

        pressedThumb = evalPressedThumb(downMotionX)

        // Only handle thumb presses.
        if (pressedThumb == null) {
          return super.onTouchEvent(event)
        }

        isPressed = true
        invalidate()
        onStartTrackingTouch()
        trackTouchEvent(event)
        attemptClaimDrag()
      }
      MotionEvent.ACTION_MOVE -> if (pressedThumb != null) {

        if (isDragging) {
          trackTouchEvent(event)
        } else {
          // Scroll to follow the motion event
          pointerIndex = event.findPointerIndex(activePointerId)
          val x = event.getX(pointerIndex)

          if (Math.abs(x - downMotionX) > scaledTouchSlop) {
            isPressed = true
            invalidate()
            onStartTrackingTouch()
            trackTouchEvent(event)
            attemptClaimDrag()
          }
        }

        if (isNotifyWhileDragging && listener != null) {
          listener!!.onRangeSeekBarValuesChanged(this, selectedMinValue, selectedMaxValue)
        }
      }
      MotionEvent.ACTION_UP -> {
        if (isDragging) {
          trackTouchEvent(event)
          onStopTrackingTouch()
          setPressed(false)
        } else {
          // Touch up when we never crossed the touch slop threshold
          // should be interpreted as a tap-seek to that location.
          performClick()
          onStartTrackingTouch()
          trackTouchEvent(event)
          onStopTrackingTouch()
        }

        pressedThumb = null
        invalidate()
        if (listener != null) {
          listener!!.onRangeSeekBarValuesChanged(this, selectedMinValue, selectedMaxValue)
        }
      }
      MotionEvent.ACTION_POINTER_DOWN -> {
        val index = event.pointerCount - 1
        // final int index = ev.getActionIndex();
        downMotionX = event.getX(index)
        activePointerId = event.getPointerId(index)
        invalidate()
      }
      MotionEvent.ACTION_POINTER_UP -> {
        onSecondaryPointerUp(event)
        invalidate()
      }
      MotionEvent.ACTION_CANCEL -> {
        if (isDragging) {
          onStopTrackingTouch()
          isPressed = false
        }
        invalidate() // see above explanation
      }
    }
    return true
  }

  override fun performClick(): Boolean {
    super.performClick()
    return true
  }

  private fun onSecondaryPointerUp(ev: MotionEvent) {
    val pointerIndex = ev.action and ACTION_POINTER_INDEX_MASK shr ACTION_POINTER_INDEX_SHIFT

    val pointerId = ev.getPointerId(pointerIndex)
    if (pointerId == activePointerId) {
      // This was our active pointer going up. Choose
      // a new active pointer and adjust accordingly.
      // TODO: Make this decision more intelligent.
      val newPointerIndex = if (pointerIndex == 0) 1 else 0
      downMotionX = ev.getX(newPointerIndex)
      activePointerId = ev.getPointerId(newPointerIndex)
    }
  }

  private fun trackTouchEvent(event: MotionEvent) {
    val pointerIndex = event.findPointerIndex(activePointerId)
    val x = event.getX(pointerIndex)

    if (Thumb.MIN == pressedThumb && !singleThumb) {
      setNormalizedMinValue(screenToNormalized(x))
    } else if (Thumb.MAX == pressedThumb) {
      setNormalizedMaxValue(screenToNormalized(x))
    }
  }

  /**
   * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
   */
  private fun attemptClaimDrag() {
    if (parent != null) {
      parent.requestDisallowInterceptTouchEvent(true)
    }
  }

  /**
   * This is called when the user has started touching this widget.
   */
  private fun onStartTrackingTouch() {
    isDragging = true
  }

  /**
   * This is called when the user either releases his touch or the touch is canceled.
   */
  private fun onStopTrackingTouch() {
    isDragging = false
  }

  /**
   * Ensures correct size of the widget.
   */
  @Synchronized
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var width = 200
    if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
      width = MeasureSpec.getSize(widthMeasureSpec)
    }

    var height = thumbImage!!.height + DimensionUtils.dpToPx(context, HEIGHT_IN_DP)
    if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
      height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec))
    }
    setMeasuredDimension(width, height)
  }

  /**
   * Draws the widget on the given canvas.
   */
  @Synchronized
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    paint.textSize = textSize.toFloat()
    paint.style = Style.FILL
    paint.color = defaultSeekBarColor
    paint.isAntiAlias = true

    // draw min and max labels
    val minLabel = ""
    val maxLabel = ""

    val minMaxLabelSize = Math.max(paint.measureText(minLabel), paint.measureText(maxLabel))
    val minMaxHeight = textOffset.toFloat() + thumbHalfHeight + (textSize / 3).toFloat()
    canvas.drawText(minLabel, 0f, minMaxHeight, paint)
    canvas.drawText(maxLabel, width - minMaxLabelSize, minMaxHeight, paint)
    padding = initialPadding + minMaxLabelSize + thumbHalfWidth

    // draw seek bar background line
    rect!!.left = padding
    rect!!.right = width - padding
    canvas.drawRect(rect!!, paint)

    val selectedValuesAreDefault =
      selectedMinValue == absoluteMinValue && selectedMaxValue == absoluteMaxValue

    val colorToUseForButtonsAndHighlightedLine = if (selectedValuesAreDefault)
      defaultSeekBarColor
    else
    // default values
      activeSeekBarColor // non default, filter is active

    // draw seek bar active range line
    rect!!.left = normalizedToScreen(normalizedMinValue)
    rect!!.right = normalizedToScreen(normalizedMaxValue)

    paint.color = colorToUseForButtonsAndHighlightedLine
    canvas.drawRect(rect!!, paint)

    // draw minimum thumb if not a single thumb control
    if (!singleThumb) {
      drawThumb(normalizedToScreen(normalizedMinValue), Thumb.MIN == pressedThumb, canvas)
    }

    // draw maximum thumb
    drawThumb(normalizedToScreen(normalizedMaxValue), Thumb.MAX == pressedThumb, canvas)

    // draw the text if sliders have moved from default edges
    if (!selectedValuesAreDefault) {
      paint.textSize = textSize.toFloat()
      paint.color = Color.WHITE
      // give text a bit more space here so it doesn't getSettings cut off
      val offset = DimensionUtils.dpToPx(context, TEXT_LATERAL_PADDING_IN_DP)

      val minText = selectedMinValue.toString()
      val maxText = selectedMaxValue.toString()
      val minTextWidth = paint.measureText(minText) + offset
      val maxTextWidth = paint.measureText(maxText) + offset

      if (!singleThumb) {
        canvas.drawText(minText,
          normalizedToScreen(normalizedMinValue) - minTextWidth * 0.5f,
          (distanceToTop + textSize).toFloat(),
          paint)
      }

      canvas.drawText(maxText,
        normalizedToScreen(normalizedMaxValue) - maxTextWidth * 0.5f,
        (distanceToTop + textSize).toFloat(),
        paint)
    }
  }

  /**
   * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the [.setId] method. Other members of this class than the normalized min and max values don't need to be saved.
   */
  override fun onSaveInstanceState(): Parcelable? {
    val bundle = Bundle()
    bundle.putParcelable("SUPER", super.onSaveInstanceState())
    bundle.putDouble("MIN", normalizedMinValue)
    bundle.putDouble("MAX", normalizedMaxValue)
    return bundle
  }

  /**
   * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the [.setId] method.
   */
  override fun onRestoreInstanceState(parcel: Parcelable) {
    val bundle = parcel as Bundle
    super.onRestoreInstanceState(bundle.getParcelable("SUPER"))
    normalizedMinValue = bundle.getDouble("MIN")
    normalizedMaxValue = bundle.getDouble("MAX")
  }

  /**
   * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
   *
   * @param screenCoord The x-coordinate in screen space where to draw the image.
   * @param pressed Is the thumb currently in "pressed" state?
   * @param canvas The canvas to draw upon.
   */
  private fun drawThumb(screenCoord: Float, pressed: Boolean, canvas: Canvas) {
    canvas.drawBitmap(thumbImage!!, screenCoord - thumbHalfWidth,
      textOffset.toFloat(),
      paint)
  }

  /**
   * Decides which (if any) thumb is touched by the given x-coordinate.
   *
   * @param touchX The x-coordinate of a touch event in screen space.
   * @return The pressed thumb or null if none has been touched.
   */
  private fun evalPressedThumb(touchX: Float): Thumb? {
    var result: Thumb? = null
    val minThumbPressed = isInThumbRange(touchX, normalizedMinValue)
    val maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue)
    if (minThumbPressed && maxThumbPressed) {
      // if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
      result = if (touchX / width > 0.5f) Thumb.MIN else Thumb.MAX
    } else if (minThumbPressed) {
      result = Thumb.MIN
    } else if (maxThumbPressed) {
      result = Thumb.MAX
    }
    return result
  }

  /**
   * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
   *
   * @param touchX The x-coordinate in screen space to check.
   * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
   * @return true if x-coordinate is in thumb range, false otherwise.
   */
  private fun isInThumbRange(touchX: Float, normalizedThumbValue: Double): Boolean {
    return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth
  }

  /**
   * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will getSettings invalidated when calling this method.
   *
   * @param value The new normalized min value to set.
   */
  private fun setNormalizedMinValue(value: Double) {
    normalizedMinValue = Math.max(0.0, Math.min(1.0, Math.min(value, normalizedMaxValue)))
    invalidate()
  }

  /**
   * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will getSettings invalidated when calling this method.
   *
   * @param value The new normalized max value to set.
   */
  private fun setNormalizedMaxValue(value: Double) {
    normalizedMaxValue = Math.max(0.0, Math.min(1.0, Math.max(value, normalizedMinValue)))
    invalidate()
  }

  /**
   * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
   *
   * @param normalized double value
   * @return T
   */
  private fun normalizedToValue(normalized: Double): T {
    val v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim)
    // TODO parameterize this rounding to allow variable decimal points
    return numberType!!.toNumber(Math.round(v * 100) / 100.0) as T
  }

  /**
   * Converts the given Number value to a normalized double.
   *
   * @param value The Number value to normalize.
   * @return The normalized double.
   */
  private fun valueToNormalized(value: T): Double {
    return if (0.0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
      // prevent division by zero, simply return 0.
      0.0
    } else (value.toDouble() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim)
  }

  /**
   * Converts a normalized value into screen space.
   *
   * @param normalizedCoord The normalized value to convert.
   * @return The converted value in screen space.
   */
  private fun normalizedToScreen(normalizedCoord: Double): Float {
    return (padding + normalizedCoord * (width - 2 * padding)).toFloat()
  }

  /**
   * Converts screen space x-coordinates into normalized values.
   *
   * @param screenCoord The x-coordinate in screen space to convert.
   * @return The normalized value.
   */
  private fun screenToNormalized(screenCoord: Float): Double {
    val width = width
    return if (width <= 2 * padding) {
      // prevent division by zero, simply return 0.
      0.0
    } else {
      val result = ((screenCoord - padding) / (width - 2 * padding)).toDouble()
      Math.min(1.0, Math.max(0.0, result))
    }
  }

  /**
   * Thumb constants (min and max).
   */
  private enum class Thumb {

    MIN, MAX
  }

  private enum class NumberType {

    LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

    fun toNumber(value: Double): Number {
      return when (this) {
        LONG -> value.toLong()
        DOUBLE -> value
        INTEGER -> value.toInt()
        FLOAT -> value.toFloat()
        SHORT -> value.toShort()
        BYTE -> value.toByte()
        BIG_DECIMAL -> BigDecimal.valueOf(value)
      }
    }

    companion object {

      @Throws(IllegalArgumentException::class)
      fun <E : Number> fromNumber(value: E): NumberType {
        if (value is Long) {
          return LONG
        }
        if (value is Double) {
          return DOUBLE
        }
        if (value is Int) {
          return INTEGER
        }
        if (value is Float) {
          return FLOAT
        }
        if (value is Short) {
          return SHORT
        }
        if (value is Byte) {
          return BYTE
        }
        if (value is BigDecimal) {
          return BIG_DECIMAL
        }
        throw IllegalArgumentException("Number class '" + value.javaClass.name + "' is not supported")
      }
    }
  }

  interface OnRangeSeekBarChangeListener<T> {

    fun onRangeSeekBarValuesChanged(bar: RangeSeekBar<*>, minValue: T, maxValue: T)
  }

  companion object {
    const val DEFAULT_MINIMUM = 0
    const val DEFAULT_MAXIMUM = 100
    const val HEIGHT_IN_DP = 30
    const val TEXT_LATERAL_PADDING_IN_DP = 3
    const val INVALID_POINTER_ID = 255

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    const val ACTION_POINTER_INDEX_MASK = 0x0000ff00
    const val ACTION_POINTER_INDEX_SHIFT = 8

    private const val INITIAL_PADDING_IN_DP = 8
    private const val DEFAULT_TEXT_SIZE_IN_DP = 9
    private const val DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 8
    private const val DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 0
    private const val LINE_HEIGHT_IN_DP = 2
  }
}
