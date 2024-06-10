package com.example.application.views.calculadoratiempocompleto;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@PageTitle("Calculadora Tiempo Completo")
@Route(value = "my-view", layout = MainLayout.class)
public class CalculadoraTiempoCompletoView extends VerticalLayout {

    public CalculadoraTiempoCompletoView() {
        setWidth("100%");
        setHeightFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        DatePicker fechaInicio = new DatePicker("Fecha de Inicio");
        DatePicker fechaFin = new DatePicker("Fecha de Fin");
        NumberField sueldoMensual = new NumberField("Sueldo Mensual");
        sueldoMensual.setPrefixComponent(new Span("$"));
        sueldoMensual.setStep(0.01);
        sueldoMensual.setMin(0);

        ComboBox<String> auxilioTransporte = new ComboBox<>("Auxilio de Transporte");
        auxilioTransporte.setItems("SI", "NO");

        Button calcularButton = new Button("Calcular Liquidación");
        TextField resultado = new TextField("Resultado");
        resultado.setReadOnly(true);
        resultado.setWidthFull();
        resultado.getStyle().set("font-size", "24px");

        calcularButton.addClickListener(event -> {
            if (fechaInicio.isEmpty() || fechaFin.isEmpty() || sueldoMensual.isEmpty() || auxilioTransporte.isEmpty()) {
                Notification.show("Por favor, complete todos los campos.");
                return;
            }

            LocalDate inicio = fechaInicio.getValue();
            LocalDate fin = fechaFin.getValue();
            double sueldo = sueldoMensual.getValue();
            boolean tieneAuxilioTransporte = auxilioTransporte.getValue().equalsIgnoreCase("SI");

            String resultadoLiquidacion = calcularLiquidacion(inicio, fin, sueldo, tieneAuxilioTransporte);
            resultado.setValue(resultadoLiquidacion);
        });

        add(fechaInicio, fechaFin, sueldoMensual, auxilioTransporte, calcularButton, resultado);
    }

    private String calcularLiquidacion(LocalDate fechaInicio, LocalDate fechaFin, double sueldoMensual, boolean tieneAuxilioTransporte) {
        // Calcular los días totales trabajados
        long diasTotalesTrabajados = ChronoUnit.DAYS.between(fechaInicio, fechaFin);

        // Calcular salarios pendientes
        double salarioDiario = sueldoMensual / 30; // Suponiendo 30 días por mes
        double salarioPendiente = (diasTotalesTrabajados % 30) * salarioDiario;

        // Calcular cesantías
        double cesantias = (diasTotalesTrabajados / 360.0) * sueldoMensual;

        // Calcular intereses sobre las cesantías
        double interesesCesantias = cesantias * 0.12 * (diasTotalesTrabajados / 365.0);

        // Calcular primas de servicios
        double primasServicios = (diasTotalesTrabajados / 360.0) * sueldoMensual / 2;

        // Calcular vacaciones
        double vacaciones = (diasTotalesTrabajados / 720.0) * sueldoMensual;

        // Calcular auxilio de transporte
        double auxilioTransporteMensual = 140606; // Auxilio de transporte para 2024 (puede variar cada año)
        double totalAuxilioTransporte = tieneAuxilioTransporte ? (diasTotalesTrabajados / 30.0) * auxilioTransporteMensual : 0;

        // Calcular aportes a la seguridad social
        double pensionMensual = 0.04 * sueldoMensual; // 4% del salario mensual
        double saludMensual = 0.04 * sueldoMensual; // 4% del salario mensual
        double totalPension = pensionMensual * (diasTotalesTrabajados / 30.0);
        double totalSalud = saludMensual * (diasTotalesTrabajados / 30.0);

        // Total Liquidación
        double totalLiquidacion = salarioPendiente + cesantias + interesesCesantias + primasServicios + vacaciones + totalAuxilioTransporte - totalPension - totalSalud;

        return String.format("Total Liquidación: $%.2f", totalLiquidacion);
    }
}
