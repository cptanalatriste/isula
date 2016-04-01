# Isula: A Framework for Ant Colony Algorithms

Isula allows an easy implementation of Ant-Colony Optimization algorithms using the Java Programming Language. It contains the common elements present in the meta-heuristic to allow algorithm designers the reutilization of common behaviors. With Isula, solving optimization problems with Ant Colony can be done in few lines of code.

Isula in Action
---------------
If you are not familiar with the framework, a good place to start is the classic Travelling Salesman Problem:
* The Travelling Salesman Problem, using Ant System: https://github.com/cptanalatriste/aco-tsp
* The Travelling Salesman Problem, using Ant Colony System: https://github.com/cptanalatriste/aco-acs-tsp


Here are some advanced examples of optimization problems solved with Isula-based algorithms:
* The Flow-Shop Scheduling  Problem, using Max-Min Ant System: https://github.com/cptanalatriste/aco-flowshop
* Binary Image Segmentation using Ant System: https://github.com/cptanalatriste/aco-image-thresholding
* Image Clustering using Max-Min Ant System: https://github.com/cptanalatriste/aco-image-segmentation

An Isula Primer
---------------
To solve a problem with an Ant-Colony Optimization algorithm, you need a Colony of Agents (a.k.a Ants), a graph representing the problem, and a pheromone data-structure to allow communication between this agents. Isula tries to emulate that pattern:

```java
    ConfigurationProvider configurationProvider = new ProblemConfiguration();
    AcoProblemSolver<ImagePixel> problemSolver = new AcoProblemSolver<ImagePixel>();

    EnvironmentForImageThresholding environment = new EnvironmentForImageThresholding(
        imageGraph, ProblemConfiguration.NUMBER_OF_STEPS);

    ImageThresholdingAntColony antColony = new ImageThresholdingAntColony();
    antColony.buildColony(environment);

    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver.addDaemonActions(new StartPheromoneMatrix<ImagePixel>(),
        new RandomizeHive(), new PerformEvaporation<ImagePixel>());
    antColony.addAntPolicies(new NodeSelectionForImageThresholding(),
        new OnlinePheromoneUpdateForThresholding());

    problemSolver.solveProblem();
```
That's a snippet from the Image Thresholding  problem solution. Some things to notice there:
* Problem and algorithm configuration is provided by `ConfigurationProvider` instances. Make your own with the values you need.
* The class that does everything is `AcoProblemSolver`. In this case, we're using the same one provided by the framework but you can extend it to suit your needs.
* The Problem Solver needs an Environment that manages the problem graph and the pheromone matrix. You need to extend the `Environment` class provided with the framework to adjust it to support your problem.
* And we need an Ant Colony, of course. The Ant Colony main responsibility is to create Ants, and make them built solutions in iterations. The robust base `AntColony` class makes implementing this very easy.
* The hearth of the algorithm is the `Ant` class. You will need to define an Ant that suits your needs.
* Isula supports daemon actions -global behaviors- and ant-level policies, such as the ones present in multiple ACO Algorithms. You can add them easily to your current solver.
* Finaly, you call the `solveProblem()` method and wait for the best solution to be shown.

Isula in detail
---------------
The Javadoc of every class on the framework is available here: http://cptanalatriste.github.io/isula/doc/

Also, in this article Ant Colony Optimization algorithms are discussed and Isula is used to implement them in the Java Programming Language: http://www.codeproject.com/Articles/1011148/A-Java-Primer-of-Ant-Colony-Algorithms

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. You should be able to import it as an existing project to your current IDE.

Once the Isula Project is imported to your workspace, you can use it as a dependency on your personal Ant Colony Optimization project. Or if you prefer, you can generate a JAR file of the framework if it is more convenient for you.

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
