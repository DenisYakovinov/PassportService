# passport service

The service provides a passport management service.
The service provides a REST API for accessing data

/save, save passport data<br>
/update?id=*, update passport data<br>
/delete?id=*, delete passport data<br>
/find, upload all passports<br>
//find?seria=*, upload passports with the specified series<br>
//unavaliable, upload passports whose expiration date has<br>
expired /find-replaceable, upload passports that need to be replaced in the next 3 months

The second service calls the methods of the first using RestTemplate

in progress..

