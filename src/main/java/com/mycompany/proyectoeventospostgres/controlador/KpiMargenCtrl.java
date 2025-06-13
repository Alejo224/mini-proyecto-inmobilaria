package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.KpiMargenModel;
import com.mycompany.proyectoeventospostgres.vista.KpiMargenView;
import com.mycompany.proyectoeventospostgres.vista.MenuKpisView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KpiMargenCtrl implements ActionListener{
    private final KpiMargenView kpiMargenView;
    private MenuKpisView menuKpisView;
    private MenuView menuView;
    private KpiMargenModel kpiMargenModel = new KpiMargenModel();
    
    public KpiMargenCtrl(KpiMargenView kpiMargenView) {
        this.kpiMargenView = kpiMargenView;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(kpiMargenView.getJbSalir())){
            if (menuKpisView == null){
                menuKpisView = new MenuKpisView();
            }
            menuKpisView.setVisible(true);
            kpiMargenView.dispose();
            System.out.println("salir funciona");
        }
    }
}
