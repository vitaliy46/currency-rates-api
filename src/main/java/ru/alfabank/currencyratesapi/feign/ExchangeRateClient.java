package ru.alfabank.currencyratesapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "exchange-rates", url = "${openExchangeRates.url}")
public interface ExchangeRateClient {

    @RequestMapping(method = RequestMethod.GET,
            value = "${openExchangeRates.latest}?app_id=${openExchangeRates.appId}&base=${openExchangeRates.baseCurrency}&symbols={currency}")
    String fetchCurrentRate(@RequestParam("currency") String currency);

    @RequestMapping(method = RequestMethod.GET,
            value = "${openExchangeRates.historical}/{yesterday}.json?app_id=${openExchangeRates.appId}&base=${openExchangeRates.baseCurrency}&symbols={currency}")
    String fetchYesterdayRate(@PathVariable("yesterday") String yesterday, @RequestParam("currency") String currency);
}
