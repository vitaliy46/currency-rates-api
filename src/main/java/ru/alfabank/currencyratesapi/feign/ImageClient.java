package ru.alfabank.currencyratesapi.feign;

import feign.RequestLine;

public interface ImageClient {

    @RequestLine("GET")
    byte[] getResponse();
}
