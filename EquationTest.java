import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by rn30 on 25/10/2015.
 */
public class EquationTest {
    @Test
    public void testEquationPartMethods() throws Exception {
        double a = 1.3;
        double b = 2.4;

        // Test Constructors.
        Equation.EquationPart part = new Equation.EquationPart("hello", a, b);
        assertTrue(part.getA() == a);
        assertTrue(part.getB() == b);
        assertTrue(part.getName().equals("hello"));

        part = new Equation.EquationPart("hello", a);
        assertTrue(part.getA() == a);
        assertTrue(part.getB() == 1);
        assertTrue(part.getName().equals("hello"));

        part = new Equation.EquationPart(a);
        assertTrue(part.getA() == a);
        assertTrue(part.getB() == 0);
        assertTrue(part.getName().equals(""));

        // Test Getters and Setters.
        double temp = 4.5;
        part.setA(temp);
        assertTrue(part.getA() == temp);

        part.setB(temp);
        assertTrue(part.getB() == temp);

        part.setName("BLA");
        assertTrue(part.getName().equals("BLA"));
    }

    @Test
    public void testEquationPartToString() throws Exception {
        // Test for b = 1.
        Equation.EquationPart part = new Equation.EquationPart("name", 1.4, 1);

        String output = part.toString();
        assertTrue(output.equals("1.4*name"));

        // Test for b = 0.
        part = new Equation.EquationPart("name", 1.4, 0);
        output = part.toString();
        assertTrue(output.equals("1.4"));

        // Test for b !=0 and b != 1.
        part = new Equation.EquationPart("name", 1.4, 2);
        output = part.toString();
        assertTrue(output.equals("1.4*Math.pow(name, 2.0)"));
    }

    @Test
    public void testEquationDifferentiate() throws Exception {
        List<Object> connectors = new ArrayList<>();
        connectors.add("+");
        connectors.add("+");

        List<Equation.EquationPart> partList = new ArrayList<>();
        partList.add(new Equation.EquationPart("x", 2, 2));
        partList.add(new Equation.EquationPart("x", 0.2, 2));

        Equation testEq = new Equation(partList, connectors);

        Equation diffEq = Equation.differentiateEquation(testEq);

        for (int i = 0; i < partList.size(); i++) {
            // Test that multiplication of coefficient happened.
            assertTrue(diffEq.getParts(i).getA() == testEq.getParts(i).getB() * testEq.getParts(i).getA());

            // Test that exponent was decremented.
            assertTrue(diffEq.getParts(i).getB() == testEq.getParts(i).getB() - 1);
        }
    }

    @Test
    public void testEquationDifferentiateInvert() throws Exception {
        List<Object> connectors = new ArrayList<>();
        connectors.add("+");

        List<Equation.EquationPart> partList = new ArrayList<>();
        partList.add(new Equation.EquationPart("x", 2, -2));
        partList.add(new Equation.EquationPart("x", 0.2, -2));

        Equation testEq = new Equation(partList, connectors);

        Equation diffEq = Equation.differentiateEquation(testEq);

        assertTrue(diffEq.toString().equals("-4.0*Math.pow(x, -3.0) - 0.4*Math.pow(x, -3.0)"));
    }

    @Test
    public void testEquationDiffTermElimination() throws Exception {
        List<Object> connectors = new ArrayList<>();
        connectors.add("+");
        connectors.add("+");

        List<Equation.EquationPart> partList = new ArrayList<>();
        partList.add(new Equation.EquationPart(2));
        partList.add(new Equation.EquationPart("x", 0.2, 2));

        Equation testEq = new Equation(partList, connectors);
        Equation diffEq = Equation.differentiateEquation(testEq);

        assertTrue(diffEq.getParts(0).getA() == 0.4);
        assertTrue(diffEq.getParts(0).getB() == 1);
    }

    @Test
    public void testJavaScriptStringCreation() throws Exception {
        Map<String, Double> valueMap = new HashMap<>();
        valueMap.put("x", 2.0);
        valueMap.put("y", 3.4);

        List<Equation.EquationPart> parts = new ArrayList<>();
        parts.add(new Equation.EquationPart(2));
        parts.add(new Equation.EquationPart("x", 0.2, 2));
        parts.add(new Equation.EquationPart("y", 1, 3));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");
        connectors.add("-");

        Equation test = new Equation(parts, connectors);

        // Check the string created.
        String formula = test.toString();
        assertTrue(formula.equals("2.0 + 0.2*Math.pow(x, 2.0) - Math.pow(y, 3.0)"));

    }

    @Test
    public void testJsStringSubstitutionMap() throws Exception {
        Map<String, Double> valueMap = new HashMap<>();
        valueMap.put("x", 2.0);
        valueMap.put("y", 3.4);

        List<Equation.EquationPart> parts = new ArrayList<>();
        parts.add(new Equation.EquationPart("x", 3, 2));
        parts.add(new Equation.EquationPart("x", 5, 3));
        parts.add(new Equation.EquationPart("y", 2, 5));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");
        connectors.add("-");

        Equation test = new Equation(parts, connectors);

        String jsExpr = test.createJsExpression(valueMap);

        assertTrue(jsExpr.equals("3.0*Math.pow(2.0, 2.0) + 5.0*Math.pow(2.0, 3.0) - 2.0*Math.pow(3.4, 5.0)"));
    }

    @Test
    public void testJsStringSubstitutionSingleVal() throws Exception {
        double val = 4;

        List<Equation.EquationPart> parts = new ArrayList<>();
        parts.add(new Equation.EquationPart("x", 3, 2));
        parts.add(new Equation.EquationPart("x", 5, 3));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");

        Equation test = new Equation(parts, connectors);

        String jsExpr = test.createJsExpression(val);
        assertTrue(jsExpr.equals("3.0*Math.pow(4.0, 2.0) + 5.0*Math.pow(4.0, 3.0)"));
    }


    @Test
    public void testEval() throws Exception {
        String input = "3 * 4 / 2.0 + 3";
        double output = Equation.evaluate(input);

        assertTrue(output == 9);
    }

    @Test
    public void testEvalSingleVal() throws Exception {
        List<Equation.EquationPart> parts = new ArrayList<>();
        parts.add(new Equation.EquationPart("x", 3, 2));
        parts.add(new Equation.EquationPart("x", 5, 3));
        parts.add(new Equation.EquationPart("x", 5));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");
        connectors.add("+");

        Equation test = new Equation(parts, connectors);

        double output = test.evaluate(12);

        assertTrue(output == 9132);
    }

    @Test
    public void testEvalMap() throws Exception {
        List<Equation.EquationPart> parts = new ArrayList<>();
        parts.add(new Equation.EquationPart("x", 3, 2));
        parts.add(new Equation.EquationPart("y", 5, 3));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");

        Equation test = new Equation(parts, connectors);

        Map<String, Double> valMap = new HashMap<>();
        valMap.put("x", 12.0);
        valMap.put("y", 10.0);
        double output = test.evaluate(valMap);

        assertTrue(output == 5432); //TODO
    }
}