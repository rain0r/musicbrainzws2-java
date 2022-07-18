package org.musicbrainz.model;

import java.util.List;
import java.util.logging.Logger;

import org.musicbrainz.model.entity.RecordingWs2;
import org.musicbrainz.model.entity.listelement.RecordingListWs2;

/**
 * <p>
 * A single Puid and the List of referring recordings.
 */
public class PuidWs2 {

	private static Logger log = Logger.getLogger(PuidWs2.class.getName());

	private String id;

	private RecordingListWs2 recordingList = new RecordingListWs2();

	/**
	 * Default Constructor
	 */
	public PuidWs2() {

	}

	public PuidWs2(String id, RecordingListWs2 recordingList) {
		this.id = id;
		this.recordingList = recordingList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the underlying <code>List</clode> of recording.
	 * @return the recordings
	 */
	public List<RecordingWs2> getRecordings() {
		return (recordingList == null ? null : recordingList.getRecordings());
	}

	/**
	 * Sets the underlying <code>List</clode> of recordings.
	 *
	 * Note: This will implicitly create a new {@link #recordingList} if it is null.
	 * @param recordings the recordings to set
	 */
	public void setRecordings(List<RecordingWs2> recordings) {
		if (recordingList == null) {
			recordingList = new RecordingListWs2();
		}

		this.recordingList.setRecordings(recordings);
	}

	/**
	 * @return the recordingList
	 */
	public RecordingListWs2 getRecordingList() {
		return recordingList;
	}

	/**
	 * @param recordingList the recordingList to set
	 */
	public void setRecordingList(RecordingListWs2 recordingList) {
		this.recordingList = recordingList;
	}

	/**
	 * <p>
	 * Adds a recording to the underlying <code>List</clode> of recordings.
	 * </p>
	 *
	 * <p>
	 * <em>Note: This will implicitly create a new {@link #recordingList} if it is
	 * null.</em>
	 * </p>
	 * @param recording The {@link RecordingWs2} to add.
	 */
	public void addRecording(RecordingWs2 recording) {
		if (recordingList == null) {
			recordingList = new RecordingListWs2();
		}
		recordingList.addRecording(recording);
	}

	@Override
	public String toString() {
		return id;
	}

}
