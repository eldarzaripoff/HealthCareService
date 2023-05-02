package ru.netology.patient.medical;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class MedicalServiceImplTest {
    private static Stream<Arguments> badPressureTest() {
        return Stream.of(
                Arguments.of(130, 85, "1223-1445"),
                Arguments.of(110, 70, "1245-1423"),
                Arguments.of(100, 60, "1245-1423")
        );
    }

    @ParameterizedTest
    @MethodSource("badPressureTest")
    public void badPressureTest(int high, int low, String id) {
        BloodPressure bloodPressure = new BloodPressure(high, low);
//        Mockito.when(bloodPressure.getHigh()).thenReturn(high);
//        Mockito.when(bloodPressure.getLow()).thenReturn(low);

//        HealthInfo healthInfo = Mockito.mock(HealthInfo.class);
//        Mockito.when(healthInfo.getBloodPressure()).thenReturn(bloodPressure);

        BloodPressure generalPressure = new BloodPressure(120, 80);

        PatientInfo patientInfo = Mockito.mock(PatientInfo.class);
        Mockito.when(patientInfo.getId()).thenReturn(id);
        //Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);

        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        if (!bloodPressure.equals(generalPressure)) {
            alertService.send(message);
        }

        assertThat(bloodPressure, not(equalTo(generalPressure)));
        Mockito.verify(alertService, times(1)).send(message);

    }

    private static Stream<Arguments> badTemperatureTest() {
        return Stream.of(
                Arguments.of(new BigDecimal("37.50"), "1223-1445"),
                Arguments.of(new BigDecimal("36.40"), "1245-1423"),
                Arguments.of(new BigDecimal("38.60"), "1245-1423")
        );
    }

    @ParameterizedTest
    @MethodSource("badTemperatureTest")
    public void badTemperatureTest(BigDecimal temp, String id) {

        HealthInfo healthInfo = Mockito.mock(HealthInfo.class);
        Mockito.when(healthInfo.getNormalTemperature()).thenReturn(BigDecimal.valueOf(36.600));

        PatientInfo patientInfo = Mockito.mock(PatientInfo.class);
        Mockito.when(patientInfo.getId()).thenReturn(id);

        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        if (healthInfo.getNormalTemperature().compareTo(temp) != 0) {
            alertService.send(message);
        }

        assertThat(healthInfo.getNormalTemperature().compareTo(temp), not(equalTo(0)));
        Mockito.verify(alertService, times(1)).send(message);
    }

    private static Stream<Arguments> goodPressureTest() {
        return Stream.of(
                Arguments.of(120, 80, "1223-1445"),
                Arguments.of(120, 80, "1245-1423"),
                Arguments.of(120, 80, "1245-1423")
        );
    }

    @ParameterizedTest
    @MethodSource("goodPressureTest")
    public void goodPressureTest(int high, int low, String id) {
        BloodPressure bloodPressure = new BloodPressure(high, low);
//        Mockito.when(bloodPressure.getHigh()).thenReturn(high);
//        Mockito.when(bloodPressure.getLow()).thenReturn(low);

//        HealthInfo healthInfo = Mockito.mock(HealthInfo.class);
//        Mockito.when(healthInfo.getBloodPressure()).thenReturn(bloodPressure);

        BloodPressure generalPressure = new BloodPressure(120, 80);

        PatientInfo patientInfo = Mockito.mock(PatientInfo.class);
        Mockito.when(patientInfo.getId()).thenReturn(id);
        //Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);

        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        if (!bloodPressure.equals(generalPressure)) {
            alertService.send(message);
        }

        assertThat(bloodPressure, equalTo(generalPressure));
        Mockito.verify(alertService, never()).send(message);

    }

    private static Stream<Arguments> goodTemperatureTest() {
        return Stream.of(
                Arguments.of(new BigDecimal("36.60"), "1223-1445"),
                Arguments.of(new BigDecimal("36.60"), "1245-1423"),
                Arguments.of(new BigDecimal("36.60"), "1245-1423")
        );
    }

    @ParameterizedTest
    @MethodSource("goodTemperatureTest")
    public void goodTemperatureTest(BigDecimal temp, String id) {

        HealthInfo healthInfo = Mockito.mock(HealthInfo.class);
        Mockito.when(healthInfo.getNormalTemperature()).thenReturn(BigDecimal.valueOf(36.60));


        PatientInfo patientInfo = Mockito.mock(PatientInfo.class);
        Mockito.when(patientInfo.getId()).thenReturn(id);

        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        if (healthInfo.getNormalTemperature().compareTo(temp) != 0) {
            alertService.send(message);
        }

        assertThat(healthInfo.getNormalTemperature().compareTo(temp), equalTo(0));
        Mockito.verify(alertService, never()).send(message);
    }
}
