package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Monde {

    private final ObservableList<Segment> segments = FXCollections.observableArrayList();
    private final Personnage personnage;

    public Monde(Personnage personnage){
        this.personnage = personnage;
    }

    public Personnage getPersonnage() {
        return personnage;
    }
    public void addSegment(Segment s) {
        if(s != null)
            segments.add(s);
    }

    public ObservableList<Segment> getSegments() {
        return FXCollections.observableArrayList(segments);
    }

}
