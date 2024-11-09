package com.test.hospital.service;

import com.test.hospital.data.dto.PatientProfileDto;
import com.test.hospital.data.dto.legacy.ClientLegacy;
import com.test.hospital.data.dto.legacy.GetNotesFromLegacyRequest;
import com.test.hospital.data.dto.legacy.NoteLegacy;
import com.test.hospital.data.entity.CompanyUser;
import com.test.hospital.data.entity.PatientNote;
import com.test.hospital.data.entity.PatientProfile;
import com.test.hospital.data.mapper.PatientNoteMapper;
import com.test.hospital.data.mapper.PatientProfileMapper;
import com.test.hospital.data.repository.CompanyUserRepository;
import com.test.hospital.data.repository.PatientNoteRepository;
import com.test.hospital.data.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.test.hospital.data.dto.PatientStatus.ACTIVE;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpMethod.POST;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportNotesScheduledService {

    @Value(value = "${legacy.uri}")
    private String LEGACY_URI;

    @Value("${dateFrom}")
    private String dateFrom;

    @Value("${dateTo}")
    private String dateTo;

    private final String MM_DD_YYYY_PATTERN = "MM-dd-yyyy";
    private final String CLIENTS = "/clients";
    private final String NOTES = "/notes";

    private final RestTemplate restTemplate = new RestTemplate();
    private final PatientProfileMapper patientProfileMapper;
    private final PatientProfileService patientProfileService;
    private final PatientProfileRepository patientProfileRepository;
    private final PatientNoteRepository patientNoteRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PatientNoteMapper patientNoteMapper;

    private final AtomicInteger counterUpdate = new AtomicInteger(0);
    private final AtomicInteger counterCreate = new AtomicInteger(0);


    @Scheduled(cron = "0 15 */2 * * *")
    public void importNotes() {
        var clientsFromLegacy = getClientsFromLegacy();

        var startTime = System.currentTimeMillis();

        if (nonNull(clientsFromLegacy)) {
            log.info("Importing legacy notes - {}", LocalDate.now());

            var mapOfActivePatientsWithAgencies = getAggregatedActivePatientsWithLegacyData(clientsFromLegacy);

            for (var activePatientEntry : mapOfActivePatientsWithAgencies.entrySet()) {
                CompletableFuture.runAsync(() -> importNotesForActivePatient(activePatientEntry))
                        .exceptionally(ex -> {
                            log.error("Error importing notes for patient {}", activePatientEntry.getKey().id(), ex);
                            return null;
                        });
            }
            var endTime = System.currentTimeMillis();
            log.info("Importing finished - {}", endTime);
            log.info("Importing lasts {}ms", endTime - startTime);
            log.info("Imported new notes - {}", counterCreate.getAndUpdate(value -> 0));
            log.info("Updated notes - {}", counterUpdate.getAndUpdate(value -> 0));
        } else {
            log.warn("No clients found");
        }

    }

    private Map<PatientProfileDto, List<ClientLegacy>> getAggregatedActivePatientsWithLegacyData(List<ClientLegacy> clientsFromLegacy) {
        var activePatients = getActivePatients(clientsFromLegacy);
        return activePatients.stream()
                .collect(Collectors.toMap(
                        patient -> patient,
                        patient -> clientsFromLegacy.stream()
                                .filter(client -> patient.oldClientGuidsFromLegacy().contains(client.guid()))
                                .collect(Collectors.toList())
                ));
    }

    private List<PatientProfileDto> getActivePatients(List<ClientLegacy> clientsFromLegacy) {
        var clientGuids = clientsFromLegacy.stream()
                .map(ClientLegacy::guid).toList();
        var patients = patientProfileService.getAllByGuids(clientGuids);
        var activePatients = patients.stream()
                .filter(p -> p.statusId().equals(ACTIVE)).toList();
        log.info("Clients from legacy {}, Active patients {}", clientsFromLegacy.size(), activePatients.size());
        return activePatients;
    }

    private void importNotesForActivePatient(Map.Entry<PatientProfileDto, List<ClientLegacy>> activePatientWithAgencies) {
        var dateFormatter = DateTimeFormatter.ofPattern(MM_DD_YYYY_PATTERN);
        var parseDateFrom = LocalDate.parse(dateFrom, dateFormatter);
        var parseDateTo = LocalDate.parse(dateTo, dateFormatter);
        var getNotesFromLegacyRequestList = activePatientWithAgencies.getValue().stream()
                .map(c -> new GetNotesFromLegacyRequest(c.agency(), parseDateFrom, parseDateTo, c.guid())).toList();
        processNotes(getNotesFromLegacyRequestList, activePatientWithAgencies.getKey());
    }

    private void processNotes(List<GetNotesFromLegacyRequest> getNotesFromLegacyRequestList, PatientProfileDto patientProfileDto) {
        log.info("Importing for {} {} {}", patientProfileDto.id(), patientProfileDto.firstName(), patientProfileDto.lastName());
        getNotesFromLegacyRequestList.parallelStream()
                .forEach(getNotesFromLegacyRequest -> {
                    var notesFromLegacy = getNotesFromLegacy(getNotesFromLegacyRequest);
                    var mapOfNewSystemNotesByCreatedDate = patientNoteRepository.findAllByPatientProfile_Id(patientProfileDto.id())
                            .stream()
                            .collect(Collectors.toMap(PatientNote::getCreatedDateTime, note -> note));
                    notesFromLegacy.forEach(noteLegacy -> {
                        var existingNote = mapOfNewSystemNotesByCreatedDate.get(noteLegacy.createdDateTime());
                        if (nonNull(existingNote)) {
                            if (isLegacyNoteModified(existingNote, noteLegacy)) {
                                updateNote(existingNote, noteLegacy);
                            }
                        } else {
                            createNote(patientProfileDto, noteLegacy);
                        }

                    });
                });
    }


    private void updateNote(PatientNote note, NoteLegacy noteLegacy) {
        note.setLastModifiedDateTime(noteLegacy.modifiedDateTime());
        note.setNote(noteLegacy.comments());
        try {
            patientNoteRepository.save(note);
        } catch (Exception ex) {
            log.error("Error saving note {} for patient {}: {}", noteLegacy.guid(), note.getPatientProfile().getId(), ex.getMessage(), ex);
        }
        counterUpdate.incrementAndGet();
        log.info("Updated note {} for user {}", noteLegacy.guid(), noteLegacy.loggedUser());
    }


    private static boolean isLegacyNoteModified(PatientNote note, NoteLegacy noteLegacy) {
        return noteLegacy.modifiedDateTime().isAfter(note.getLastModifiedDateTime());
    }

    private void createNote(PatientProfileDto patientProfileDto, NoteLegacy noteLegacy) {
        try {
            var user = companyUserRepository.findByLogin(noteLegacy.loggedUser());
            if (user.isEmpty()) {
                user = Optional.of(companyUserRepository.save(new CompanyUser(noteLegacy.loggedUser())));
            }
            var patientProfile = findPatientProfile(patientProfileDto);
            var toSave = patientNoteMapper.toEntityFromLegacyNote(noteLegacy, user.get(), patientProfile);
            patientNoteRepository.save(toSave);
        } catch (Exception ex) {
            log.error("Error saving note {} for patient {}: {}", noteLegacy.guid(), patientProfileDto.id(), ex.getMessage(), ex);
        }
        log.info("Saved note {} for user {}", noteLegacy.guid(), noteLegacy.loggedUser());
        counterCreate.incrementAndGet();
    }

    private PatientProfile findPatientProfile(PatientProfileDto patientProfileDto) {
        return patientProfileRepository.findById(patientProfileDto.id())
                .orElseGet(() -> patientProfileMapper.toEntity(patientProfileDto));
    }

    private List<NoteLegacy> getNotesFromLegacy(GetNotesFromLegacyRequest getNotesFromLegacyRequest) {
        return restTemplate.exchange(
                LEGACY_URI + NOTES,
                POST,
                new HttpEntity<>(getNotesFromLegacyRequest),
                new ParameterizedTypeReference<List<NoteLegacy>>() {
                }).getBody();

    }

    private List<ClientLegacy> getClientsFromLegacy() {
        return restTemplate.exchange(LEGACY_URI + CLIENTS,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ClientLegacy>>() {
                }).getBody();
    }

}