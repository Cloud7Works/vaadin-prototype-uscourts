package org.cloud7works.vaadinui;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import org.cloud7works.domain.model.Customer;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class CustomerForm extends AbstractForm<Customer> {

    TextField firstName = new MTextField("Case Number").withFullWidth();
    TextField lastName = new MTextField("Filer").withFullWidth();
    TextField email = new MTextField("Filer Email").withFullWidth();

    @Override
    protected Component createContent() {

        setStyleName(ValoTheme.LAYOUT_CARD);

        return new MVerticalLayout(
                new Header("Edit Case").setHeaderLevel(3),
                // Form layout puts caption on left, component on right
                new MFormLayout(
                        firstName,
                        lastName,
                        email
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
    }

    @Override
    protected void adjustResetButtonState() {
        // Always true, closes the form even if not modified
        getResetButton().setEnabled(true);
    }

}
