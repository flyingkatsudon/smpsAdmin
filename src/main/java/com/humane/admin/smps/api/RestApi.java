package com.humane.admin.smps.api;

import com.humane.admin.smps.dto.*;
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

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> toolbar(@QueryMap Map<String, Object> parameterMap);

    @GET("status/attendDept")
    Observable<Response<PageResponse<StatusDeptDto>>> attendDept(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/attendMajor")
    Observable<Response<PageResponse<StatusMajorDto>>> attendMajor(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/attendHall")
    Observable<Response<PageResponse<StatusHallDto>>> attendHall(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/attendGroup")
    Observable<Response<PageResponse<StatusGroupDto>>> attendGroup(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/sheet")
    Observable<Response<PageResponse<SheetDto>>> sheet(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<ExamineeDto>>> examinee(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/score")
    Observable<Response<PageResponse<ScoreDto>>> score(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/scoreFix")
    Observable<Response<PageResponse<ScoreFixDto>>> scoreFix(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/send")
    Observable<Response<PageResponse<CheckSendDto>>> send(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/checkScorer")
    Observable<Response<PageResponse<CheckScorerDto>>> checkScorer(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/checkItem")
    Observable<Response<PageResponse<CheckItemDto>>> checkItem(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);
}