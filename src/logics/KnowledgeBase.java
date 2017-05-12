package logics;

import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jonathan on 5/11/17.
 */
public class KnowledgeBase {
    private LinkedList<Clause> data;

    public KnowledgeBase() {
        data = new LinkedList<>();
    }

    public void add(Clause clause) {
        data.add(clause);
    }

    public void calculate() {
        LinkedList<Clause> newData = new LinkedList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).pruned) continue;

            for (int j = 0; j < data.size(); j++) {
                if (j == i) continue;
                if (data.get(j).pruned) continue;

                Clause c = infer(data.get(i), data.get(j));

                if (c != null) {
                    newData.add(c);
                    data.get(i).prune();
                    data.get(j).prune();
                }
            }
        }

        if (newData.size() > 0) {
            for (int i = data.size() - 1; i == 0; i--) {
                newData.addFirst(data.get(i));
            }

            data.clear();
            data = newData;
            System.out.println("inference complete");
            System.out.println(this);
        } else {
            System.out.println("inference incomplete");
        }
    }

    private Clause infer(Clause c1, Clause c2) {
        Clause newClause;
        boolean nothingChanged = true;

        ArrayList<Atom> combinedAtoms = new ArrayList<>();

        for (int i = 0; i < c1.list().size(); i++)
            combinedAtoms.add(c1.list().get(i));
        for (int i = 0; i < c2.list().size(); i++)
            combinedAtoms.add(c2.list().get(i));

        for (int i = 0; i < c1.list().size(); i++) {
            for (int j = 0; j < c2.list().size(); j++) {
                Atom a1 = c1.list().get(i);
                Atom a2 = c2.list().get(j);

                if (a1.getValue().equals(a2.getValue()) && a1.getSign() != a2.getSign()) {
                    nothingChanged = false;

                    // Remove a1 and a2 from new clause
                    combinedAtoms.remove(a1);
                    combinedAtoms.remove(a2);
                }
            }
        }

        if (nothingChanged)
            return null;
        else {
            newClause = new Clause(combinedAtoms);
            return newClause;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).pruned)
                sb.append(data.get(i).toString() + "\n");
        }

        return sb.toString();
    }
}
