package com.hirocode.story.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.hirocode.story.R

class PasswordEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        addTextChangedListener(onTextChanged = { p0, _, _, _ ->
            if (p0 != null) {
                if (p0.isNotEmpty() && p0.length < 6)
                    error = context.getString(R.string.error_password)
            } else {
                error = null
            }
        })
    }

}