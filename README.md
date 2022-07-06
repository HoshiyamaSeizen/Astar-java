<h1> A* Algorithm in Java with GUI</h1>

<h2> What does the app do?</h2>

The user enters a *weighted directed graph*. The application runs the A* algorithm, visually displays the process of the algorithm *step by step*, finds *the shortest path* from vertex A to vertex B and outputs *its length*.

<h2>Installation and launch</h2>
Make sure you have Maven installed and configured. Then go to the project folder and type the following to create a modular runtime image:

<code>mvn clean javafx:jlink</code>

It will create a folder with the application which you can launch:

<h4> Windows: </h4>

<code>.\target\AStar\bin\AStar.bat</code> (cmd)
<code>./target/AStar/bin/AStar.bat</code> (PowerShell)

<h4> Linux: </h4>

<code>./target/AStar/bin/AStar</code>

If instead you just want to build and run the project:

Set JAVA_HOME

<code>export JAVA_HOME=path/to/jdk </code> (e.g., /home/tyoma/.jdks/openjdk-18.0.1.1)

Save changes

<code>source ~/.bash</code> 

Run project

<code>mvn clean javafx:run</code> 

<h2>Input data</h2>

<li>Weighted directed graph (created by mouse clicks / import from file)</li>

<h2>Output data</h2>

<li>Visualization of the step-by-step process of the A* algorithm <br/></li>
<li>The shortest path from A to B and its length<br/></li>

<h2>App's features</h2>
<li>Create graph by mouse clicks / import from file (number of vertices + vertices coordinates + adjacency matrix with weights)</li>
<li>Heuristic choice: Euclidean distance, Manhattan distance and Chebyshev distance</li>
<li>Move to the next step in the algorithm and pause during rendering</li>
<li>Explanatory notes for the user</li>
<h2>User's Guide</h2>
<p> Do you need to find the shortest path in a graph from vertex A to vertex B?

Then, **define the graph** in one of two ways:
a) By *clicking on the window*, choosing the necessary actions: create a vertex, connect vertices, delete a vertex.
b) *Import the graph from a text file* that should contain the following information about the graph:
  ~ number of vertices in the graph
  ~ coordinates of the vertices
  ~ adjacency matrix of graph with weights

Next, **choose the heuristic** to be used in the A* algorithm:
a) *Chebyshev distance*
b) *Manhattan Metric*
c) *Euclidean distance*

**Press the "Run"** button to run the algorithm.
**Select the start and end vertex** of the path.
The algorithm is running.
You can pause the process of the algorithm and proceed to the next step of the algorithm.

As a result, you will get the shortest path from A to B and the length of this path.
You can also output the resulting graph along with the results to a text file.</p>
