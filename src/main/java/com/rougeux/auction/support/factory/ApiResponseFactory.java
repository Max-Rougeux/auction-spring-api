package com.rougeux.auction.support.factory;

import com.rougeux.auction.support.I18nHelper;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.web.api.Slice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

    private final I18nHelper localeHelper;

    public <T> ApiResponse<T> success(String code, String messageKey) {
        return  ApiResponse.<T>builder()
                .code(code)
                .message(localeHelper.i18N(messageKey))
                .build();
    }

    public <T> ApiResponse<T> success(String code, String messageKey, T data) {
        return  ApiResponse.<T>builder()
                .code(code)
                .message(localeHelper.i18N(messageKey))
                .data(data)
                .build();    }

    public <T> ApiResponse<T> success(String code, String messageKey, Slice<T> slice) {
        return  ApiResponse.<T>builder()
                .code(code)
                .message(localeHelper.i18N(messageKey))
                .data(slice.data())
                .meta(slice.meta())
                .build();
    }

    public <T> ApiResponse<T> error(String code, String messageKey) {
        return  ApiResponse.<T>builder()
                .code(code)
                .message(localeHelper.i18N(messageKey))
                .build();
    }
}