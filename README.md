# Долой пробелы

## Задача
Программа, которая удаляет лишние пробелы в введенной пользователем строке. Решение включает в себя клиент и сервер. Сервер умеет удалять из любых строк все пробельные символы. Клиент должен "продемонстрировать" функциональность сервера, используя его.

### Работа программы
1. Клиент бесконечно просит пользователя вводить строки с пробелами
2. Каждая строка передается на сервер
3. Сервер читает все, что ему передали, удаляет пробельные символы, и результат отправляет обратно
4. Клиент отображает результат
5. Если пользователь вводит end, клиент завершается

### Комментарий
Использован Non-Blocking IO