import java.util.Random;

/**
 * Simulated annealing
 * @author Lucia Blondel
 */

public class SimulatedAnnealing {
        
        private final int[][] distanceMatrix;
        private int[] path;
        
        private final Tool tool = new Tool();

        
        public SimulatedAnnealing(int[][] distanceMatrix) {
                this.distanceMatrix = distanceMatrix;
        }

        public void simulatedAnnealing() {
                long start = System.nanoTime();
                
                double T = 100;
                int[] current = new NearestNeighbor(distanceMatrix, 0).getPath();
                int[] best = current.clone();
                
                while(T > 0.1 * Math.pow(10, -300)){
                        if (((System.nanoTime()) - start) * Math.pow(10, -9) > 180.0) {
                                break;
                        }
                        int i = 0;
                        while( i < 400) {
                                int[] next = new TwoOpt(current, distanceMatrix,true).getPath();
                                if(tool.pathEquals(next, current) == true) {
                                        break;
                                }
                                int deltaE = tool.computeCost(next, distanceMatrix) - tool.computeCost(current, distanceMatrix);
                                
                                if(deltaE < 0) {
                                        System.out.println("HERE1");
                                        current = next.clone();
                                        if(tool.computeCost(next, distanceMatrix) < tool.computeCost(current, distanceMatrix)) {
                                                if(tool.pathEquals(best, next) == true) {
                                                        break;
                                                }
                                                best = next.clone();
                                        }
                                } else {
                                        System.out.println("HERE2");
                                        Random a = new Random();
                                        int r = a.nextInt(1);
                                        if(r < Math.exp(-deltaE/T)) {
                                                current = next.clone();
                                        }
                                }
                                i++;
                        }
                        T = 0.95 * T;
                }
                
                path = new TwoOpt(best, distanceMatrix,false).getPath();
                
        }
        
        public int[] getPath() {
                return path;
        }
}