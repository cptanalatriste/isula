# Isula: A Framework for Ant Colony Algorithms

[![Build Status](https://travis-ci.org/cptanalatriste/isula.svg?branch=master)](https://travis-ci.org/cptanalatriste/isula)
[![packagecloud](https://img.shields.io/badge/java-packagecloud.io-844fec.svg)](https://packagecloud.io/cgavidia/isula)


*For an in-depht discussion of the framework and its features you can read our paper in the SoftwareX journal: https://www.sciencedirect.com/science/article/pii/S2352711019300639*

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
    TspProblemConfiguration configurationProvider = new TspProblemConfiguration(problemRepresentation);
    AntColony<Integer, TspEnvironment> colony = getAntColony(configurationProvider);
    TspEnvironment environment = new TspEnvironment(problemRepresentation);

    AcoProblemSolver<Integer, TspEnvironment> solver = new AcoProblemSolver<>();
    solver.initialize(environment, colony, configurationProvider);
    solver.addDaemonActions(new StartPheromoneMatrix<Integer, TspEnvironment>(),
            new PerformEvaporation<Integer, TspEnvironment>());

    solver.addDaemonActions(getPheromoneUpdatePolicy());

    solver.getAntColony().addAntPolicies(new RandomNodeSelection<Integer, TspEnvironment>());
    solver.solveProblem();
```
That's a snippet from the Travelling Salesman Problem solution. Some things to notice there:
* Problem and algorithm configuration is provided by `ConfigurationProvider` instances. Make your own with the values you need.
* The class that does everything is `AcoProblemSolver`. In this case, we're using the same one provided by the framework but you can extend it to suit your needs.
* The Problem Solver needs an Environment that manages the problem graph and the pheromone matrix. You need to extend the `Environment` class provided with the framework to adjust it to support your problem.
* And we need an Ant Colony, of course. The Ant Colony main responsibility is to create Ants, and make them built solutions in iterations. The robust base `AntColony` class makes implementing this very easy.
* The hearth of the algorithm is the `Ant` class. You will need to define an Ant that suits your needs.
* Isula supports daemon actions -global behaviors- and ant-level policies, such as the ones present in multiple ACO Algorithms. You can add daemon actions to a solver via the `addDaemonActions` method and ant policies to a colony via the `addAntPolicies` method.
* Finaly, you call the `solveProblem()` method and wait for the best solution to be shown.

Isula internals
------------------
Here is a sequence diagram of the `solveProblem()` method, for you to get an idea on how isula works:

![Alt text](https://github.com/cptanalatriste/isula/blob/master/resources/ACO_metaheuristic.png?raw=true "Title")

Isula will provide you the basic execution flow for an algorithm in the ACO metaheuristic. On a common use case, you can rely on the implementations already available for `AcoProblemSolver` and `AntColony` but you are free to override and extend in case you need it. Take in mind that usually you will need to create your own `Ant` instance according to your project needs, however the base implementation already contains a lot of functionality available. If you need some reference, please take a look to the projects on the "Isula in action" section.

Every ACO algorithm has a set of customized behaviours that are executed during the solution processes: this behaviours can have global impact (`DaemonAction` instances, like pheromone update rules) or only affect an ant and its solution (like component selection rules: they are subclasses of `AntPolicy`). Isula already provides this behaviours for some representative algorithms -take a look at the `isula.aco.algorithms` package- but you might be in the need of defining your own policies or extending the ones already available.

Read about Isula
---------------
The Javadoc of every class on the framework is available here: http://cptanalatriste.github.io/isula/doc/

Also, in this article Ant Colony Optimization algorithms are discussed and Isula is used to implement them in the Java Programming Language: http://www.codeproject.com/Articles/1011148/A-Java-Primer-of-Ant-Colony-Algorithms


How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. 
As such, it is strongly recommended that you have Maven installed before working with Isula.

You can use Isula as a dependency on your personal Ant Colony Optimization project by adding the following
to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>isula</id>
        <url>https://packagecloud.io/cgavidia/isula/maven2
        </url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>isula</groupId>
        <artifactId>isula</artifactId>
        <version>2.0.1</version>
    </dependency>
</dependencies>
```

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
