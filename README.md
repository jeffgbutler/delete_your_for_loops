# Delete Your For Loops
This is example code for my talk titled "Delete Your For Loops".  The purpose of the talk is to use a real world problem to show how to refactor from a fully imperative programming model towards a functional model.

The "java" directory contains a successive refinement exercise refactoring to Java8 streams, lambdas, and optional.

The "javascript" directory contains a successive refinement exercise refactoring to ES6 map/filter/reduce.

Key concepts demonstrated:

1. Pure Functions
2. Immutability
3. Map/Filter/Reduce
4. Higher Order Functions

All execises solve the same problem - translating a spreadsheet into an SQL script.  In this case, the spreadsheet contains rows of user IDs and the application permissions that should be granted to each user.  The spreadsheet also contains a lot of other information that is helpful to a human, but not necessary for the programs.

The authority model is simple.  There is a database table named `ApplicationPermission` containinug two columns: `user_id` and `application_id`.  If user f.smith has authority to application 23, then there is a row in the table with those values.

On inspection of the spreadsheet, we've determined the following:

1. If the text of the first column contains a '.' in the second position, then the row contains a valid user ID
2. If the text of the second column is X, then the user should have access to the application with the ID 2237
3. If the text of the third column is X, then the user should have access to the application with the ID 4352
4. If the text of the fourth column is X, then the user should have access to the application with the ID 3657
5. If the text of the fifth column is X, then the user should have access to the application with the ID 5565
