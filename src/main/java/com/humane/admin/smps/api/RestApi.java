package com.humane.admin.smps.api;

import com.humane.admin.smps.dto.ExamineeDto;
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

    @GET("status/all")
    Observable<Response<StatusDto>> all(@QueryMap Map<String, Object> params, @Query("page") String... sort);

    @GET("status/attend")
    Observable<Response<PageResponse<StatusDto>>> attend(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/dept")
    Observable<Response<PageResponse<StatusDto>>> dept(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/hall")
    Observable<Response<PageResponse<StatusDto>>> hall(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/group")
    Observable<Response<PageResponse<StatusDto>>> group(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<ExamineeDto>>> examinee(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> toolbar(@QueryMap Map<String, Object> parameterMap);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);

    @GET("status/signature")
    Observable<Response<PageResponse<StatusDto>>> signature(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/paper")
    Observable<Response<PageResponse<ExamineeDto>>> paper(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

}