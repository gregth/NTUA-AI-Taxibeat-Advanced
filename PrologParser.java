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

    public boolean canMoveFromTo(SearchNode A, SearchNode B) {
        double Ax = A.getNode().getX();
        double Ay = A.getNode().getY();
        double Bx = B.getNode().getX();
        double By = B.getNode().getY();

        String queryString = "canMoveFromTo(" + Ax + "," + Ay + "," + Bx + ","  + By + ").";
        jipQuery = jip.openSynchronousQuery(parser.parseTerm(queryString));
        if (jipQuery.nextSolution() != null) {
            return true;
        } else {
            return false;
        }
    }

    public double calculateFactor(SearchNode A, SearchNode B) {
        double Ax = A.getNode().getX();
        double Ay = A.getNode().getY();
        double Bx = B.getNode().getX();
        double By = B.getNode().getY();

        String queryString = "weightFactor(" + Ax + "," + Ay + "," + Bx + ","  + By + ", Value).";
        jipQuery = jip.openSynchronousQuery(parser.parseTerm(queryString));
		term = jipQuery.nextSolution();
		if (term != null) {
            String factorString = term.getVariablesTable().get("Value").toString();
            double factor = Double.parseDouble(factorString);
            return factor;
		} else {
            System.out.println("Factor calculation failed :(");
            return -1;
        }
    }

    public boolean isQualifiedDriver(int driverID) {
        String queryString = "isQualifiedDriverForClient(" + driverID + ").";
        jipQuery = jip.openSynchronousQuery(parser.parseTerm(queryString));
        if (jipQuery.nextSolution() != null) {
            return true;
        } else {
            return false;
        }
    }

    public double getDriverRank(int driverID) {
        String queryString = "driverRank(" + driverID + ",Rank).";
        jipQuery = jip.openSynchronousQuery(parser.parseTerm(queryString));
		term = jipQuery.nextSolution();
		if (term != null) {
            String rankString = term.getVariablesTable().get("Rank").toString();
            double rank = Double.parseDouble(rankString);
            return rank;
		} else {
            System.out.println("Rank calculation failed :(");
            return -1;
        }
    }


}
