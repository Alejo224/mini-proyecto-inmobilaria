package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.modelo.DAO.ArriendoDAO;
import com.mycompany.proyectoeventospostgres.vista.ArriendoView;
import com.mycompany.proyectoeventospostgres.vista.InmuebleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;

public class ArrendarCtrl implements ActionListener, ItemListener {
    private ArriendoView arriendoView;
    private final ArriendoDAO arriendoDAO = new ArriendoDAO();
    private InmuebleView inmuebleView;

    public ArrendarCtrl(ArriendoView arriendoView){
        this.arriendoView = arriendoView;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(arriendoView.getBtnGuardar())){
            System.out.println("Boton guardar");

            try{

                int cedulaCliente = Integer.parseInt( arriendoView.getComboBoxCliente().getSelectedItem().toString());
                int cedulaAgente = Integer.parseInt( arriendoView.getComboBoxAgente().getSelectedItem().toString());
                int codigoInmueble = Integer.parseInt( arriendoView.getComboBoxInmueble().getSelectedItem().toString());
                BigDecimal montoMensual = new BigDecimal (arriendoView.getTxtMontoMensual().getText());
                Date fechaInicio = arriendoView.getJdateInicio().getDate();
                Date fechaFin =  arriendoView.getJdateFin().getDate();

                // validar fecha
                if (fechaInicio == null){
                    JOptionPane.showMessageDialog(arriendoView, "Seleccione una fecha de inicio valida",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArriendoModel arriendoModel = new ArriendoModel(cedulaCliente, codigoInmueble,
                        fechaInicio, cedulaAgente, fechaFin, montoMensual);

                //validar si la fecha seleccionada se encuentra disponible
                if (!arriendoDAO.isDisponible(codigoInmueble, new java.sql.Date(fechaInicio.getTime()), new java.sql.Date(fechaFin.getTime()))) {
                    JOptionPane.showMessageDialog(arriendoView, "Ya se encuentra arrendado. Fechas posibles \n"
                                    + arriendoModel.getFechaInicioSQL() + " y " + arriendoModel.getFechaFinSQL(),
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                arriendoDAO.crearArriendo(arriendoModel);
                arriendoView.limpiarFormulario();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(arriendoView, "Error al guardar en base de datos: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(arriendoView, "Datos numéricos inválidos: " + nf.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                nf.printStackTrace();
            } catch (IllegalArgumentException ia) {
                JOptionPane.showMessageDialog(arriendoView, "Datos inválidos: " + ia.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ia.printStackTrace();
            }

        }

        //volver a gestion de inmueble
        if (e.getSource().equals(arriendoView.getBtnVolver())){
            System.out.println("Boton volver");

            if (inmuebleView == null) inmuebleView = new InmuebleView();
            inmuebleView.setVisible(true);
            arriendoView.setVisible(false);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(arriendoView.getComboBoxAgente())){
            String seleccionado = (String) arriendoView.getComboBoxAgente().getSelectedItem();
            System.out.println(seleccionado);
        }
        if (e.getSource().equals(arriendoView.getComboBoxCliente())){
            String seleccionado = (String) arriendoView.getComboBoxCliente().getSelectedItem();
            System.out.println(seleccionado);
        }
        if (e.getSource().equals(arriendoView.getComboBoxInmueble())){
            String seleccionado = (String) arriendoView.getComboBoxInmueble().getSelectedItem();
        }
    }
}
