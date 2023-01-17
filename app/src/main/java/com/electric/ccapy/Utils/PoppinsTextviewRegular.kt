package com.electric.ccapy.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PoppinsTextviewRegular (context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    /**
     * The init block runs every time the class is instantiated.
     */
    init {
        // Call the function to apply the font to the components.
        applyFont()
    }
    /**
     * Applies a font to a TextView.
     */
    private fun applyFont() {
        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Poppins-Regular.ttf")
        setTypeface(typeface)
    }
}