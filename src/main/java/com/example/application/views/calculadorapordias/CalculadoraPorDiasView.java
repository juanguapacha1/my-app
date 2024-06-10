package com.example.application.views.calculadorapordias;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Calculadora Por Dias")
@Route(value = "my-view2", layout = MainLayout.class)
public class CalculadoraPorDiasView extends Composite<VerticalLayout> {

    public CalculadoraPorDiasView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
    }
}
