import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static Scanner getScannerFromFile(String fileDesc) {
        // Declare a Scanner object to read input
        Scanner input = new Scanner(System.in);

        // Prompt for an input file. Keep trying until a correct file is specified
        File inputFile;
        Scanner fileInput;
        while (true) {
            try {
                System.out.println("Specify " + fileDesc + " filename: ");
                inputFile = new File(input.next());
                fileInput = new Scanner(inputFile);
                break;
            } catch(FileNotFoundException e) {
                System.out.println("File not found. (Mistyped filename?)");
                System.out.println(e);
                //e.printStackTrace(); // More verbose
            }
        }

        return fileInput;
    }

    private static void loadProblemFromFile(ProblemInstance problem) {
        Scanner fileInput = getScannerFromFile("problem");
        problem.loadFromInput(fileInput);
    }

    private static void loadSolutionFromFile(SolutionInstance sol) {
        Scanner fileInput = getScannerFromFile("solution");
        sol.loadFromInput(fileInput);
    }



    private static SolutionInstance solveAndRecordTime(ProblemInstance problem, Solver solver) {
        long startTime = System.nanoTime();
        SolutionInstance sol = solver.solve(problem);
        long duration = System.nanoTime() - startTime;
        System.out.printf("Time taken: %.4f s\n", duration*.000000001);
        return sol;
    }



    private enum ProgramMode {
        LOAD_FROM_FILE,
        GENERATE_RANDOM_PROBLEM
    }
    public static void main(String[] args) {
        final ProgramMode PROGRAM_MODE = ProgramMode.GENERATE_RANDOM_PROBLEM;
        final boolean RECORD_TIME_TAKEN = false;

        System.out.println("--== EXT. EUCLIDEAN ALGORITHM ==--");

        // Load/generate problem
        ProblemInstance problem = new ProblemInstance();
        switch (PROGRAM_MODE) {
            case LOAD_FROM_FILE:
                loadProblemFromFile(problem); // tests/in/1.in
                break;
            case GENERATE_RANDOM_PROBLEM:
                problem.generateRandomProblem(10000);
                break;
        }

        // Check problem for errors
        try {
            problem.validate();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Print problem stats
        problem.print(System.out);

	    // Solve, and capture the time taken
        Solver solver = new Solver();
        System.out.println("Solving...");
        SolutionInstance sol = RECORD_TIME_TAKEN
                ? solveAndRecordTime(problem, solver)
                : solver.solve(problem);

        // Print solution
        sol.print(System.out);

        // Load correct solution
        SolutionInstance correctSolution = new SolutionInstance();
        if (PROGRAM_MODE == ProgramMode.LOAD_FROM_FILE)
            loadSolutionFromFile(correctSolution); // tests/out/1.out

        // Check solution for errors
        try {
            sol.validate(problem);
            if (PROGRAM_MODE == ProgramMode.LOAD_FROM_FILE)
                sol.compare(correctSolution);
            System.out.println("Correct");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
