# Сервис для работы с курсами валют

### Описание

Сервис обращается к сервису курсов валют, и отображает gif:

если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомный gif отсюда:

https://giphy.com/search/rich

если ниже - отсюда:

https://giphy.com/search/broke

### Требования

Java Development Kit 11

API токены для работы с сервисами openexchangerates.org и giphy.com

### Настройки application.yml

**openExchangeRates**:

appId – токен для работы с openexchangerates.org

baseCurrency – базовая валюта, которая сравнивается с другими валютами

**giphy**:

apiKey – токен для работы с giphy.com

upTag – ключевое слово, по которому выдается рандомный GIF если текущий курс выше вчерашнего

downTag - ключевое слово, по которому выдается рандомный GIF если текущий курс ниже вчерашнего

rating – фильтрует выдачу GIF по контенту, подробнее: [https://developers.giphy.com/docs/optional-settings/#rating](https://developers.giphy.com/docs/optional-settings/#rating)

### Сборка

Выполните команду в папке проекта:

> gradlew build

в папке **[PROJECT_FOLDER]/build/libs** будет сгенерирован файл currency-rates-api-1.0.0.jar

### Запуск

Выполните команду:

> java -jar currency-rates-api-1.0.0.jar

### API<span style="color:red">*</span>

После запуска сервис доступен по адресу:

https://localhost:8083/api/v1/rates/{currency}

где {currency} – валюта по отношению к которой запрашивается курс базовой валюты, пример:

[https://localhost:8083/api/v1/rates/RUB](https://localhost:8083/api/v1/rates/RUB)

Список поддерживаемых валют:

[https://docs.openexchangerates.org/docs/supported-currencies](https://docs.openexchangerates.org/docs/supported-currencies)

<span style="color:red">__*__</span> Приложение использует самоподписанный сертификат, для работы в браузере необходимо выбрать «Принять риск и продолжить», в Postman в настройках SETTINGS -&gt; General отключить параметр SSL certificate verification