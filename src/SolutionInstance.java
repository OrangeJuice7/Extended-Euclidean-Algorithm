import java.io.PrintStream;
import java.util.Scanner;

public class SolutionInstance {
    public long gcd;
    public long coeff1, coeff2;

    // Statistics
    float timeTaken; // in seconds

    public void loadFromInput(Scanner input) {
        gcd = input.nextInt();
    }

    /**
     *  Do a basic check to see if this solution has the correct format for the problem given.
     *  Does not guarantee that this solution actually satisfies the problem.
     */
    public void validate(ProblemInstance problem) throws Exception {
        if (problem.in1 * coeff1 + problem.in2 * coeff2 != gcd)
            throw new Exception("Coefficients are incorrect");
    }

    /**
     *  Checks that this solution is the same as the given solution.
     *  If there is a difference, this throws an exception specifying where the difference is.
     */
    public void compare(SolutionInstance other) throws Exception {
        if (this.gcd != other.gcd)
            throw new Exception("GCD is incorrect");
    }

    public void print(PrintStream out) {
        out.println("GCD: " + gcd);
        out.println("Coeffs: (" + coeff1 + "," + coeff2 + ")");
    }
    public void printTimeTaken(PrintStream out) {
        out.printf("Time taken: %.4f s\n", timeTaken);
    }
}
