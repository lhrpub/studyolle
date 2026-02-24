package com.studyolle.study;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyolle.domain.QStudy;
import com.studyolle.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Study> findByKeyword(String keyword , Pageable pageable) {

        QStudy study = QStudy.study;

        List<Study> content = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(study.countDistinct())
                .from(study)
                .where(
                        study.published.isTrue()
                                .and(
                                        study.title.containsIgnoreCase(keyword)
                                                .or(study.tags.any().title.containsIgnoreCase(keyword))
                                                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword))
                                )
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);

    }
}
