package com.qixiu.lejia.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Long on 2018/4/24
 */
class GsonResponseBodyConverterWrapper<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverterWrapper(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String body = value.string();
        ResponseStatus responseStatus = gson.fromJson(body, ResponseStatus.class);
        if (!responseStatus.isSuccess()) {
            value.close();
            throw ApiException.of(responseStatus.getMessage());
        }
        try {
            return adapter.fromJson(body);
        } finally {
            value.close();
        }
    }

}
