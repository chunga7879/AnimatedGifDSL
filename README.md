# Project1Group15 - Gif DSL

![Alt text](https://media0.giphy.com/media/o7nouq5RZguiuWZv0Y/giphy.gif?cid=790b7611d751fe8e3133a02a6ec9319cadbddb3a4700fe7d&rid=giphy.gif&ct=g)

### Basics
- The goal of this language is to create a GIF file from a series of static images (PNG, JPEG, etc...) that are manipulated by the statements

#### Features
- Create GIF animations in a programmatic way
- Perform manipulations to images (e.g. rotate, crop, overlay, etc...)
- Apply filters to images (e.g. greyscale, sepia, blur, etc...)

### Setup with IntelliJ
1. Clone the repo
2. Open the project in IntelliJ
3. Go to `File` > `Project Structure`
4. Under `Project Settings` > `Project`, set up JDK (version 18) and compiler output
5. In the project, for both `src/main/parser/GifDSLLexer.g4` & `src/main/parser/GifDSLParser.g4`, right-click and click `Generate ANTLR Recognizer`

### How to run the program
The entry point to our program is `./src/main/Main.java`. You must specify the language code file as the first argument and add any additional arguments afterwards. These are the additional arguments that can be used:

| Argument     | Default | Description                                                                                                                                                |
|--------------|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `-nocheck`   | No      | Disables static checker.                                                                                                                                   |
| `-onlycheck` | No      | Executes everything up to the static checker. If enabled, it does not run the program.                                                                     |
| `-shortcuts` | Yes     | Enables shortcuts for function call syntax (see [**Function**](../main/documentation.md#functions) section on API). Requires static checker to be enabled. |

See the Examples folder or [examples.md](examples.md) for example DSL input files.

### How to write the code
Our language is similar to programming languages like Python with syntax that reflects natural language.

The basic workflow of our code is:
1. Load images from your local device
2. Create a background image either using a built-in function or a loaded image
3. Perform manipulations to the images (`ROTATE`, `COLOUR-FILL`, etc...)
4. Overlay the manipulated image onto the background
5. Add the images to a list of frames for the GIF creation
6. Create the final GIF from those frames

Example:
```
// Load an image of a dog from "./dog.png"
LOAD "./dog.png" AS dog

// Create background using CREATE-RECTANGLE
CREATE-RECTANGLE AS background
  WITH width: 400
  WITH height: 400

// Depends on the dog image's size, but typically will have to
// resize the image to overlay it on the background
RESIZE dog
  WITH width: 20
  WITH height: 20

// Create a list
CREATE-LIST AS frames

// Loop 30 times, setting i from 1 to 30
LOOP i IN 1 TO 30:
  // Rotate the image of the dog by 1
  ROTATE dog
    WITH angle: 1

  // Overlay image on background created
  OVERLAY dog ON background AS frame
    WITH x: 200
    WITH y: 200

  // Add the manipulated image overlaid on the background to the list of GIF frames
  ADD frames
    WITH item: frame

// Save the list of frames as a Gif of 10 seconds to "./final.gif"
SAVE frames
  WITH duration: 10
  WITH location: "./final.gif"
```

****Note**: All programs should end with a new line (\n) character.**

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

### Running a `.gifify` file with in Intellij
1. Select `Run` > `Edit configurations`
2. Add the path to the `.gifify` file in `Program arguments`
3. Click OK
4. Navigate to `src/main`
5. Right click `Main` and select `Run 'Main.main()'`
