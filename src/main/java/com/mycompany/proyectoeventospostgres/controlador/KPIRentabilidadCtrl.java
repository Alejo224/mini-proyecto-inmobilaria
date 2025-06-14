package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.KPIRentabilidadModel;
import com.mycompany.proyectoeventospostgres.vista.KPIRentabilidadView;
import com.mycompany.proyectoeventospostgres.vista.MenuKpisView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KPIRentabilidadCtrl implements ActionListener {

    private final KPIRentabilidadView kpiRentabilidadView;
    private MenuKpisView menuKpisView;

    KPIRentabilidadModel kpiRentabilidadModel = new KPIRentabilidadModel();

    public KPIRentabilidadCtrl(KPIRentabilidadView kpiRentabilidadView){
        this.kpiRentabilidadView = kpiRentabilidadView;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //inmobilaria
        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI1())){
            System.out.println("kpi inmobilaria");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(0);
            kpiRentabilidadModel.kpiInmobilaria(kpiRentabilidadView.getGananciaTotal(), kpiRentabilidadView.getJlCantidadArriendos(),
                    kpiRentabilidadView.getJlPromedioComision(), kpiRentabilidadView.getJlPrimerArriendo(), kpiRentabilidadView.getJlUltimoArriendo());
        }

        // accionador del kpi de agente
        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI2())){
            System.out.println("kpi gente");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(1);
            kpiRentabilidadModel.comisionAgentes(kpiRentabilidadView.getjTablePKI2());
        }

        //assionador del kpi de los inmuebles mas arrendados
        if (e.getSource().equals(kpiRentabilidadView.getBtnKPI3())){
            System.out.println("kpi 3");
            kpiRentabilidadView.getjTabbedPane1().setSelectedIndex(2);
            kpiRentabilidadModel.inmueblesMasRentados(kpiRentabilidadView.getjTablePKI3());

        }

        if (e.getSource().equals(kpiRentabilidadView.getBtnSalir())){
            System.out.println("salir");

            kpiRentabilidadView.setVisible(false);

            if (menuKpisView == null){
                menuKpisView = new MenuKpisView();
            }
            menuKpisView.setVisible(true);

        }

    }
}
