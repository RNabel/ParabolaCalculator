import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by rn30 on 25/10/2015.
 */
public class Main {
    public static void main(String[] args) {
        // Test Equation class.
        List<Equation.EquationPart> eParts = new ArrayList<>();
        eParts.add(new Equation.EquationPart("x", 2, 2));
        eParts.add(new Equation.EquationPart("y", 3, 5));

        List<Object> connectors = new ArrayList<>();
        connectors.add("+");

        Equation eq = new Equation(eParts, connectors);
        System.out.println(Equation.differentiateEquation(eq));
        System.out.println(eq);
    }
}
