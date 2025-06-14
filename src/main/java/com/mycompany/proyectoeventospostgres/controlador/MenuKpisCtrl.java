package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuKpisCtrl implements ActionListener {

    private final MenuKpisView menuKpisView;
    private KpiMargenView kpiMargenView;
    private KPIRentabilidadView kpiRentabilidadView;

    public MenuKpisCtrl(MenuKpisView menuKpisView){
        this.menuKpisView = menuKpisView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuKpisView.getJbMargenGanancia())){
            System.out.println("ingresando agente comercial");
            menuKpisView.setVisible(false);

            if (kpiMargenView == null){
                kpiMargenView = new KpiMargenView();
            }
            kpiMargenView.setVisible(true);
        }

        if (e.getSource().equals(menuKpisView.getjButton1())){
            menuKpisView.setVisible(false);

            if (kpiRentabilidadView == null){
                kpiRentabilidadView = new KPIRentabilidadView();
            }
            kpiRentabilidadView.setVisible(true);
        }


    }
}

