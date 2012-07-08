package net.samsolutions.hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SHARE_INFORMATION")
@SuppressWarnings("serial")
public class ShareInformation implements Serializable {

	private Integer id;
	private Album album;
	private User user;

	public ShareInformation() {
	}

	public ShareInformation(Integer id, Album album, User user) {
		this.id = id;
		this.album = album;
		this.user = user;
	}

	@Id
	@Column (name = "ID")
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn (name = "ALBUM_ID")
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@ManyToOne
	@JoinColumn (name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
