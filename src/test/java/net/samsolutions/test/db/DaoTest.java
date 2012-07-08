package net.samsolutions.test.db;

import net.samsolutions.hibernate.Album;
import net.samsolutions.hibernate.File;
import net.samsolutions.hibernate.ShareInformation;
import net.samsolutions.hibernate.User;
import net.samsolutions.spring.AlbumService;
import net.samsolutions.spring.FileService;
import net.samsolutions.spring.ShareInformationService;
import net.samsolutions.spring.UserService;
import net.samsolutions.utils.MD5;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NamingException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration ("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DaoTest {

	@Autowired
	private UserService userService;
	@Autowired
	private AlbumService albumService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ShareInformationService shareInformationService;

	@BeforeClass
	public static void setUpJndi() throws NamingException {
		PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:h2:~/H2/PhotoAlbums");
        p.setUrl("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:db_create.sql';DB_CLOSE_DELAY=-1");
        p.setDriverClassName("org.h2.Driver");
        p.setUsername("sa");
        DataSource ds = new DataSource();
        ds.setPoolProperties(p);
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:comp/env/jdbc/TestDB", ds);
	}

	@Test
	public void testUser() {
        User user = new User(null, "123", MD5.getHash("pass"));
        User userSharedTo = new User(null, "456", MD5.getHash("pass"));

		//test create&update user
		this.userService.create(user);
		this.userService.create(user);
		System.out.println("-----------------");
		assertNotNull(this.userService.getUser(user.getEmail(), "pass"));
		assertNotNull(this.userService.getUser(user));
		user.setPassword(MD5.getHash("pass1"));
		this.userService.update(user);
		assertNotNull(this.userService.getUser(user.getEmail(), "pass1"));

		//test create&update album
		Album album = new Album();
		album.setName("111");
		album.setUser(user);
		this.albumService.create(album);
		this.albumService.create(album);
		assertNotNull(this.albumService.getAlbum(album.getName(), album.getUser().getId()));
		this.albumService.rename(album, "222");
		assertNotNull(this.albumService.getAlbum(album.getName(), album.getUser().getId()));

		//test create file
		File file = new File(null, "123", new byte[]{1}, new byte[]{2}, album);
		this.fileService.create(file);
		assertNotNull(this.fileService.getFileOwn(file.getId(), album.getName(), user.getId()));

		//test move file from album to another
		Album album2 = new Album();
		album2.setName("333");
		album2.setUser(user);
		this.albumService.create(album2);
		this.fileService.changeAlbum(file, album2);
		assertNotNull(this.fileService.getFileOwn(file.getId(), album2.getName(), user.getId()));
		this.fileService.changeAlbum(file, album);

		//test create share
		this.userService.create(userSharedTo);
		ShareInformation shareInformation = new ShareInformation(null, album, userSharedTo);
		this.shareInformationService.create(shareInformation);
		assertNotNull(this.shareInformationService.getShare(album.getName(), userSharedTo.getId(), user.getEmail()));

		//test cascade delete
		this.userService.delete(user);
		this.userService.delete(userSharedTo);
		assertNull(this.userService.getUser(user));
		assertNull(this.albumService.getAlbum(album.getName(), album.getUser().getId()));
		assertNull(this.fileService.getFileOwn(file.getId(), album2.getName(), user.getId()));
		assertNull(this.shareInformationService.getShare(album.getName(), userSharedTo.getId(), user.getEmail()));
	}
}
