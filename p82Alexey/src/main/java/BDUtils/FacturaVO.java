/*
 * Clase para mapear los datos de la tabla Persona
 */
package BDUtils;

import java.time.LocalDate;

/**
 *
 * @author J. Carlos F. Vico <jcarlosvico@maralboran.es>
 */
public class FacturaVO {

    private int pk;
    private String codigoUnico;
    private LocalDate fechaEmision;
    private String descripcion;
    private double totalImporteFactura;

    public FacturaVO() {
    }

    public FacturaVO(int pk, String codigoUnico, LocalDate fechaEmision, String descripcion, double totalImporteFactura) {
        this.pk = pk;
        this.codigoUnico = codigoUnico;
        this.fechaEmision = fechaEmision;
        this.descripcion = descripcion;
        this.totalImporteFactura = totalImporteFactura;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotalImporteFactura() {
        return totalImporteFactura;
    }

    public void setTotalImporteFactura(double totalImporteFactura) {
        this.totalImporteFactura = totalImporteFactura;
    }

    @Override
    public String toString() {
        return "FacturaVO{" + "pk=" + pk + ", codigoUnico=" + codigoUnico + ", fechaEmision=" + fechaEmision + ", descripcion=" + descripcion + ", totalImporteFactura=" + totalImporteFactura + '}';
    }

}
