/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.vista.LoguinView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Estudiante
 */
public class LoguinCtrl implements ActionListener {

    private LoguinView loguinView ;
    private MenuView menuView;
    private String password = "univalle";
    private String email = "jhonWick@gmail.com";


    public LoguinCtrl(LoguinView loguinView){
        this.loguinView = loguinView;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(loguinView.getJbIniciar())){
            System.out.println("boton iniciar");

            String email = loguinView.getTxtEmail().getText();
            String password = new String(loguinView.getjPasswordField1().getPassword()); // No olvides manejar contraseñas.

            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            System.out.println("Email get: " + getEmail());
            System.out.println("Password get: " + getPassword());

            if (getEmail().equals(email) && getPassword().equals(password)){
                if (menuView == null) menuView = new MenuView();
                JOptionPane.showMessageDialog(null, "Bienvenido");
                menuView.setVisible(true);
                loguinView.setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(null, "EMAIL o CONTRASEÑA incorrecta");
            }
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
