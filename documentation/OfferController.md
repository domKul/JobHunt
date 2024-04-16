# Offer Controller endpoints
[To README](https://github.com/domKul/JobHunt)

* `All offer endpoints are secured, registration are required otherwise you will get status 403 Forbbiden`

## 1. Get list of all offers from DB

`request url: /offers`

### HTTP Request:

`GET: /offers`

* ### Request Body:
  empty

* ### Patch Variable:
  empty

### Expect:

**JSON** with array of all offers with `200 OK` status

Example :

* HTTP status 200 OK with offers
  ```
  [
    {
        "id": "661ba5af71d6c81eed37528f",
        "company": "Reply Polska",
        "salary": "5 000 – 9 000 PLN",
        "position": "Junior Java Developer",
        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-reply-polska-katowice-xxthogac"
    },
    {
        "id": "661ba5af71d6c81eed375290",
        "company": "Reply Polska",
        "salary": "5 000 – 9 000 PLN",
        "position": "Młodszy Programista",
        "offerUrl": "https://nofluffjobs.com/pl/job/mlodszy-programista-reply-polska-katowice-a08iptd7"
    },
    {
        "id": "661ba5af71d6c81eed375291",
        "company": "Enigma SOI",
        "salary": "6 300 – 12 000 PLN",
        "position": "Junior Java Backend Developer",
        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-backend-developer-enigma-soi-warszawa-ziaekkrf"
    },
    {
        "id": "661ba5af71d6c81eed375292",
        "company": "Fair Place Finance S.A.",
        "salary": "6 000 – 9 000 PLN",
        "position": "Junior Java Developer",
        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
    }
   ]
  ```

## 2. Get offer By ID

`request url: /offers/{{offerId}}`

### HTTP Request:

`GET: /offers/{{offerId}}`

* ### Request Body:
  empty

* ### Patch Variable:
  offer ID required

### Expect:

**JSON**  offer with `200 OK` status

Example :

* HTTP status 200 OK with correct offer id
  ```
  {
        "id": "661ba5af71d6c81eed375292",
        "company": "Fair Place Finance S.A.",
        "salary": "6 000 – 9 000 PLN",
        "position": "Junior Java Developer",
        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
  }
  ```
* HTTP status 404 Not Found with wrong address id

  ```
  {
  "message": "Offer not found with id : dgh1iu2g13",
  "status": "NOT_FOUND"
  }
  ```

## 3. Create and add Offer

`request url: /offers`

### HTTP Request:

`POST: /offers`

* ### Request Body:
  ```
  {
  "company": "string",
  "offerUrl": "string",
  "position": "string",
  "salary": "string"
  }
  ```

* ### Patch Variable:
  empty

### Expect:

**JSON** with status `201` and will return te body

Example :

* HTTP status 201 CREATED
  ```
  {
  "houseNumber": "string",
  "street": "string",
  "city": "string",
  "postalCode": "string"
  } 
  ```
    * HTTP status 409 CONFLICT if offer url is already exist

  ```
  {
  "message": "Offer url already exist",
  "status": "CONFLICT"
  }
  ```