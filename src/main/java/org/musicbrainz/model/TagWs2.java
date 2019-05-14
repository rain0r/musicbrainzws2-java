package org.musicbrainz.model;

import java.util.logging.Logger;

/**
 * <p>
 * Represents a Tag for an entity (not release at the moment). count is the times this tag has been added by different
 * people.
 * </p>
 */
public class TagWs2 {
	public TagWs2() {

	}

	public TagWs2(String name) {
		this.name = name;
	}

	private static Logger log = Logger.getLogger(TagWs2.class.getName());

	/**
	 * The tag
	 */
	private String name;

	/**
	 * The count of peoples who adds this tag
	 */
	private Long count;

	/**
	 * @return the value
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the value to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

}
