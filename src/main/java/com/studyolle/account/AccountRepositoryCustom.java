package com.studyolle.account;

import com.studyolle.domain.Account;
import com.studyolle.domain.Tag;
import com.studyolle.domain.Zone;

import java.util.List;
import java.util.Set;

public interface AccountRepositoryCustom {
    List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones);
}
