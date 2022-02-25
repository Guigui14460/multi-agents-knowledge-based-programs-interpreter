package MAKBPInterpreter;

import java.util.Scanner;

/**
 * Main class allowing to choose the problem to be interpreted.
 */
public class Main {
    /**
     * Default executed method.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Problem to launch :");
        System.out.println("0. Muddy Children Problem (n=2, k=1)");
        System.out.println("1. Muddy Children Problem (n=2, k=2)");
        System.out.println("2. Muddy Children Problem (n=3, k=2)");
        System.out.println("3. Muddy Children Problem (n=3, k=3)");
        System.out.println("4. Muddy Children Problem (general)");
        System.out.println("-------------------------");

        System.out.println("Your choice : ");

        String stringChoice = scanner.nextLine();
        int choice = Integer.parseInt(stringChoice);
        switch (choice) {
            case 0:
                MuddyChildrenProblem.problemN2K1();
                break;
            case 1:
                MuddyChildrenProblem.problemN2K2();
                break;
            case 2:
                MuddyChildrenProblem.problemN3K2();
                break;
            case 3:
                MuddyChildrenProblem.problemN3K3();
                break;
            case 4:
                Main.generalizedMuddyChildrenProblem(scanner);
                break;
            default:
                System.out.println("No problem selected");
                break;
        }
        scanner.close();
    }

    /**
     * Retrieves additional arguments and runs the generalized muddy children
     * problem.
     * 
     * @param scanner current opened scanner object
     */
    private static void generalizedMuddyChildrenProblem(Scanner scanner) {
        System.out.println("Number of children : ");
        String nString = scanner.nextLine();
        int n = Integer.parseInt(nString);

        System.out.println("Real world : ");
        String realWorldString = scanner.nextLine();
        int realWorld = Integer.parseInt(realWorldString);

        System.out.println("Max number of iterations : ");
        String maxIterationString = scanner.nextLine();
        int maxIteration = Integer.parseInt(maxIterationString);

        MuddyChildrenProblem.problem(n, realWorld, maxIteration);
    }
}
