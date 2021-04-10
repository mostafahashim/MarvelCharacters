package com.marvel.characters.posterUtil

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.marvel.characters.R
import com.marvel.characters.model.CharacterModel
import kotlinx.android.synthetic.main.view_poster_overlay.view.*

class PosterOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_poster_overlay, this)
        setBackgroundColor(Color.TRANSPARENT)
    }

    var onShareClick: (CharacterModel) -> Unit = {}
    fun update(poster: CharacterModel) {
        posterOverlayShareButton.setOnClickListener { onShareClick(poster) }
    }
}