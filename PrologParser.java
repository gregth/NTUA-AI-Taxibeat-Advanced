import java.io.IOException;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class PrologParser {
    // Make Singleton Instance of the World Class
    private static final PrologParser instance = new PrologParser();

    private JIPEngine jip;
    private JIPTermParser parser;
    private JIPQuery jipQuery;
    private JIPTerm term;

    private PrologParser() {
        try {
            jip = new JIPEngine();
            jip.consultFile("prolog.pl");
            parser = jip.getTermParser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static PrologParser getInstance() {
        return instance;
    }

    // Asserts a predicate in prolog database
    void asserta(String predicate) {
        System.out.println("Asserted: " + predicate);
        jip.asserta(parser.parseTerm(predicate));
    }

    public void test() {
        try {

            String x, y;
            x = "ton";
            y = "mary";

            System.out.println("CASE 1");
            jipQuery = jip.openSynchronousQuery(parser.parseTerm("likes(" + x + "," + y + ")."));
            if (jipQuery.nextSolution() != null) {
                System.out.println("Yes. " + x + " likes " + y + ".");
            } else {
                System.out.println("No. " + x + " doesn't like " + y + ".");
            }
        } catch (Exception e) {
            System.out.println("Error in Prolog Parser");
            System.out.println(e.getMessage());
        }

	}

    public boolean canMoveFromTo(Node A, Node B) {
        double Ax = A.getX();
        double Ay = A.getY();
        double Bx = B.getX();
        double By = B.getY();

        String queryString = "canMoveFromTo(" + Ax + "," + Ay + "," + Bx + ","  + By + ").";
        jipQuery = jip.openSynchronousQuery(parser.parseTerm(queryString));
        if (jipQuery.nextSolution() != null) {
            System.out.println("Prolog query invoked. Can move from to");
            System.out.println(queryString);
            return true;
        } else {
            System.out.println("Prolog query invoked. Can not move from to");
            System.out.println(queryString);
            return false;
        }
    }
}
