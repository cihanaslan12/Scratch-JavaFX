package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class Monde {
    private static final int SIZE = 500;
    private static final int CENTER = SIZE / 2;
    private final double DEFAULT_ANGLE = 0.0;
    private final ObservableList<Segment> segments = FXCollections.observableArrayList();
    private final Personnage personnage;
    private final Point startPos;
    private final DoubleProperty startAngle = new SimpleDoubleProperty();
    private final Map<String, Integer> variables = new HashMap<>();

    public Monde(Personnage personnage) {
        this.personnage = personnage;
        this.getPosPersonnageX().set(CENTER);   // initialisation de pos x de la tortue
        this.getPosPersonnageY().set(CENTER);   // initialisation de pos y de la tortue
        this.startPos = new Point(CENTER, CENTER); // initialisation du point de départ du monde
        this.startAngle.set(DEFAULT_ANGLE);     // angle de départ du monde
    }

    public int getWorldSize() {
        return SIZE;
    }

    public int getWorldOriginX() {
        return CENTER;
    }

    public int getWorldOriginY() {
        return CENTER;
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

    public DoubleProperty getPosPersonnageX() {
        return personnage.getX();
    }

    public DoubleProperty getPosPersonnageY() {
        return personnage.getY();
    }

    public DoubleProperty getPersonnageAngle() {
        return personnage.angleProperty();
    }

    public void reset() {
        segments.clear();
        variables.clear();
        personnage.getPosition().getX().set(CENTER);
        personnage.getPosition().getY().set(CENTER);
        personnage.setAngle(startAngle.get());
        personnage.penDown();
    }

    public void declareVariable(String name) {
        if (variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable déjà déclarée : " + name);
        }
        variables.put(name, 0);
    }

    public boolean isVaraibleDeclared(String name) {
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
        if(isVaraibleDeclared(text)) {
            return getVariableValue(text);
        }
        throw new IllegalArgumentException("Valeur invalide ou variable non déclarée : " + text);
    }
}
