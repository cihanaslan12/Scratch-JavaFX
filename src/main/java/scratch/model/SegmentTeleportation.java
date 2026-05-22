package scratch.model;

public class SegmentTeleportation extends Segment {
    public SegmentTeleportation(Point start, Point end) {
        super(start, end);
    }

    @Override
    public boolean isDashed() {
        return true;
    }
}