import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    private static Scanner getScannerFromFile(String filename) throws FileNotFoundException {
        File inputFile = new File(filename);
        return new Scanner(inputFile);
    }
    private static Scanner getScannerFromUser(String fileDesc) {
        // Declare a Scanner object to read input
        Scanner input = new Scanner(System.in);

        // Prompt for an input file. Keep trying until a correct file is specified
        while (true) {
            try {
                System.out.println("Specify " + fileDesc + " filename: ");
                return getScannerFromFile( input.next() );
            } catch(FileNotFoundException e) {
                System.out.println("File not found. (Mistyped filename?)");
                System.out.println(e);
                //e.printStackTrace(); // More verbose
            }
        }
    }

    private static void loadProblemFromFile(ProblemInstance problem, String filename) throws FileNotFoundException {
        Scanner fileInput = getScannerFromFile(filename);
        problem.loadFromInput(fileInput);
    }
    private static void loadProblemFromUser(ProblemInstance problem) {
        Scanner fileInput = getScannerFromUser("problem");
        problem.loadFromInput(fileInput);
    }

    private static void loadSolutionFromFile(SolutionInstance sol, String filename) throws FileNotFoundException {
        Scanner fileInput = getScannerFromFile(filename);
        sol.loadFromInput(fileInput);
    }
    private static void loadSolutionFromUser(SolutionInstance sol) {
        Scanner fileInput = getScannerFromUser("solution");
        sol.loadFromInput(fileInput);
    }



    /** Tests whether the given solver can produce the correct solution for the given problem.
     *  @param problem The problem. Test will be rejected if this is null.
     *  @param solver The solver. Test will be rejected if this is null.
     *  @param correctSolution The correct solution. Can be null; then the solver's solution will not compare against this.
     *  @param isVerbose Whether to print statuses to System.out. Warnings and errors will be printed
     *                   regardless of this setting.
     *  @return True if the solver successfully gave the correct solution. False if the solver gave the wrong solution,
     *  or there is some diagnostic error.
     */
    public static boolean testProblem(
            ProblemInstance problem,
            Solver solver,
            SolutionInstance correctSolution,
            boolean isVerbose) {

        // Validate the parameters
        if (problem == null) {
            System.out.println("WARNING: `problem` is null. Skipping test");
            return false;
        }
        if (solver == null) {
            System.out.println("WARNING: `solver` is null. Skipping test");
            return false;
        }

        // Check problem for errors
        try {
            problem.validate();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        // Print problem stats
        if (isVerbose)
            problem.print(System.out);

        // Solve
        if (isVerbose)
            System.out.println("Solving...");
        SolutionInstance sol = solver.solve(problem);

        // Print solution
        if (isVerbose) {
            sol.print(System.out);
            sol.printTimeTaken(System.out);
        }

        // Check solution for errors
        if (isVerbose)
            System.out.println("Checking solution...");
        try {
            sol.validate(problem);
            if (correctSolution != null)
                sol.compare(correctSolution);

            if (isVerbose)
                System.out.println("Correct");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public static void batchTest(
            final int NUM_OF_TEST_CASES,
            Function<Integer,String> problemFilenameGenerator,
            Function<Integer,String> solutionFilenameGenerator,
            boolean isVerbose) {

        ProblemInstance problem = new ProblemInstance();
        Solver solver = new Solver(isVerbose ? System.out : null);
        SolutionInstance correctSolution = new SolutionInstance();

        if (isVerbose)
            System.out.println("Checking test cases...");

        for (int i = 1; i <= NUM_OF_TEST_CASES; ++i) {
            if (isVerbose)
                System.out.printf("\nRunning test case %d/%d\n", i, NUM_OF_TEST_CASES);

            String problemFilename = problemFilenameGenerator.apply(i);
            String solutionFilename = solutionFilenameGenerator.apply(i);

            try {
                loadProblemFromFile(problem, problemFilename);
                loadSolutionFromFile(correctSolution, solutionFilename);
            } catch (FileNotFoundException e) {
                System.out.println(e);
                continue;
            }

            testProblem(problem, solver, correctSolution, isVerbose);
        }

        if (isVerbose)
            System.out.println("Batch test completed.\n");
    }



    private enum ProgramMode {
        DIRECT_INPUT,
        LOAD_FROM_USER,
        GENERATE_RANDOM_PROBLEM
    }
    public static void main(String[] args) {
        final ProgramMode PROGRAM_MODE = ProgramMode.GENERATE_RANDOM_PROBLEM;

        System.out.println("--== EXT. EUCLIDEAN ALGORITHM ==--");

        /*batchTest(1,
                i -> "tests/in/" + i + ".in",
                i -> "tests/out/" + i + ".out",
                true);*/

        switch (PROGRAM_MODE) {
            case DIRECT_INPUT -> System.out.println("Mode: Input problem directly");
            case LOAD_FROM_USER -> System.out.println("Mode: Load problem from user-specified file");
            case GENERATE_RANDOM_PROBLEM -> System.out.println("Mode: Generate random problem");
        }

        // Load/generate problem
        ProblemInstance problem = new ProblemInstance();
        switch (PROGRAM_MODE) {
            case DIRECT_INPUT:
                problem.loadFromInput( new Scanner(System.in) );
                break;
            case LOAD_FROM_USER:
                loadProblemFromUser(problem); // tests/in/1.in
                break;
            case GENERATE_RANDOM_PROBLEM:
                problem.generateRandomProblem(10000);
                break;
        }

        // Load solution if applicable
        SolutionInstance correctSolution = null;
        if (PROGRAM_MODE == ProgramMode.LOAD_FROM_USER) {
            correctSolution = new SolutionInstance();
            loadSolutionFromUser(correctSolution); // tests/out/1.out
        }

        // Test the problem
        testProblem(problem, new Solver(System.out), correctSolution, true);
    }
}
