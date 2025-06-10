package com.mycompany.proyectoeventospostgres.modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ArriendoModel {
    private int cedulaCliente, codigoInmueble, cedulaAgente, idArriendo;
    private Date fechaInicio, fechaFin;
    private BigDecimal montoMensual, comisionAgente, comisionInmobilaria;
    private Timestamp fechaRegistro;
    private  boolean activo;

   //constructor
    public ArriendoModel(){}

    public ArriendoModel(int cedulaCliente, int codigoInmueble,
                         Date fechaInicio, int cedulaAgente, Date fechaFin, BigDecimal montoMensual) {
        this.cedulaCliente = cedulaCliente;
        this.codigoInmueble = codigoInmueble;
        this.fechaInicio = fechaInicio;
        this.cedulaAgente = cedulaAgente;
        this.fechaFin = fechaFin;
        this.activo = true;
        this.montoMensual = montoMensual;
        calcularComisiones();
    }

    //calcular comisiones
    private void calcularComisiones(){
        BigDecimal porcentajeAgente = new BigDecimal("0.10");  // 10%
        BigDecimal porcentajInmobilaria = new BigDecimal("0.15"); // 15%

        this.comisionAgente = this.montoMensual.multiply(porcentajeAgente);
        this.comisionInmobilaria = this.montoMensual.multiply(porcentajInmobilaria);
    }

    @Override
    public String toString() {
        return "ArriendoModel{" +
                "cedulaCliente=" + cedulaCliente +
                ", codigoInmueble=" + codigoInmueble +
                ", cedulaAgente=" + cedulaAgente +
                ", idArriendo=" + idArriendo +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", montoMensual=" + montoMensual +
                ", comisionAgente=" + comisionAgente +
                ", comisionInmobilaria=" + comisionInmobilaria +
                ", fechaRegistro=" + fechaRegistro +
                ", activo=" + activo +
                '}';
    }

    // Método para obtener fecha inicio en formato SQL
    public java.sql.Date getFechaInicioSQL() {
        return new java.sql.Date(fechaInicio.getTime());
    }

    // Método para obtener fecha fin en formato SQL (nullable)
    public java.sql.Date getFechaFinSQL() {
        return fechaFin != null ? new java.sql.Date(fechaFin.getTime()) : null;
    }

    //Getters y Setters

    public int getIdArriendo() {
        return idArriendo;
    }

    public void setIdArriendo(int idArriendo) {
        this.idArriendo = idArriendo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getCodigoInmueble() {
        return codigoInmueble;
    }

    public void setCodigoInmueble(int codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public int getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(int cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public int getCedulaAgente() {
        return cedulaAgente;
    }

    public void setCedulaAgente(int cedulaAgente) {
        this.cedulaAgente = cedulaAgente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getMontoMensual() {
        return montoMensual;
    }

    public void setMontoMensual(BigDecimal montoMensual) {
        this.montoMensual = montoMensual;
    }

    public BigDecimal getComisionAgente() {
        return comisionAgente;
    }

    public void setComisionAgente(BigDecimal comisionAgente) {
        this.comisionAgente = comisionAgente;
    }

    public BigDecimal getComisionInmobilaria() {
        return comisionInmobilaria;
    }

    public void setComisionInmobilaria(BigDecimal comisionInmobilaria) {
        this.comisionInmobilaria = comisionInmobilaria;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
