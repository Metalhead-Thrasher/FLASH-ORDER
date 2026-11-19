package org.example;
import java.util.Scanner;
public class Main {

    static Scanner sc = new Scanner(System.in);

    static String[] usuarios = new String[50];
    static String[] claves = new String[50];
    static String[] roles = new String[50];
    static int numUsuarios = 0;

    static String[] prodNombre = new String[50];
    static double[] prodPrecio = new double[50];
    static int[] prodExistencia = new int[50];
    static int[] prodMinimo = new int[50];
    static boolean[] prodActivo = new boolean[50];
    static int numProductos = 0;

    static String[] estadoTexto = {"PENDIENTE", "EN PREPARACION", "LISTO", "PAGADO"};
    static String[] pedCliente = new String[200];
    static int[] pedEstado = new int[200];
    static double[] pedTotal = new double[200];
    static int[][] pedProducto = new int[200][20];
    static int[][] pedCantidad = new int[200][20];
    static int[] pedLineas = new int[200];
    static int numPedidos = 0;

    public static void main(String[] args)
    {
        cargarDatosIniciales();

        boolean programa = true;
        while (programa)
        {
            System.out.println();
            System.out.println("=== FLASH ORDER ===");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Crear cuenta");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");
            int op = sc.nextInt();

            if (op == 1)
            {
                iniciarSesion();
            } else if (op == 2)
            {
                crearCuenta();
            } else if (op == 0)
            {
                programa = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void cargarDatosIniciales()
    {
        usuarios[0] = "admin";  claves[0] = "admin1234";  roles[0] = "ADMINISTRADOR";
        usuarios[1] = "cajero"; claves[1] = "cajero1234"; roles[1] = "CAJERO";
        usuarios[2] = "cocina"; claves[2] = "cocina1234"; roles[2] = "COCINA";
        numUsuarios = 3;

        String[] baseNombres = {"Hamburguesa", "Torta", "Hotdog", "Quesadilla", "Refresco", "Jugo", "Agua", "Papas"};
        double[] basePrecios = {45, 40, 30, 35, 20, 18, 15, 25};
        int b = 0;
        while (b < 8)
        {
            prodNombre[b] = baseNombres[b];
            prodPrecio[b] = basePrecios[b];
            prodExistencia[b] = 20;
            prodMinimo[b] = 5;
            prodActivo[b] = true;
            b++;
        }
        numProductos = 8;
    }

    private static void iniciarSesion()
    {
        System.out.print("Nombre / Nickname: ");
        String nombre = sc.next();
        System.out.print("Contrasena: ");
        String clave = sc.next();

        int actual = -1;
        int i = 0;
        while (i < numUsuarios)
        {
            if (usuarios[i].equals(nombre) && claves[i].equals(clave))
            {
                actual = i;
            }
            i++;
        }

        if (actual == -1)
        {
            System.out.println("[!] Usuario o contrasena incorrectos");
        } else
        {
            menuSesion(actual);
        }
    }

    private static void crearCuenta()
    {
        System.out.print("Ingresa tu Nombre / Nickname: ");
        String nombre = sc.next();
        System.out.print("Ingresa una contrasena (4 o mas caracteres): ");
        String clave = sc.next();

        boolean existe = false;
        int i = 0;
        while (i < numUsuarios)
        {
            if (usuarios[i].equals(nombre))
            {
                existe = true;
            }
            i++;
        }

        if (clave.length() < 4)
        {
            System.out.println("[!] Datos incorrectos (la contrasena debe tener 4 o mas caracteres)");
        } else if (existe)
        {
            System.out.println("[!] Ya existe el usuario");
        } else if (numUsuarios == 50)
        {
            System.out.println("[!] Ya no caben mas usuarios");
        } else
        {
            usuarios[numUsuarios] = nombre;
            claves[numUsuarios] = clave;
            roles[numUsuarios] = "CLIENTE";
            numUsuarios++;
            System.out.println("[ok] Registro exitoso. Ya puedes iniciar sesion.");
        }
    }

    private static void menuSesion(int actual)
    {
        String rol = roles[actual];
        boolean sesion = true;
        while (sesion)
        {
            System.out.println();
            System.out.println("=== MENU PRINCIPAL: " + usuarios[actual] + " (" + rol + ") ===");
            mostrarOpcionesDelRol(rol);
            System.out.println("0. Cerrar sesion");
            System.out.print("Elige una opcion: ");
            int opMenu = sc.nextInt();

            int modulo = 0;
            if (rol.equals("CLIENTE") && opMenu == 1) modulo = 1;
            if (rol.equals("CAJERO") && opMenu == 1) modulo = 1;
            if (rol.equals("CAJERO") && opMenu == 2) modulo = 3;
            if (rol.equals("COCINA") && opMenu == 1) modulo = 2;
            if (rol.equals("ADMINISTRADOR") && opMenu == 1) modulo = 4;
            if (rol.equals("ADMINISTRADOR") && opMenu == 2) modulo = 5;
            if (rol.equals("ADMINISTRADOR") && opMenu == 3) modulo = 3;

            if (opMenu == 0)
            {
                sesion = false;
            } else if (modulo == 0)
            {
                System.out.println("[!] Opcion no valida");
            } else if (modulo == 1)
            {
                tomaDePedidos(usuarios[actual]);
            } else if (modulo == 2)
            {
                cocina();
            } else if (modulo == 3)
            {
                cajaYVentas();
            } else if (modulo == 4)
            {
                gestionDeMenu();
            } else if (modulo == 5)
            {
                inventario();
            }
        }
    }

    private static void mostrarOpcionesDelRol(String rol)
    {
        if (rol.equals("CLIENTE"))
        {
            System.out.println("1. Hacer un pedido");
        } else if (rol.equals("CAJERO"))
        {
            System.out.println("1. Toma de pedidos");
            System.out.println("2. Caja y ventas");
        } else if (rol.equals("COCINA"))
        {
            System.out.println("1. Cocina (cola de pedidos)");
        } else
        {
            System.out.println("1. Gestion de menu");
            System.out.println("2. Inventario");
            System.out.println("3. Caja y ventas");
        }
    }

    private static void tomaDePedidos(String cliente)
    {
        int[] carProd = new int[20];
        int[] carCant = new int[20];
        int carN = 0;
        boolean pidiendo = true;
        while (pidiendo)
        {
            System.out.println();
            System.out.println("--- MENU ---");
            int i = 0;
            while (i < numProductos)
            {
                if (prodActivo[i])
                {
                    String disp = "AGOTADO";
                    if (prodExistencia[i] > 0) disp = "" + prodExistencia[i];
                    System.out.println((i + 1) + ". " + prodNombre[i] + "  $" + prodPrecio[i] + "  disponibles: " + disp);
                }
                i++;
            }
            System.out.println("--- TU PEDIDO ---");
            if (carN == 0) System.out.println("(vacio)");
            double total = 0;
            i = 0;
            while (i < carN)
            {
                double sub = prodPrecio[carProd[i]] * carCant[i];
                System.out.println("Linea " + (i + 1) + ": " + carCant[i] + " x " + prodNombre[carProd[i]] + "  $" + sub);
                total = total + sub;
                i++;
            }
            System.out.println("TOTAL: $" + total);
            System.out.println("1. Agregar producto  2. Quitar linea  3. Confirmar pedido  0. Cancelar");
            System.out.print("Elige una opcion: ");
            int o = sc.nextInt();

            if (o == 1)
            {
                System.out.print("Numero del producto: ");
                int id = sc.nextInt() - 1;
                System.out.print("Cantidad: ");
                int cant = sc.nextInt();
                if (id < 0 || id >= numProductos || !prodActivo[id])
                {
                    System.out.println("[!] No existe ese producto");
                } else if (cant <= 0)
                {
                    System.out.println("[!] La cantidad debe ser mayor a 0");
                } else
                {
                    int enCarrito = 0;
                    int pos = -1;
                    int k = 0;
                    while (k < carN)
                    {
                        if (carProd[k] == id)
                        {
                            enCarrito = carCant[k];
                            pos = k;
                        }
                        k++;
                    }
                    if (prodExistencia[id] < enCarrito + cant)
                    {
                        System.out.println("[!] No hay suficiente existencia");
                    } else if (pos != -1)
                    {
                        carCant[pos] = carCant[pos] + cant;
                        System.out.println("[ok] Agregado");
                    } else if (carN == 20)
                    {
                        System.out.println("[!] El pedido esta lleno");
                    } else
                    {
                        carProd[carN] = id;
                        carCant[carN] = cant;
                        carN++;
                        System.out.println("[ok] Agregado");
                    }
                }
            } else if (o == 2)
            {
                System.out.print("Numero de linea a quitar: ");
                int linea = sc.nextInt() - 1;
                if (linea < 0 || linea >= carN)
                {
                    System.out.println("[!] Esa linea no existe");
                } else
                {
                    int k = linea;
                    while (k < carN - 1)
                    {
                        carProd[k] = carProd[k + 1];
                        carCant[k] = carCant[k + 1];
                        k++;
                    }
                    carN--;
                }
            } else if (o == 3)
            {
                if (carN == 0)
                {
                    System.out.println("[!] El pedido esta vacio");
                } else if (numPedidos == 200)
                {
                    System.out.println("[!] Ya no caben mas pedidos");
                } else
                {
                    boolean hayStock = true;
                    int k = 0;
                    while (k < carN)
                    {
                        if (prodExistencia[carProd[k]] < carCant[k]) hayStock = false;
                        k++;
                    }
                    if (!hayStock)
                    {
                        System.out.println("[!] Ya no hay existencia de algun producto");
                    } else
                    {
                        pedCliente[numPedidos] = cliente;
                        pedEstado[numPedidos] = 0;
                        pedTotal[numPedidos] = total;
                        pedLineas[numPedidos] = carN;
                        k = 0;
                        while (k < carN)
                        {
                            pedProducto[numPedidos][k] = carProd[k];
                            pedCantidad[numPedidos][k] = carCant[k];
                            prodExistencia[carProd[k]] = prodExistencia[carProd[k]] - carCant[k];
                            k++;
                        }
                        numPedidos++;
                        System.out.println("[ok] Pedido confirmado. FOLIO: " + numPedidos + "  TOTAL: $" + total);
                        System.out.println("     Pasa a caja a pagar cuando este listo.");
                        pidiendo = false;
                    }
                }
            } else if (o == 0)
            {
                pidiendo = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
    }

    private static void cocina()
    {
        boolean enCocina = true;
        while (enCocina)
        {
            System.out.println();
            System.out.println("--- COLA DE PEDIDOS ---");
            int hay = 0;
            int i = 0;
            while (i < numPedidos)
            {
                if (pedEstado[i] == 0 || pedEstado[i] == 1)
                {
                    String lista = "";
                    int k = 0;
                    while (k < pedLineas[i])
                    {
                        lista = lista + pedCantidad[i][k] + " x " + prodNombre[pedProducto[i][k]] + "  ";
                        k++;
                    }
                    System.out.println("Folio " + (i + 1) + " (" + pedCliente[i] + ") [" + estadoTexto[pedEstado[i]] + "]: " + lista);
                    hay++;
                }
                i++;
            }
            if (hay == 0) System.out.println("No hay pedidos en espera.");
            System.out.println("1. Avanzar estado de un pedido  0. Volver");
            System.out.print("Elige una opcion: ");
            int o = sc.nextInt();
            if (o == 1)
            {
                System.out.print("Folio: ");
                int f = sc.nextInt() - 1;
                if (f < 0 || f >= numPedidos)
                {

                    System.out.println("[!] No existe ese folio");
                } else if (pedEstado[f] >= 2)
                {
                    System.out.println("[!] Ese pedido ya no se puede avanzar");
                } else
                {
                    pedEstado[f] = pedEstado[f] + 1;
                    System.out.println("[ok] Ahora esta: " + estadoTexto[pedEstado[f]]);
                }
            } else if (o == 0)
            {
                enCocina = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
    }

    private static void cajaYVentas()
    {
        boolean enCaja = true;
        while (enCaja)
        {
            System.out.println();
            System.out.println("--- PEDIDOS LISTOS POR COBRAR ---");
            int hay = 0;
            int i = 0;
            while (i < numPedidos)
            {
                if (pedEstado[i] == 2)
                {
                    System.out.println("Folio " + (i + 1) + " (" + pedCliente[i] + ")  $" + pedTotal[i]);
                    hay++;
                }
                i++;
            }
            if (hay == 0) System.out.println("(ninguno)");
            System.out.println("1. Cobrar un pedido  2. Ver ventas y corte  0. Volver");
            System.out.print("Elige una opcion: ");
            int o = sc.nextInt();
            if (o == 1)
            {
                System.out.print("Folio a cobrar: ");
                int f = sc.nextInt() - 1;
                System.out.print("Dinero recibido: $");
                double recibido = sc.nextDouble();
                if (f < 0 || f >= numPedidos)
                {
                    System.out.println("[!] No existe ese folio");
                } else if (pedEstado[f] != 2)
                {
                    System.out.println("[!] El pedido aun no esta listo o ya se pago");
                } else if (recibido < pedTotal[f])
                {
                    System.out.println("[!] El pago es menor al total ($" + pedTotal[f] + ")");
                } else
                {
                    pedEstado[f] = 3;
                    System.out.println("[ok] Cobro registrado. Cambio: $" + (recibido - pedTotal[f]));
                }
            } else if (o == 2)
            {
                System.out.println("--- VENTAS ---");
                int ventas = 0;
                double suma = 0;
                i = 0;
                while (i < numPedidos)
                {
                    if (pedEstado[i] == 3)
                    {
                        System.out.println("Folio " + (i + 1) + " (" + pedCliente[i] + ")  $" + pedTotal[i]);
                        ventas++;
                        suma = suma + pedTotal[i];
                    }
                    i++;
                }
                System.out.println("CORTE: " + ventas + " ventas, total $" + suma);
            } else if (o == 0)
            {
                enCaja = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
    }

    private static void gestionDeMenu()
    {
        boolean enMenu = true;
        while (enMenu)
        {
            System.out.println();
            System.out.println("--- PRODUCTOS ---");
            int i = 0;
            while (i < numProductos)
            {
                if (prodActivo[i])
                {
                    System.out.println((i + 1) + ". " + prodNombre[i] + "  $" + prodPrecio[i]
                            + "  existencia: " + prodExistencia[i] + "  minimo: " + prodMinimo[i]);
                }
                i++;
            }
            System.out.println("1. Agregar producto  2. Cambiar precio  3. Eliminar producto  0. Volver");
            System.out.print("Elige una opcion: ");
            int o = sc.nextInt();
            if (o == 1) {
                System.out.print("Nombre (una palabra): ");
                String nombre = sc.next();
                System.out.print("Precio: ");
                double precio = sc.nextDouble();
                System.out.print("Existencia inicial: ");
                int existencia = sc.nextInt();
                System.out.print("Existencia minima: ");
                int minimo = sc.nextInt();
                boolean repetido = false;
                i = 0;
                while (i < numProductos)
                {
                    if (prodActivo[i] && prodNombre[i].equalsIgnoreCase(nombre)) repetido = true;
                    i++;
                }
                if (repetido)
                {
                    System.out.println("[!] Ya existe ese producto");
                } else if (precio <= 0 || existencia < 0 || minimo < 0)
                {
                    System.out.println("[!] Datos incorrectos");
                } else if (numProductos == 50)
                {
                    System.out.println("[!] Ya no caben mas productos");
                } else
                {
                    prodNombre[numProductos] = nombre;
                    prodPrecio[numProductos] = precio;
                    prodExistencia[numProductos] = existencia;
                    prodMinimo[numProductos] = minimo;
                    prodActivo[numProductos] = true;
                    numProductos++;
                    System.out.println("[ok] Producto registrado");
                }
            } else if (o == 2)
            {
                System.out.print("Numero del producto: ");
                int id = sc.nextInt() - 1;
                System.out.print("Nuevo precio: ");
                double precio = sc.nextDouble();
                if (id < 0 || id >= numProductos || !prodActivo[id])
                {
                    System.out.println("[!] No existe ese producto");
                } else if (precio <= 0)
                {
                    System.out.println("[!] El precio debe ser mayor a 0");
                } else
                {
                    prodPrecio[id] = precio;
                    System.out.println("[ok] Precio actualizado");
                }
            } else if (o == 3)
            {
                System.out.print("Numero del producto a eliminar: ");
                int id = sc.nextInt() - 1;
                if (id < 0 || id >= numProductos || !prodActivo[id])
                {
                    System.out.println("[!] No existe ese producto");
                } else
                {
                    prodActivo[id] = false;
                    System.out.println("[ok] Producto eliminado");
                }
            } else if (o == 0)
            {
                enMenu = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
    }

    private static void inventario()
    {
        boolean enInv = true;
        while (enInv)
        {
            System.out.println();
            System.out.println("--- INVENTARIO ---");
            int i = 0;
            while (i < numProductos)
            {
                if (prodActivo[i])
                {
                    String estado = "OK";
                    if (prodExistencia[i] <= prodMinimo[i]) estado = "POCA EXISTENCIA";
                    System.out.println((i + 1) + ". " + prodNombre[i] + "  existencia: " + prodExistencia[i]
                            + "  minimo: " + prodMinimo[i] + "  " + estado);
                }
                i++;
            }
            System.out.println("1. Registrar entrada (+)  2. Registrar salida (-)  0. Volver");
            System.out.print("Elige una opcion: ");
            int o = sc.nextInt();
            if (o == 1 || o == 2)
            {
                System.out.print("Numero del producto: ");
                int id = sc.nextInt() - 1;
                System.out.print("Cantidad: ");
                int cant = sc.nextInt();
                if (id < 0 || id >= numProductos || !prodActivo[id])
                {
                    System.out.println("[!] No existe ese producto");
                } else if (cant <= 0)
                {
                    System.out.println("[!] La cantidad debe ser mayor a 0");
                } else if (o == 2 && cant > prodExistencia[id])
                {
                    System.out.println("[!] No puedes sacar mas de lo que hay (" + prodExistencia[id] + ")");
                } else if (o == 1)
                {
                    prodExistencia[id] = prodExistencia[id] + cant;
                    System.out.println("[ok] Entrada registrada");
                } else
                {
                    prodExistencia[id] = prodExistencia[id] - cant;
                    System.out.println("[ok] Salida registrada");
                }
            } else if (o == 0)
            {
                enInv = false;
            } else
            {
                System.out.println("[!] Opcion no valida");
            }
        }
    }
}