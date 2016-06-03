package com.humane.util.zip4j;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class ZipFile extends net.lingala.zip4j.core.ZipFile {
    private final ZipParameters parameters;

    public ZipFile(String zipFile) throws ZipException {
        super(zipFile);

        parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
    }

    public ZipFile(File zipFile) throws ZipException {
        super(zipFile);

        parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
    }

    public void addFile(File sourceFile) throws ZipException {
        parameters.setRootFolderInZip("");
        super.addFile(sourceFile, parameters);
    }

    public void addFile(String path, File sourceFile) throws ZipException {
        parameters.setRootFolderInZip(path);
        super.addFile(sourceFile, parameters);
    }
}
