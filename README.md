# Projekt Wypożyczalni Samochodów
# Opis Projektu
Projekt ten jest aplikacją wypożyczalni samochodów zrealizowaną w języku Java, która pozwala na zarządzanie relacyjną bazą danych PostgreSQL za pomocą graficznego interfejsu użytkownika (GUI). Aplikacja została stworzona przy użyciu frameworków JavaFX (do GUI) oraz Hibernate (do komunikacji z bazą danych). Celem projektu jest umożliwienie studentom nauki tworzenia oprogramowania z GUI oraz obsługi relacyjnych baz danych.

## Funkcjonalności
Aplikacja wypożyczalni samochodów umożliwia:

* Dodawanie rekordów do poszczególnych tabel bazy danych
* Usuwanie wybranych rekordów z tabel
* Odczytywanie i operowanie na danych z tabel
* Edytowanie wybranych rekordów w bazie danych
# Technologie
* Język programowania: Java 21
* Framework do GUI: JavaFX
* Framework do ORM: Hibernate
* Baza danych: PostgreSQL
## Struktura Bazy Danych
Baza danych PostgreSQL składa się z czterech głównych tabel w schemacie `car_rental`:

1. **clients**:
   ```sql
   CREATE TABLE car_rental.clients (
       IDClient SERIAL PRIMARY KEY,
       FirstName VARCHAR(50),
       LastName VARCHAR(50),
       Email VARCHAR(100)
   );
   ```
   
2. **engine**
    ```sql
    CREATE TABLE car_rental.engine (
        IDEngine SERIAL PRIMARY KEY,
        Name VARCHAR(50),
     Power INT,
        FuelType VARCHAR(50)
    );
    ```
3. **cars**
    ```sql
    CREATE TABLE car_rental.cars (
        IDCar SERIAL PRIMARY KEY,
        Brand VARCHAR(50),
        Model VARCHAR(50),
        IDEngine INT,
        Price DECIMAL(9,2),
        FOREIGN KEY (IDEngine) REFERENCES car_rental.engine(IDEngine)
    );
    ```
4. **rented_car**
    ```sql
    CREATE TABLE car_rental.rented_car (
        IDRent SERIAL PRIMARY KEY,
        IDClient INT,
        IDCar INT,
        Rented_Date TIMESTAMP,
        Rented_From TIMESTAMP,
        Rented_Until TIMESTAMP,
        FOREIGN KEY (IDClient) REFERENCES car_rental.clients(IDClient),
        FOREIGN KEY (IDCar) REFERENCES car_rental.cars(IDCar)
    );
    ```
    ## Przykładowe dane
    ```sql
    INSERT INTO car_rental.clients (FirstName, LastName, Email) VALUES
    ('John', 'Smith', 'john@example.com'),
    ('Alice', 'Johnson', 'alice@example.com'),
    ('Emma', 'Davis', 'emma@example.com'),
    ('Michael', 'Brown', 'michael@example.com');
    
    INSERT INTO car_rental.engine (Name, Power, FuelType) VALUES
    ('V8', 400, 'Petrol'),
    ('V6', 300, 'Diesel'),
    ('I4', 150, 'Electric');
    
    INSERT INTO car_rental.cars (Brand, Model, IDEngine, Price) VALUES
    ('Toyota', 'Corolla', 3, 25000),
    ('Ford', 'Mustang', 1, 45000),
    ('BMW', 'X5', 2, 60000);
    
    INSERT INTO car_rental.rented_car (IDClient, IDCar, Rented_Date, Rented_From, Rented_Until) VALUES
    (1, 1, '2024-06-10', '2024-06-10', '2024-06-12'),
    (2, 2, '2024-06-11', '2024-06-11', '2024-06-15');
    
   ```
# Instalacja i Uruchomienie
1. **Klonowanie repozytorium:**

 ```bash
git clone https://github.com/hubertfus/car-rental-app.git
cd car-rental-app
```
2. **Konfiguracja bazy danych:**

* Utwórz bazę danych PostgreSQL i schemat car_rental zgodnie z dostarczonymi skryptami SQL.
* Zaimportuj dane z pliku schema.sql.

# Użycie
Po uruchomieniu aplikacji użytkownik zostanie powitany przez interfejs graficzny umożliwiający zarządzanie danymi w bazie. Za pomocą menu nawigacyjnego można:

* Dodawać nowe rekordy do tabel
* Usuwać istniejące rekordy
* Wyświetlać dane z tabel w * przejrzystej formie
* Edytować wybrane rekordy
# Wymagania
* Java 21
* Maven
* PostgreSQL
# Autorzy
Projekt został stworzony przez Huberta Fusiarza, Macieja Pintala,
Dawida Skibe, Marcina Wajde i Kamilą Węża w ramach projektu na studiach.
