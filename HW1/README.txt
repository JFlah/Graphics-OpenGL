Jack Flaherty
Assignment 1 README

Interaction features:

1) Press + to increase the size, - to decrease
	This works by increasing and decreasing the radius variable

2) Press T to increase the number of triangles, t to 	decrease
	This works by increasing and decreasing the triangles variable, which controls how many triangles are made via the loop and the incrementation of theta during drawing

3) Press r to change to a red color scheme, b for blue, g for green
	This works by listening for the key press and then changing the color values before rendering again - it basically increases the color value of the color you set it to, and then during rendering there is still a green shading effect taking place

4) Click anywhere in the window to stop or start rotation
	This is a basic mouse click listener that changes the theta incrementation variable to start or stop rotation

5) Check the 'Reverse' box to reverse rotation
	This works by checking the 'Reverse' box, which will then increment theta in a negative direction, thereby reversing the rotation

6) Check the 'Jump Around' box to move around randomly in the frame
	This works by checking the box, which will change the coordinates of the center of the circle by a random integer between -6 and 6.