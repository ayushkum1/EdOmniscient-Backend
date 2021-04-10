package com.brainsinjars.projectbackend.service;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */
import java.util.List;

import com.brainsinjars.projectbackend.dto.MediaDto;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Media;

public interface IMediaService {

	// Method to store Multiple Image files in database
	boolean createMedia(String instituteId, MediaDto dto) throws RecordCreationException, RecordNotFoundException;

	// Method to get media by id
	MediaDto getMedia(long id);

	// Method to update the media after creation //if needed then only use it
	Media updateMedia(long id, Media media);

	// delete the media files from the database
	Media deleteMedia(String id);

	// Fetch the list of media files from the database and pass to the service layer
	List<Media> getAllMedia(long id) throws RecordNotFoundException;

}
