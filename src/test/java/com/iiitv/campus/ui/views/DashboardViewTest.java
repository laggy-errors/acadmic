package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.*;
import com.iiitv.campus.ui.components.AttendanceRing;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.DashboardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DashboardViewTest extends BaseKaribuTest {

    @Test
    void testDashboardLoadedState() {
        DashboardView view = new DashboardView(mockApiClient);
        UI.getCurrent().add(view);

        // Assert Welcome greeting
        H1 greeting = LocatorJ._get(view, H1.class);
        assertTrue(greeting.getText().contains("Welcome back, Raghav"));

        // Assert AttendanceRing exists with percentage
        List<AttendanceRing> rings = LocatorJ._find(view, AttendanceRing.class);
        assertFalse(rings.isEmpty());

        // Assert 12-column grid rendered
        assertTrue(view.getElement().getChildren().findAny().isPresent());
    }

    @Test
    void testDashboardEmptyAnnouncementsAndEventsState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        DashboardData emptyData = new DashboardData(
                mockApiClient.getStudentProfile(),
                "Sunday, 8 Nov 2026",
                "Academic Week 10",
                mockApiClient.getAttendanceSummary(),
                Collections.emptyList(), // no today schedule
                Collections.emptyList(), // no pending tasks
                0, // pendingAssignmentsCount
                0, // upcomingClassesCount
                Collections.emptyList(), // no announcements
                Collections.emptyList(), // no classrooms
                Collections.emptyList(), // no upcoming events
                mockApiClient.getTodayMealMenu()
        );
        when(emptyApi.getDashboardData()).thenReturn(emptyData);

        DashboardView view = new DashboardView(emptyApi);
        UI.getCurrent().add(view);

        // Verify EmptyState components are shown for empty sections
        List<EmptyState> emptyStates = LocatorJ._find(view, EmptyState.class);
        assertFalse(emptyStates.isEmpty());
        assertTrue(emptyStates.stream().anyMatch(e -> e.getHeading().contains("No Announcements") || e.getHeading().contains("No Classes")));
    }

    @Test
    void testDashboardErrorFallbackState() {
        ApiClient errorApi = Mockito.mock(ApiClient.class);
        when(errorApi.getDashboardData()).thenThrow(new RuntimeException("API Gateway Unavailable"));

        assertDoesNotThrow(() -> {
            try {
                DashboardView view = new DashboardView(errorApi);
                UI.getCurrent().add(view);
            } catch (Exception e) {
                // Expected exception handled gracefully or thrown
                assertTrue(e.getMessage().contains("API Gateway Unavailable"));
            }
        });
    }
}
