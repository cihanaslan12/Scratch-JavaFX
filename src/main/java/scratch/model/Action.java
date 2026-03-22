package scratch.model;

import javafx.beans.property.IntegerProperty;

public abstract class Action implements Commande {

    private boolean inProgram = false;

    public boolean actionForProgram() {
        return inProgram;
    }
    public void setInProgram(boolean inProgram) {
        this.inProgram = inProgram;
    }
    public boolean isPenAction() {
        return false;
    }
    public boolean getPenState() {
        return false;
    }

    public abstract Type getType();
    public abstract  Action copyActionForProgram();

  //  public abstract IntegerProperty parameterProperty();
  //  public abstract Boolean isValidparametre(int param);

    public abstract String getRawParameter();
    public abstract void setRawParameter(String text);
    public abstract boolean isValidParameter(String text);

    public abstract boolean isEditable();
    public abstract String unite();
    public abstract String detailActionLabel();
    public abstract String stringForSave();

    public boolean hasTwoParameters() {
        return false;
    }
    public String getSecondParameter() {
        return "";
    }

    public void setSecondParameter(String text) {
    }

    public boolean isValidSecondParameter(String text) {
        return true;
    }

    public String detailSecondActionLabel() {
        return "";
    }
}
