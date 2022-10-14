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

# Milestone 3
### Parser DSL
```
program          : (statement (NL statement)*)? EOF ;

statement        : INDENT? (function | with_statement | return_statement | control | COMMENT) ;

control          : control_type COLON ;
control_type     : define_statement | loop_statement | if_statement ;

define_statement : DEFINE VARIABLE VARIABLE (DEFINE_WITH define_params)? ;
define_params    : BRACKET_START VARIABLE (BRACKET_SEP VARIABLE)* BRACKET_END ;

if_statement     : IF BRACKET_START comparison BRACKET_END ;
comparison       : num_or_var COMPARE num_or_var ;
loop_statement   : LOOP VARIABLE IN (range | VARIABLE) ;
range            : BRACKET_START num_or_var BRACKET_SEP num_or_var BRACKET_END ;

function         : FUNCTION_NAME value? (ON value)? (AS value)? ;
with_statement   : WITH VARIABLE COLON value ;

return_statement : RETURN value ;

value            : num_or_var | TEXT ;
num_or_var       : (NUMBER | VARIABLE) (OPERATOR (NUMBER | VARIABLE))? ;
```
### Lexer DSL
```
options { caseInsensitive=true; }

INDENT        : [ \t]+ ;
COMMENT       : '//' ~[\r\n]* -> mode(OPTIONS_MODE);
DEFINE        : 'DEFINE' -> mode(OPTIONS_MODE);
IF            : 'IF' -> mode(OPTIONS_MODE);
LOOP          : 'LOOP' -> mode(OPTIONS_MODE);
WITH          : 'WITH' -> mode(OPTIONS_MODE);
RETURN        : 'RETURN' -> mode(OPTIONS_MODE);
FUNCTION_NAME : [a-z]+[_\-a-z0-9]* -> mode(OPTIONS_MODE);

mode OPTIONS_MODE;
AS            : 'AS' ;
IN            : 'IN' ;
ON            : 'ON' ;
DEFINE_WITH   : 'WITH' ;
COLON         : ':' ;
BRACKET_START : '(' ;
BRACKET_SEP   : ',' ;
BRACKET_END   : ')' ;
OPERATOR      : '+' | '-' | '*' | '/' ;
COMPARE       : '>=' | '<=' | '>' | '<' | '=' | '!=' ;
VARIABLE      : [a-z]+[_\-a-z0-9]* ;
NUMBER        : '-'?[0-9]+ ;
TEXT          : '"' ~[\r\n"]* '"' ;
SP            : [ \t] -> channel(HIDDEN);
NL            : [\r\n]+ -> mode(DEFAULT_MODE);
```
### Reference
- Variables:
  - Numbers:
    - Integers
    - Anywhere with a number value can have an equation
      - Equations are ```[number] [+, -, *, /] [number]```
        - Division rounds down
  - Images:
    - Stores pixels, width, and height
    - (Potentially?) Store images overlayed on it
  - Text:
    - Stores string characters
  - List:
    - Stores a list of images
- All variables/functions are case-insensitive (e.g. `SET 10 AS X` is the same as `set 10 as x`)
- Variables are global scoped and can be accessed anywhere if it has been created
  - Parameters are function scoped
- Variables are created when `AS [variable]` is used
- Multiple variables will never be a reference to the same object (i.e. mainly images). Every built-in function will return a copy of any input and `RETURN` will always copy the value before setting to the function's `AS` variable.
- Indents indicate function parameters and functions inside of if/loop/defines. The only rules are: 1) parameters/if/loop/define ends when it returns to the same indent level or below, 2) indent level shouldn't change if there is no function/if/loop/define before it. Tabs are equivalent to 2 spaces.
- Shortcut: If a function returns a value, and you insert a variable into the target, you can omit the `AS [variable]` to assign the return value to the same variable. (e.g. `FUNCTION var1` is equivalent to `FUNCTION var1 AS var1`)

