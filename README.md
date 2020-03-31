# SoftRobotics
An implementation of soft robots, using the paper by Andrew Spielberg et al.
as reference. Some heavy simplifications were made, such as the learning step
only learning muscles to be used, rather than a full model of the system.

The goal is to show how soft robots can learn to deform to meet certain goals.
Here, the goal is for a random perturbation of points to deform into a goal
position (a bunny). It does this by alternating between learning the model 
of the system and optimizing over the tensions of muscles for linear proximity
of each of the points to its twin.
Once adding another muscle cannot help the opstimization any more, the 
learning stops.
Some artifacts in the model used should be expected.
