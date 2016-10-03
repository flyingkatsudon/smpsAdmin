package com.humane.smps.service;

import com.humane.smps.dto.AppUrlDto;
import com.humane.smps.model.Devi;
import com.humane.smps.model.ExamHall;
import com.humane.smps.model.ExamMap;
import com.humane.smps.model.Item;
import com.humane.util.spring.Page;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

public interface ApiService {
    @GET("api/url")
    Observable<List<AppUrlDto>> checkUrl();

    @GET("api/examHall")
    Observable<Page<ExamHall>> examHall(@QueryMap Map<String, Object> queryMap, @Query("page") int page, @Query("size") int size, @Query("sort") String sort);

    @GET("api/devi")
    Observable<Page<Devi>> devi(@QueryMap Map<String, Object> queryMap, @Query("page") int page, @Query("size") int size, @Query("sort") String sort);

    @GET("api/item")
    Observable<Page<Item>> item(@QueryMap Map<String, Object> queryMap, @Query("page") int page, @Query("size") int size, @Query("sort") String sort);

    @GET("api/examMap?sort=_id")
    Observable<Page<ExamMap>> examMap(@QueryMap Map<String, Object> queryMap, @Query("page") int page, @Query("size") int size);

    @GET("image/examinee/{fileName}")
    Observable<ResponseBody> imageExaminee(@Path("fileName") String fileName);
}
