import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by rn30 on 25/10/2015.
 */
public class EquationTest {
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
        parts.add(new Equation.EquationPart("x", 3, 2));
        parts.add(new Equation.EquationPart("x", 5, 3));
        parts.add(new Equation.EquationPart("y", 2, 5));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+"); connectors.add("-");

        Equation test = new Equation(parts, connectors);

        // Check the string created.
        String formula = test.toString();
        assertTrue(formula.equals("3*x"));

    }
}