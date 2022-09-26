# Milestone 1

## Image Animation Creator

This DSL will allow users to modify images in various ways, including the ability to combine multiple images to create an animated gif.

### Main Language Features
* Variables
  * users can open an image file and store it in a variable
  * users can store the result of an image manipulation in a variable
  * users can save an image from a variable to a file
* Arrays
  * Users can create an array of images.
  * Users can append images to an array.
* Functions
  * Users can create their own functions with arguments.
  * Functions comprise of other functions and/or built-in manipulation functions.
  * The DSL will have some built in functions:
    * filter
    * crop
    * resize
    * rotate
    * getHeight
    * getWidth
    * saveFile
    * openFile
* Loops
  * Users can loop over images in array and manipulate them.
  * Users can use loops and arrays to create gifs.
    * Append to array
* Conditionals
  * Can be used inside loops
    * e.g. Every second image is rotated.

### Changes from the TA discussion
* DSL now supports variables, arrays and functions.
* The original design we came up with was solely image manipulations on a provided image. However the TA suggested that there wasn’t enough complexity or interactivity, as you can imagine a user of the DSL would just be listing out sequences of image manipulation commands, without the features of our DSL really interacting with one another
* To incorporate more complexity into our DSL, we decided to expand the scope of our project to an animation/GIF creator, instead of just manipulating images. This allowed us to add more features and complexity (our features interact with one another), while still keeping the original basic idea of image manipulation commands (e.g. loops)

### Follow-up tasks

* The filters we're going to support.
* GIF timing
* Determine what language/libraries to use.
* Decide what parameters for each function
* Decide on the syntax (brackets?)
* Determine other use cases (besides storing images) for arrays.
* Determine how users will interact with arrays.


# Milestone 2

## Responsibilities

| What | Who | When |
|------|-----|------|
| Create parsing/tokenization rules (input features) | Emiru | DSL - Before user study / Milestone 2/3 |
| User study (on language) | Katherine | Milestone 3 - Sept 30 |
| Core Language Implementation | Devon | After DSL - Milestone 3/4 |
| Static checking (in code) | Emiru | Milestone 3/4 |
| Language Features - Variables, Arrays | Devon | Milestone 34 |
| Language Features - Loops, Conditionals (optionally functions) | Shiven | Milestone 3/4 |
| File system functions | Katherine | Milestone 3/4 |
| Functions - Image manipulation | Chunga | Milestone 3/4 |
| Functions - Image filtering | Katherine | Milestone 3/4 |
| Functions - Gif Creation | Shiven | Milestone 3/4 |
| Uesr study (on complete project) | Katherine & Chunga | (Plan in milestone 4) Milestone 5 |
| Video | All | Oct 17 |

## Stack

* Java
* Libraries
  * ANTLR
  * Need to find an image manipulation library
   
## DSL Example: Create a GIF from a static image
```
IMG img1 = OPEN IMG: "~/Desktop/background.png"
IMG img2 = OPEN IMG: "~/Desktop/DVD.png"


SET BACKGROUND: img1 

LIST<IMG> frames = CREATE LIST

OVERLAY: img2 (2, 2) 

DEFINE MOVE(DIRECTION direction, COLOR color): 
  LOOP: 
    IF (img2.x < img1.width OR img2.y < img1.height): 
        ADD: frames, (MOVE: img2, direction)
    IF NOT: 
        COLOR: img2, color
        LOOP END

MOVE: BOTTOM_RIGHT, (20, 30, 40)
MOVE: UP, (30, 10, 50)


GIF gif = CREATE GIF: frames, "~/Downloads"
```


## Notes
* May be able to use built-in java functions for image manipulation.
* For now, the image should be created as a file when the program is run (if we have time, we could implement a text editor for the DSL that will display the image in the same window)


## Summary of progress
* Have a draft of how our language will look like
* Have the features we want to include in our language
* Responsibilities have been scheduled and divided

