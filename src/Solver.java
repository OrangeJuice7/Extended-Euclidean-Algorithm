public class Solver {
    private void printWorkingLine(long num, long coeff1, long coeff2) {
        System.out.printf("(%9d, %5d, %5d)\n", num, coeff1, coeff2);
    }

    // Main Euclidean algorithm here
    public SolutionInstance solve(ProblemInstance problem) {
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
}
