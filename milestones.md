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

