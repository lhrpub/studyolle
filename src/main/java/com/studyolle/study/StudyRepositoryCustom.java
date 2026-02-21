package com.studyolle.study;

import com.studyolle.domain.Study;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryCustom {

    List<Study> findByKeyword(String keyword);
}
