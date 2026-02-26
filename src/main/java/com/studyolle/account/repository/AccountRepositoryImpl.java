package com.studyolle.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyolle.account.entity.Account;
import com.studyolle.account.entity.QAccount;
import com.studyolle.tag.entity.Tag;
import com.studyolle.zone.entity.Zone;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;

        return queryFactory
                .selectDistinct(account)
                .from(account)
                .leftJoin(account.tags).fetchJoin()
                .leftJoin(account.zones).fetchJoin()
                .where(
                        account.tags.any().in(tags),
                        account.zones.any().in(zones)
                )
                .fetch();
    }
}
