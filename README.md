Description :

A restful API for our statistics. The main use case for our API is to
calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is
called every time a transaction is made. It is also the sole input of this rest API. The other one
returns the statistic based of the transactions of the last 60 seconds.

Build Project
mvn clean install

To start Application
java -jar target/n26-0.0.1-SNAPSHOT.jar

API Details : 

1. Create Transaction API
Method : POST
URL    : http://localhost:8080/transactions
Content-type : application/json 
RequestBody : 
{
 "amount" : 100,
 "timestamp" : 1527065702000
}
Expected Response Status Code
201 - in case of success
204 - if transaction is older than 60 seconds

2. Get Statistics
Method : GET
URL    : http://localhost:8080/statistics

Expected Response
{
    "sum": double specifying the total sum of transaction value in the last 60 seconds,
    "avg": double specifying the average amount of transaction value in the last 60
seconds,
    "max": specifying single highest transaction value in the last 60 seconds,
    "min": specifying single lowest transaction value in the last 60 seconds,
    "count": specifying the total number of transactions happened in the last 60
seconds
}
