# FoxShopNullPointerNinjasOtocyon

In application.properties are used some environment variables:

* `DB` for connection to database (the example of the value of this
  variable: `jdbc:mysql://localhost/foxshop?serverTimezone=UTC`)
* `DB_USERNAME` for username for connection to the database
* `DB_PASSWORD` for the password
* `JWT_SECRET` for the secret code, minimum length is 64 characters
* `SENDGRID_API_KEY` for the Send Grid API for sending verification mails

You need to set up these environment variables.

There are scripts for filling database tables in the `resources/assets/databasesetup` directory:

There are scripts for filling database tables in the `resources/assets/databasesetup` directory:
* role_setup.sql
* location_setup.sql
* report_status_setup.sql


You should implement it on your computer in the database.

