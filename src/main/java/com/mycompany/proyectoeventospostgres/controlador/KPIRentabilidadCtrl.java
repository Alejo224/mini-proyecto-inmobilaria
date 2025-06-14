package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.KPIRentabilidadModel;
import com.mycompany.proyectoeventospostgres.vista.KPIRentabilidadView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KPIRentabilidadCtrl implements ActionListener {

    private final KPIRentabilidadView kpiRentabilidadView;

    KPIRentabilidadModel kpiRentabilidadModel = new KPIRentabilidadModel();

    public KPIRentabilidadCtrl(KPIRentabilidadView kpiRentabilidadView){
        this.kpiRentabilidadView = kpiRentabilidadView;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI1())){
            System.out.println("kpi gente");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(0);
            kpiRentabilidadModel.comisionAgentes(kpiRentabilidadView.getjTable2());
        }

        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI2())){
            System.out.println("kpi 2");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(1);

        }

        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI3())){
            System.out.println("kpi 3");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(2);

        }

    }
}
