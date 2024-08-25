// Clase para representar la empresa
data class Empresa(
    val nombre: String,
    val nit: String,
    val direccion: String,
    val clientes: MutableList<Cliente> = mutableListOf(),
    val empleados: MutableList<Empleado> = mutableListOf()
) {
    fun obtenerNominaTotal(): Double {
        return empleados.sumOf { it.salario }
    }

    fun obtenerNominaPorDepartamento(departamento: String): Double {
        return empleados.filter { it.departamento == departamento }.sumOf { it.salario }
    }

    fun obtenerPorcentajeDeClientesPorSexo(): Map<String, Double> {
        val totalClientes = clientes.size.toDouble()
        return clientes.groupBy { it.sexo }
            .mapValues { (_, clientes) -> clientes.size / totalClientes * 100 }
    }

    fun obtenerCantidadDeEmpleadosPorCargo(): Map<String, Int> {
        return empleados.groupBy { it.cargo.nombre }
            .mapValues { (_, empleados) -> empleados.size }
    }

    fun obtenerEmpleadoConMayorAntiguedad(): Pair<Empleado, String> {
        val empleadoConMayorAntiguedad = empleados.maxByOrNull { it.anoDeIngreso }
        val departamento = empleadoConMayorAntiguedad?.departamento
        return Pair(empleadoConMayorAntiguedad!!, departamento!!)
    }
}

// Clase para representar un cargo
data class Cargo(
    val nombre: String,
    val nivelJerarquico: Int
)

// Clase para representar un empleado
data class Empleado(
    var nombreCompleto: String,
    var identificacion: String,
    var sexo: String,
    var correo: String,
    var salario: Double,
    var departamento: String,
    var anoDeIngreso: Int,
    val subordinados: MutableList<Empleado> = mutableListOf(),
    var cargo: Cargo
)

// Clase para representar un cliente
data class Cliente(
    var nombreCompleto: String,
    var identificacion: String,
    var sexo: String,
    var correo: String,
    var direccion: String,
    var numeroTelefonico: String
)

// Funciones CRUD para empleados y clientes
fun crearEmpleado(
    empresa: Empresa,
    nombreCompleto: String,
    identificacion: String,
    sexo: String,
    correo: String,
    salario: Double,
    departamento: String,
    anoDeIngreso: Int,
    cargo: Cargo
): Empleado {
    val empleado = Empleado(
        nombreCompleto, identificacion, sexo, correo, salario, departamento, anoDeIngreso, cargo = cargo
    )
    empresa.empleados.add(empleado)
    return empleado
}

fun crearCliente(
    empresa: Empresa,
    nombreCompleto: String,
    identificacion: String,
    sexo: String,
    correo: String,
    direccion: String,
    numeroTelefonico: String
): Cliente {
    val cliente = Cliente(nombreCompleto, identificacion, sexo, correo, direccion, numeroTelefonico)
    empresa.clientes.add(cliente)
    return cliente
}

fun actualizarEmpleado(empleado: Empleado, nombreCompleto: String, identificacion: String, sexo: String, correo: String, salario: Double, departamento: String, anoDeIngreso: Int, cargo: Cargo) {
    empleado.nombreCompleto = nombreCompleto
    empleado.identificacion = identificacion
    empleado.sexo = sexo
    empleado.correo = correo
    empleado.salario = salario
    empleado.departamento = departamento
    empleado.anoDeIngreso = anoDeIngreso
    empleado.cargo = cargo
}

fun actualizarCliente(cliente: Cliente, nombreCompleto: String, identificacion: String, sexo: String, correo: String, direccion: String, numeroTelefonico: String) {
    cliente.nombreCompleto = nombreCompleto
    cliente.identificacion = identificacion
    cliente.sexo = sexo
    cliente.correo = correo
    cliente.direccion = direccion
    cliente.numeroTelefonico = numeroTelefonico
}

fun eliminarEmpleado(empresa: Empresa, empleado: Empleado) {
    empresa.empleados.remove(empleado)
    for (subordinado in empleado.subordinados) {
        eliminarEmpleado(empresa, subordinado)
    }
}

fun eliminarCliente(empresa: Empresa, cliente: Cliente) {
    empresa.clientes.remove(cliente)
}

fun main() {
    val empresa = Empresa("MiEmpresa", "123456789", "Calle Principal 123")

    // Crear empleados
    val gerente = crearEmpleado(
        empresa, "Juan Pérez", "12345678", "M", "juan.perez@miempresa.com", 100000.0, "Gerencia", 2010, Cargo("Gerente", 1)
    )
    val vendedor = crearEmpleado(
        empresa, "Ana Gómez", "87654321", "F", "ana.gomez@miempresa.com", 50000.0, "Ventas", 2015, Cargo("Representante de Ventas", 2)
    )
    val empleadoRH = crearEmpleado(
        empresa, "Roberto Jiménez", "13579086", "M", "roberto.jimenez@miempresa.com", 60000.0, "Recursos Humanos", 2012, Cargo("Especialista en RRHH", 2)
    )

    // Crear clientes
    val cliente1 = crearCliente(
        empresa, "Carla Fernández", "24681357", "F", "carla.fernandez@ejemplo.com", "Calle Roble 456", "555-1234"
    )
    val cliente2 = crearCliente(
        empresa, "Miguel López", "97531864", "M", "miguel.lopez@ejemplo.com", "Avenida Olmo 789", "555-5678"
    )

    // Obtener la nómina total y por departamento
    println("Total nómina: ${empresa.obtenerNominaTotal()}")
    println("Nómina por departamento:")
    println("Gerencia: ${empresa.obtenerNominaPorDepartamento("Gerencia")}")
    println("Ventas: ${empresa.obtenerNominaPorDepartamento("Ventas")}")
    println("Recursos Humanos: ${empresa.obtenerNominaPorDepartamento("Recursos Humanos")}")

    // Obtener el porcentaje de clientes por sexo
    println("Porcentaje de clientes por sexo: ${empresa.obtenerPorcentajeDeClientesPorSexo()}")

    // Obtener la cantidad de empleados por cargo
    println("Cantidad de empleados por cargo: ${empresa.obtenerCantidadDeEmpleadosPorCargo()}")

    // Obtener el empleado con más tiempo en la empresa
    val (empleadoConMayorAntiguedad, departamento) = empresa.obtenerEmpleadoConMayorAntiguedad()
    println("Empleado con mayor antigüedad en la empresa: ${empleadoConMayorAntiguedad.nombreCompleto} (Departamento: $departamento)")
}
