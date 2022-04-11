package ru.alfabank.currencyratesapi.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.currencyratesapi.exceptions.BadRequestException;
import ru.alfabank.currencyratesapi.services.CurrencyRatesService;

@RestController
@RequestMapping("/api/${version}")
public class CurrencyRatesController {

    private final CurrencyRatesService currencyRatesService;

    public CurrencyRatesController(CurrencyRatesService currencyRatesService) {
        this.currencyRatesService = currencyRatesService;
    }

    @GetMapping(value = "/rates/{currency}", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getRate(@PathVariable("currency") String currency) throws BadRequestException {
        return currencyRatesService.getRate(currency);
    }
}
