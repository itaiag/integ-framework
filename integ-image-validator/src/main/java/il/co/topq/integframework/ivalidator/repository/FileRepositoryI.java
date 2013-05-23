package il.co.topq.integframework.ivalidator.repository;

import java.io.File;

public interface FileRepositoryI {

	void addFile(File file, String[] folderAttributes, String[] fileAttributes) throws FileRepositoryException;

	File[] getFiles(String[] folderAttributes, String[] fileAttributes) throws FileRepositoryException;

	File[] getFiles(String[] folderAttributes) throws FileRepositoryException;
}
