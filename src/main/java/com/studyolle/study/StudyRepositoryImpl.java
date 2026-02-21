package com.studyolle.study;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyolle.domain.QStudy;
import com.studyolle.domain.Study;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Study> findByKeyword(String keyword) {
        QStudy study = QStudy.study;
        return queryFactory
                .selectDistinct(study)
                .from(study)
                .leftJoin(study.tags).fetchJoin()
                .leftJoin(study.zones).fetchJoin()
                .where(
                        study.published.isTrue()
                                .and(
                                        study.title.containsIgnoreCase(keyword)
                                                .or(study.tags.any().title.containsIgnoreCase(keyword))
                                                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword))
                                )
                )
                .fetch();

    }
}
