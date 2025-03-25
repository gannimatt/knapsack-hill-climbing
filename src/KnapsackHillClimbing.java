import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class KnapsackHillClimbing {
    private static final int MAX_ITERATIONS = 10000;
    private static int capacity;
    private static int[] weights;
    private static int[] values;
    private static int itemCount;

    public static void main(String[] args) throws FileNotFoundException {

        loadData("src\\data");

        boolean[] currentSolution = generateValidInitialSolution();
        int currentBestValue = evaluate(currentSolution);

        System.out.println("Initial solution with value: " + currentBestValue);

        boolean[] bestSolution = currentSolution.clone();
        int bestValue = currentBestValue;

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            boolean[] neighborSolution = generateNeighbor(bestSolution);
            int neighborValue = evaluate(neighborSolution);

            System.out.println("Searched solution: " + vectorToString(neighborSolution) + " with value: " + neighborValue);

            if (neighborValue > bestValue) {
                bestSolution = neighborSolution.clone();
                bestValue = neighborValue;
                System.out.println("New best found: " + vectorToString(bestSolution) + " with value: " + bestValue);
            }
        }

        System.out.println("Best solution found: " + vectorToString(bestSolution) + " with total value: " + bestValue);
    }

    private static void loadData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        capacity = scanner.nextInt();
        int[] tempWeights = new int[100];
        int[] tempValues = new int[100];
        int index = 0;

        while (scanner.hasNextInt()) {
            tempWeights[index] = scanner.nextInt();
            tempValues[index] = scanner.nextInt();
            index++;
        }

        weights = new int[index];
        values = new int[index];
        itemCount = index;
        System.arraycopy(tempWeights, 0, weights, 0, index);
        System.arraycopy(tempValues, 0, values, 0, index);
    }

    private static boolean[] generateValidInitialSolution() {
        boolean[] vector = new boolean[itemCount];
        Random rand = new Random();
        int totalWeight = 0;

        for (int i = 0; i < itemCount; i++) {
            if (rand.nextBoolean() && (totalWeight + weights[i] <= capacity)) {
                vector[i] = true;
                totalWeight += weights[i];
            } else {
                vector[i] = false;
            }
        }
        return vector;
    }

    private static boolean[] generateNeighbor(boolean[] solution) {
        Random rand = new Random();
        boolean[] neighbor = solution.clone();
        int index;
        do {
            index = rand.nextInt(itemCount);
            neighbor[index] = !neighbor[index];
        } while (!isValidSolution(neighbor));
        return neighbor;
    }

    private static boolean isValidSolution(boolean[] solution) {
        return evaluate(solution) > 0;
    }

    private static int evaluate(boolean[] solution) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        return (totalWeight <= capacity) ? totalValue : 0;
    }

    private static String vectorToString(boolean[] vector) {
        StringBuilder sb = new StringBuilder();
        for (boolean b : vector) {
            sb.append(b ? 1 : 0);
        }
        return sb.toString();
    }
}
