# Project1Group15 - Gif DSL

### Basics
- The goal of this language is to create a GIF file from a series of static images (PNG, JPEG, etc...) that are manipulated by the statements

#### Features
- Create GIF animations in a programmatic way
- Perform manipulations to images (e.g. rotate, crop, overlay, etc...)
- Apply filters to images (e.g. greyscale, sepia, blur, etc...)

### How to run the program
The entry point to our program is `./src/main/Main.java`. You must specify the language code file as the first argument and add any additional arguments afterwards. These are the additional arguments that can be used:

| Argument     | Default | Description                                                                                                                                                |
|--------------|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `-nocheck`   | No      | Disables static checker.                                                                                                                                   |
| `-onlycheck` | No      | Executes everything up to the static checker. If enabled, it does not run the program.                                                                     |
| `-shortcuts` | Yes     | Enables shortcuts for function call syntax (see [**Function**](../main/documentation.md#functions) section on API). Requires static checker to be enabled. |

### How to write the code
Our language is similar to programming languages like Python with syntax that reflects natural language.

The basic workflow of our code is:
1. Load images from the computer
2. Perform manipulations to the images
3. Add the images to a list of frames
4. Create the final gif from those frames

Example:
```
// Load an image of a dog from "./dog.png"
LOAD "./dog.png" AS dog

// Create a list
CREATE-LIST AS frames

// Loop 30 times, setting i from 1 to 30
LOOP i IN 1 TO 30:
  // Rotate the image of the dog by 1
  ROTATE dog
    WITH angle: 1
  // Add the image to a list of frames
  ADD frames
    WITH item: dog

// Save the list of frames as a Gif of 10 seconds to "./final.gif"
SAVE frames
  WITH duration: 10
  WITH location: "./final.gif"
```

For information on language features & syntax: [API documentation](../main/documentation.md)

#### Debugging code
Our static checker can apply checks to make sure the code runs beforehand. Here are some of the checks that are performed:

| Check                                         | Description                                                                                                                                                                                                                                                                  |
|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Using undefined variables                     | Checks if a variable has been set a value before it is used. Does not check for cases when If statement may result in the value being set or not set.                                                                                                                        |
| Calling undefined functions                   | Checks if a function has been defined before it is used.                                                                                                                                                                                                                     |
| Calling functions correctly                   | Checks if a function has been called with the correct target & parameters.                                                                                                                                                                                                   |
| Function names are unique                     | Checks that a function with the same name has not been defined before.                                                                                                                                                                                                       |
| Type checking                                 | Checks that correct types are passed into the function (and other places that require specific type). Types are reflected by the latest value that the variable is set to. There may be cases where the static checker is unsure of the type, which will not fail the check. |
| Invalid arithmetic operations and comparisons | Checks that arithmetic operations and comparisons are performed with numbers.                                                                                                                                                                                                |
| Correctly placed statements                   | Checks that statements like `RETURN` and `DEFINE` are placed in the correct places.                                                                                                                                                                                          |

And the static checker performs more minor checks which are not listed here.

Our static checker will indicate the line and column of where the error occurred in the format `line:column`.
