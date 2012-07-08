package net.samsolutions.utils;

import net.samsolutions.hibernate.File;

import java.util.Comparator;

/**
 * Defines comparator for Files, comparing by id
 */
public class FileComparator implements Comparator<File> {

	public int compare(File file1, File file2) {
	    if (file1.getId() > file2.getId()) {
	    	return 1;
	    } else {
	    	return -1;
	    }
	}
}
