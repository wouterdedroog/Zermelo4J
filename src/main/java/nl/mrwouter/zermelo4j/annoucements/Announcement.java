package nl.mrwouter.zermelo4j.annoucements;

import com.google.gson.JsonObject;

/**
 * Announcement object for easy overview
 */
public class Announcement {
	
	private long id, start, end;
	private String title, text;
	
	public Announcement(long id, long start, long end, String title, String text) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.title = title;
		this.text = text;
	}
	
	/**
	 * Get the ID of this announcement. Do not store this value as it could change.
	 * @return id of announcement
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * UTC Unix time from which on the announcement should be shown
	 * @return start time of this announcement
	 */
	public long getStart() {
		return start;
	}
	
	/**
	 * UTC Unix time until which the announcement should be shown
	 * @return end time of this announcement
	 */
	public long getEnd() {
		return end;
	}
	
	/**
	 * The (descriptive) title of the announcement
	 * @return title of announcement
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * More detailed information about the announcement
	 * @return detailed description of this announcement
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Added custom {@link #toString()} method because easy debugging.
	 */
	@Override
	public String toString() {
		JsonObject appointment = new JsonObject();
		appointment.addProperty("id", id);
		appointment.addProperty("start", start);
		appointment.addProperty("end", end);
		appointment.addProperty("title", title);
		appointment.addProperty("text", text);
		
		return appointment.toString();
	}
}
