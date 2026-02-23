package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Monde {

    private final ObservableList<Segment> segments = FXCollections.observableArrayList();
    private final Personnage personnage;
    private final Point startPos;
    private final int startAngle;

    public Monde(Personnage personnage, Point startPos, int startAngle){
        this.personnage = personnage;
        this.startPos = startPos;
        this.startAngle = startAngle;
    }

    public Personnage getPersonnage() {
        return personnage;
    }
    public void addSegment(Segment s) {
        if(s != null)
            segments.add(s);
    }

    public ObservableList<Segment> getSegments() {
        return FXCollections.unmodifiableObservableList(segments);
    }
    public void reset() {
        segments.clear();
        personnage.setPosition(new Point(startPos.getX(), startPos.getY()));
        personnage.setAngle(startAngle);
        personnage.penDown();
    }

}
