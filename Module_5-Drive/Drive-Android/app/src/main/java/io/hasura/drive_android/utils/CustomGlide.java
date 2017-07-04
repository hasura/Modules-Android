package io.hasura.drive_android.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by jaison on 07/04/17.
 */

public class CustomGlide {

    Context context;
    ImageView imageView;
    GlideUrl glideUrl;
    ResponseListener responseListener;
    Integer placeholder;

    public static CustomGlide with(Context context) {
        CustomGlide customGlide = new CustomGlide();
        customGlide.context = context;
        return customGlide;
    }

    public CustomGlide load(String url) {
        GlideUrl glideUrl = new GlideUrl(url,
                new LazyHeaders.Builder().addHeader("Authorization", "Bearer " + io.hasura.sdk.Hasura.getClient().getUser().getAuthToken()).build());
        this.glideUrl = glideUrl;
        return this;
    }

    public CustomGlide addListener(ResponseListener listener) {
        this.responseListener = listener;
        return this;
    }

    public CustomGlide placeholder(Integer placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;
        DrawableRequestBuilder builder = Glide.with(context)
                .load(glideUrl)
                .centerCrop()
                .crossFade()
                .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.i("Glide", "Failed");
                        e.printStackTrace();
                        if (responseListener != null) {
                            responseListener.onException(e, model, target, isFirstResource);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.i("Glide", "Successful");
                        if (responseListener != null) {
                            responseListener.onResourceReady(resource, model, target, isFromMemoryCache, isFirstResource);
                        }
                        return false;
                    }
                });
        if (placeholder != null) {
            builder.placeholder(placeholder);
        }
        builder.into(imageView);
    }

    public interface ResponseListener {
        void onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource);

        void onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource);
    }
}
