# Menedżer Haseł Online - MHO

## Spis treści
- [Menedżer Haseł Online - MHO](#menedżer-haseł-online---mho)
  - [Spis treści](#spis-treści)
  - [Opis](#opis)
  - [Diagramy](#diagramy)
    - [Diagram wzorca projektowego UML](#diagram-wzorca-projektowego-uml)
    - [Diagram klas UML](#diagram-klas-uml)
    - [Diagram użycia (Use-Case Diagram)](#diagram-użycia-use-case-diagram)
  - [Obsługa narzędzia](#obs-uga-narz-dzia)
    - [Uruchomienie programu](#uruchomienie-programu)
    - [Logowanie](#logowanie)
    - [Rejestracja](#rejestracja)
    - [Token uwierzytelniający](#token-uwierzytelniaj-cy)
    - [Interfejs zalogowanego użytkownika](#interfejs-zalogowanego-u-ytkownika)
    - [Wyszukiwanie hasła](#wyszukiwanie-has-a)
    - [Rejestracja nowej pozycji](#rejestracja-nowej-pozycji)
    - [Edycja pozycji](#edycja-pozycji)
    - [Edycja haseł z wyświetlaniem daty ostatniej aktualizacji](#edycja-hase--z-wy-wietlaniem-daty-ostatniej-aktualizacji)
    - [Eksport hasła do schowka](#eksport-has-a-do-schowka)
    - [Zduplikowane hasła](#zduplikowane-has-a)
    - [Wygaśnięcie hasła](#wyga-ni-cie-has-a)
    - [Powiadomienie e-mail o wygaśnięciu hasła](#powiadomienie-e-mail-o-wyga-ni-ciu-has-a)
  - [Alerty](#alerty)
    - [Error](#error)
    - [Success](#success)
  - [Czynności administracyjne](#czynno-ci-administracyjne)
    - [Zarządzanie bazą danych](#zarz-dzanie-baz--danych)
  - [Ocena zgodności z założeniami oraz wkład osobowy](#ocena-zgodnosci-z-zalozenaimi-oraz-wklad-osobowy)

## Opis

Aplikacja "web-based" stworzona na podstawie [Spring Boot Framework](https://spring.io/) oraz [H2 Database Engine](http://h2database.com/html/main.html). Pozwala na przetrzymywanie nieograniczonej ilości haseł do popularnych stron internetowych, szyfrowanych w bazie danych. Program jest skierowany do osób, które chciałyby zapisywać wszystkie swoje dane dostępowe w jednym miejscu. Działa w systemach opracyjnych: Windows, MacOS, Linux.

## Diagramy

### Diagram wzorca projektowego UML
![diagram_wzorca_uml](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fsomospnt.com%2Fimages%2Fblog%2Farticulos%2F159-node-mvc%2Fmodel-view-controller-light-blue.png&f=1&nofb=1)

### Diagram klas UML
![diagram_klas_uml](https://cdn.discordapp.com/attachments/1030523826225819700/1064317780125560872/uml.jpg)

### Diagram użycia (Use-Case Diagram)
![diagram_uzycia](https://media.discordapp.net/attachments/1030523826225819700/1064308259588157500/image.png?width=894&height=683)


## Obsługa narzędzia

### Uruchomienie programu
Po uruchomieniu programu strona będzie dostępna pod adresem IP oraz portem 8080 aktualnego komputera/serwera. Po wygenerowaniu pliku .jar, można aplikację uruchomić przez:

Klikacjąc dwókrotnie na password-manager.jar lub:
> java -jar password-manager.jar

Aby dostać się do strony logowania należy wpisać adres URL, np.:
```
  https://localhost:8080
```

### Logowanie
Po załadowaniu strony wyświetli nam się strona logowania. Jeżeli mamy już konto to wystarczy wpisać swój Login (Username) oraz Hasło (Password).
![strona_logowania](https://i.imgur.com/77d1Adi.png)

### Rejestracja
Jednak w sytuacji gdy nie posiadamy jeszcze konta, należy zarejestrować się. Zostaniemy przeniesieni do strony z rejestracją po kliknięciu **Register here**.
![strona_rejestracji](https://i.imgur.com/Uab3dj8.png)

### Token uwierzytelniający
Po rejestracji na maila, na które zostało zarejestrowane konto, wysłany zostanie Token, który należy wpisać jako uwierzetylnienie prawdziwości danych.
![token](https://i.imgur.com/QoryL9O.png)

### Interfejs zalogowanego użytkownika
Po udanej próbie zalogowania, aplikacja przeniesie nas do głównego strony zarządzania naszymi zapisanymi hasłami. Pozycje są przedstawione w formie tabeli z takimi danymi jak:
 - Strona (adres url)
 - Login
 - Hasło
 - Ostatnia zmiana hasła
 - Przyciski do usuwania/edycji pozycji

Interfejs wygląda następująco:
![strona_po_zalogowaniu](https://i.imgur.com/arXcfoB.png)

### Wyszukiwanie hasła
Pod komunikatem o udanym zalogowaniu, mamy pasek wyszukiwania. Tutaj można wyszukać strony url, które nas interesują. Funkcjonalność działa na bazie wyrażeń regularnych. W przypadku kiedy wprowadzimy pierwsze litery interesującej nas rekordu, zostanie ono poprawnie odnaleziona:
![wyszukiwanie](https://i.imgur.com/uI4BQkR.png)

### Rejestracja nowej pozycji
Przy chęci zarejestrowania nowego hasła, użytkownik zostanie przeniesiony na podstronę, gdzie może wprowadzić swoje nowe dane do bazy danych.
![rejestracja_hasla](https://i.imgur.com/xJF7WGC.png)

### Edycja pozycji
Po przejściu do edycji naszej wygranej pozycji, ukażę nam się strona specjalnie dedykowana pod zmianę adresu e-mail, url strony lub samego hasła zapisanego w bazie danych. Operację zapisania naszej edycji akceptujemy przyciskiem **Save**.
![edycja_hasla](https://i.imgur.com/29GNAt7.png)

### Edycja haseł z wyświetlaniem daty ostatniej aktualizacji
Przy każdym wpisie pokazana jest informacja o dacie ostatniej aktualizacji hasła.

### Eksport hasła do schowka
Przy każdym zapisanym haśle znajduje się przycisk __COPY TO CLIPBOARD__, który po wciśnięciu kopiuje hasło do schowka.
![copy_to_clipboard](https://i.imgur.com/UITfimI.png)

### Zduplikowane hasła
Jeżeli do różnych wpisów zostanie użyte takie samo hasło, aplikacja przeniesie je do zakładki __Duplicated passwords__
![duplicated_passwords](https://i.imgur.com/75JbGuH.png)

### Wygaśnięcie hasła
Po upływie 3 miesięcy od ostatniej aktualizacji hasła zostanie ono przeniesione do tabeli z hasłami, które wygasły.
![expired_passwords](https://i.imgur.com/Bui8PM3.png)

### Powiadomienie e-mail o wygaśnięciu hasła
Po upływie 3 miesięcy od ostatniej aktualizacji hasła zostanie automatycznie wysłane powiadomienie e-mail do użytkownika o konieczności aktualizacji hasła ze wzgledów bezpieczeństwa.<br>
![email_notification](https://i.imgur.com/g1j9qPV.png)

## Alerty

### Error
Przy nieudanej próbie zalogowania lub rejestracji wyświetlony zostanie alert z informacją o wystąpieniu błędu.
![error_alert](https://i.imgur.com/8eTz8iA.png)

### Success
Przy udanym wylogowaniu wyświetlony zostanie odpowiedni komunikat dla użytkownika.<br>
![success_alert](https://i.imgur.com/kwjz7UV.png)

## Czynności administracyjne

### Zarządzanie bazą danych
Dostęp do bazy danych jest dostępny nawet z poziomu przeglądarki pod adresem:

```sh
  localhost:8080/h2
```

![baza_logowanie](https://i.imgur.com/OFBqDjp.png)

Aplikacja korzysta ze sterownika [JDBC](https://pl.wikipedia.org/wiki/Java_DataBase_Connectivity). W ten sposób komunikuję się automatycznie z bazą danych i wykonuje takie operacje jak **SELECT**, **UPDATE**, **INSERT INTO**, **DELETE**.
---
## Ocena zgodności z założeniami oraz wkład osobowy
![wklad](https://cdn.discordapp.com/attachments/1030523826225819700/1064314577870934036/wklad.png)
