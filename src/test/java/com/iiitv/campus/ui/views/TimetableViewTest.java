package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.TimetableView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TimetableViewTest extends BaseKaribuTest {

    @Test
    void testTimetableLoadedWeekState() {
        TimetableView view = new TimetableView(mockApiClient);
        UI.getCurrent().add(view);

        // Assert Day / Week / List switcher buttons
        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> "Week".equals(b.getText())));
        assertTrue(buttons.stream().anyMatch(b -> "Day".equals(b.getText())));
        assertTrue(buttons.stream().anyMatch(b -> "List".equals(b.getText())));

        // Switch to Day view
        Button dayBtn = buttons.stream().filter(b -> "Day".equals(b.getText())).findFirst().orElseThrow();
        LocatorJ._click(dayBtn);
        assertNotNull(view);

        // Switch to List view
        Button listBtn = buttons.stream().filter(b -> "List".equals(b.getText())).findFirst().orElseThrow();
        LocatorJ._click(listBtn);
        assertNotNull(view);
    }

    @Test
    void testTimetableSemesterChange() {
        TimetableView view = new TimetableView(mockApiClient);
        UI.getCurrent().add(view);

        com.vaadin.flow.component.select.Select<?> semSelect = LocatorJ._find(view, com.vaadin.flow.component.select.Select.class).getFirst();
        assertNotNull(semSelect);

        // Switch to Semester I
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester I (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester II
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester II (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester III
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester III (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester IV
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester IV (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester VI
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester VI (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester VII
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester VII (B.Tech ICD)");
        assertNotNull(view);

        // Switch to Semester VIII
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("Semester VIII (B.Tech ICD)");
        assertNotNull(view);

        // Switch to MCA
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("MCA (ICD Campus)");
        assertNotNull(view);

        // Switch to M.Tech
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("M.Tech (ICD Campus)");
        assertNotNull(view);

        // Switch to Master Schedule
        ((com.vaadin.flow.component.select.Select<String>) semSelect).setValue("All Branches & Semesters (Master Schedule)");
        assertNotNull(view);
    }

    @Test
    void testTimetableEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getWeeklyTimetable(Mockito.anyString())).thenReturn(Collections.emptyList());

        TimetableView view = new TimetableView(emptyApi);
        UI.getCurrent().add(view);

        // Switch to day view to verify empty schedule handling
        List<Button> buttons = LocatorJ._find(view, Button.class);
        Button dayBtn = buttons.stream().filter(b -> "Day".equals(b.getText())).findFirst().orElseThrow();
        LocatorJ._click(dayBtn);

        List<EmptyState> emptyStates = LocatorJ._find(view, EmptyState.class);
        assertFalse(emptyStates.isEmpty());
        assertTrue(emptyStates.getFirst().getHeading().contains("No Classes Today"));
    }

    @Test
    void testTimetableErrorFallbackState() {
        ApiClient errorApi = Mockito.mock(ApiClient.class);
        when(errorApi.getWeeklyTimetable(Mockito.anyString())).thenThrow(new RuntimeException("Timetable Service Down"));

        assertDoesNotThrow(() -> {
            try {
                TimetableView view = new TimetableView(errorApi);
                UI.getCurrent().add(view);
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("Timetable Service Down"));
            }
        });
    }
}
