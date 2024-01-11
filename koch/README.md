# Koch's Snowflake Etude 2
## Author
- Vincent Lee

This code was tested on Windows 11 with Visual Studio Code (Version 1.76.1) using the Extension Pack for Java (Version 0.25.9).

Compile and run the program. A new window will pop up and allow you to interact with the snowflake.

This program allows the user to draw a Koch snowflake and manipulate it on a JPanel. The user can adjust the order of the snowflake, zoom in and out, reset the zoom, reset the entire snowflake, invert the snowflake, and draw the snowflake in both normal and inverted orientations. The user can also click and drag the snowflake to move its position.

The snowflake is drawn using a recursive algorithm that calculates and draws each line segment. The order of the snowflake determines the number of times this algorithm is repeated.
## Testing
Having a visual aid was very helpful with testing the program. Before calculating the point that joins the two segments with an equilateral triangle, I was able to draw the pattern without the point.  By calculating the length of the line segment, I could print out the values of the two new line segments and make sure that they aligned. By using only one of the 3 initial lines, I could check to see that program was correctly splitting the lines like it should, without getting overwhelmed with a large number of points. Once I knew one line was working, I knew the other 2 would work too. When calculating the middle point, I could use the program and see if the point was in the right place. 