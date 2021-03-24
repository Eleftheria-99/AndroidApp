# AndroidApp

The basic goal of the app is to find the location if it changes more than 10 meters by using a boradcaster and a service and then save the lacation every time in a database.

The app checks if there is wifi.
If yes, the broadcast receiver starts a service tyring to find the location, if it chnages more than 10 meters than the previous one. 
Every new location is being saved into the database. Also, I have use used a seperate thread for the service so that the app won't crush. 
There are also being shown different notifications so that we know the process is successful.
