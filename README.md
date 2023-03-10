# Online-Banking-App
This app is developed with Spring boot and MYSQL. Its functionality is as follows:

CUSTOMER
The customer will register with the app, the app will generate a token to each customer using UUID, a customer will need to login to the app before he/she can perform other operations in the customer module such as updating and displaying of the customer’s information.

BANK ACCOUNT
The app create a bank account with 16 digits account number and assigned it to a customer, check if the customer has already been assigned to a bank account by using the customer’s phone number and email address which are unique keys because more than one customer cannot be allowed to use the phone number or email address that another customer has already been used. The minimum amount for account opening is 2000. The account type such as SAVINGS, CURRENT, FIXED DEPOSIT as well as account status such as ACTIVE or DOMANT are also specified. There are so many validations done here.

DEBIT CARD
Debit card is created for each customer on demand. There are different card types in the App for selection. Each card type has different price tag. Before a card can be created for a customer, the app will check the customer’s current account balance to know if he/she is qualified for the desired card. If the customer is qualified, the app will generate a debit card to the customer. The debit card has the following details:
Debit card number (16 digit number generated by the app which starts with EL).
Card Expiring date (This is gotten from LocalDate.now to plus (3) years method in java. That’s adding 3 years to the date that the card was created for the customer because each card expired after 3 years).
Card secret pin (This is a 4 digit number in which the customer will be asked to input). This 4 digit number will be hash to avoid accessibility by unknown person.
Before a card is being issued to a customer, the app will also check if the customer has been given a debit card before that is not yet expired. If true, it will through an exception.

TRANSACTION
There are several transactions that are performed by the app such as credit an account, debit (with a debit card in which all the card’s details will be provided and validated before a transaction can be successful), debit at the bank(that’s from the counter), transfer(from one account to another.
 Before a transfer can be successful, the sender must have a debit card because the entire card’s information is required during the account to account transfer. The information entered by the customer is validated to be sure that it’s the rightful owner that tries to make the transaction).

ACCOUNT INFORMATION
Each transaction made in a day is stored in a database and can be retrieved at any time. The customer can also request for his/her account statement. The customer will provide the start and end date and the app will return all the transactions made by that customer within the given date just as it’s always done in other Banking app.

SECURITY
The API is secured using JWT (Json Web Token). Before one can have access to the endpoint, he/she must register for it and a token will be issued to the person/organization. Your access to a particular endpoint depends on the authorization that you have because there are different roles that gives access to the endpoint. There are endpoints that a user with the ADMIN ROLE can access while a user with MANAGER or USER ROLE cannot access.
For example, it’s only a user with the MANAGER’S ROLE that can access the endpoint that process a customer’s transaction in the bank i.e at the counter.

EXCEPTION HANDLING
The app has been designed to capture all the necessary exceptions. There are several custom exceptions handling that are implemented
