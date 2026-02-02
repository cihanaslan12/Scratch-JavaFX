package scratch.model;

public abstract class Action implements Commande {

    private final Personnage personnage;

    public Action(Personnage personnage) {
        this.personnage = personnage;
    }



}
