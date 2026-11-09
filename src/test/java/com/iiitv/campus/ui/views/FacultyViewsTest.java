package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.ui.views.faculty.FacultyAttendanceView;
import com.iiitv.campus.ui.views.faculty.FacultyGradebookView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacultyViewsTest extends BaseKaribuTest {

    @Test
    void testFacultyAttendanceRosterLoadedState() {
        FacultyAttendanceView view = new FacultyAttendanceView();
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Mark All Present")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Save")));

        // Check toggle buttons P / A / E exist
        assertTrue(buttons.stream().anyMatch(b -> "P".equals(b.getText())));
        assertTrue(buttons.stream().anyMatch(b -> "A".equals(b.getText())));
        assertTrue(buttons.stream().anyMatch(b -> "E".equals(b.getText())));
    }

    @Test
    void testFacultyGradebookLoadedState() {
        FacultyGradebookView view = new FacultyGradebookView();
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Export Grade Sheet")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Publish Grades")));
    }
}
