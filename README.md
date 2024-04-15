# JobHunt
## Web application for find job offers for Junior Java Developer!

### Description
Function of JobHunt is fetching offers form external server in scheduled time. In order to receive offers, user has to register then you can generate JWT token for grant access to all endpints.


### Technologies

![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring) &nbsp;
![image](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/Testcontainers-9B489A?style=for-the-badge) &nbsp;

### Endpoints

Application provides the following endpoints:

|   ENDPOINT   | METHOD |                          REQUEST                           |      FUNCTION       |
|:------------:|:------:|:----------------------------------------------------------:|:-------------------:|
|  /register   |  POST  |  [User Register](documentation/UserRegisterController.md)  |   register a user   |
|    /token    |  POST  |     [User Token](documentation/UserTokenController.md)     | generate JWT token  |
|   /offers    |  POST  |         [Offer](documentation/OfferController.md)          |  creates new offer  |
|   /offers    |  GET   |         [Offer](documentation/OfferController.md)          |   sending offers    |
| /offers/{id} |  GET   |         [Offer](documentation/OfferController.md)          | finding offer by id |

  
