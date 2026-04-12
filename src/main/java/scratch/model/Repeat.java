package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Repeat extends Action {

    private static final int DEFAULT_VALUE = 4;
    private StringProperty value = new SimpleStringProperty(String.valueOf(DEFAULT_VALUE)) ;
    private boolean loop;
    private final IntegerProperty start = new SimpleIntegerProperty();
    private final IntegerProperty remain = new SimpleIntegerProperty();

    public Repeat(boolean loop) {
        this.loop = loop;
    }
    public Repeat(int start, int remain) {
        this.loop = false;
        this.start.set(start);
        this.remain.set(remain);
    }

    public int getRepeatIndex() {
        return start.get();
    }
    public int getRemain() {
        return remain.get();
    }
    public void decrementRemain() {
        remain.set(remain.get() - 1);
    }
    public boolean isLoopStart() {
        return loop;
    }
    @Override
    public Type getType() {
        return loop ? Type.REPEAT : Type.END_REPEAT;
    }

    @Override
    public Action copyActionForProgram() {
        Repeat copy = new Repeat(loop);
        copy.setRawParameter(value.get());
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return value.get();
    }

    @Override
    public void setRawParameter(String text) {
        this.value.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        if (text == null ) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            int val = Integer.parseInt(text);
            return val > 0;
        }

        return VarDeclaration.isValidName(text);
    }
    @Override
    public boolean isEditable() {
        return loop;
    }

    @Override
    public String unite() {
        return loop ? "fois" : "";
    }

    @Override
    public String detailActionLabel() {
        return loop ? "Répeter" : "Fin répéter";
    }

    @Override
    public String stringForSave() {
        return loop ? "REPEAT;" + value.get() : "END_REPEAT";
    }

    @Override
    public void execute(Monde monde) {
        if (loop) {
            // je stocke "x" fois que la boucles doit tourner dans count
            int count = monde.resolveValue(value.get());
            if (count <= 0) {
                throw new IllegalStateException("Counter must be grater than 0");
            }
            // si la boucle doit tourner au moins une fois
            monde.pushLoop(new Repeat(monde.getExecIdx(), count));
        } else {
            if (monde.repeatStackEmpty()) {
                throw new IllegalStateException("End of repeat, without repeat");
            }
            // je récupère la première boucle rencontrer et la stocker dans une instance de repeat
            Repeat currentLoop = monde.peekLoop();
            // je decrémente le nombre de fois que la boucle doit tourner
            currentLoop.decrementRemain();
            if (currentLoop.getRemain() > 0) {
                monde.setExecIdx(currentLoop.getRepeatIndex()); // revient au corps et recommence l'exécution du bloc
            } else {
                monde.popLoop();// la fin de repeat donc je vide la pile
            }
        }
    }
    @Override
    public String toString() {
        if(!actionForProgram()) {
            return loop ? "Répéter" : "Fin répéter";
        }
        String s = value.get().isEmpty()  ? String.valueOf(4) : value.get(); // Par défaut c'est 4
        return loop ? "Répéter " + s + " fois" : "Fin répéter";
    }
}
