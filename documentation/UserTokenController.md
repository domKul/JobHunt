# User Controller endpoints

* `Here you can generate jwt for get access to all endpoints`

## 1. Create token for  user

`request url: /token`

### HTTP Request:

`POST: /token`

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

**JSON** with username and token body with `201 CREATED` status

Example response body:

* HTTP status 201 CREATED with offers
  ```
  {
  "username": "string",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
  eyJzdWIiOiJzdHJpbmciLCJpc3MiOiJqb2Itb2ZmZXJzLWJhY2tl
  bmQiLCJleHAiOjE3MTU3OTgxNTYsImlhdCI6MTcxMzIwNjE1Nn0.
  NZWk2F0kTu7MWU6vWZ-zkbZvi71WN_PShPc0cktL2NI"
  }
  ```