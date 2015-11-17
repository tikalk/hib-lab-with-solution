Hibernate Tutorial Lab

Preparation

The purpose of the following steps is to help you prepare the lab environment. 
Please follow the steps bellow:

1. Run from the extracted folder the following command: 
In Linux/MAC run: ./gradlew eclipse
In Windows run: gradlew.bat eclipse.
2. Now you can import the projects into the eclipse workspace
3. Please note that all the files needed to for the lab already exist, and you only have to fill them according the instructions – YOU SHOULD NOT CREATE ANY NEW FILES, PLEASE FILL EXISTING METHODS ONLY!!!.
4. Now we can start the lab :) 

Exercise 1a
In this exercise we will use the class Person. This class will be used as a domain model class and will be saved, updated, deleted and read from the DB. A Person should have the following fields:
id : java.lang.Integer – will serve as the Person identifier.
name : java.lang.String – the person's name – will also be used as a “business key” for person.
age : int – the person's age.
address : com.tikal.sample.model.Address– A domain model class that is conceptually part of the Person – An address life cycle is bounded to the person life cycle (an instance of Address is always in the context of an owner Person instance).  The address class should contain city(String), street(String), houseNo(int) properties. These properties should have only getter methods (no setter). Add a constructor that accept these parameters.

1. Just to check that everything is OK with your environment, run the HibernateTest (right-click on the file and click on “Run as”->”Junit Test”).
2. Use the Person class located at src/main/java provided in the exercise and update its fields as defined above
3. Create setter and getter for its properties. Since we will use auto generated id in Hibernate, you can provide a “private” modifier to the setId() method.
4. Create a business key in the Person class 
5. Update the testCreatePerson test  – Create a new person instance, set its properties, and save it to the DB. assert the id is not null  after the commit statement Run the test and see that Hibernate runs “insert” statement.
6. Create another test (you can copy the previous test and use it as a template for this test) that creates a person and then update it. Check what happens if you do it in the same transaction and in two different ones (How many SQL statements do you get in each case?).
7. Create another test that delete a person from the DB.
8. Create test that fetch a person from the DB by id (using the “find”) method
9. Create a query that load all persons with age greater than 30.
10. Map the Celebrity class – It should extend the Person class and should have famousName persistent property. Both Persons and Celebrities should be persisted in the same table. User the person_type column as a discriminator column. Run again the tests and make sure the Person table has 2 new columns : person_type and famousName (follow the create table statements).



Exercise 1b
1. Create another domain object called Car with the following properties:
id – java.lang.Integer – will serve as the Car identifier.
serialNo – java.lang.String – a unique serial number comes with the car – use it as “business key” for Car.
manufacturer – java.lang.String the manufacturer of the car.
model – java.util.Date The creation date of the car.	

	Add getter and setters for the properties (setId should be private).
2.  Run your Test and check your mapping is OK – see that a new table is created for the Car class.
3. Add “cars” property of type java.util.Set to the Person class. Add getter and setters to the class.
4. Map the cars as a oneToMany relationships between the Person and the Car.
11. Create a business key in the Car class
5.  Run your Test and check your mapping is OK – see that a foreign key is created on the car table.
6. Create a new test that create a new person, create a new car. In a new transaction update the person by adding the car to the person. Watch the output and see that both the person and car table are updated on the second transaction (explain why ?).
7. Create a test that assert Laziness – Create a new Person in the DB. In a separate transaction load the person by id. After the second transaction try to see the cars size. Fail the test (using fail()) method of Junit if the test does NOT throw a LazyInitializationException. If the test does throw this exception it should finish successfully.
8. Create a test that loads all persons (with their cars) that have cars belongs to “Volvo” manufacturer.


Happy Coding!
