package nl.mrwouter.zermelo4j.annoucements;

import java.util.Comparator;

public class AnnouncementComparator implements Comparator<Announcement> {

	@Override
	public int compare(Announcement ann1, Announcement ann2) {
        if (ann1.getStart() == ann2.getStart()) {
            return 0;
        }
        if (ann1.getStart() < ann2.getStart()) {
            return -1;
        }
        return 1;
	}	
}
