package com.studyolle.study;

import com.studyolle.domain.Study;
import com.studyolle.domain.Tag;
import com.studyolle.domain.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryCustom {

    Page<Study> findByKeyword(String keyword , Pageable pageable);

    List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones);
}
