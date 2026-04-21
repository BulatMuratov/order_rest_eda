package com.bulka.mapper;

public interface Mapper<T, F> {
    default F toDto(T t){
        throw new RuntimeException("method not supported");
    }
    default T toEntity(F f){
        throw new RuntimeException("method not supported");
    }
}
