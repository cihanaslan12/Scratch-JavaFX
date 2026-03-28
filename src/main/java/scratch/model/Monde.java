package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class Monde {

    private final ObservableList<Segment> segments = FXCollections.observableArrayList();
    private final Personnage personnage;
    private final Point startPos;
    private final int startAngle;
    private final Map<String, Integer> variables = new HashMap<>();

    public Monde(Personnage personnage, Point startPos, int startAngle) {
        this.personnage = personnage;
        this.startPos = startPos;
        this.startAngle = startAngle;
    }

    public Personnage getPersonnage() {
        return personnage;
    }

    public void addSegment(Segment s) {
        if (s != null)
            segments.add(s);
    }

    public ObservableList<Segment> getSegments() {
        return FXCollections.unmodifiableObservableList(segments);
    }

    public double getPosPersonnageX() {
        return personnage.getX();
    }

    public double getPosPersonnageY() {
        return personnage.getY();
    }

    public double getPersonnageAngle() {
        return personnage.getAngle();
    }

    public void reset() {
        segments.clear();
        variables.clear();
        personnage.setPosition(new Point(startPos.getX(), startPos.getY()));
        personnage.setAngle(startAngle);
        personnage.penDown();
    }

    public void declareVariable(String name) {
        if (variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable déjà déclarée : " + name);
        }
        variables.put(name, 0);
    }

    public boolean isVariableDeclared(String name) {
        return variables.containsKey(name);
    }

    public int getVariableValue(String name) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable non déclarée : " + name);
        }
        return variables.get(name);
    }

    public void setVariableValue(String name, int value) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable non déclarée : " + name);
        }
        variables.put(name, value);
    }

    public int resolveValue(String text) {
        if(text == null || text.isBlank()) {
            throw  new IllegalArgumentException("Valeur vide");
        }
        if (text.matches("-?\\d+")) {
            return Integer.parseInt(text);
        }
        if(isVariableDeclared(text)) {
            return getVariableValue(text);
        }
        throw new IllegalArgumentException("Valeur invalide ou variable non déclarée : " + text);
    }
}
