import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rn30 on 25/10/2015.
 * Class able to calculate a parabola Equation based on 3 points passed.
 * It is further able to find the length between two points on the line.
 * Converted to Java from code used at http://www.analyzemath.com/parabola/three_points_para_calc.html
 */
public class ParabolaCalculator {

    // Calculates Arc length between 2 points on a parabola.
    // Takes: 3 points, to calculate the parabola.
    // Returns: The distance between the first and last point.
    // Uses substitution of the first differential with sinh(t).
    public static double getArcDistance(double[] A, double[] B, double[] C) {
        Equation equation = createEquationFromParabolaCoefficients(A, B, C);

        double[] xInterval = new double[]{A[0], C[0]};

        // Calculate distance using: http://math.stackexchange.com/questions/390080/definite-integral-of-square-root-of-polynomial

        // Differentiate equation.
        Equation firstDiffEq = Equation.differentiateEquation(equation);
        Equation secondDiffEq = Equation.differentiateEquation(firstDiffEq);

        double yDoubleDash = secondDiffEq.getParts(0).getA();

        // Calculate 1/2 sinh-1 (yDash)
        double startYDash = 0;
        double endYDash = 0;
        try {
            startYDash = firstDiffEq.evaluate(xInterval[0]);
            endYDash = firstDiffEq.evaluate(xInterval[1]);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        double part1 = calculateIntegral(yDoubleDash, startYDash);
        double part2 = calculateIntegral(yDoubleDash, endYDash);

        return part2 - part1;
    }

    // --- Arc Length helpers. ---
    protected static double calculateIntegral(double yDoubleDash, double yDash) {
        double part1FirstTerm = getFirstPart(yDash);
        double part1SecondTerm = getSecondPart(yDash);
        return (1 / yDoubleDash) * (part1FirstTerm + part1SecondTerm);
    }

    // Calculates the equations below.
    // 0.5 * sinh-1 (yDash)
    // 0.5 * ln (yDash + sqrt ( 1 + yDash ^ 2 ) )
    protected static double getFirstPart(double yDash) {
        return 0.5 * Math.log(yDash + Math.sqrt(1 + yDash * yDash));
    }

    protected static double getSecondPart(double yDash) {
        return 0.5 * yDash * Math.sqrt(1 + yDash * yDash);
    }

    protected static Equation createEquationFromParabolaCoefficients(double[] A, double[] B, double[] C) {
        // Calculate the parabola coefficients
        int[][] coeffs = getParabolaCoefficients(A, B, C);

        // If the points are in one line, parabola can not be calculated,
        //    and line coefficients need to be calculated.
        if (coeffs == null) {
            // TODO create line coefficient calculating function.
            return null;
        } else {
            // Convert the coefficients into EquationPart objects.
            List<Equation.EquationPart> partList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int[] tempCoeff = coeffs[i];
                double coeff = tempCoeff[0] / (double) tempCoeff[1];

                partList.add(new Equation.EquationPart("x", coeff, 2 - i));
            }

            // Create Equation object.
            List<Object> connectors = new ArrayList<>();
            connectors.add("+");
            connectors.add("+");

            return new Equation(partList, connectors);
        }
    }

    // --- End of Arc helper methods. ---

    // --- Helper methods for parabola coefficient calculation. ---
    public static int[][] getParabolaCoefficients(double[] A, double[] B, double[] C) {

        int[] ax = constructFraction(A[0]);
        int[] ay = constructFraction(A[1]);

        int[] bx = constructFraction(B[0]);
        int[] by = constructFraction(B[1]);

        int[] cx = constructFraction(C[0]);
        int[] cy = constructFraction(C[1]);


        // Calculate coefficients
        int[] A1 = subtractFractions(multiplyFractions(ax, ax), multiplyFractions(bx, bx));
        int[] B1 = subtractFractions(ax, bx);
        int[] C1 = subtractFractions(ay, by);


        int[] A2 = subtractFractions(multiplyFractions(bx, bx), multiplyFractions(cx, cx));
        int[] B2 = subtractFractions(bx, cx);
        int[] C2 = subtractFractions(by, cy);

        int[] det = determinant(A1, B1, A2, B2);
        int[] deta = determinant(C1, B1, C2, B2);
        int[] detb = determinant(A1, C1, A2, C2);

        if (det[0] != 0) {

            // calculate coefficients coefa, coefb and coefc
            int[] coefa = divideFractions(deta, det);
            int[] coefb = divideFractions(detb, det);
            int[] cc1 = multiplyFractions(coefa, ax);
            int[] cc2 = subtractFractions(ay, multiplyFractions(cc1, ax));

            int[] coefc = subtractFractions(cc2, multiplyFractions(coefb, ax));

            // Display equation of the form y = a x^2 + b x + c
            return new int[][]{coefa, coefb, coefc};
        } else {
            return null;
        }
    }

    public static int[] determinant(int[] a, int[] b, int[] c, int[] d) {
        return subtractFractions(multiplyFractions(a, d), multiplyFractions(b, c));
    }

    public static int[] constructFraction(double x) {
        int[] fraction;

        if (Math.floor(x) == x && !Double.isInfinite(x)) { //test for integer
            fraction = simplifyFraction((int) x, 1);

        } else { // test for decimal
            // how many digits after decimal n?
            int numOfDigitsAfterDecPoint = retr_dec(x);

            // Multiply numerator until no more decimal points needed.
            int numerator = (int) (x * Math.pow(10, numOfDigitsAfterDecPoint));

            fraction = simplifyFraction(numerator, (int) Math.pow(10, numOfDigitsAfterDecPoint));
        }

        return fraction;
    }

    // Returns number of digits after the decimal point.
    public static int retr_dec(double num) {
        return Double.toString(num).split("[.]")[1].length();
    }

    public static int[] addFractions(int[] a, int[] b) {
        return simplifyFraction(a[0] * b[1] + b[0] * a[1], a[1] * b[1]);
    }

    public static int[] subtractFractions(int[] a, int[] b) {
        return simplifyFraction(a[0] * b[1] - b[0] * a[1], a[1] * b[1]);
    }

    public static int[] multiplyFractions(int[] a, int[] b) {
        return simplifyFraction(a[0] * b[0], a[1] * b[1]);
    }

    public static int[] divideFractions(int[] a, int[] b) {
        return simplifyFraction(a[0] * b[1], a[1] * b[0]);
    }

    // Cancel out unnecessary factors and signs if possible.
    public static int[] simplifyFraction(int a, int b) {
        int x = a;
        int y = b;

        int gcf = findGCF(x, y);

        // Cancel out terms if possible.
        x = a / gcf;
        y = b / gcf;

        // If both negative cancel out sign.
        if (x < 0 && y < 0) {
            x = -a / gcf;
            y = -b / gcf;
        }

        return new int[]{x, y};
    }

    // Find greatest common factor of two numbers.
    public static int findGCF(int a, int b) {
        // Get absolute values.
        a = Math.abs(a);
        b = Math.abs(b);

        // To avoid zero division.
        if (a == 0) return b;

        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }

        return a;
    }

}
