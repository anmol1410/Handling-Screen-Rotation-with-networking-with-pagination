package com.android.anmol.githubapi;

import android.support.annotation.Nullable;

/**
 * Converter to convert One Model into Another.
 *
 * @param <S> Source Model.
 * @param <D> Destination Model.
 */
public interface ModelConverter<S, D> {

    @Nullable
    D convert(@Nullable S source);
}