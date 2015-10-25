import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Robin Nabel on 25/10/2015.
 * Class able to hold a basic polynomial equation and differentiate it.
 */
public class Equation {

    private List<EquationPart> parts = new ArrayList<>();
    private List<TermConnector> connectors = new ArrayList<>();

    // Class representing a * x ^ b
    public static class EquationPart {
        double a; // Coefficient.
        double b; // Exponent.
        String name; // Variable name.

        public EquationPart(String name, double a, double b) {
            this.a = a;
            this.b = b;
            this.name = name;
        }

        // No exponent.
        public EquationPart(String name, double a) {
            this(name, a, 1);
        }

        // For constant.
        public EquationPart(double val) {
            this.a = val;
            this.b = 0;
            this.name = "";
        }

        public static EquationPart differentiateEquationPart(EquationPart part) {
            double coefficientA = part.a * part.b;
            double exponentB = part.b - 1;
            if (exponentB < 0) {
                return null;
            }
            return new EquationPart(part.name, coefficientA, exponentB);
        }

        @Override
        public String toString() {
            String output = "";

            // Only print a if it is not 1
            if (this.a != 1.0) {
                output = this.a + "";

                if (printVariableName(this.b)) {
                    output += "*";
                }
            }

            // Only print exponent if not 1.
            if (b != 0) {
                output += this.name;
                if (b != 1) {
                    output += "^" + this.b;
                }
            }
            return output;
        }

        // Helpers.
        private boolean printVariableName(double b) {
            return b!=0;
        }

        public double getA() {
            return a;
        }

        public void setA(double a) {
            this.a = a;
        }

        public double getB() {
            return b;
        }

        public void setB(double b) {
            this.b = b;
        }
    }

    private enum TermConnector {
        PLUS, MINUS, MULTIPLY;

        // String to enum converter.
        public static TermConnector getTermConnector(String connector) {
            switch (connector) {
                case "+":
                    return PLUS;
                case "-":
                    return MINUS;
                case "*":
                    return MULTIPLY;
                default:
                    return null;
            }
        }


        @Override
        public String toString() {
            switch (this) {
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case MULTIPLY:
                    return "*";
                default:
                    return null;
            }
        }

        public static TermConnector invertSign(TermConnector input) {
            switch (input) {
                case PLUS:
                    input = MINUS;
                    break;
                case MINUS:
                    input = MINUS;
                    break;
                case MULTIPLY:
                    break;
            }
            return input;
        }
    }

    public Equation(List<EquationPart> parts, List<Object> connectors) {

        if (connectors.size() > 0 && connectors.get(0) instanceof String) {
            for (Object string : connectors) {
                String tempString = (String) string;
                this.connectors.add(TermConnector.getTermConnector(tempString));
            }

        } else if (connectors.size() > 0 && connectors.get(0) instanceof TermConnector) {
            for (Object termConnect : connectors) {
                TermConnector termConnector = (TermConnector) termConnect;
                this.connectors.add(termConnector);
            }
        }

        this.parts = parts;
    }

    public String createJsExpression(Map<String, Double> nameMapping) {
        // TODO create string.
        // If input is string
        // TODO
        // Else input is map
        // TODO

        return this.toString();
    }

    public static double evaluate(String formula) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return (double) engine.eval(formula);
    }

    public double evaluate(Map<String, Double> nameMapping) throws ScriptException {
        return evaluate(this.createJsExpression(nameMapping));
    }

//    public double evaluate(double oneVar) throws ScriptException {
//
//        return evaluate(this.createJsExpression(nameMapping))
//    }

    public static Equation differentiateEquation(Equation input) {
        // Differentiate all parts.
        List<EquationPart> eParts = new ArrayList<>();
        for (EquationPart part : input.parts) {
            EquationPart diffPart = EquationPart.differentiateEquationPart(part);

            if (diffPart != null) {
                eParts.add(diffPart);
            }
        }

        List<Object> connectors = input.connectors.stream().collect(Collectors.toList());

        // Update all + or - sign connectors as necessary.
        for (int i = 1; i < eParts.size(); i++) {
            // Invert connecting sign if coefficient is negative
            if (eParts.get(i).a < 0) {
                TermConnector currentConnector = (TermConnector) connectors.get(i);
                // Update the connector.
                TermConnector newConnector = TermConnector.invertSign(currentConnector);
                connectors.set(i, newConnector);
            }
        }

        return new Equation(eParts, connectors);
    }

    @Override
    public String toString() {
        String output = "";

        // Print first element.
        output += parts.get(0).toString();

        int connectorIndex = 0;
        boolean firstIter = true;
        for (EquationPart part : parts) {
            if (firstIter) {
                firstIter = false;
                continue;
            }
            // Print connector.
            output += " " + connectors.get(connectorIndex++).toString() + " ";

            // Print part.
            output += part.toString();
        }

        return output;
    }

    public EquationPart getParts(int index) {
        return parts.get(index);
    }
}
