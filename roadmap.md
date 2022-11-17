# Weather Forecast Evaluation

## Objectives
Extract historical weather data for different locations from 1960-01-01. Use [open meteo API](https://open-meteo.com/en). Store this data in MySQL/MariaDB database.  
Build data pipeline which runs weekly and add the newest data to the database.   
Fetch the forecast data.  
Store the forecast data in MongoDB collection(s).  
Aggregate and join the forecast and actual weather data, store it in another database (PostgreSQL).
Create a REST API from the PostgreSQL data.

## Data Pipeline
### Historical data
Export hourly data: temperature, relative humidity, precipitation, snowfall, weathercode, cloudcover total, wind speed (10 m), wind direction (10 m)  
Locations: Freiburg, München, Sopron, Szeged.  
Load it once into a MySQL/MariaDB database.  
Table design:  
- locations: id, city name, coordinates
- weather data: id, datetime, the above written fields, weathercode (foreign key), city id (foreign key)  
- weather codes: id, description

### Update Data
Create a weekly workflow (DAG) with Airflow or any other workflow orchestration framework. It has to be widely used within the industry.  
It has to extract the last week's weather data for the given locations and append it to the historical data.  
Make sure no duplicates are in the table after each load job.

### Forecast
Create a daily workflow for fetching the forecast from the same API.  
Necessary information: 

Hourly:  
- all the above written fields in the case of historical data for the next 3 days from today
- 
Daily:
- weathercode, precipitation sum, snowfall sum, precipitation hours, max temp, min temp, sunrise, sunset for the 4-6 days from today

Store the hourly and daily forecast data in collection(s) in MongoDB.
Schema design  should be clear and obvious. We want to use NoSQL to be flexible, we might want to fetch more hourly or daily forecast data in the future.  

### Building Data Warehouse
We want to compare the forecasts to the actual data when looking back and evaluating.  
- Create a new database in PostgreSQL
- Create a table where we store the actual data and the forecast on hourly basis
- We actually have multiple forecasts for the same hour as we always fetch 3 days' data in advance. Make sure all these forecasts are registered to every single actual data point

### Create Aggregated Tables
We need to create the following tables in order to prepare the data for reporting purposes
- Daily average difference between the hourly temperature and precipitation (there will be 3 differences because we always get 3 day forecasts)
- Weekly average difference between the hourly temperature and precipitation (there will be 3 differences because we always get 3 day forecasts)
- For every day, we want to look back 7 days and get the above differences always for the last 7 days (rolling average)

These aggregations should be scheduled on a weekly basis
  
### Web Service
We want to be able to get the aggregated data from an API. Only GET requests should be allowed.  
The website should contain additional information which is updated weekly:
- last weeks max hourly difference in temperature between forecast and actual weather data for Freiburg (only compare the next closest - next day - forecast with the actual data)
- last month's 10 smallest daily difference in temperature forecast and actual weather data

### CLI application
Build a CLI application to get the forecasts and historical data easily.

### Dashboard
Use a BI tool (PowerBI, Data Studio or anything else) to visualize some interesting parts of the data.

### Bonus:
#### Data Streaming with Kafka
Use Kafka Connect to connect to MongoDB and write the daily forecasts into a topic.
If there is 5 Celsius or more difference two days' average temperature, display an alert on the API.

#### Custom predictions
Use historical data to build a prediction system.

#### Automated remote server deployment
Automate the remote server setup with an Infrastructure as Code tool (Ansible, Terraform) to build databases in containers, use test data, run automated tests and manually destroy the server.
