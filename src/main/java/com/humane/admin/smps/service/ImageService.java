package com.humane.admin.smps.service;

import com.humane.admin.smps.api.RestApi;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import rx.Observable;

import java.io.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageService {
    private final RestApi restApi;

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

    public InputStream getImageExaminee(String fileName) {
        try {
            File path = new File(pathImageExaminee);
            if(!path.exists()) path.mkdirs();
            File file = new File(pathImageExaminee + "/" + fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            } else {
                Observable<Response<ResponseBody>> observable = restApi.imageExaminee(fileName);
                Response<ResponseBody> response = observable.toBlocking().first();
                if (response.isSuccessful()) {
                    byte[] ba = response.body().bytes();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(ba);
                    IOUtils.closeQuietly(fos);
                    return new ByteArrayInputStream(ba);
                }
            }
        } catch (IOException e) {

        }
        return null;
    }
}
