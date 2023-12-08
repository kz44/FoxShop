# FoxShopNullPointerNinjasOtocyon

In application.properties are used some environment variables:

`DB` for connection to database (the example of the value of this
variable: `jdbc:mysql://localhost/foxshop?serverTimezone=UTC`)
I suggest to remove the "?serverTimeZone=UTC" part of it. It cause the problem with editMessage. It has different time zone.

`DB_USERNAME` for username for connection to the database

`DB_PASSWORD` for the password.

`expiration-time-minutes` for the expiration time in minutes

`JWT_SECRET` for the secret code, minimum length is 64 characters

You need to set up these environment variables.


You will find a file in the resources/assets/databasesetup directory called role_setup.sql.

You should implement it on your computer in the database.

