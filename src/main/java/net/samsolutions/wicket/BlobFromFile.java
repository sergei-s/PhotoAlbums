package net.samsolutions.wicket;

import net.samsolutions.hibernate.File;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Gets Blob from File
 */
public final class BlobFromFile {

	private BlobFromFile() { }

	/**
	 * @param file file, small image from what you want to get
	 * @return blob of small image
	 */
	public static Blob getSmall(File file) {
		try {
            return new SerialBlob(file.getFileSmall());
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * @param file file, big image from what you want to get
	 * @return blob of big image
	 */
	public static Blob getBig(File file) {
		try {
            return new SerialBlob(file.getFile());
		} catch (SQLException e) {
			return null;
		}
	}
}
