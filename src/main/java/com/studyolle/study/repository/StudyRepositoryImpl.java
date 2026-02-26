package com.studyolle.study.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyolle.study.entity.QStudy;
import com.studyolle.study.entity.Study;
import com.studyolle.tag.entity.QTag;
import com.studyolle.tag.entity.Tag;
import com.studyolle.zone.entity.QZone;
import com.studyolle.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom {

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

    @Override
    public List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones) {
        QStudy study = QStudy.study;
        QTag tag = QTag.tag;
        QZone zone = QZone.zone;

        return queryFactory
                .selectFrom(study)
                .leftJoin(study.tags, tag).fetchJoin()
                .leftJoin(study.zones, zone).fetchJoin()
                .where(
                        study.published.isTrue()
                                .and(study.closed.isFalse())
                                .and(study.tags.any().in(tags))
                                .and(study.zones.any().in(zones))
                )
                .orderBy(study.publishedDateTime.desc())
                .distinct()
                .limit(9)
                .fetch();
    }
}
