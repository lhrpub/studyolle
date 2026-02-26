package com.studyolle.account.repository;

import com.studyolle.account.entity.Account;
import com.studyolle.tag.entity.Tag;
import com.studyolle.zone.entity.Zone;

import java.util.List;
import java.util.Set;

public interface AccountRepositoryCustom {
    List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones);
}
