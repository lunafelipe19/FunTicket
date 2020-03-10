package com.famtechnology.funticket.util;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

public class GliderHelper {

    public static void loadImageFromAssets(Context context, String imagePath, ImageView imageView) {
        GlideApp.with(context)
                .load(Uri.parse("file:///android_asset/" + imagePath + ".png"))
                .into(imageView);
    }

    public static void setImage(Context context, @DrawableRes int drawable, ImageView imageView) {
        GlideApp
                .with(context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .into(imageView);
    }

    public static void setImageWithoutCache(Context context, @DrawableRes int drawable, ImageView imageView) {
        GlideApp
                .with(context)
                .load(drawable)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .into(imageView);
    }

    public static void setImageWithoutcenter(Context context, @DrawableRes int drawable, ImageView imageView) {
        GlideApp
                .with(context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);
    }

    public static void setImage(Context context, String path, ImageView imageView) {
        GlideApp
                .with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .into(imageView);
    }

    public static void setImage(Context context, String path, View loading, ImageView imageView) {
        GlideApp
                .with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .listener(new ImageLoaderListener(loading))
                .into(imageView);
    }
}
