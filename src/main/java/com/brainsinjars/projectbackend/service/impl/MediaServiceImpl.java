package com.brainsinjars.projectbackend.service.impl;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dto.MediaDto;
import com.brainsinjars.projectbackend.exceptions.MediaHandlingException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Category;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Media;
import com.brainsinjars.projectbackend.pojo.MediaType;
import com.brainsinjars.projectbackend.repository.MediaRepository;
import com.brainsinjars.projectbackend.service.IMediaService;

@Service
@Transactional
public class MediaServiceImpl implements IMediaService {

	private final MediaRepository mediaRepo;
	private final InstituteDao instituteDao;
	private final String MEDIA_ROOT;

	@Autowired
	public MediaServiceImpl(MediaRepository mediaRepo, InstituteDao instituteDao,
			@Value("${media-root}") String MEDIA_ROOT) {
		this.mediaRepo = mediaRepo;
		this.instituteDao = instituteDao;
		this.MEDIA_ROOT = MEDIA_ROOT;
	}

	// This is creating media of Images files
	@Override
	public boolean createMedia(String instituteId, MediaDto dto)
			throws RecordCreationException, RecordNotFoundException {
		try {
			Institute institute = instituteDao.findById(Long.parseLong(instituteId));
			for (String url : dto.getUrls()) {
				saveToDB(institute, dto.getCategory(), dto.getMediaType(), url, dto.getName());
			}
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

	// This method is used to get the media by ID
	@Override
	public MediaDto getMedia(long id) throws MediaHandlingException {
		Media media = mediaRepo.findById(id).orElseThrow(() -> new MediaHandlingException("Invalid media ID !!!!"));
		MediaDto mediaDto = new MediaDto(media.getId(), new String[] { media.getUrl() }, media.getCategory(),
				media.getMediaType(), media.getName(), media.getInstitute().getId());
		return mediaDto;
	}

	//not used
	// This method is used to update the existing media data from the database
	@Override
	public Media updateMedia(long id, Media media) {
		System.out.println("in service " + media);
		System.out.println(media.getName());
		Media media1 = mediaRepo.findById(id)
				.orElseThrow(() -> new MediaHandlingException(" Updation failed !!!: invalid media id"));
		media1.setId(id);
		return media1;
	}

	// This method is used to delete the media by Id
	@Override
	public Media deleteMedia(String id) {
		try {
			long mediaId = Long.parseLong(id);
			Media media = mediaRepo.findById(mediaId)
					.orElseThrow(() -> new MediaHandlingException(" Deletion fails : Invalid media ID !!!!"));
			mediaRepo.deleteById(mediaId);
			return media;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// This method is used to save the image files into directory and the save the
	// directory files to the database
	/*
	 * public boolean saveToFs(String instituteId, Category category, MediaType
	 * mediaType, MultipartFile[] files) { Optional<Institute> opInst =
	 * instituteRepository.findById(Long.parseLong(instituteId)); if
	 * (opInst.isPresent()) {
	 * 
	 * char separator = File.separatorChar; System.out.println(MEDIA_ROOT);
	 * 
	 * @SuppressWarnings("StringBufferReplaceableByString") String path = new
	 * StringBuffer(MEDIA_ROOT).append(separator).append(instituteId).append(
	 * separator) .append(category.name()).toString();
	 * 
	 * File directory = new File(path); if (!directory.exists()) { // noinspection
	 * ResultOfMethodCallIgnored directory.mkdirs(); }
	 * 
	 * int count = 1; for (MultipartFile file : files) {
	 * 
	 * String filename = file.getOriginalFilename(); if (filename != null) { // in
	 * frontend strictly allow png/jpg files only String ext =
	 * filename.substring(filename.indexOf(".")); // get first character of filename
	 * filename = filename.substring(0, 3); // concat a count + timestamp and
	 * extension to filename // filename.concat("_" + count + //
	 * "_").concat(LocalDateTime.now().toString()).concat("." + ext); filename =
	 * filename + "_" + count + "_" +
	 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss")
	 * ) + ext; File imageFile = new File(directory, filename); try
	 * (FileOutputStream fos = new FileOutputStream(imageFile)) { // Save to
	 * filesystem fos.write(file.getBytes()); // Save to Database
	 * saveToDB(opInst.get(), category, mediaType, imageFile.getCanonicalPath(),
	 * file.getOriginalFilename()); } catch (IOException | IllegalArgumentException
	 * e) { System.err.println(e.getMessage()); } finally { ++count; } } } return
	 * true; } return false; }
	 */

	// This method is used to save the url of the files in the database
	public boolean saveToDB(Institute institute, Category category, MediaType mediaType, String url, String name) {

		Media media = new Media(url, mediaType, category, name, institute);
		return institute.addMedia(media);
	}

	// This method is used to convert the image data from the url and then pass it
	// to the controller
	/*
	 * public byte[] getImageData(String url) { try { return Files.readAllBytes(new
	 * File(url).toPath()); } catch (IOException e) {
	 * System.err.println(e.getMessage()); } return null; }
	 */

	// This method is used to fetch list of media files from the database
	@Override
	public List<Media> getAllMedia(long id) throws RecordNotFoundException {

		return mediaRepo.findAllMediaByIntituteId(id);
	}

}
