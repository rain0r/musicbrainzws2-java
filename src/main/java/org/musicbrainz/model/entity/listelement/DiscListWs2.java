package org.musicbrainz.model.entity.listelement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.musicbrainz.model.entity.DiscWs2;
import org.musicbrainz.wsxml.element.ListElement;

/**
 * <p>
 * A List of Disc Info (Disc & catalog number)referred by a release
 * </p>
 * 
 */
public class DiscListWs2 extends ListElement {
	private static Logger log = Logger.getLogger(DiscListWs2.class.getName());

	/**
	 * A string containing the complete credit as join of credit names in the list.
	 */

	private List<DiscWs2> discs = new ArrayList<DiscWs2>();

	/**
	 * Minimal Constructor
	 * @param discs A list of {@link DiscWs2} describing the Artist Credit.
	 */
	public DiscListWs2(List<DiscWs2> discs) {
		if (discs != null) {
			for (DiscWs2 element : discs) {
				addDisc(element);
			}
		}
	}

	public List<DiscWs2> getDiscs() {
		return discs;
	}

	private void addDisc(DiscWs2 element) {

		discs.add(element);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
}