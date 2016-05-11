package com.humane.admin.smps.service;

import com.humane.admin.smps.api.RestApi;
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

    public Response<List<StatusDto>> toolbar(Map<String, Object> params) {
        Observable<Response<List<StatusDto>>> observable = restApi.toolbar(params);

        return observable.toBlocking().first();
    }

    public Response<StatusDto> all(Map<String, Object> params, String... sort) {
        Observable<Response<StatusDto>> observable = restApi.all(params,sort);
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

    public Response<PageResponse<StatusDto>> major(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.major(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> major(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.major(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> sheet(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.sheet(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> sheet(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.sheet(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> examinee(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.examinee(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> examinee(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.examinee(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> scorer(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.scorer(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> scorer(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.scorer(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> send(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.send(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> send(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.send(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> virtNo(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.virtNo(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> virtNo(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.virtNo(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDto>> scoring(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.scoring(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDto> scoring(Map<String, Object> params, String... sort) {
        Observable<List<StatusDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.scoring(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

}
