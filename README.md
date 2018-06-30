# Ciphered Text Editor
A text editor that provides support for enciphering and deciphering file contents according to a key/code. If quit through the supported keyboard command (as opposed to OS-level keyboard shortcuts such as Command-Q), contents will be enciphered before the file is saved if they are not enciphered when the user quits.
## Use
### Download
Make a pull request or download this repository as a zip file and extract the runnable jar. Run from terminal with `java -jar ./cipher.jar` or drag the jar to Desktop and double-click the icon.
### Current Implemented Keyboard Commands
- ESC: escape input mode, go to "vi" mode
- i: go to input mode. Allows text to be edited.
- ZZ: encipher if necessary, save, and quit
- ve: encipher contents based on code
- vd: decipher contents based on code
- Ve: set "enciphered" to true (allow decipher to be called)
- Vd: set "enciphered" to false (allow encipher to be called)

In addition, OS-level keyboard shortcuts for text editing will work in input mode.
## Built With
* Java
    * JFrame
## Acknowledgements
Credit and thanks to user Turk4n on Codecall for starter code related to the GUI. See tutorial here: http://forum.codecall.net/topic/49721-simple-text-editor/
