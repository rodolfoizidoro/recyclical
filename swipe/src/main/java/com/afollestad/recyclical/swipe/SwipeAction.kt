/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afollestad.recyclical.swipe

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.afollestad.recyclical.RecyclicalMarker

/**
 * Return true to remove the item from the list, false to push it back
 * into place.
 */
typealias SwipedCallback = (index: Int, item: Any) -> Boolean

/**
 * Represents what position the swipe callback represents. Left means a right-to-left swipe,
 * right means left-to-right swipe. A long swipe means you swipe further vs a short swipe.
 */
enum class SwipeLocation {
  LEFT,
  RIGHT,
  LEFT_LONG,
  RIGHT_LONG
}

@RecyclicalMarker
class SwipeAction(private val context: Context) {
  internal var iconDrawable: Drawable? = null
  internal var backgroundDrawable: ColorDrawable? = null
  internal var callback: SwipedCallback? = null

  internal var text: String? = null
  internal var textPaint: TextPaint? = null
  private var textBounds: Rect? = null

  fun icon(
    @DrawableRes res: Int? = null,
    literal: Drawable? = null
  ): SwipeAction {
    check(res != null || literal != null) {
      "Must provide a res or literal value to icon()"
    }
    iconDrawable = literal ?: ContextCompat.getDrawable(context, res!!)
    return this
  }

  fun color(
    @ColorRes res: Int? = null,
    @ColorInt literal: Int? = null
  ): SwipeAction {
    check(res != null || literal != null) {
      "Must provide a res or literal value to color()"
    }
    val colorValue = literal ?: ContextCompat.getColor(context, res!!)
    backgroundDrawable = ColorDrawable(colorValue)
    return this
  }

  fun text(
    @StringRes res: Int? = null,
    literal: String? = null,
    @ColorRes color: Int = android.R.color.white,
    size: Int = R.dimen.swipe_default_text_size,
    typeface: Typeface = Typeface.DEFAULT
  ) {
    check(res != null || literal != null) {
      "Must provide a res or literal value to text()"
    }
    text = literal ?: context.getString(res!!)
    textPaint = TextPaint().apply {
      this.isAntiAlias = true
      this.color = ContextCompat.getColor(context, color)
      this.typeface = typeface
      this.textSize = context.resources.getDimension(size)
    }
  }

  fun callback(block: SwipedCallback): SwipeAction {
    this.callback = block
    return this
  }

  internal fun getTextWidth(): Int = getTextBounds().width()

  internal fun getTextHeight(): Int = getTextBounds().height()

  private fun getTextBounds(): Rect {
    if (textBounds == null) {
      textBounds = Rect()
      textPaint!!.getTextBounds(text, 0, text!!.length, textBounds!!)
    }
    return textBounds!!
  }
}
