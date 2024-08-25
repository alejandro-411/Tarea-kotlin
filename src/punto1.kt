fun dividirPorRestasSucesivas(dividendo: Int, divisor: Int): Int {
    // Caso base: si el dividendo es menor que el divisor, la división ha terminado
    if (dividendo < divisor) {
        return 0
    }
    // Llamada recursiva restando el divisor del dividendo y sumando 1 al resultado
    return 1 + dividirPorRestasSucesivas(dividendo - divisor, divisor)
}

fun main() {
    val resultado = dividirPorRestasSucesivas(12, 3)
    println("Resultado: $resultado") // Debería imprimir: Resultado: 4
}