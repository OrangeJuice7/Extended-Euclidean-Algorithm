import java.io.PrintStream;

public class Solver {
    private PrintStream logStream;

    public Solver(PrintStream logStream) {
        setLogStream(logStream);
    }

    public boolean isVerbose() {
        return logStream != null;
    }
    public void setLogStream(PrintStream logStream) {
        this.logStream = logStream;
    }



    private void print(String format, Object... args) {
        if (isVerbose())
            logStream.printf(format, args);
    }
    private void printWorkingLine(long num, long coeff1, long coeff2) {
        print("(%9d, %5d, %5d)\n", num, coeff1, coeff2);
    }



    // Main Euclidean algorithm here
    private SolutionInstance solveNoStats(ProblemInstance problem) {
        SolutionInstance sol = new SolutionInstance();

        // Helper variables to init the others
        int f1 = problem.in1 > problem.in2 ? 1 : 0;
        int f2 = 1-f1;

        // The two most recent numbers to do the gcd of
        long a = Math.max(problem.in1, problem.in2);
        long b = Math.min(problem.in1, problem.in2);

        // Coeffs to get a and b from the original numbers a0 and b0
        long ac1 = f1, ac2 = f2;
        long bc1 = f2, bc2 = f1;

        printWorkingLine(a, ac1, ac2);
        printWorkingLine(b, bc1, bc2);

        while (b > 0) {
            long q = a/b;
            long r = a%b;

            a = b;
            b = r;

            /* r = a - q*b
             *   = (ac1*in1 + ac2*in2) - q*(bc1*in1 + bc2*in2)
             *   = (ac1 - q*bc1)*in1 + (ac2 - q*bc2)*in2
             */
            long nbc1 = ac1 - q*bc1;
            long nbc2 = ac2 - q*bc2;

            ac1 = bc1;
            ac2 = bc2;
            bc1 = nbc1;
            bc2 = nbc2;

            printWorkingLine(b, bc1, bc2);
        }

        sol.gcd = a;
        sol.coeff1 = ac1;
        sol.coeff2 = ac2;

        return sol;
    }

    public SolutionInstance solve(ProblemInstance problem) {
        long startTime = System.nanoTime();

        SolutionInstance sol = solveNoStats(problem);

        long duration = System.nanoTime() - startTime;
        sol.timeTaken = duration*.000000001f;

        return sol;
    }
}
