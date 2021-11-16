package MAKBPInterpreter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Problem to launch :");
        System.out.println("0. Muddy Children Problem");
        System.out.println("-------------------------");
        System.out.println("Your choice :");
        String stringChoice = scanner.nextLine();
        int choice = Integer.parseInt(stringChoice);
        switch (choice) {
        case 0:
            MuddyChildrenProblem.main(args);
            break;

        default:
            break;
        }
        scanner.close();
    }
}
