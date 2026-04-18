package scratch.model;

public abstract class Action implements Commande {

    // indique si cette action est utilisé dans le programme
    // ou seulement dans la liste des actions disponibles
    private boolean inProgram = false;

    // true si l'action est dans le programme
    public boolean actionForProgram() {
        return inProgram;
    }

    // modifie l'état de l'action dans le programme ou non
    public void setInProgram(boolean inProgram) {
        this.inProgram = inProgram;
    }
    // indique si l'action conecerne le stylo
    public boolean isPenAction() {
        return false;
    }
    // retourne l'état du stylo
    public boolean getPenState() {
        return false;
    }

    public abstract Type getType();

    // crée une copie de l'action pour l'ajouter au programme
    public abstract  Action copyActionForProgram();


    // retourne le premier paramètre de l'action sous forme de texte (10, val)
    public abstract String getRawParameter();

    // modifie le premier paramètre de l'action
    public abstract void setRawParameter(String text);

    // vérifie si le premier paramètre est valide
    public abstract boolean isValidParameter(String text);

    // action peut être modifié ou non
    public abstract boolean isEditable();

    // unité affichée pour le paramètre ( turn : degré, move : pixels, ...)
    public abstract String unite();

    // texte descriptif du premier paramètre
    public abstract String detailActionLabel();

    // texte qu'on utilise pour la sauvegarde des fichiers
    public abstract String stringForSave();

    // indique si l'action possède un deuxième paramètre
    public boolean hasTwoParameters() {
        return false;
    }
    // retourne le deuxième paramètre sous forme de texte
    public String getSecondParameter() {
        return "";
    }

    // modifie le deuxième paramètre
    public void setSecondParameter(String text) {
    }

    // vérifie si le deuxième paramètre est valide
    public boolean isValidSecondParameter(String text) {
        return true;
    }

    // texte descriptif du deuxième paramètre
    public String detailSecondActionLabel() {
        return "";
    }
}
