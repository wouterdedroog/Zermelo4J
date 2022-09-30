package nl.mrwouter.zermelo4j.annoucements;

import java.util.Comparator;

/**
 * Comparator used to make sure that all announcements are sorted from start to beginning.
 */
public class AnnouncementComparator implements Comparator<Announcement> {

    @Override
    public int compare(Announcement ann1, Announcement ann2) {
        return Long.compare(ann1.getStart(), ann2.getStart());
    }
}
