package com.humane.admin.smps.api;

import com.humane.admin.smps.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

public interface RestApi {

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> toolbar(@QueryMap Map<String, Object> parameterMap);

    @GET("status/all")
    Observable<Response<StatusDto>> all(@QueryMap Map<String, Object> params, @Query("page") String... sort);

    @GET("status/attend/dept")
    Observable<Response<PageResponse<StatusDto>>> dept(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/attend/major")
    Observable<Response<PageResponse<StatusDto>>> major(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/attend/hall")
    Observable<Response<PageResponse<StatusDto>>> hall(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/sheet")
    Observable<Response<PageResponse<StatusDto>>> sheet(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<StatusDto>>> examinee(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/scorer")
    Observable<Response<PageResponse<StatusDto>>> scorer(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/send")
    Observable<Response<PageResponse<StatusDto>>> send(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/virtNo")
    Observable<Response<PageResponse<StatusDto>>> virtNo(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/scoring")
    Observable<Response<PageResponse<StatusDto>>> scoring(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);
}