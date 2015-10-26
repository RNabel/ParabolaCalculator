import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rn30 on 25/10/2015.
 */
public class ParabolaCalculatorTest {
    @Test
    public void testGreatestCommonFactor() {
        int numA = 12;
        int numB = 4;
        int greatestCommonF = ParabolaCalculator.findGCF(numA, numB);

        assertEquals(greatestCommonF, 4.0, 0.0001);

        numA = 16;
        numB = 24;
        greatestCommonF = ParabolaCalculator.findGCF(numA, numB);

        assertEquals(greatestCommonF, 8.0, 0.0001);
    }

    @Test
    public void testSimplifyFraction() {
        // Check it scales correctly.
        int a = 6;
        int b = 8;

        int[] result = ParabolaCalculator.simplifyFraction(a, b);
        assertEquals(result[0], 3);
        assertEquals(result[1], 4);

        // Check that it cancels out the negative sign if possible.
        a = -2;
        b = -5;
        result = ParabolaCalculator.simplifyFraction(a, b);
        assertEquals(result[0], 2);
        assertEquals(result[1], 5);

        // Check both works mechanisms work at the same time.
        a = -3;
        b = -6;
        result = ParabolaCalculator.simplifyFraction(a, b);
        assertEquals(result[0], 1);
        assertEquals(result[1], 2);
    }

    @Test
    public void testAddFraction() {
        int[] fracA = new int[]{2, 3};
        int[] fracB = new int[]{4, 5};
        int[] result = ParabolaCalculator.addFractions(fracA, fracB);

        assertTrue(Arrays.equals(result, new int[]{22, 15}));
    }

    @Test
    public void testSubstractFraction() {
        int[] fracA = new int[]{2, 3};
        int[] fracB = new int[]{4, 5};
        int[] result = ParabolaCalculator.subtractFractions(fracA, fracB);

        assertTrue(Arrays.equals(result, new int[]{-2, 15}));
    }

    @Test
    public void testMultiplyFraction() {
        int[] fracA = new int[]{2, 3};
        int[] fracB = new int[]{4, 5};
        int[] result = ParabolaCalculator.multiplyFractions(fracA, fracB);

        assertTrue(Arrays.equals(result, new int[]{8, 15}));
    }

    @Test
    public void testDivideFraction() {
        int[] fracA = new int[]{2, 3};
        int[] fracB = new int[]{4, 5};
        int[] result = ParabolaCalculator.divideFractions(fracA, fracB);

        assertTrue(Arrays.equals(result, new int[]{5, 6}));
    }

    @Test
    public void testRetr_Dec() throws Exception {
        double num = 123.123;
        double result = ParabolaCalculator.retr_dec(num);

        assertEquals(3, result, 0.001);

        num = 100.01230000;
        result = ParabolaCalculator.retr_dec(num);
        assertEquals(4, result, 0.001);
    }

    @Test
    public void testConstructFraction() throws Exception {
        // Integer fraction.
        double input = 2.0;
        int[] frac = ParabolaCalculator.constructFraction(input);

        assertTrue(Arrays.equals(frac, new int[]{2, 1}));

        // Decimal fraction
        input = 3.452;
        frac = ParabolaCalculator.constructFraction(input);
        assertTrue(Arrays.equals(frac, new int[]{863, 250}));
    }

    @Test
    public void testGetParabolaCoeffs() throws Exception {
        double[] a = new double[]{0, 0};
        double[] b = new double[]{1, 1};
        double[] c = new double[]{2, 1};

        int[][] result = ParabolaCalculator.getParabolaCoefficients(a, b, c);
        assertTrue(Arrays.equals(result[0], new int[]{1, -2}));
        assertTrue(Arrays.equals(result[1], new int[]{3, 2}));
        assertTrue(Arrays.equals(result[2], new int[]{0, -1}));
    }

    @Test
    public void testGetParabolaCoeffs2() throws Exception {
        double[] a = new double[]{1, 1};
        double[] b = new double[]{2, 2};
        double[] c = new double[]{3, 1};

        int[][] result = ParabolaCalculator.getParabolaCoefficients(a, b, c);
        assertTrue(Arrays.equals(result[0], new int[]{1, -1}));
        assertTrue(Arrays.equals(result[1], new int[]{4, 1}));
        assertTrue(Arrays.equals(result[2], new int[]{-2, 1}));
    }

    @Test
    public void testCreateEquationFromCoeffs() throws Exception {
        double[] a = new double[]{0, 0};
        double[] b = new double[]{1, 1};
        double[] c = new double[]{2, 1};


        Equation equation = ParabolaCalculator.createEquationFromParabolaCoefficients(
                a, b, c);

        String formula = equation.toString();
        assertTrue(formula.equals("-0.5*Math.pow(x, 2.0) + 1.5*x + -0.0"));
    }

    @Test
    public void testGetFirstPart() throws Exception {
        double b1 = -1.5;
        double x = 1;
        double sqrtPOfX = 1;
        fail("Fix");
//        double result = ParabolaCalculator.getFirstPart(b1, x, sqrtPOfX);
//        assertTrue(result == -0.5);
    }

    @Test
    public void testGetSecondPart() throws Exception {
        double b1 = -1.5;
        double c1 = 1.0;
        double x = 1;
        double sqrtPOfX = 1;
        fail("FIX");
//        double result = ParabolaCalculator.getSecondPart(c1, b1, x, sqrtPOfX);
//        assertEquals(-0.69315, result, 0.001);

    }

    @Test
    public void testCalculateIntegral() throws Exception {
        double b1 = -1.5;
        double x;
        double sqrtPOfX = 1;

        // f(x) = -x + 1.5
        List<Object> connectors = new ArrayList<>();
        connectors.add("+");

        List<Equation.EquationPart> partList = new ArrayList<>();
        partList.add(new Equation.EquationPart("x", -0.5, 2));
        partList.add(new Equation.EquationPart("x", 1.5));
        Equation diffEq = new Equation(partList, connectors);

        x = 2;
        fail("FIX");
//        double result = ParabolaCalculator.calculateIntegral(b1, x, diffEq);
//        assertEquals(0.4527, result, 0.001);

        x = 1;
//        result = ParabolaCalculator.calculateIntegral(b1, x, diffEq);
//        assertEquals(-0.597, result, 0.001);
    }

    @Test
     public void testGetArcDistance() throws Exception {
        double[] A = new double[]{1,1};
        double[] B = new double[]{0,0};
        double[] C = new double[]{2,1};

        double result = ParabolaCalculator.getArcDistance(A, B, C);

        assertEquals(1.04, result, 0.01);
    }

    @Test
    public void testGetArcDistance2() throws Exception {
        double[] A = new double[]{1, 1};
        double[] B = new double[]{2, 4};
        double[] C = new double[]{3, 1};

        double result = ParabolaCalculator.getArcDistance(A, B, C);

        assertEquals(6.499059, result, 0.001);
    }
}