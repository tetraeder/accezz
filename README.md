# accezz

## Run Syntax:

  accezz.jar =Command File= =Configuration File=
  
  	example: accezz.jar C:\Devhome\Accezz\oneEach.txt c:\test\config.json
  
  accezz.jar =Command File=
  
	example: accezz.jar C:\Devhome\Accezz\oneEach.txt'
	by default 'c:/accezz/config.json' will be selected

  For CLI logging, please run as 'java -jar accezz.jar C:\Devhome\Accezz\oneEach.txt'     
  
## Location:  
  accezz and it's [Configuration Files](Https://github.com/tetraeder/accezz/tree/master/src/resources) should be installed under:
  C:\accezz\
  
## Command Syntax:

  http -u http://www.ynet.co.il
  
  
  https -u https://www.ynet.co.il
  
  
  dns -u http://www.ynet.co.il
  
## [Resourcs](Https://github.com/tetraeder/accezz/tree/master/src/resources):

accezz.jar -Runnable jar

config.json - Program configuration

services.json - Service configuration

## Configuration: 

#### config.json -

  "servicesFile" - services file location 
  
  "logFile" - log file location
  
  "database" - db file location
  
  "curlPath" - path to curl.exe 
  
  "rolloutAfter" - log rollout to next file after X commands
  
  "smtp" - mail configuration
  
  
#### services.json - for each command: 

  "serviceUrl" - future usage for micro services
  
  "name" - service name
  
  "syntax" - service command name 
  
  "latency" - ms threshold (example: 10000)
	
  "bandwidth" - degregation percentage threshold (example: 10%)
	
  
## Test Cases

  [Basic test cases and command files](https://github.com/tetraeder/accezz/tree/master/test)
  
  
## Not Completed:
  * Relative paths 
  * Internal Log to file (currently prints to console)
  * Elaborate testing (currently performing scripted tests withut assertion)
  * Full documentation (Code not fully covered)
