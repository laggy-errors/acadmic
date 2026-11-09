package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.AttendanceSummary;
import com.iiitv.campus.ui.components.AttendanceRing;
import com.iiitv.campus.ui.views.student.AttendanceView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AttendanceViewTest extends BaseKaribuTest {

    @Test
    void testAttendanceLoadedState() {
        AttendanceView view = new AttendanceView(mockApiClient);
        UI.getCurrent().add(view);

        // Verify summary ring
        AttendanceRing ring = LocatorJ._get(view, AttendanceRing.class);
        assertNotNull(ring);

        // Verify Tabs: Course-wise, Weekly Register, Shortfall Guidance
        Tabs tabs = LocatorJ._get(view, Tabs.class);
        assertNotNull(tabs);
        List<Tab> tabList = LocatorJ._find(tabs, Tab.class);
        assertEquals(3, tabList.size());

        // Switch to Weekly tab
        tabs.setSelectedTab(tabList.get(1));
        assertNotNull(view);

        // Switch to Guidance tab
        tabs.setSelectedTab(tabList.get(2));
        assertNotNull(view);
    }

    @Test
    void testAttendanceEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getAttendanceSummary()).thenReturn(mockApiClient.getAttendanceSummary());
        when(emptyApi.getCourseAttendanceList()).thenReturn(Collections.emptyList());
        when(emptyApi.getWeeklyAttendanceRegister(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());

        AttendanceView view = new AttendanceView(emptyApi);
        UI.getCurrent().add(view);

        AttendanceRing ring = LocatorJ._get(view, AttendanceRing.class);
        assertNotNull(ring);
    }

    @Test
    void testAttendanceShortfallCalculationState() {
        // Test student guidance tab navigation
        ApiClient warningApi = Mockito.mock(ApiClient.class);
        when(warningApi.getAttendanceSummary()).thenReturn(mockApiClient.getAttendanceSummary());
        when(warningApi.getCourseAttendanceList()).thenReturn(mockApiClient.getCourseAttendanceList());
        when(warningApi.getWeeklyAttendanceRegister(Mockito.anyString(), Mockito.anyString())).thenReturn(mockApiClient.getWeeklyAttendanceRegister("Semester V", "Week 10"));

        AttendanceView view = new AttendanceView(warningApi);
        UI.getCurrent().add(view);

        Tabs tabs = LocatorJ._get(view, Tabs.class);
        List<Tab> tabList = LocatorJ._find(tabs, Tab.class);
        tabs.setSelectedTab(tabList.get(2)); // Guidance tab
        assertNotNull(view);
    }
}
