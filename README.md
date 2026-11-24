# Scenario Quality Checker (SQC)

## Opis projektu

Dla analityków dokumentujących wymagania funkcjonalne za pomocą scenariuszy nasza aplikacja **SQC** dostarczy informacji ilościowych oraz umożliwi wykrywanie problemów w wymaganiach funkcjonalnych zapisanych w postaci scenariuszy.

Aplikacja będzie dostępna poprzez GUI, a także jako zdalne API, dzięki czemu można ją zintegrować z istniejącymi narzędziami.

## Notacja scenariuszy

- Scenariusz zawiera nagłówek określający jego **tytuł** i **aktorów** (zewnętrznych oraz system).
- Scenariusz składa się z **kroków** (każdy krok zawiera tekst).
- Kroki mogą zawierać **pod-scenariusze** (dowolny poziom zagłębień).
- Kroki mogą się zaczynać od słów kluczowych:
  - `IF`
  - `ELSE`
  - `FOR EACH`

## Przykład scenariusza

**Tytuł:** Dodanie książki

**Aktorzy:** Bibliotekarz

**Aktor systemowy:** System

### Kroki scenariusza

1. Bibliotekarz wybiera opcję dodania nowej pozycji książkowej.
2. Wyświetla się formularz.
3. Bibliotekarz podaje dane książki.
4. **IF:** Bibliotekarz pragnie dodać egzemplarze książki
   1. Bibliotekarz wybiera opcję definiowania egzemplarzy.
   2. System prezentuje zdefiniowane egzemplarze.
   3. **FOR EACH** egzemplarz:
      1. Bibliotekarz wybiera opcję dodania egzemplarza.
      2. System prosi o podanie danych egzemplarza.
      3. Bibliotekarz podaje dane egzemplarza i zatwierdza.
      4. System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.
5. Bibliotekarz zatwierdza dodanie książki.
6. System informuje o poprawnym dodaniu książki.
