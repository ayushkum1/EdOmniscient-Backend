package com.brainsinjars.projectbackend.controller;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */

import static com.brainsinjars.projectbackend.util.ResponseUtils.badRequestResponse;
import static com.brainsinjars.projectbackend.util.ResponseUtils.successResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.MediaDto;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Media;
import com.brainsinjars.projectbackend.service.IMediaService;
import com.brainsinjars.projectbackend.util.ResponseUtils;

@RestController
@RequestMapping("/institutes/{instituteId}")
public class MediaController {

	@Autowired
	IMediaService mediaService;

	@GetMapping("/media")
	public List<Media> getAllMediaByInstituteId(@PathVariable long instituteId) throws RecordNotFoundException {
		try {
			return mediaService.getAllMedia(instituteId);
		} catch (RecordNotFoundException e) {
			throw new RecordNotFoundException("Record Not present with this institute Id");
		}
	}

	@GetMapping("/media/{id}")
	public ResponseEntity<?> getMedia(@PathVariable long id) {
		System.out.println("in get media summary " + id);
		return ResponseEntity.ok(mediaService.getMedia(id));
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.ROOT })
	@PostMapping("/media/image")
	public ResponseEntity<?> createImageMedia(@PathVariable String instituteId, @RequestBody MediaDto dto)
			throws RecordCreationException, RecordNotFoundException {
		if (mediaService.createMedia(instituteId, dto)) {
			return successResponse("Your Image url is saved to DB....");
		} else {
			return badRequestResponse("Institute does not exist with this ID..");
		}
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.ROOT })
	@PostMapping("/media/video")
	public ResponseEntity<?> createVideoMedia(@PathVariable String instituteId, @RequestBody MediaDto dto)
			throws RecordCreationException, RecordNotFoundException {
		if (mediaService.createMedia(instituteId, dto)) {
			return successResponse("video Url saved in DB....");
		} else {
			return badRequestResponse("Institute does not exist with this ID..");
		}
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.ROOT })
	@DeleteMapping("/media/{id}")
	public ResponseEntity<?> deleteMedia(@PathVariable String id) throws RecordDeletionException {
		System.out.println("in delete  summary " + id);
		Media media = mediaService.deleteMedia(id);
		if (media != null)
			return ResponseEntity.ok(media);
		else
			return ResponseUtils.badRequestResponse("Could not delete media!");
	}

}
