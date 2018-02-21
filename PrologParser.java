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
}
