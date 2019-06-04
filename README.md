# version 3

a networked version of the game, with added features
- player lobby 
- leaderboard 
- login/signup
- database storage of user and game data

tech:
- PubNub for messaging
- JDBC to interact with PostgreSQL 


Sign up with PubNub.com to get your pub/sub keys, then copy/paste them here

![alt text](https://github.com/justisketcham/ticTacToeJava/blob/v3/wiki/pubnub.png)

and set these add-ons in PubNub admin console

![alt text](https://github.com/justisketcham/ticTacToeJava/blob/v3/wiki/pnAddOns.png)

install postgres and fill in your user name and password; if necessary modify the the default urls here

![alt text](https://github.com/justisketcham/ticTacToeJava/blob/v3/wiki/db.png)


### set up the database and users table
- run server.databaseOperations.DBOperations.main() to create the database and users table

### running the program
- run server.Server.main()
- run the gradle build for each instance of the app desired
