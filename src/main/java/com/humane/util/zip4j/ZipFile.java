package com.humane.util.zip4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.humane.util.gson.GsonDateDeserializer;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ZipFile extends net.lingala.zip4j.core.ZipFile {
    private ZipParameters parameters;

    public ZipFile(String zipFile) throws ZipException {
        this(new File(zipFile));
    }

    public ZipFile(File zipFile) throws ZipException {
        this(zipFile, null);
    }

    public ZipFile(String zipFile, ZipParameters parameters) throws ZipException {
        this(new File(zipFile), parameters);
    }

    public ZipFile(File zipFile, ZipParameters parameters) throws ZipException {
        super(zipFile);
        this.parameters = parameters == null ? getDefaultParameters() : parameters;
    }

    public void addFile(File sourceFile) throws ZipException {
        parameters.setRootFolderInZip("");
        super.addFile(sourceFile, parameters);
    }

    public void addFile(String path, File sourceFile) throws ZipException {
        parameters.setRootFolderInZip(path);
        super.addFile(sourceFile, parameters);
    }

    public void addFile(String path, File sourceFile, String fileNameInZip) throws ZipException {
        parameters.setFileNameInZip(path + "/" + fileNameInZip);
        parameters.setSourceExternalStream(true);
        parameters.setRootFolderInZip(path);
        super.addFile(sourceFile, parameters);
    }

    public void setParameters(ZipParameters parameters) {
        this.parameters = parameters;
    }

    private ZipParameters getDefaultParameters() {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        return parameters;
    }

    public <T> T parseObject(FileHeader fileHeader, TypeToken<T> typeToken, String charset) {

        try (ZipInputStream zis = this.getInputStream(fileHeader)) {
            byte[] ba = IOUtils.toByteArray(zis);
            String str = charset != null ? new String(ba, charset) : new String(ba);
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateDeserializer()).create();
            return (T) gson.fromJson(str, typeToken.getType());
        } catch (IOException | ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T parseObject(FileHeader fileHeader, TypeToken<T> typeToken) {
        return parseObject(fileHeader, typeToken, null);
    }

    public String getCharset() throws ZipException {
        List<FileHeader> fileHeaders = this.getFileHeaders();
        FileHeader fileHeader = fileHeaders.get(0);
        return fileHeader.isFileNameUTF8Encoded() ? "UTF-8" : "EUC-KR";
    }
}
