package org.rapidpm.jumpstart.vaadin.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import javax.inject.Inject;

/**
 * Created by svenruppert on 30.11.15.
 */
@Theme("valo")
public class JumpstartUI extends UI {

  @Inject JumpstartUIComponentFactory jumpstartUIComponentFactory;

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    final ComponentContainer mainComponent =
        jumpstartUIComponentFactory.createComponentToSetAsContent(vaadinRequest);
    setContent(mainComponent);
  }
}