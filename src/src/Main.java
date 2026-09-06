import model.Propietario;
import repository.PropietarioRepository;
import service.PropietarioService;
import org.mindrot.BCrypt;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        PropietarioRepository repo = new PropietarioRepository();
        PropietarioService service = new PropietarioService(repo);

        System.out.println("=== PLAZOLETA DE COMIDAS - DEMO HU1/HU5 ===\n");

        // 1. CASO EXITOSO - ADMINISTRADOR crea propietario valido
        System.out.println("1. Creacion valida (ADMINISTRADOR):");
        try {
            Propietario p1 = new Propietario(
                    "Juan", "Perez", "12345678", "+573005698325",
                    LocalDate.of(1995, 5, 20), "juan.perez@mail.com", "Clave123"
            );
            Propietario creado = service.crearPropietario(p1, "ADMINISTRADOR");
            System.out.println("   OK -> " + creado);
            System.out.println("   Clave encriptada: " + creado.getClave());
            System.out.println("   BCrypt.checkpw: " + BCrypt.checkpw("Clave123", creado.getClave()));
        } catch (Exception e) {
            System.out.println("   ERROR: " + e.getMessage());
        }

        // 2. AUTORIZACION - rol no administrador
        System.out.println("\n2. Intento con rol CLIENTE (debe fallar HU5):");
        try {
            Propietario p2 = new Propietario("Ana","Gomez","87654321","+573001112233",
                    LocalDate.of(1990,1,1),"ana@mail.com","Clave123");
            service.crearPropietario(p2, "CLIENTE");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        // 3. VALIDACION - email invalido
        System.out.println("\n3. Email invalido:");
        try {
            Propietario p3 = new Propietario("Ana","Gomez","11111111","+573001112233",
                    LocalDate.of(1990,1,1),"ana-mail.com","Clave123");
            service.crearPropietario(p3, "ADMINISTRADOR");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        // 4. VALIDACION - celular >13 o sin formato
        System.out.println("\n4. Celular invalido (+ mas de 13):");
        try {
            Propietario p4 = new Propietario("Ana","Gomez","22222222","+5730056983259999",
                    LocalDate.of(1990,1,1),"ana2@mail.com","Clave123");
            service.crearPropietario(p4, "ADMINISTRADOR");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        // 5. VALIDACION - documento no numerico
        System.out.println("\n5. Documento no numerico:");
        try {
            Propietario p5 = new Propietario("Ana","Gomez","ABC123","+573001112233",
                    LocalDate.of(1990,1,1),"ana3@mail.com","Clave123");
            service.crearPropietario(p5, "ADMINISTRADOR");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        // 6. VALIDACION - menor de edad
        System.out.println("\n6. Menor de edad:");
        try {
            Propietario p6 = new Propietario("Ana","Gomez","33333333","+573001112233",
                    LocalDate.now().minusYears(17),"ana4@mail.com","Clave123");
            service.crearPropietario(p6, "ADMINISTRADOR");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        // 7. VALIDACION - duplicados
        System.out.println("\n7. Correo duplicado (juan.perez@mail.com ya existe):");
        try {
            Propietario p7 = new Propietario("Carlos","Lopez","99999999","+573009998877",
                    LocalDate.of(1992,2,2),"juan.perez@mail.com","Clave123");
            service.crearPropietario(p7, "ADMINISTRADOR");
        } catch (Exception e) {
            System.out.println("   ERROR esperado: " + e.getMessage());
        }

        System.out.println("\n=== FIN DEMO - Total en repo: " + repo.findAll().size() + " ===");
        repo.findAll().forEach(p -> System.out.println(" - " + p));
    }
}