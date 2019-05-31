package com.example.ukeje.countrypedia.web.helper;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-05-24 14:04
 */
public interface ApiResponseListener<S, E> {

    void onApiSuccessful(S successResponse);

    void onApiFailed(@Nullable E errorResponse);

    void onNetworkFailure();
}
