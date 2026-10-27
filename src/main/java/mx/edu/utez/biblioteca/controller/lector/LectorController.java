package mx.edu.utez.biblioteca.controller.lector;

import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.service.lector.LectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/lector")
public class LectorController {
    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @PatchMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity.status(200)
                .body(lectorService.rehabilitarLector(id));
    }
}
