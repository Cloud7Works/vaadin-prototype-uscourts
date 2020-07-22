package org.cloud7works.vaadinui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.cdi.CDIUI;
import org.vaadin.cdiviewmenu.ViewMenuUI;


/**
 * Maps ViewMenuUI to root, provides automatic top level layout and navigation
 * for CDIView annotated Views.
 */
@CDIUI("")
@Theme("valo")
@Title("Application Assessment CM/ECF")
public class AppUI extends ViewMenuUI {



}
