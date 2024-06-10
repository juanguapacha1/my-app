package com.example.application.views.calculadoratiempocompleto;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Calculadora Tiempo Completo")
@Route(value = "my-view", layout = MainLayout.class)
public class CalculadoraTiempoCompletoView extends Composite<VerticalLayout> {

    public CalculadoraTiempoCompletoView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
    }
}
