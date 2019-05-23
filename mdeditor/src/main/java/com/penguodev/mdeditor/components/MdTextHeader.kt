package com.penguodev.mdeditor.components

import android.graphics.Typeface

enum class MdTextHeader(val textSizeSP: Int, val textStyle: Int) {
    NORMAL(16, Typeface.NORMAL),
    H1(28, Typeface.BOLD),
    H2(24, Typeface.BOLD),
    H3(21, Typeface.BOLD),
    H4(21, Typeface.NORMAL),
    H5(18, Typeface.BOLD),
    H6(18, Typeface.NORMAL)
}