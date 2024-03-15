# Описание:

Этот проект представляет собой  пример REST API, созданный с использованием Java, Spring Framework, Spring Boot и Spring Security.

### Использованы следующие технологии:

- Java 17;
- Spring Boot версии 3;
- Spring Secutiry;
- JWT;
- Spring Data;
- Hibernate;
- JSON;

# API:
## AuthenticationController:
### 1. Регистрация пользователя:

Метод: POST\
URI: /api/v1/auth/register\
Запрос:Body: JSON объект с данными регистрации (RegisterRequest)

username: Имя пользователя\
password: Пароль\
email: Email\
firstName: Имя\
lastName: Фамилия

Ответ:\
201 Created: При успешной регистрации\
403 Forbidden: Если текущий пользователь не имеет прав администратора

Требуется аутентификация: Да, с ролью ADMIN
### 2. Аутентификация пользователя:

Метод: POST\
URI: /api/v1/auth/authenticate\
Запрос:\
Body: JSON объект с данными аутентификации (AuthenticationRequest)\
username: Имя пользователя\
password: Пароль

Ответ:\
200 OK: При успешной аутентификации, содержит JSON объект с данными пользователя (AuthenticationResponse)\
token: JWT токен\
401 Unauthorized: При ошибке аутентификации

Требуется аутентификация: Нет


## CollectionController:
### 1. Получение информации о сборе:

GET /api/v1/collections/{id}:\
Возвращает информацию о сборе по его ID.\
Доступно только администраторам.
### 2. Получение списка сборов:

POST /api/v1/collections/day:\
Возвращает список сборов за день.\
Доступно только администраторам.

POST /api/v1/collections/month:\
Возвращает список сборов за месяц.\
Доступно только администраторам.

POST /api/v1/collections/week:\
Возвращает список сборов за неделю.\
Доступно только администраторам.
### 3. Создание нового сбора:

POST /api/v1/collections/create:\
Создает новый сбор.\
Доступно только сотрудникам и администраторам.

## EmployeeController:
### 1. Получение информации о сотруднике:

GET /api/v1/employees/{id}:\
Возвращает информацию о сотруднике по его ID.\
Доступно только администраторам.
### 2. Получение списка всех сотрудников:

GET /api/v1/employees:\
Возвращает список всех сотрудников.\
Доступно только администраторам.
### 3. Обновление информации о сотруднике:

PUT /api/v1/employees/{id}:\
Обновляет информацию о сотруднике.\
Доступно только администраторам.
### 4. Удаление сотрудника:

DELETE /api/v1/employees/{id}:\
Удаляет сотрудника.\
Доступно только администраторам.


## ProductController:
### 1. Создание нового продукта:

POST /api/v1/products:\
Создает новый продукт.\
Доступно только администраторам.
### 2. Получение информации о продукте:

GET /api/v1/products/{name}:\
Возвращает информацию о продукте по его имени.\
Доступно только администраторам.
### 3. Получение списка всех продуктов:

GET /api/v1/products:\
Возвращает список всех продуктов.\
Доступно только администраторам.
### 4. Обновление информации о продукте:

PUT /api/v1/products/{name}:\
Обновляет информацию о продукте.\
Доступно только администраторам.
### 5. Удаление продукта:

DELETE /api/v1/products/{name}:\
Удаляет продукт.\
Доступно только администраторам.

# Примеры запросов

### Аутентификация пользователя на примере администратора

URL: /api/v1/auth/authenticate\
Метод: POST\
Тело запроса:
```json
{
"email" : "ADMIN_EMAIL",
"password" : "0000"
}
```
В ответ сервер передаст JSON файл содержащий JWT содержащий информацию об аутентификации:
```json
{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBRE1JTl9FTUFJTCIsImlhdCI6MTcxMDQ5MDA3NCwiZXhwIjoxNzEwNTA0NDc0fQ.oRtRFiJWMWkGF3Vm9E7V_n1_ES2DdE686JNpkwi7bKY"
}
```

### Регистрация сотрудника