# Milestone 3 (WIP)
Parser DSL
```
program          : (statement (NL statement)*)? EOF ;

statement        : INDENT? (function | control | with_statement) ;

control          : control_type COLON ;
control_type     : define_statement | loop_statement | if_statement ;

define_statement : DEFINE VARIABLE define_params ;
define_params    : BRACKET_START VARIABLE (BRACKET_SEP VARIABLE)* BRACKET_END ;

if_statement     : IF BRACKET_START comparison BRACKET_END ;
comparison       : num_or_var COMPARE num_or_var ;
loop_statement   : LOOP VARIABLE IN (range | VARIABLE) ;
range            : BRACKET_START num_or_var BRACKET_SEP num_or_var BRACKET_END ;

function         : FUNCTION_NAME value? (ON value)? (AS value)? ;
with_statement   : WITH VARIABLE COLON value ;

value            : num_or_var | TEXT ;
num_or_var       : (NUMBER | VARIABLE) (OPERATOR (NUMBER | VARIABLE))? ;
```
Lexer DSL
```
options { caseInsensitive=true; }

INDENT        : [ \t]+ ;
COMMENT       : '//' ~[\r\n] [\r\n]+ ;
DEFINE        : 'DEFINE' -> mode(OPTIONS_MODE);
IF            : 'IF' -> mode(OPTIONS_MODE);
LOOP          : 'LOOP' -> mode(OPTIONS_MODE);
WITH          : 'WITH' -> mode(OPTIONS_MODE);
FUNCTION_NAME : [a-z]+[a-z0-9]* -> mode(OPTIONS_MODE);

mode OPTIONS_MODE;
AS            : 'AS' ;
IN            : 'IN' ;
ON            : 'ON' ;
COLON         : ':' ;
BRACKET_START : '(' ;
BRACKET_SEP   : ',' ;
BRACKET_END   : ')' ;
OPERATOR      : '+' | '-' | '*' | '/' ;
COMPARE       : '>=' | '<=' | '>' | '<' | '=' ;
VARIABLE      : [a-z]+[a-z0-9]* ;
NUMBER        : '-'?[0-9]+ ;
TEXT          : '"' ~[\r\n"]* '"' ;
SP            : [ \t] -> channel(HIDDEN);
NL            : [\r\n]+ -> mode(DEFAULT_MODE);
```
Reference
- Variables:
  - Numbers:
    - Integers
    - Anywhere with a number value can have an equation
      - Equations are ```[number] [+|-|*|/] [number]```
        - Division rounds down
  - Images:
    - Stores pixels, width, and height
      - Fields are accessed by (?)
    - (Potentially?) Store images overlayed on it
  - List:
    - Stores a list of images
  - (Potentially?) Colours:
    - Stores r, g, b values
  - (Potentially?) Vectors:
    - Stores x, y, z values
- All variables/functions are case insensitive
- Variables are global scoped and can be accessed anywhere(?)
  - Parameters are function scoped
- Indents indicate function parameters and functions inside of if/loop/defines
```
// Load - Create an image variable from a image file
LOAD AS [variable name]
  WITH location: [file path]     // file path of image to be loaded

// Save - Create and save a gif from list of images
SAVE
  WITH frames: [list of images]  // list of images in sequence
  WITH duration: 30              // total time of gif in seconds
  WITH location: [file path]     // file path of the gif to be saved

// Custom functions
// - Parameters get deleted at the end of the function
DEFINE [function name] ([parameter 1], [parameter 2], [...]):
  [...]

// Calling custom functions
[function name]
  WITH [parameter 1]: [value]
  WITH [parameter 2]: [value]
  [...]

// Conditional
IF ([value] [>=, <=, >, <, =] [value]):
  [...]

// Loop - Loop over the inner statements from "from" to "to"
LOOP [variable name] IN ([from], [to]):
  [...]

// Loop - Loop over list
LOOP [variable name] IN [list of images]:

// Set - Assign value to global variable
SET [value] AS [variable name]

// Overlay - Create an image with an overlay of an image on top of another image
OVERLAY [above image] ON [below image] AS [variable name]
  WITH x: [x position]           // x position of the top-left corner of the above image
  WITH y: [y: position]          // y position of the top-left corner of the above image
  WITH rotation: [degrees]       // [Default=0] degrees in rotation of image

// List - Create an empty list and assign to a variable
LIST AS [variable name]

// Add - Add a copy of the image to list
ADD [image]
  WITH list: [list]
...
```