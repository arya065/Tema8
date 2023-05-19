package entidades;

import entidades.Cliente;
import entidades.Vehiculo;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-05-15T12:57:26", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Alquiler.class)
public class Alquiler_ { 

    public static volatile SingularAttribute<Alquiler, Cliente> cliente;
    public static volatile SingularAttribute<Alquiler, Date> fechaInicio;
    public static volatile SingularAttribute<Alquiler, Integer> id;
    public static volatile SingularAttribute<Alquiler, Vehiculo> vehiculo;
    public static volatile SingularAttribute<Alquiler, Integer> numeroDias;

}