package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.vista.AgenteView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuCtrl implements ActionListener {

    private final MenuView menuView;
    private AgenteView agenteView = new AgenteView();

    public MenuCtrl(MenuView menuView){
        this.menuView = menuView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuView.getJbSalir())){
            System.exit(0);
        }

        if (e.getSource().equals(menuView.getJbGestionAgenteComercial())){
            System.out.println("ingresando agente comercial");
            menuView.setVisible(false);

            if (agenteView == null){
                agenteView = new AgenteView();
            }
            agenteView.setVisible(true);
        }

        if (e.getSource().equals(menuView.getJbGestionCliente())){
            System.out.println("ingresando cliente");
        }

        if (e.getSource().equals(menuView.getJbGestionPropietario())){
            System.out.println("Ingresando empleado");
        }

    }
}
