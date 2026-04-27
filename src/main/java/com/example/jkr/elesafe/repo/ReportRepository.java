package com.example.jkr.elesafe.repo;

import com.example.jkr.elesafe.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    // find by nic
    List<Report> findByReporterId(String reporterId);

    // Get all reports in a specific district (Great for regional Wild Officers)
    List<Report> findByDistrict(String district);

    //Get all reports in a specific village (Great for Villager map warnings)
    List<Report> findByVillage(String village);

    //Get all reports ordered by the most recent first (Using your new 'dateTime' field)
    List<Report> findAllByOrderByDateTimeDesc();
}