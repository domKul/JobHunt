# User Controller endpoints

[To README](https://github.com/domKul/JobHunt)
* `To generate JWT token you need to register user first`


## 1. Register user

`request url: /register`

### HTTP Request:

`POST: /offers`

* ### Request Body:
  ```
  {
  "password": "string",
  "username": "string"
  }
  ``` 

* ### Patch Variable:
  empty

### Expect:

**JSON** with user response body with `201 CREATED` status

Example response body:

* HTTP status 201 CREATED with offers
  ```
  {
  "created": true,
  "id": "string",
  "username": "string"
  }
  ```