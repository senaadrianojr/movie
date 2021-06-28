# Movie List Upload

Upload csv file with movie data

## Enviroment

Java 11
Maven

## Execution
```
mvn spring-boot:run
```
## Resourcers
Default host for local enviroment is http://localhost:8080

### Upload
Upload, read csv file and save data in database.

 - Path: {host}/movies/csv
 - Method: POST
 - Content-type: multipart/form-data
 - Request body: 
 ```
 form-data
 key: file
 value: file path
 ```
 - Response body:
```
Http status 200
File uploaded successfully. Wait a moment for the data to be saved
```
### Get winning movies report
 Return result of the winning movies.
 
 - Path: {host}/movies/producers/report
 - Method: GET
- Response:
```
{
	"min": [
		{
			"producerId": 112,
			"producer": "Joel Silver",
			"previousWin": 1990,
			"followingWin": 1991,
			"interval": 1
		}
	],
	"max": [
		{
			"producerId": 268,
			"producer": "Matthew Vaughn",
			"previousWin": 2002,
			"followingWin": 2015,
			"interval": 13
		}
	]
}
```