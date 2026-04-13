package scratch.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Monde {
    private static final int SIZE = 500;
    private static final int CENTER = SIZE / 2;
    private final double DEFAULT_ANGLE = 0.0;
    private final ObservableList<Segment> segments = FXCollections.observableArrayList();
    private final Personnage personnage;
    private final Point startPos;
    private final DoubleProperty startAngle = new SimpleDoubleProperty();
    private final ObservableList<VarDeclaration> variables = FXCollections.observableArrayList();

    private final Stack<Repeat> repeatStack = new Stack<>();
    private int execIdx;
    public Monde(Personnage personnage) {
        this.personnage = personnage;
        this.getPosPersonnageX().set(CENTER);   // initialisation de pos x de la tortue
        this.getPosPersonnageY().set(CENTER);   // initialisation de pos y de la tortue
        this.startPos = new Point(CENTER, CENTER); // initialisation du point de départ du monde
        this.startAngle.set(DEFAULT_ANGLE);     // angle de départ du monde
    }

    public ObservableList<VarDeclaration> getVariables() {
        return variables;
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
    public int getExecIdx() {
        return execIdx;
    }

    public void setExecIdx(int execIdx) {
        this.execIdx = execIdx;
    }

    public void nextExecIdx() {
        execIdx++;
    }

    public void pushLoop(Repeat repeat) {
        repeatStack.push(repeat);
    }

    public Repeat peekLoop() {
        if (repeatStack.isEmpty()) {
            throw new IllegalStateException("Aucune boucle active");
        }
        return repeatStack.peek();
    }

    public Repeat popLoop() {
        if (repeatStack.isEmpty()) {
            throw new IllegalStateException("Aucune boucle active");
        }
        return repeatStack.pop();
    }

    public boolean repeatStackEmpty() {
        return repeatStack.isEmpty();
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
        execIdx = 0;
        personnage.getPosition().getX().set(CENTER);
        personnage.getPosition().getY().set(CENTER);
        personnage.setAngle(startAngle.get());
        personnage.penDown();
    }

    public void declareVariable(String name) {
        if (isVariableDeclared(name)) {
            throw new IllegalArgumentException("Variable déjà déclarée : " + name);
        }
        variables.add(new VarDeclaration(name));
    }

    public boolean isVariableDeclared(String name) {
        for (VarDeclaration variable : variables) {
            if (variable.nameProperty().get().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getVariableValue(String name) {
        for (VarDeclaration variable : variables) {
            if (variable.nameProperty().get().equals(name)) {
                return variable.valueProperty().get();
            }
        }
        throw new IllegalArgumentException("Variable non déclarée : " + name);
    }

    public void setVariableValue(String name, int value) {
        for (VarDeclaration variable : variables) {
            if (variable.nameProperty().get().equals(name)) {
                variable.setValue(value);
                return;
            }
        }
        throw new IllegalArgumentException("Variable non déclarée : " + name);
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
