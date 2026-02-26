package com.studyolle.zone.repository;

import com.studyolle.zone.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone,Long> {
    Zone findByCityAndProvince(String cityName, String provinceName);
}
