package com.studyolle.study;

import com.studyolle.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryCustom {

    Page<Study> findByKeyword(String keyword , Pageable pageable);
}
