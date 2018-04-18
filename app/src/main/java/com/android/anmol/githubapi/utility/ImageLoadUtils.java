package com.android.anmol.githubapi.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.anmol.githubapi.R;
import com.android.anmol.githubapi.utility.ResUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

public class ImageLoadUtils {
    /**
     * Load the Image from the ImageUrl and set in onto the supplied ImageView.
     * Incase the ProgressBar is provided, show the progressbar until the Image is fetched.
     *
     * @param imageUrl    URL to fetch the image from.
     * @param imageView   ImageView to set te image on.
     * @param progressBar Progress Bar to show until the image is successfully downloaded.
     */
    public static void loadImage(String imageUrl,
                                 @Nullable final ImageView imageView,
                                 @Nullable final ProgressBar progressBar) {
        if (imageView == null) {
            return;
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        final Context context = imageView.getContext();
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .dontAnimate()

                // Listener to listen to the image download status.
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        // Unable to get image.
                        imageView.setImageDrawable(ResUtils.getDrawable(context, R.drawable.ic_question_mark));

                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }
}