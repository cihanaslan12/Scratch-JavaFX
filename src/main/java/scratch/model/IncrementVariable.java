package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IncrementVariable extends Action {

    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty value = new SimpleStringProperty("");

    public IncrementVariable() {

    }

    public IncrementVariable(String name, String value) {
        this.name.set(name);
        this.value.set(value);

    }

    @Override
    public Type getType() {
        return Type.VAR_INCREMENT;
    }

    @Override
    public Action copyActionForProgram() {
        IncrementVariable copy = new IncrementVariable();
        copy.setRawParameter(getRawParameter());
        copy.setSecondParameter(getSecondParameter());
        copy.setInProgram(true);
        return copy;
    }

    @Override
    public String getRawParameter() {
        return name.get();
    }

    @Override
    public void setRawParameter(String text) {
        name.set(text);
    }

    @Override
    public boolean isValidParameter(String text) {
        return VarDeclaration.isValidName(text);
    }

    @Override
    public boolean hasTwoParameters() {
        return true;
    }

    @Override
    public String getSecondParameter() {
        return value.get();
    }

    @Override
    public void setSecondParameter(String text) {
        value.set(text);
    }

    @Override
    public boolean isValidSecondParameter(String text) {
        if (text == null) {
            return false;
        }

        if (text.matches("-?\\d+")) {
            return true;
        }

        return VarDeclaration.isValidName(text);
    }
    @Override
    public String detailSecondActionLabel() {
        return "de";
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String unite() {
        return "";
    }

    @Override
    public String detailActionLabel() {
        return "Incrémentation de la variable";
    }

    @Override
    public String stringForSave() {
        return "INCREMENT_VARIABLE;" + name.get() + ";" + value.get();
    }

    @Override
    public void execute(Monde monde) {
        int currentValue = monde.getVariableValue(getRawParameter());
        int increment = monde.resolveValue(getSecondParameter());

        monde.setVariableValue(getRawParameter(), currentValue + increment);

    }
    @Override
    public String toString() {
        if(!this.actionForProgram()) {
            return "Inc/Dec variable";
        }

        return "Inc/Dec variable : " + name.get() + " de " + value.get();

    }
}
