package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuCtrl implements ActionListener {

    private final MenuView menuView;
    private AgenteView agenteView = new AgenteView();
    private ClienteView clienteView;
    private PropietarioView propietarioView;
    private InmuebleView inmuebleView;
    private LoguinView loguinView;
    private MenuKpisView kpisView;

    public MenuCtrl(MenuView menuView){
        this.menuView = menuView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuView.getJbSalir())){
            JOptionPane.showMessageDialog(null, "Has cerrado sesion");
            if (loguinView == null) loguinView = new LoguinView();

            loguinView.setVisible(true);
            menuView.dispose();
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
            menuView.setVisible(false);

            if (clienteView == null){
                clienteView = new ClienteView();
            }
            clienteView.setVisible(true);
        }

        if (e.getSource().equals(menuView.getJbGestionPropietario())){
            System.out.println("Ingresando empleado");

            menuView.setVisible(false);

            if (propietarioView == null){
                propietarioView = new PropietarioView();
            }
            propietarioView.setVisible(true);
        }

        if (e.getSource().equals(menuView.getJbGestionInmueble())){
            System.out.println("Ingresando gestion de inmuebles");

            menuView.setVisible(false);

            if (inmuebleView == null){
                inmuebleView = new InmuebleView();
            }
            inmuebleView.setVisible(true);
        }
        
        if (e.getSource().equals(menuView.getJbKpis())){
            System.out.println("Ingresando gestion de inmuebles");

            menuView.setVisible(false);

            if (kpisView == null){
                kpisView = new MenuKpisView();
            }
            kpisView.setVisible(true);
        }
        
        



    }
}

