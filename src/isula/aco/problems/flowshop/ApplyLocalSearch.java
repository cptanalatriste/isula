package isula.aco.problems.flowshop;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import java.util.ArrayList;
import java.util.List;

public class ApplyLocalSearch extends AntPolicy<Integer> {

  public ApplyLocalSearch() {
    super(AntPolicyType.AFTER_SOLUTION_IS_READY);
  }

  @Override
  public void applyPolicy(Environment environment,
      ConfigurationProvider configuration) {
    // TODO(cgavidia): This needs a HUGE REFACTOR!

    AntForFlowShop antForFlowShop = (isula.aco.problems.flowshop.AntForFlowShop) getAnt();
    double makespan = getAnt().getSolutionQuality(environment);
    Integer[] currentSolution = getAnt().getSolution();
    Integer[] localSolutionJobs = new Integer[currentSolution.length];

    List<Integer> jobsList = new ArrayList<Integer>();

    for (int job : currentSolution) {
      jobsList.add(job);
    }

    List<Integer> localSolution = jobsList;

    int indexI = 0;
    boolean lessMakespan = true;

    while (indexI < (currentSolution.length) && lessMakespan) {
      int jobI = localSolution.get(indexI);
      localSolution.remove(indexI);
      int indexJ = 0;

      while (indexJ < currentSolution.length && lessMakespan) {
        localSolution.add(indexJ, jobI);

        Integer[] intermediateSolution = new Integer[currentSolution.length];
        int anotherIndex = 0;

        for (int sol : localSolution) {
          intermediateSolution[anotherIndex] = sol;
          anotherIndex++;
        }

        double newMakespan = antForFlowShop.getScheduleMakespan(
            intermediateSolution, environment.getProblemGraph());

        if (newMakespan < makespan) {
          makespan = newMakespan;
          lessMakespan = false;
        } else {
          localSolution.remove(indexJ);
        }

        indexJ++;
      }

      if (lessMakespan) {
        localSolution.add(indexI, jobI);
      }
      indexI++;
    }

    int index = 0;
    for (int job : localSolution) {
      localSolutionJobs[index] = job;
      index++;
    }
    getAnt().setSolution(localSolutionJobs);
  }

}
