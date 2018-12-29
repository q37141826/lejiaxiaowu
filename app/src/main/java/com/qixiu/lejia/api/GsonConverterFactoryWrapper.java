package com.qixiu.lejia.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Long on 2018/4/24 0024
 */
public class GsonConverterFactoryWrapper extends Converter.Factory {

    public static GsonConverterFactoryWrapper create() {
        return create(new Gson());
    }

    public static GsonConverterFactoryWrapper create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new GsonConverterFactoryWrapper(gson);
    }

    private final Gson gson;

    private GsonConverterFactoryWrapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverterWrapper<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverterWrapper<>(gson, adapter);
    }

}
