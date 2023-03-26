package com.raf.si.patientservice.repository;

import com.raf.si.patientservice.model.HealthRecord;
import com.raf.si.patientservice.model.MedicalExamination;
import com.raf.si.patientservice.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.Date;
import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long>, JpaSpecificationExecutor<MedicalExamination> {
}