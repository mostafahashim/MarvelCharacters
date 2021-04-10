package com.marvel.characters

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.marvel.characters.util.Preferences
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class CustomViewBindings {
    companion object {
        @BindingAdapter("htmlText")
        @JvmStatic
        fun setHtmlTextValue(textView: TextView, htmlText: String?) {
            if (htmlText == null) return
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(htmlText)
            }
            textView.text = result
        }

        @BindingAdapter("adapter")
        @JvmStatic
        fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
            recyclerView.adapter = adapter
        }

        @BindingAdapter("imageUrlSquare")
        @JvmStatic
        fun loadImageUrlSquare(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrl) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrl)

                    Glide.with(imageView).load(imageUrl)
                        .apply(
                            RequestOptions().placeholder(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                                .error(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent).diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                )
                        )
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlRect")
        @JvmStatic
        fun loadImageUrlRect(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrl) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrl)

                    Glide.with(imageView).load(imageUrl)
                        .apply(
                            RequestOptions().placeholder(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                                .error(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                                .diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                )
                        )
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlRoundTopRect")
        @JvmStatic
        fun loadImageUrlRoundTopRect(imageView: ImageView, imageUrlRoundTopRect: String?) {
            if (imageUrlRoundTopRect != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrlRoundTopRect) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrlRoundTopRect)

                    var cornerType = RoundedCornersTransformation.CornerType.TOP
                    val transformation = MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(
                            20, 0,
                            cornerType
                        )
                    )
                    val requestOptions = RequestOptions.bitmapTransform(transformation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                    val thumbnail: RequestBuilder<Drawable> = Glide.with(imageView)
                        .load(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                        .apply(requestOptions)

                    Glide.with(imageView).load(imageUrlRoundTopRect)
                        .apply(requestOptions).thumbnail(thumbnail)
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlRoundRect")
        @JvmStatic
        fun loadImageUrlRoundRect(imageView: ImageView, imageUrlRoundTopRect: String?) {
            if (imageUrlRoundTopRect != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrlRoundTopRect) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrlRoundTopRect)

                    var cornerType = RoundedCornersTransformation.CornerType.ALL
                    val transformation = MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(
                            20, 0,
                            cornerType
                        )
                    )
                    val requestOptions = RequestOptions.bitmapTransform(transformation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                    val thumbnail: RequestBuilder<Drawable> = Glide.with(imageView)
                        .load(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                        .apply(requestOptions)

                    Glide.with(imageView).load(imageUrlRoundTopRect)
                        .apply(requestOptions).thumbnail(thumbnail)
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlBigRoundRect")
        @JvmStatic
        fun loadImageUrlBigRoundRect(imageView: ImageView, imageUrlRoundTopRect: String?) {
            if (imageUrlRoundTopRect != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrlRoundTopRect) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrlRoundTopRect)

                    var cornerType = RoundedCornersTransformation.CornerType.ALL
                    val transformation = MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(
                            50, 0,
                            cornerType
                        )
                    )
                    val requestOptions = RequestOptions.bitmapTransform(transformation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                    val thumbnail: RequestBuilder<Drawable> = Glide.with(imageView)
                        .load(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                        .apply(requestOptions)

                    Glide.with(imageView).load(imageUrlRoundTopRect)
                        .apply(requestOptions).thumbnail(thumbnail)
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlRoundStartRect")
        @JvmStatic
        fun loadImageUrlRoundStartRect(imageView: ImageView, imageUrlRoundTopRect: String?) {
            if (imageUrlRoundTopRect != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrlRoundTopRect) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrlRoundTopRect)

                    var cornerType = RoundedCornersTransformation.CornerType.LEFT
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        cornerType = RoundedCornersTransformation.CornerType.RIGHT
                    }
                    val transformation = MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(
                            20, 0,
                            cornerType
                        )
                    )
                    val requestOptions = RequestOptions.bitmapTransform(transformation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                    val thumbnail: RequestBuilder<Drawable> = Glide.with(imageView)
                        .load(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                        .apply(requestOptions)
                    Glide.with(imageView).load(imageUrlRoundTopRect)
                        .apply(requestOptions).thumbnail(thumbnail)
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("imageUrlCircle")
        @JvmStatic
        fun loadImageUrlCircle(imageView: ImageView, imageUrlRoundTopRect: String?) {
            if (imageUrlRoundTopRect != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrlRoundTopRect) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrlRoundTopRect)
                    val transformation = RequestOptions.circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                    val thumbnail: RequestBuilder<Drawable> = Glide.with(imageView)
                        .load(R.drawable.layout_bg_transparent_gray_selector_with_bg_transparent)
                        .apply(transformation)
                    Glide.with(imageView).load(imageUrlRoundTopRect)
                        .apply(transformation).thumbnail(thumbnail)
                        .into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("bind:layoutHeight")
        @JvmStatic
        fun setHeight(view: View, height: Double) {
            view.layoutParams.height = height.toInt()
            view.requestLayout()
        }

        @BindingAdapter("bind:layoutWidth")
        @JvmStatic
        fun setWidth(view: View, width: Double) {
            view.layoutParams.width = width.toInt()
            view.requestLayout()
        }

    }
}