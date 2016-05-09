package com.humane.admin.smps.service;

import com.humane.admin.smps.api.RestApi;
import com.humane.admin.smps.dto.ExamineeDto;
import com.humane.admin.smps.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiService {
    private final RestApi restApi;

    public Response<StatusDto> all(Map<String, Object> params, String... sort) {
        Observable<Response<StatusDto>> observable = restApi.all(params,sort);
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> attend(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.attend(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> attend(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attend(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> dept(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.dept(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> dept(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.dept(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> hall(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.hall(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> hall(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.hall(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();

    }

    public Response<PageResponse<StatusDto>> group(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.group(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> group(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.group(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<ExamineeDto>> examinee(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<ExamineeDto>>> observable = restApi.examinee(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<ExamineeDto> examinee(Map<String, Object> params, String... sort) {
        Observable<List<ExamineeDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.examinee(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<List<StatusDto>> toolbar(Map<String, Object> params) {
        Observable<Response<List<StatusDto>>> observable = restApi.toolbar(params);

        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> signature(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.signature(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> signature(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.signature(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<ExamineeDto>> paper(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<ExamineeDto>>> observable = restApi.paper(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<ExamineeDto> paper(Map<String, Object> params, String... sort) {
        Observable<List<ExamineeDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.paper(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }
}
