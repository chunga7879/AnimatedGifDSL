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
