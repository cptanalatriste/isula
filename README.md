# Isula: A Framework for Ant Colony Algorithms

Isula allows an easy implementation of Ant-Colony Optimization algorithms using the Java Programming Language. It contains the common elements present in the meta-heuristic to allow algorithm designers the reutilization of common behaviors. With Isula, solving optimization problems with Ant Colony can be done in few lines of code.

Isula in Action
---------------
To get into speed, here are some samples of optimization problems solved with Isula-based algorithms:
* The Flow-Shop Scheduling  problem, using Max-Min Ant System: https://github.com/cptanalatriste/aco-flowshop
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
* Problem and algorithm configuration is provided by ConfigurationProvider instances. Make your own with the values you need.
* The class everything is the AcoProblemSolver. In this case, we're using the same one provided by the framework but you can extend it to suit your needs.
* The Problem Solver needs an Environment that manages the problem graph and the pheromone matrix. You need to extend the Environment class provided with the framework to adjust it to support your problem.
* And we need an Ant Colony, of course. The Ant Colony main responsibility is to create Ants, and make them built solutions in iterations. The robust base AntColony class makes implementing this very easy.
* The hearth of the algorithm is the Ant class. You will need to define an Ant that suits your needs.
* Isula supports daemon actions -global behaviors- and ant-level policies, such as the ones present in multiple ACO Algorithms. You can add them easily to your current solver.
* Finaly, you call the solveProblem() method and wait for the best solution to be shown.

Isula in detail
---------------
The Javadoc of every class on the framework is available here: http://cptanalatriste.github.io/isula/doc/

