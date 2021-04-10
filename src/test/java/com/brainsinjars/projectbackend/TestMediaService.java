package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.MediaController;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dto.MediaDto;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Category;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Media;
import com.brainsinjars.projectbackend.pojo.MediaType;
import com.brainsinjars.projectbackend.service.IMediaService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 24-03-2021
*/

//testing table
/*mysql> select * from media;
+----+----------+------------+--------+---------------+--------------+
| id | category | media_type | name   | url           | institute_id |
+----+----------+------------+--------+---------------+--------------+
|  1 | LIBRARY  | IMAGE      | media1 | www.media.jpg |            1 |
|  2 | LIBRARY  | IMAGE      | media2 | www.media.jpg |            2 |
|  3 | LIBRARY  | IMAGE      | media2 | www.media.jpg |            1 |
|  4 | LIBRARY  | IMAGE      | media2 | www.media.jpg |            1 |
|  9 | HOSTEL   | IMAGE      | media5 | www.photo.jpg |            1 |
+----+----------+------------+--------+---------------+--------------+
*/

@SpringBootTest
public class TestMediaService {
	
	@Autowired
	private IMediaService service;
	
	@Autowired
	private MediaController controller;
	
	@Autowired
	private InstituteDao instituteDao;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testGetMedia() {
		MediaDto media = service.getMedia(1);
		
		assertEquals(1, media.getId());
		assertEquals("media1", media.getName());
		assertEquals(Category.LIBRARY, media.getCategory());
		assertEquals(com.brainsinjars.projectbackend.pojo.MediaType.IMAGE, media.getMediaType());
		
	}
	
	@Test
	public void testGetAllMedia() throws RecordNotFoundException {
		List<Media> medias = service.getAllMedia(1);
		
		assertEquals(5, medias.size());
		
		Media media = medias.get(0);
		
		assertEquals(1, media.getId());
		assertEquals("media1", media.getName());
		assertEquals(Category.LIBRARY, media.getCategory());
		assertEquals(com.brainsinjars.projectbackend.pojo.MediaType.IMAGE, media.getMediaType());
	
	}
	
	@Test
	public void testCreateMedia() throws RecordNotFoundException, RecordCreationException {
		
		String[] mediaArr = new String[1];
		
		mediaArr[0] = "www.photo.jpg";//array as we are taking array of image urls, only one required for testing
		
		//id is being set by auto generated
		MediaDto dto = new MediaDto((long) 5, mediaArr, Category.HOSTEL, com.brainsinjars.projectbackend.pojo.MediaType.IMAGE, "media5",(long) 1);
		
		assertEquals(true, service.createMedia("1", dto));
		
	}

//	@Test
//	public void testUpdateMedia() throws RecordNotFoundException {
//		
//		String[] mediaArr = new String[1];
//	
//		mediaArr[0] = "www.photo.jpg";
//	
//		Institute institute = instituteDao.findById(1);
//		
//		Media media  = new Media("www.photo.jpg", MediaType.IMAGE, Category.HOSTEL, "updated name", institute);
//	
//		Media updatedMedia = service.updateMedia(9, media);
//		
//		assertEquals("updated name", updatedMedia.getName());
//		
//		
//	}
//	
}




