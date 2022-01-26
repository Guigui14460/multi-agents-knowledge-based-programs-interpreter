package MAKBPInterpreter;

import java.util.Scanner;

public class Main {
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
                System.out.println("Number of children : ");
                String nString = scanner.nextLine();
                int n = Integer.parseInt(nString);
                System.out.println("Real world : ");
                String realWorldString = scanner.nextLine();
                int realWorld = Integer.parseInt(realWorldString);
                MuddyChildrenProblem.problem(n, realWorld);
                break;
            default:
                break;
        }
        scanner.close();
    }
}
