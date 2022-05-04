import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/** Problem:
 *  Given two positive integers in1 and in2, use the extended Euclidean algorithm to determine their greatest common
 *  divisor (GCD), and coefficients coeff1 and coeff2 to in1 and in2 respectively such that coeff1*in1 + coeff2*in2 = gcd.
 */
public class ProblemInstance {
    public long in1, in2;

    public ProblemInstance() {
        in1 = in2 = 0;
    }
    public ProblemInstance(long in1, long in2) {
        this.in1 = in1;
        this.in2 = in2;
    }

    public void loadFromInput(Scanner input) {
        // Skip comments
        /*while (input.next().equals("#")) {
            input.nextLine();
        }*/

        // Read in in1 and in2
        in1 = input.nextInt();
        in2 = input.nextInt();
    }

    public void generateRandomProblem(final long MAX_VALUE) {
        Random rng = new Random();

        in1 = rng.nextLong(MAX_VALUE)+1;
        in2 = rng.nextLong(MAX_VALUE)+1;
    }

    /**
     *  Do a basic check to see if this problem has the correct format. Throws an Exception if not.
     */
    public void validate() throws Exception {
        if (in1 <= 0)
            throw new Exception("in1 is not positive");

        if (in2 <= 0)
            throw new Exception("in2 is not positive");
    }

    public void print(PrintStream out) {
        out.println("Inputs: (" + in1 + "," + in2 + ")");
    }
}
