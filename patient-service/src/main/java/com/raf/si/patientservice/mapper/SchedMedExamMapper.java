package com.raf.si.patientservice.mapper;

import com.raf.si.patientservice.dto.request.SchedMedExamRequest;
import com.raf.si.patientservice.dto.request.UpdateSchedMedExamRequest;
import com.raf.si.patientservice.dto.response.SchedMedExamResponse;
import com.raf.si.patientservice.exception.BadRequestException;
import com.raf.si.patientservice.model.ScheduledMedExamination;
import com.raf.si.patientservice.model.enums.examination.ExaminationStatus;
import com.raf.si.patientservice.model.enums.examination.PatientArrivalStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedMedExamMapper {


    public ScheduledMedExamination schedMedExamRequestToScheduledMedExamination(ScheduledMedExamination scheduledMedExamination
            ,SchedMedExamRequest schedMedExamRequest){
        scheduledMedExamination.setLbp(schedMedExamRequest.getLbp());
        scheduledMedExamination.setLbzDoctor(schedMedExamRequest.getLbzDoctor());
        scheduledMedExamination.setAppointmentDate(schedMedExamRequest.getAppointmentDate());
        scheduledMedExamination.setLbzNurse(schedMedExamRequest.getLbzNurse());


        if (schedMedExamRequest.getNote() != null)
            scheduledMedExamination.setNote(schedMedExamRequest.getNote());

        return  scheduledMedExamination;
    }

    public SchedMedExamResponse scheduledMedExaminationToSchedMedExamResponse(ScheduledMedExamination scheduledMedExamination) {
        SchedMedExamResponse schedMedExamResponse= new SchedMedExamResponse();

        schedMedExamResponse.setId(scheduledMedExamination.getId());
        schedMedExamResponse.setLbp(scheduledMedExamination.getLbp());
        schedMedExamResponse.setLbzDoctor(scheduledMedExamination.getLbzDoctor());
        schedMedExamResponse.setAppointmentDate(scheduledMedExamination.getAppointmentDate());
        schedMedExamResponse.setExaminationStatus(scheduledMedExamination.getExaminationStatus());
        schedMedExamResponse.setPatientArrivalStatus(scheduledMedExamination.getPatientArrivalStatus());
        schedMedExamResponse.setNote(scheduledMedExamination.getNote());
        schedMedExamResponse.setLbzNurse(scheduledMedExamination.getLbzNurse());

        return schedMedExamResponse;
    }

    public ScheduledMedExamination updateSchedMedExamRequestToScheduledMedExaminationExamStatus(ScheduledMedExamination scheduledMedExamination
            , UpdateSchedMedExamRequest updateSchedMedExamRequest) {

        ExaminationStatus examinationStatus= ExaminationStatus.valueOfNotation(updateSchedMedExamRequest.getNewStatus());

        if(examinationStatus == null){
            String errMessage = String.format("Nepoznat status pregleda '%s'", updateSchedMedExamRequest.getNewStatus());
            log.info(errMessage);
            throw new BadRequestException(errMessage);
        }

        switch (examinationStatus) {
            case ZAKAZANO:
            case OTKAZANO:
                String errMessage = String.format("Nije dozvoljeno izmeniti status pregleda  na '%s'"
                            , updateSchedMedExamRequest.getNewStatus());
                    log.info(errMessage);
                    throw new BadRequestException(errMessage);

            case U_TOKU:
                scheduledMedExamination.setExaminationStatus(examinationStatus);
                scheduledMedExamination.setPatientArrivalStatus(PatientArrivalStatus.PRIMLJEN);
                break;
            case ZAVRSENO:
                scheduledMedExamination.setExaminationStatus(examinationStatus);
                scheduledMedExamination.setPatientArrivalStatus(PatientArrivalStatus.ZAVRSIO);
                break;
        }

        return scheduledMedExamination;
    }

    public ScheduledMedExamination updateSchedMedExamRequestToScheduledMedExaminationPatientArrivalStatus(ScheduledMedExamination scheduledMedExamination
            , UpdateSchedMedExamRequest updateSchedMedExamRequest) {

        PatientArrivalStatus patientArrivalStatus= PatientArrivalStatus.valueOfNotation(updateSchedMedExamRequest.getNewStatus());

        if(patientArrivalStatus == null){
            String errMessage = String.format("Nepoznat status o prispeću pacijenta '%s'", updateSchedMedExamRequest.getNewStatus());
            log.info(errMessage);
            throw new BadRequestException(errMessage);
        }

        switch (patientArrivalStatus) {
            case CEKA:
            case ZAVRSIO:
            case PRIMLJEN:
            case NIJE_DOSAO:
                scheduledMedExamination.setPatientArrivalStatus(patientArrivalStatus);
                break;
            case OTKAZAO:
                scheduledMedExamination.setExaminationStatus(ExaminationStatus.OTKAZANO);
                scheduledMedExamination.setPatientArrivalStatus(patientArrivalStatus);
                break;
        }

        return scheduledMedExamination;
    }
}