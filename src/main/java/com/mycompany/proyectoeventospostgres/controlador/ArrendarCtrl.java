package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.ArriendoModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.modelo.DAO.ArriendoDAO;
import com.mycompany.proyectoeventospostgres.vista.ArriendoView;
import com.mycompany.proyectoeventospostgres.vista.InmuebleView;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;

public class ArrendarCtrl implements ActionListener, ItemListener, MouseListener {
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

                if (arriendoDAO.validarArriendo(-1,codigoInmueble,fechaInicio,
                        fechaFin, montoMensual.doubleValue())){
                    ArriendoModel arriendoModel = new ArriendoModel(cedulaCliente, codigoInmueble,
                            fechaInicio, cedulaAgente, fechaFin, montoMensual);

                    arriendoDAO.crearArriendo(arriendoModel);
                    arriendoView.limpiarFormulario();
                    arriendoDAO.mostrarArriendos(codigoInmueble, arriendoView.getjTable1());
                }

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

        //Acción para finalizar el arriendo
        if (e.getSource().equals(arriendoView.getBtnFinalizarContrato())){
            System.out.println("Boton finalizar arriendo");
            int codigoArriendo = arriendoDAO.getIdArriendo();

            if ( codigoArriendo < 0){
                JOptionPane.showMessageDialog(inmuebleView, "Por favor seleccione un arriendo que desea finalizar.",
                        "Seleccione un arriendo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String motivo = JOptionPane.showInputDialog(arriendoView, "Motivo de finalizar el contrato: ",
                    "Finalizar contrato");
            System.out.println("motivo: " + motivo);
            System.out.println("codigo arriendo: "+ arriendoDAO.getIdArriendo());
            int codigoInmueble = Integer.parseInt( arriendoView.getComboBoxInmueble().getSelectedItem().toString());

            //validar si desea finalizar el arriendo
            if (motivo != null){
                System.out.println("se puede finalizar");
                arriendoDAO.finalizarArriendo(codigoArriendo, motivo);
                arriendoDAO.setIdArriendo(-1);
                arriendoView.limpiarFormulario();
                arriendoDAO.mostrarArriendos(codigoArriendo, arriendoView.getjTable1());
            }
        }

        //Boton modificar arriendo
        if (e.getSource().equals(arriendoView.getBtnActualizar())){
            System.out.println("Boton actualizar");
            try {
                JComboBox cedulaCliente =  arriendoView.getComboBoxCliente();
                JComboBox cedulaAgente = arriendoView.getComboBoxAgente();
                JComboBox codigoInmueble = arriendoView.getComboBoxInmueble();
                BigDecimal montoMensual = new BigDecimal (arriendoView.getTxtMontoMensual().getText());
                JTextField jtxtMontoMensual = arriendoView.getTxtMontoMensual();
                JDateChooser fechaInicio = arriendoView.getJdateInicio();
                JDateChooser fechaFin =  arriendoView.getJdateFin();
                int codigoArriendo = arriendoDAO.getIdArriendo();

                System.out.println("fecha inicio" + fechaInicio.getDate().getTime() + "fechaFin " + fechaFin.getDate());
                System.out.println("ID arriendo: " + codigoArriendo);

                if ( codigoArriendo < 0){
                    JOptionPane.showMessageDialog(inmuebleView, "Por favor seleccione un arriendo que desea finalizar.",
                            "Seleccione un arriendo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //metodo para actualizar el arriendo
                 if (arriendoDAO.actualizarArriendo(fechaInicio, fechaFin, jtxtMontoMensual, cedulaCliente, cedulaAgente, codigoInmueble,
                        codigoArriendo)){
                     arriendoView.limpiarFormulario();
                     codigoArriendo = -1;
                     // Recargar la tabla
                     int codigo = Integer.parseInt(codigoInmueble.getSelectedItem().toString());
                     arriendoDAO.mostrarArriendos(codigo, arriendoView.getjTable1());
                 }


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

        //boton cancelar para no seleccionar los datos posibles que deseas eliminar o actualizar
        if (e.getSource().equals(arriendoView.getBtnCancelar())){
            System.out.println("Boton cancelar");
            arriendoView.limpiarFormulario();
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
            arriendoDAO.mostrarArriendos(Integer.parseInt(seleccionado), arriendoView.getjTable1());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(arriendoView.getjTable1())) {
            System.out.println("Fila seleccionada");
            JDateChooser fechaInicio = arriendoView.getJdateInicio();
            JDateChooser fechaFin = arriendoView.getJdateFin();
            JTextField montoMensual = arriendoView.getTxtMontoMensual();
            JComboBox codigoCliente = arriendoView.getComboBoxCliente();
            JComboBox codigoAgente = arriendoView.getComboBoxAgente();
            //seleccionar los datos de la tabla
            arriendoDAO.seleccionar(arriendoView.getjTable1(), fechaInicio, fechaFin,
                    montoMensual, codigoCliente, codigoAgente);

            arriendoView.getComboBoxInmueble().setEnabled(false);
            //ACTIVAR BOTONES AL SELECCIONAR UN ARRIENDO
            arriendoView.getBtnCancelar().setVisible(true);
            arriendoView.getBtnActualizar().setEnabled(true);
            arriendoView.getBtnFinalizarContrato().setEnabled(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
