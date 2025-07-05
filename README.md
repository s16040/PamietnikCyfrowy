# PamietnikCyfrowy

Prosta aplikacja w Kotlinie stworzona z użyciem Jetpack Compose. Umożliwia zapisywanie notatek wraz z lokalizacją, zdjęciem i nagraniem audio. Zapisane notatki można wyświetlać na mapie Google Maps.

## Najważniejsze funkcje
- przechowywanie notatek w bazie Room
- pobieranie bieżącej lokalizacji urządzenia (wymaga udzielenia uprawnienia)
- możliwość dodania zdjęcia i nagrania (przykładowe implementacje)
- lista notatek z opcją usuwania
- wyświetlanie notatek na mapie z markerami

## Budowanie projektu

Projekt wymaga zainstalowanego Android SDK. W pliku `local.properties` należy ustawić `sdk.dir` wskazujące na katalog SDK. Następnie można uruchomić kompilację poleceniem:

```bash
./gradlew assembleDebug
```

Testy uruchomimy poleceniem:

```bash
./gradlew test
```

Ze względu na ograniczenia środowiska uruchomieniowego w repozytorium testy mogą zakończyć się niepowodzeniem, jeśli SDK nie jest dostępne.

Do poprawnego działania map konieczny jest klucz API Google Maps. W pliku `local.properties` dodaj wpis `MAPS_API_KEY=TWÓJ_KLUCZ`.
