package com.example.application.views.calculadorapordias;

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

@PageTitle("Calculadora Por Días")
@Route(value = "my-view2", layout = MainLayout.class)
public class CalculadoraPorDiasView extends VerticalLayout {

    public CalculadoraPorDiasView() {
        setWidth("100%");
        setHeightFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        DatePicker fechaInicio = new DatePicker("Fecha de Inicio");
        DatePicker fechaFin = new DatePicker("Fecha de Fin");
        NumberField sueldoDiario = new NumberField("Sueldo Diario");
        sueldoDiario.setPrefixComponent(new Span("$"));
        sueldoDiario.setStep(0.01);
        sueldoDiario.setMin(0);

        ComboBox<Integer> diasSemana = new ComboBox<>("Días por Semana");
        diasSemana.setItems(1, 2, 3, 4, 5, 6);

        ComboBox<String> auxilioTransporte = new ComboBox<>("Auxilio de Transporte");
        auxilioTransporte.setItems("SI", "NO");

        Button calcularButton = new Button("Calcular Liquidación");
        TextField resultado = new TextField("Resultado");
        resultado.setReadOnly(true);
        resultado.setWidthFull();
        resultado.getStyle().set("font-size", "24px");

        calcularButton.addClickListener(event -> {
            if (fechaInicio.isEmpty() || fechaFin.isEmpty() || sueldoDiario.isEmpty() || diasSemana.isEmpty() || auxilioTransporte.isEmpty()) {
                Notification.show("Por favor, complete todos los campos.");
                return;
            }

            LocalDate inicio = fechaInicio.getValue();
            LocalDate fin = fechaFin.getValue();
            double sueldo = sueldoDiario.getValue();
            int dias = diasSemana.getValue();
            boolean tieneAuxilioTransporte = auxilioTransporte.getValue().equalsIgnoreCase("SI");

            String resultadoLiquidacion = calcularLiquidacion(inicio, fin, sueldo, dias, tieneAuxilioTransporte);
            resultado.setValue(resultadoLiquidacion);
        });

        add(fechaInicio, fechaFin, sueldoDiario, diasSemana, auxilioTransporte, calcularButton, resultado);
    }

    private String calcularLiquidacion(LocalDate fechaInicio, LocalDate fechaFin, double sueldoDiario, int diasSemana, boolean tieneAuxilioTransporte) {
        // Calcular los días totales trabajados
        long diasTotalesTrabajados = ChronoUnit.DAYS.between(fechaInicio, fechaFin);

        // Calcular el salario mensual
        double salarioMensual = sueldoDiario * diasSemana * 4.33; // Promedio de semanas por mes

        // Calcular salarios pendientes
        double salarioPendiente = (diasTotalesTrabajados % 30) * sueldoDiario;

        // Calcular cesantías
        double cesantias = (diasTotalesTrabajados / 360.0) * salarioMensual;

        // Calcular intereses sobre las cesantías
        double interesesCesantias = cesantias * 0.12 * (diasTotalesTrabajados / 365.0);

        // Calcular primas de servicios
        double primasServicios = (diasTotalesTrabajados / 360.0) * salarioMensual / 2;

        // Calcular vacaciones
        double vacaciones = (diasTotalesTrabajados / 720.0) * salarioMensual;

        // Calcular auxilio de transporte
        double auxilioTransporteMensual = 140606; // Auxilio de transporte para 2024 (puede variar cada año)
        double totalAuxilioTransporte = tieneAuxilioTransporte ? (diasTotalesTrabajados / 30.0) * auxilioTransporteMensual : 0;

        // Calcular aportes a la seguridad social
        double pensionMensual = 0.04 * salarioMensual; // 4% del salario mensual
        double saludMensual = 0.04 * salarioMensual; // 4% del salario mensual
        double totalPension = pensionMensual * (diasTotalesTrabajados / 30.0);
        double totalSalud = saludMensual * (diasTotalesTrabajados / 30.0);

        // Total Liquidación
        double totalLiquidacion = salarioPendiente + cesantias + interesesCesantias + primasServicios + vacaciones + totalAuxilioTransporte - totalPension - totalSalud;

        return String.format("Total Liquidación: $%.2f", totalLiquidacion);
    }
}
