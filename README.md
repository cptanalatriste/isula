# Isula: A Framework for Ant Colony Algorithms

Isula allows an easy implementation of Ant-Colony Optimization algorithms using the Java Programming Language. It contains the common elements present in the meta-heuristic to allow algorithm designers the reutilization of common behaviours. With Isula, solving optimization problems with Ant Colony can be done in few lines of code.

Isula in Action
---------------
To get into speed, here are some samples of optimization problems solved with Isula algorithms:
* The Flow-Shop Schedulling problem, using Max-Min Ant System: https://github.com/cptanalatriste/ACOImageSegmentationWithIsula
* Binary Image Segmentation using Ant System: https://github.com/cptanalatriste/ACOImageThresholdingWithIsula
* Image Clustering using Max-Min Ant System: https://github.com/cptanalatriste/ACOImageSegmentationWithIsula

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
