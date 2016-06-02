package com.humane.util.zip4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.humane.util.charset.CharsetUtil;
import com.humane.util.gson.GsonDateDeserializer;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ZipUtils {
    public static <T> T parseObject(TypeToken<T> typeToken, ZipFile zipFile, FileHeader fileHeader) {
        try (ZipInputStream zis = zipFile.getInputStream(fileHeader)) {
            byte[] ba = IOUtils.toByteArray(zis);
            String charset = CharsetUtil.getCharset(ba);
            String str = charset != null ? new String(ba, charset) : new String(ba);
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateDeserializer()).create();
            return (T) gson.fromJson(str, typeToken.getType());
        } catch (IOException | ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCharset(File file) throws ZipException {
        ZipFile zipFile = new ZipFile(file);
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        FileHeader fileHeader = fileHeaders.get(0);
        return fileHeader.isFileNameUTF8Encoded() ? "UTF-8" : "EUC-KR";
    }
}
