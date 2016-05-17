package com.humane.admin.smps.service;

import com.humane.admin.smps.api.RestApi;
import com.humane.admin.smps.dto.*;
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

    public Response<List<StatusDto>> toolbar(Map<String, Object> params) {
        Observable<Response<List<StatusDto>>> observable = restApi.toolbar(params);

        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusDeptDto>> attendDept(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusDeptDto>>> observable = restApi.attendDept(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusDeptDto> attendDept(Map<String, Object> params, String... sort) {
        Observable<List<StatusDeptDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attendDept(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusMajorDto>> attendMajor(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusMajorDto>>> observable = restApi.attendMajor(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusMajorDto> attendMajor(Map<String, Object> params, String... sort) {
        Observable<List<StatusMajorDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attendMajor(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusHallDto>> attendHall(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusHallDto>>> observable = restApi.attendHall(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusHallDto> attendHall(Map<String, Object> params, String... sort) {
        Observable<List<StatusHallDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attendHall(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<StatusGroupDto>> attendGroup(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<StatusGroupDto>>> observable = restApi.attendGroup(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<StatusGroupDto> attendGroup(Map<String, Object> params, String... sort) {
        Observable<List<StatusGroupDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attendGroup(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<SheetDto>> sheet(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<SheetDto>>> observable = restApi.sheet(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<SheetDto> sheet(Map<String, Object> params, String... sort) {
        Observable<List<SheetDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.sheet(params, currentPage, Integer.MAX_VALUE, sort))
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

    public Response<PageResponse<ScoreDto>> score(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<ScoreDto>>> observable = restApi.score(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<ScoreDto> score(Map<String, Object> params, String... sort) {
        Observable<List<ScoreDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.score(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<ScoreFixDto>> scoreFix(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<ScoreFixDto>>> observable = restApi.scoreFix(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<ScoreFixDto> scoreFix(Map<String, Object> params, String... sort) {
        Observable<List<ScoreFixDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.scoreFix(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }


    public Response<PageResponse<CheckSendDto>> send(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<CheckSendDto>>> observable = restApi.send(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<CheckSendDto> send(Map<String, Object> params, String... sort) {
        Observable<List<CheckSendDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.send(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<CheckItemDto>> checkItem(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<CheckItemDto>>> observable = restApi.checkItem(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<CheckItemDto> checkItem(Map<String, Object> params, String... sort) {
        Observable<List<CheckItemDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.checkItem(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }

    public Response<PageResponse<CheckScorerDto>> checkScorer(Map<String, Object> params, int page, int rows, String... sort) {
        Observable<Response<PageResponse<CheckScorerDto>>> observable = restApi.checkScorer(params, page, rows, sort);
        return observable.toBlocking().first();
    }

    public List<CheckScorerDto> checkScorer(Map<String, Object> params, String... sort) {
        Observable<List<CheckScorerDto>> observable = Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.checkScorer(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
        return observable.toBlocking().first();
    }
}
