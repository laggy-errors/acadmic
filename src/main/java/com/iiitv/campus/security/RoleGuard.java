package com.iiitv.campus.security;

import com.iiitv.campus.ui.views.AccessDeniedView;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleGuard implements VaadinServiceInitListener {

    private final ObjectProvider<UserSession> userSessionProvider;

    public RoleGuard(ObjectProvider<UserSession> userSessionProvider) {
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(beforeEnterEvent -> {
                Class<?> target = beforeEnterEvent.getNavigationTarget();
                if (target.isAnnotationPresent(RequiresRole.class)) {
                    RequiresRole annotation = target.getAnnotation(RequiresRole.class);
                    Role[] allowedRoles = annotation.value();

                    UserSession userSession = userSessionProvider.getObject();
                    boolean hasPermission = Arrays.stream(allowedRoles)
                            .anyMatch(userSession::hasRole);

                    if (!hasPermission) {
                        beforeEnterEvent.forwardTo(AccessDeniedView.class);
                    }
                }
            });
        });
    }
}
