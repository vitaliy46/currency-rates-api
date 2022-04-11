package ru.alfabank.currencyratesapi.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.alfabank.currencyratesapi.exceptions.BadRequestException;
import ru.alfabank.currencyratesapi.feign.ExchangeRateClient;
import ru.alfabank.currencyratesapi.feign.GiphyClient;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CurrencyRatesServiceTest {

    @Autowired
    private CurrencyRatesService currencyRatesService;

    @MockBean
    private ExchangeRateClient exchangeRateClient;

    @MockBean
    private GiphyClient giphyClient;

    WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {

        Mockito.when(giphyClient.fetchUpImage())
                .thenReturn("{\"data\": {\"images\": {\"original\": {\"url\": \"http://localhost:8888/up\"}}}}");
        Mockito.when(giphyClient.fetchDownImage())
                .thenReturn("{\"data\": {\"images\": {\"original\": {\"url\": \"http://localhost:8888/down\"}}}}");

        wireMockServer = new WireMockServer(8888);

        wireMockServer.stubFor(get("/up")
                        .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("up")));
        wireMockServer.stubFor(get("/down")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("down")));

        wireMockServer.start();
    }

    @Test
    void rateUp() throws BadRequestException {

        Mockito.when(exchangeRateClient.fetchCurrentRate(any())).thenReturn("{\"rates\": {\"RUB\": 80}}");
        Mockito.when(exchangeRateClient.fetchYesterdayRate(any(), any())).thenReturn("{\"rates\": {\"RUB\": 70}}");

        assertEquals("up", new String(currencyRatesService.getRate("RUB"), StandardCharsets.UTF_8));
    }

    @Test
    void rateDown() throws BadRequestException {

        Mockito.when(exchangeRateClient.fetchCurrentRate(any())).thenReturn("{\"rates\": {\"RUB\": 70}}");
        Mockito.when(exchangeRateClient.fetchYesterdayRate(any(), any())).thenReturn("{\"rates\": {\"RUB\": 80}}");

        assertEquals("down", new String(currencyRatesService.getRate("RUB"), StandardCharsets.UTF_8));
    }

    @Test
    void validationError() {
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> currencyRatesService.getRate("RU"));
        assertEquals("Код валюты должен состоять из 3 латинских букв", thrown.getMessage());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}