#### File System
Load - Create an image variable from a image file
```
LOAD [file path] AS [variable name]
```
Save - Create and save a gif from list of images
- Duration: total time of gif in seconds
- Location: file path of the gif to be saved
```
SAVE [list of images]
  WITH duration: [number]
  WITH location: [file path]
```
#### Control statements

Conditional - Execute the inner statements if the evaluation succeeds
```
IF ([value] [>=, <=, >, <, =, !=] [value]):
  [...]
```
Loop - Loop over the inner statements from "from" to "to"
```
LOOP [variable name] IN ([from], [to]):
  [...]
```
Loop - Loop over the inner statements for each of the elements in the list
```
LOOP [variable name] IN [list of images]:
  [...]
```
#### Images
Overlay - Create an image with an overlay of an image on top of another image
 - X: x position of the top-left corner of the above image
 - Y: y position of the top-left corner of the above image
 - Rotation: degrees in rotation of image (clock-wise)
```
OVERLAY [above image] ON [below image] AS [variable name]
  WITH x: [x position]
  WITH y: [y: position]
  WITH rotation: [degrees]
```
Rectangle - Create a rectangle image with size and colour
```
RECTANGLE AS [variable name]
  WITH width: [number]
  WITH height: [number]
  WITH r: [number]
  WITH g: [number]
  WITH b: [number]
```
Write - Write text as an image
```
WRITE [text] AS [variable name]
  WITH size: [font size]
  WITH font: [font]
```
Color - Fill each pixel of an image with a colour but maintain its transparency
```
COLOR [image] AS [variable name]
  WITH r: [number]
  WITH g: [number]
  WITH b: [number]
```
Opacity - Sets transparency of image (0 = transparent, 100 = opaque)
```
OPACITY [image] AS [variable name]
  WITH amount: [number]
```
Filter - Apply a filter to an image
```
FILTER [image] AS [variable name]
  WITH filter: [filter name]
```
#### Custom Functions
Define - Create a custom function
 - Target and parameters are copies of the input passed in
   - Target and parameters get deleted at the end of the function
 - Target, parameters, and return value are optional
```
DEFINE [function name] [target] WITH ([parameter 1], [parameter 2], [...]):
  [...]
  RETURN [return value]
```
Calling custom functions
 - If there is a return value, it gets assigned to the return variable
 - Parameters can be in any order
```
[function name] [target] AS [return variable]
  WITH [parameter 1]: [value]
  WITH [parameter 2]: [value]
  [...]
```
#### Variables
Set - Assign value to global variable
```
SET [value] AS [variable name]
```
Random - Generate a random number between min and max (inclusive)
```
RANDOM AS [variable name]
  WITH min: [number]
  WITH max: [number]
```
List - Create an empty list and assign to a variable
```
LIST AS [variable name]
```
Add - Add a copy of an item to list
```
ADD [list]
  WITH item: [item]
```
Add - Add a list to another list (items will be copied)
 - The final result will have order: `list1`, `list2`
```
ADD [list1]
  WITH list: [item2]
```
### Progress
- Initial DSL grammar is done
- Initial core AST / code runner is done
- No roadmap changes

### User-Study