URL: /api/v1/auth/register\
Метод: POST\
В заголовок: Authorization следует добавить Bearer JWT\
Тело запроса:
```json
{
  "firstName" : "Дмитрий",
  "lastName" : "Валяльщиков",
  "email" : "770vda@mail.ru",
  "password" : "0000"
}
```
В ответ сервер передаст код 200, если все успешно:\

### Регистрация продукта

URL: /api/v1/products\
Метод: POST\
В заголовок: Authorization следует добавить Bearer JWT\
Тело запроса:
```json
{
  "name" : "Хлопок",
  "measure" : "кг"
}
```
В ответ сервер вернет созданный объект, если все успешно:
```json
{
  "id": 1,
  "name": "Хлопок",
  "measure": "кг"
}
```

### Внос информации о сборе сотрудником

URL: /api/v1/collections/create\
Метод: POST\
В заголовок: Authorization следует добавить Bearer JWT\
Тело запроса:
```json
{
  "type" : "Хлопок",
  "amount" : "20"
}
```
В ответ сервер передаст код 200, если все успешно:\

### Просмотр статистики администратором

URL: /api/v1/collections/day
Метод: POST\
В заголовок: Authorization следует добавить Bearer JWT\
Тело запроса:
```json
{
  "employeeEmail" : "770vda@mail.ru"
}
```
В данном случае мы передаем только один параметр, чтобы просмотреть сборы только одного сотрудника с указанным email.\
Также указан роут day, с данным роутом сервер вернет события за последние сутки, чтобы отсортировать по неделе и месяце,\
следует выбрать роут week и month\
В ответ сервер вернет список объектов, которые соответсвуют условию, если все успешно:
```json
[
  {
    "id": 1,
    "employeeDto": {
      "id": 2,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "770vda@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 20.0,
    "dateCreating": "2024-03-15T11:42:56.351+00:00"
  },
  {
    "id": 2,
    "employeeDto": {
      "id": 2,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "770vda@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 34.0,
    "dateCreating": "2024-03-15T11:43:04.205+00:00"
  }
]
```
\
Eсли нам необходимо подбить статистику , следует ввести параметр total = true,

```json
{
  "employeeEmail" : "770vda@mail.ru",
  "total" : "true"
}
```
В таком случае сервер вернет объект следующего вида:
```json
[
  {
    "id": null,
    "employeeDto": null,
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 54.0,
    "dateCreating": null
  }
]
```
Фактически он вернет список со всеми продуктами подходящими под выборку, в таком виде как если бы это была активность,\
только поле amount отвечающее за количественную меру будет просуммировано

Если нам необходимо узнать все действия за указанный период, о следует прислать на сервер пустой объект.\
В таком случае сервер вернет, список со всеми сборами.
```json
[
  {
    "id": 1,
    "employeeDto": {
      "id": 2,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "770vda@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 20.0,
    "dateCreating": "2024-03-15T11:42:56.351+00:00"
  },
  {
    "id": 2,
    "employeeDto": {
      "id": 2,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "770vda@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 34.0,
    "dateCreating": "2024-03-15T11:43:04.205+00:00"
  },
  {
    "id": 3,
    "employeeDto": {
      "id": 4,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "someone@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 1,
      "name": "Хлопок",
      "measure": "кг"
    },
    "amount": 23.0,
    "dateCreating": "2024-03-15T11:56:43.999+00:00"
  },
  {
    "id": 4,
    "employeeDto": {
      "id": 4,
      "firstName": "Дмитрий",
      "lastName": "Валяльщиков",
      "email": "someone@mail.ru",
      "password": null,
      "role": "EMPLOYEE"
    },
    "productDto": {
      "id": 3,
      "name": "Картофель",
      "measure": "кг"
    },
    "amount": 2.0,
    "dateCreating": "2024-03-15T11:59:55.582+00:00"
  }
]
```
Для того чтобы подбить статистику по каждому из продуктов следует добавить total = true
```json
[
{
"id": null,
"employeeDto": null,
"productDto": {
"id": 1,
"name": "Хлопок",
"measure": "кг"
},
"amount": 77.0,
"dateCreating": null
},
{
"id": null,
"employeeDto": null,
"productDto": {
"id": 3,
"name": "Картофель",
"measure": "кг"
},
"amount": 2.0,
"dateCreating": null
}
]
```