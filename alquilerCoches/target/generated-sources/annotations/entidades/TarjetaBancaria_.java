package entidades;

import entidades.Cliente;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-05-15T12:57:26", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(TarjetaBancaria.class)
public class TarjetaBancaria_ { 

    public static volatile SingularAttribute<TarjetaBancaria, Date> fechaCaducidad;
    public static volatile SingularAttribute<TarjetaBancaria, Cliente> cliente;
    public static volatile SingularAttribute<TarjetaBancaria, String> numero;
    public static volatile SingularAttribute<TarjetaBancaria, Integer> csv;
    public static volatile SingularAttribute<TarjetaBancaria, Integer> id;

}