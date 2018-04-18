package com.android.anmol.githubapi.utility;

import android.support.annotation.Nullable;

/**
 * Created by anmolsehgal on 27-03-2018.
 * <p>
 * Utility methods to handle error scenarios.
 */
public class Utils {

    /**
     * Check if the reference is null.
     * If null throw exception.
     *
     * @param reference    Reference to check.
     * @param errorMessage Error Message to show in case the reference is null.
     * @param <T>          Reference Type.
     * @return Reference if not null.
     */
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    /**
     * Check if the reference is null.
     * If null throw exception.
     *
     * @param reference Reference to check.
     * @param <T>       Reference Type.
     * @return Reference if not null.
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

}
