package com.isaac.m17.util

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object GlideImageLoader {

    fun loadImage(url: String?, view: ImageView?) {
        loadImage(url, view, null)
    }

    private fun loadImage(
        resource: Any?,
        view: ImageView?,
        options: RequestOptions?
    ) {
        loadImage(
            resource,
            view!!,
            options,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.CENTER_INSIDE,
            0
        )
    }

    private fun loadImage(
        resource: Any?,
        view: ImageView,
        options: RequestOptions?,
        scaleType: ScaleType?,
        errscaleType: ScaleType?,
        maxWidth: Int
    ) {
        if (resource == null) return
        Log.d("Image", resource.toString())
        val context = view.context
        if (context is Activity) {
            if (context.isDestroyed || context.isFinishing) return
        }
        val builder: RequestBuilder<*> = Glide.with(view)
            .load(resource)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = errscaleType
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    view.scaleType = scaleType
                    if (maxWidth > 0) {
                        if (resource.intrinsicWidth < maxWidth) {
                            val gain =
                                maxWidth.toFloat() / resource.intrinsicWidth
                            val _h = (resource.intrinsicHeight * gain).toInt()
                            view.layoutParams.height = _h
                            view.layoutParams.width = maxWidth
                        } else {
                            val gain =
                                resource.intrinsicWidth.toFloat() / maxWidth
                            val _h = (resource.intrinsicHeight / gain).toInt()
                            view.layoutParams.height = _h
                            view.layoutParams.width = maxWidth
                        }
                    }
                    return false
                }
            })
        view.scaleType = errscaleType
        if (options != null) {
            builder.apply(options)
        }
        builder
            .into(view)
    }
}