A link to the Google Doc that contains the user-study can be found [here](https://docs.google.com/document/d/1vJIDnICeQTAP0XjK7LEF1eORv3plqajaRAmD7GMD3wQ/edit#)

#### Feedback
- The parameters to functions can be a bit confusing
  - Hard to remember parameter names for each function
- e.g. Functions that have parameters for RGB separately often get mix-matched when user calls the function
```
// user calls COLOR with R, G, B order
COLOR image
  WITH r: 0
  WITH g: 0
  WITH b: 0

// user calls COLOR R, B, G order
COLOR image
  WITH r: 0
  WITH b: 0
  WITH g: 0
```
- Learnability:
  - The language was easy to understand and pick up
- Users do need to have a basic understanding of how GIFs are created
  - It wasn’t obvious what the list of image frames was, and why we were adding to it
- Did not call OVERLAY with the image on the background after each image manipulation
  - Perhaps user was assuming that the image once overlaid on the background the first time, will implicitly get overlaid in subsequent image manipulation calls
- For the built-in FILTER function, user thinks it’s more readable to have one FILTER function and specify what filter we want, instead of creating separate built-in functions for each filter
  - Perhaps needs documentation on what filters are available
- COLOR was an intuitive name for the built-in function to color images

# Milestone 4

### Current progress:
- Tokenization & parser rules have been defined.
- ParseTree to AST conversion (WIP).
- AST base code has been defined for language features (under-review).
- Visitor interface has been defined (under-review).
- Built-in functions (WIP).
- `IF`, `LOOP`, user-defined functions, variable declaration and assignment are implemented (WIP).

### User study (WIP):
Plans for user study #1 (done on MVP or finished implementation) can be found [here](https://docs.google.com/document/d/1HMK92j9mUhBFW2DignROPrhLQZzBKzrWGZMWgreDc28/edit#heading=h.m0fumzvjd1ow). (*DISCLAIMER: user task has not been performed yet*)

Plans for user study #2 (done on MVP or finished implementation) can be found [here](https://docs.google.com/document/d/13dpW9UDKtEYe3Ni2mFj5QlVg7VM9vQWI4IcQqZAkxTw/edit#heading=h.1cfvfprzdix3). (*DISCLAIMER: user task has not been performed yet*)

### Planned timeline for remaining days:

#### General:
| What                             | When                    |
|----------------------------------|-------------------------|
| MVP                              | 10/10/2022              |
| User study #2                    | 10/10/2022 - 10/14/2022 |
| User study #3                    | 10/10/2022 - 10/14/2022 | 
| Final product                    | 10/10/2022 - 10/14/2022 | 
| Final testing                    | 10/10/2022 - 10/14/2022 |
| Video                            | 10/15/2022 / 10/16/2022 | 

#### Specific breakdown of remaining tasks:

*Built-in functions:*

| Task | Who |
|------|-----|
 |`LOAD`| Katherine |
|`OVERLAY`| Chunga |
| `COLOUR-FILL` | Katherine |
| `OPACITY`| Katherine |
| `FILTER` | Katherine |
| `GET-RGB` | Katherine |
| `CREATE-COLOUR` | Katherine |
| `CREATE-LIST` | Shiven |
| `RANDOM` | Shiven | 
|`CROP` | Chunga |
| `GET-WIDTH` | Chunga | 
| `GET-HEIGHT` | Chunga |
| `TRANSLATE` | Chunga | 
| `RECTANGLE` | Chunga |
| `WRITE` | Chunga|
| `ROTATE` | Chunga | 

*Video*

| Task | Who |
|------|-----|
|Video script| Katherine |
|Video editing| Chunga & Devon |
|Video voice-over | TBD |

*Other* 

| Task                               | Who      |
|------------------------------------|----------|
| Final testing of completed product | Everyone |
| Unit testing of functionality      | Everyone |
| Static checking                    | Shiven   |
| Runtime exceptions                 | Everyone |

# Milestone 5
### Status of Implementation
- Full working build - currently testing & bug fixing
- Further improvements:
  - Error logging
  - Some missing static checks

### Final User Study

#### User Study #2 - Case 1
https://docs.google.com/document/d/13dpW9UDKtEYe3Ni2mFj5QlVg7VM9vQWI4IcQqZAkxTw/edit#
- Easier for loop to use natural language
- Add to frames was not intuitive
- Overlay was not intuitive

#### User Study #2 - Case 2
[TODO]

### Changes to Language Design
- Slight change to scoping rules to overwrite variables in certain cases
- Renamed some functions

### Planned timeline

| What                         | When          | Who                      |
|------------------------------|---------------|--------------------------|
| Script                       | 10/14         | Katherine                |
| Recording video              | 10/14 - 10/15 | Shiven                   |
| Editing video                | 10/15 - 10/16 | Chunga & Devon           |
| Bug fixing & testing         | 10/14 - 10/16 | Emiru, Katherine, Shiven |
| Final implementation changes | 10/14 - 10/15 | Emiru & Shiven           |



