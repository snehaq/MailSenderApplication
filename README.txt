 Mail Sending Application
=======================================================================================
  
    Pre-Requisites

1.)Install Java jdk  version 1.7 or up.
2.)Install Apache tomcat server version 1.7 or up.
3.)Install MySql 5.6 or up.
4.)Apache Ant installed 1.8 or up

========================================================================================= 

    Steps To Install

1.) Import database from MailSendingApplication.sql
 In command line go to the directory containing MYSQL\bin
 Run the command 
     mysql.exe -u username -p < filepath\MailSendingApplication.sql

2.) In MailSendingApplication.propeties file set the values for 
	setFrom= e-mail id to be sent from
	setPassword= password the email account
        setDbSchemaLink= (for e.g) jdbc\:mysql\://localhost\:3306/xlsdata
	setDbPassword=password of your MYSQL server
	setDbUsername=username of your MYSQL server

3.) Build the application 
	In Application directory locate file build.properties.
	In build.properties add the path of your server in tha variable warTargetPath
	        warTargetPath=C:/xyz/xyz/wtpweabapps
        For building the application first to till the directory of the application in command line
        Next in command line type the following command
		ant MailSendingApplication
	This will build tha application and generate MailSendingApplication.war file.
	The .war file will be placed in the server wtpwebapps folder.
4.) Start/Restart the server for deploying the application.
5.) Upon deploying hit the url 
	serverpath\applicationname\pages\Login.jsp
6.) Enter the credentials in following manner
	username:admin		
	password:admin123

You will be authenticated and redirected to the main page of the ui

7.)Select a xls file to be uploaded to the server and added to the database using the ui
8.)Select zip file containing images to be uploaded to the server and ui
	*note:Images should be placed directly in the zip and not in any sub folder
9.)You can also upload single .jpg files to the server
10.)Select mail functionality using enable or disable.
11.)Set the cron job time to send mail automatically
12.)Upon clickin set the cron job will be up and running.
13.)You can alter the time to send mail any time you like using the UI.
14.)Select the templates you want to send for the mail.
    Tha application will randomly choose a template and send a mail respectively.
15.)Mail Logs will be displayed in Mail Logs section
     You can choose a.)All logs
	  	    b.)Last Week Logs
=========================================================================================
   MailSendingApplication.properties

 This file contains the data needed by the application.
 Altering the data in the file could affect certain functionalities of the application.
 
*note:Change the values in  MailSendingApplication.properties file carefully.

=========================================================================================
    Cron Job Execution

 Once the cron job is up and running the application will wait for the crob job to execute
 the mail sending functionality.
When time set by the user arrives the application searches for employees whose
 birthday lies on current date and will send the mail accordingly.
It will also update the mail logs for the same.

=========================================================================================
   Forgot Password/Reset Password

   On login page you can use forgot password to reset your password.
   Upon clickin on "forgot password" link the application will prompt you
    for recovery email address which you can use to reset your password.
   After entering the email  a mail will be sent to the email adress with a link to direct
    to the reset password page.
   Upon confirming the password will be successfully changed and user can login
    using the newly set password.
===========================================================================================
   



	

