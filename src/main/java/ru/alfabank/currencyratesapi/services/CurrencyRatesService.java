package ru.alfabank.currencyratesapi.services;

import feign.Feign;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import ru.alfabank.currencyratesapi.exceptions.BadRequestException;
import ru.alfabank.currencyratesapi.feign.ExchangeRateClient;
import ru.alfabank.currencyratesapi.feign.GiphyClient;
import ru.alfabank.currencyratesapi.feign.ImageClient;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CurrencyRatesService {

    private final ExchangeRateClient exchangeRateClient;

    private final GiphyClient giphyClient;

    public CurrencyRatesService(ExchangeRateClient exchangeRateClient, GiphyClient giphyClient) {
        this.exchangeRateClient = exchangeRateClient;
        this.giphyClient = giphyClient;
    }

    public byte[] getRate(String currency) throws BadRequestException {

        // Проверяем входные данные
        Pattern pattern = Pattern.compile("[a-zA-Z]{3}");
        Matcher matcher = pattern.matcher(currency);
        if(!matcher.matches()) throw new BadRequestException("Код валюты должен состоять из 3 латинских букв");

        String normalizedCurrency = currency.toUpperCase();

        // Получаем текущий курс
        JSONObject currentResponse = new JSONObject(exchangeRateClient.fetchCurrentRate(normalizedCurrency));
        double currentRate = currentResponse.getJSONObject("rates").getDouble(normalizedCurrency);

        String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
                .format(Instant.now().minus(1, ChronoUnit.DAYS));

        // Получаем вчерашний курс
        JSONObject yesterdayResponse = new JSONObject(exchangeRateClient.fetchYesterdayRate(yesterday, normalizedCurrency));
        double yesterdayRate = yesterdayResponse.getJSONObject("rates").getDouble(normalizedCurrency);

        // Загружаем GIF в зависимости от курса
        String giphyResponse = currentRate >= yesterdayRate ? giphyClient.fetchUpImage() : giphyClient.fetchDownImage();
        JSONObject giphyJson = new JSONObject(giphyResponse);
        String giphyLink = giphyJson
                .getJSONObject("data")
                .getJSONObject("images")
                .getJSONObject("original")
                .getString("url");

        return Feign.builder().target(ImageClient.class, giphyLink).getResponse();
    }
}
