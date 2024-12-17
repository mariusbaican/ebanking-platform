Marius Baican 323CA

# J. POO Morgan Chase & Co.

## Introduction
For the first part of the project, I wanted to learn more about implementing design patterns and how 
to better organise my code. Improving upon the first homework, I have tried to move further away from  
the C-type programming, where I used to use flags to differentiate slightly different objects, instead  
of each having its own class, better embracing the OOP concepts.

## Bank
The main point of the project is of course the bank itself. It possesses the database and the exchange  
rates. Additionally, it runs all the operations required in the tests: initializing and filling the database,  
storing the exchange rates and processing and handling requests. 

## Database
The database is the place where the information is stored. The system uses database entries, which are 
user specific and contain the user, their accounts, cards and transaction history. To access the data,  
it uses an SQL-based approach, pulling entries based on the provided user, account or card. The 
aforementioned database entries are stored in multiple hashMaps as values, with the keys being user 
emails, account IBANs or card numbers. This approach has been used to improve pulling times. The  
database implements the Singleton design pattern.

## User
The user is one of the simpler objects, only storing the name and email of a user.

## Account
The account implementation uses the Factory design pattern due to the multiple types of account  
(looking at the second part of the project it looked like a good decision XD).

## Card
The card implementation also uses the Factory design pattern, which might be a bit of an exaggeration  
but it allows for easy implementation of extra card types.

## CurrencyExchanger
The CurrencyExchanger stores the provided rates and computes all the possible rates using the initial  
ones. I have created a currency tuple to be able to use them as keys for their rates in a hashMap for  
time efficiency. Everytime a new rate is provided, the algorithm tries to compute new ones using the  
previously provided ones, afterward storing them in the hashMap.

## Commands
The Command system intuitively implements the Command design pattern, along with the Factory design  
pattern. To better manage the variety of requests. The actual implementation has gone through  
multiple iterations, with the current one being the simplest. It uses a Factory class to create the  
right type of command object, based on the given commandInput, while also storing the input to be used  
for fulfilling the request.

## Summary
Overall, this project was very good at helping me develop my understanding of OOP concepts and  
has grown my passion for it. The requirements were very intuitive and never really got me stuck  
thinking about how to implement something. That is exactly what I enjoy most about OOP, every concept  
is much more graspable and understandable.
