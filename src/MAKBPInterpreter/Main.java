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
        System.out.println("-------------------------");
        System.out.println("Your choice :");
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

            default:
                break;
        }
        scanner.close();
    }
}
