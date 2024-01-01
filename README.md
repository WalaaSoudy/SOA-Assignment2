# SOA-Assignment2
Description:

This Java program is designed to manage student information within a university structure based on the provided XML schema. The program offers a menu-driven interface allowing the user to perform various operations, including adding, updating, searching, sorting, and saving student records.

Key Features:

Add Student:

The user can add a new student by providing all required attributes (ID, FirstName, LastName, Gender, GPA, Level, Address).
Input data is validated to ensure that no attribute is null or empty.
The program checks for duplicate IDs to maintain unique student records.
Restrictions are applied to ensure that names (first name and last name) and address contain only characters (a-z), and GPA is within the range of 0 to 4.
Update Student Details:

Users can update selected attributes of an existing student record.
The ID field cannot be updated.
Unprovided details are preserved, meaning if only the first name is changed, other attributes remain the same.
Search for a Student:

The program enables users to search for a student using any attribute.
Multiple filters are implemented to allow searching with various fields.
The number of found students is displayed, and matching students are retrieved and presented from the XML file.
Sort the File:

Users can sort the student records based on any attribute (e.g., ID, FirstName) in ascending or descending order.
The sorted content is displayed to the user.
Save Sorted File:

After sorting, the program allows users to save the sorted file, replacing the old file content.
This ensures that the changes made during sorting are permanently stored.
Note: This program uses Java and XML processing libraries to handle XML data. It adheres to the provided XML schema and implements the specified validation rules for data integrity. The user interface is text-based, and interactions are performed through a console menu.
 
