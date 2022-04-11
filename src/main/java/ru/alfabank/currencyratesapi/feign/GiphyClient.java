package ru.alfabank.currencyratesapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "giphy", url = "${giphy.url}")
public interface GiphyClient {

    @RequestMapping(method = RequestMethod.GET,
            value = "?api_key=${giphy.apiKey}&tag=${giphy.upTag}&rating=${giphy.rating}")
    String fetchUpImage();

    @RequestMapping(method = RequestMethod.GET,
            value = "?api_key=${giphy.apiKey}&tag=${giphy.downTag}&rating=${giphy.rating}")
    String fetchDownImage();
}
