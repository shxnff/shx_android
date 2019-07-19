package com.shx.base.okHttp;

public interface IRespondParse {
    public <T> T parse(Class<T> cls, String json);
}
