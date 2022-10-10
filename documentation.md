## Documentation
This is the updated documentation file after Milestone 3.

### Basic
- This language contains a list of statements that are, in general, executed sequentially from top to bottom.
- The goal of this language is to create a GIF file from a series of static images (PNG, JPEG, etc...) that are manipulated by the statements

### Variables
- Types of variables
  - Numbers:
    - Integers
    - Anywhere with a number value can have an equation
      - Equations are ```[number] [+, -, *, /] [number]```
        - Division rounds down
  - Images:
    - Stores pixels, width, and height
  - Text:
    - Stores string characters
  - Colour:
    - Stores r, g, b values
    - Set by hex values
      - e.g. '#FFFFFF'
  - List:
    - Stores a list of images
- Variables are created when `AS [variable]` is used when a function is called
- All variables/functions are case-insensitive
  - e.g. `SET 10 AS X` is the same as `set 10 as x`
- Variables are global scoped and can be accessed anywhere if it has been created
  - Parameters are function scoped
- There are no reference variables, so multiple variables will never be a reference to the same object (i.e. image). Every built-in function will return a copy of any input and `RETURN` will always copy the value before setting to the function's `AS` variable.

#### Constants
- Constants are pre-defined variables
- User-defined variables cannot have the same name as constants
- All constants
  - Colours:
    - Black, Grey, White, Red, Green, Blue, Yellow, Cyan, Magenta, Orange, Purple

### Functions
- Functions are statements that performs an action
- **Function name** is the name of the function that is being called
- **Target** is the main input of the function
  - This may not exist depending on the function
- **Return variable** is the variable that will be assigned the return value of the function
  - This is required unless the function does not return anything or when the shortcut is used
  - Shortcut: If a function returns a value, and you insert a variable into the target, you can omit the `AS [variable]` to assign the return value to the same variable. 
    - e.g. `FUNCTION var1` is equivalent to `FUNCTION var1 AS var1`
- **Parameters** are the secondary inputs of the function
  - In built-in functions, some parameters are optional
  - Can be in any order as long as all the required parameters are given values
  - `WITH` statements must follow the function call with indents (see Indent Rule)
```
[function name] [target] AS [return variable]
  WITH [parameter 1]: [value 1]
  WITH [parameter 2]: [value 2]
  ...
```

#### Define statement
- Define statements create a custom function
- Target and parameters are copies of the inputs passed in
    - Target and parameters get deleted at the end of the function
- Target, parameters, and return value are optional
- **Return value** will be assigned to the return variable after the function completes
```
DEFINE [function name] [target] WITH ([parameter 1], [parameter 2], [...]):
  [...]
  RETURN [return value]
```

### Control statements
- Control statements determine if / how its inner statements get executed
- Each inner statement is indicted by indents (see Indent Rule)

Conditional - Execute the inner statements if the evaluation succeeds
```
IF ([value] [>=, <=, >, <, =, !=] [value]):
  [...]
```
Loop - Loop over the inner statements from numbers "from" to "to"
- *Iterator variable* is assigned each number from "from" to "to" during the loop iterations
```
LOOP [iterator variable] IN ([from], [to]):
  [...]
```
Loop - Loop over the inner statements for each of the elements in the list
- *Iterator variable* is assigned an image on each loop iteration from start to end of the list
```
LOOP [iterator variable] IN [list of images]:
  [...]
```

#### Indent Rule
Indents indicate function parameters and functions inside of if/loop/defines. The only rules are:
  1) parameters / if / loop / define ends when it returns to the same indent level or below,
  2) indent level shouldn't change if there is no function / if / loop / define before it. Tabs are equivalent to 2 spaces.

### Built-in Functions
#### File System
Load - Create an image variable from a image file
```
LOAD [file path] AS [variable name]
```
Save - Create and save a gif from list of images, each image represents a frame in  the final gif
- Duration: total time of gif in seconds
- Location: file path of the gif to be saved
```
SAVE [list of images]
  WITH duration: [number]
  WITH location: [file path]
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
CREATE-RECTANGLE AS [variable name]
  WITH width: [number]
  WITH height: [number]
  WITH colour: [colour]
```
Write - Write text as an image
```
WRITE [text] AS [variable name]
  WITH size: [font size]
  WITH font: [font]
```
Colour Fill - Fill each pixel of an image with a colour but maintain its transparency
```
COLOUR-FILL [image] AS [variable name]
  WITH colour: [colour]
```
Opacity - Sets transparency of image (0 = transparent, 100 = opaque)
```
SET-OPACITY [image] AS [variable name]
  WITH amount: [number]
```
Filter - Apply a filter to an image 

*Supported Filters:* `"invert"`, `"greyscale"`, `"blur"`, `"sharpen"`, `"sepia"`, `"chrome"`
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
CREATE-LIST AS [variable name]
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
Create Colour - Create a colour from RGB values
```
CREATE-COLOUR AS [variable name]
  WITH r: [number]
  WITH g: [number]
  WITH b: [number]
```

#### Getters
Get R - Get R value (number) of colour
```
GET-R [colour] AS [variable name]
```
Get G - Get G value (number) of colour
```
GET-G [colour] AS [variable name]
```
Get B - Get B value (number) of colour
```
GET-B [colour] AS [variable name]
```
