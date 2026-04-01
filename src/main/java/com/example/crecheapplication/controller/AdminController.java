package com.example.crecheapplication.controller;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.service.AdminService;
import com.example.crecheapplication.service.JwtService;
import com.example.crecheapplication.service.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")

public class AdminController {

    private final AdminService adminService;
    private final ParentService parentService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService,
                           ParentService parentService,
                           JwtService jwtService) {
        this.adminService = adminService;
        this.parentService = parentService;
        this.jwtService = jwtService;
    }

    private void checkAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("token manquant");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        Parent admin = parentService.afficherParentParEmail(email);

        if (!"ROLE_ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Accès refusé pas admin");
        }
    }


    @GetMapping("/parents")
    public List<Parent> getAllParents(@RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getAllParents();
    }
    @GetMapping("/activites")
    public List<Activitebebe> getAllActivites(@RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getAllActivites();
    }

    @GetMapping("/bebes")
    public List<Bebe> getAllBebes(@RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getAllBebes();
    }

    //  parent d’un bébé
    @GetMapping("/bebe/{id}/parent")
    public Parent getParentOfBebe(@PathVariable Long id,
                                  @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getParentOfBebe(id);
    }



    // 🔹 supprimer parent
    @DeleteMapping("/parent/{id}")
    public String deleteParent(@PathVariable Long id,
                               @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        adminService.deleteParent(id);
        return "Parent supprimé";
    }
    @PostMapping("/bebe/{parentId}")
        public Bebe ajouterBebe(
            @PathVariable Long parentId,
            @RequestBody Bebe bebeDetails,
            @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);

        return adminService.ajouterBebe(
                parentId,
                bebeDetails.getNom(),
                bebeDetails.getPrenom(),
                bebeDetails.getDateNais()
        );
    }
    @PostMapping("/parent")
    public Parent createParent(@RequestBody Parent parent, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.createParent(
                parent.getNom(),
                parent.getPrenom(),
                parent.getEmail(),
                parent.getTelephone(),
                parent.getPassword());
    }
    @PutMapping("/parents/{id}")
    public Parent updateParent(@PathVariable Long id,@RequestBody Parent parent,@RequestHeader("Authorization") String authHeader) {

        checkAdmin(authHeader);
        return adminService.updateParent(id,parent.getNom(),parent.getPrenom(),parent.getEmail(),parent.getTelephone(),parent.getPassword(),parent.getRole()
        );
    }
    @PutMapping("/bebe/{id}")
    public Bebe updateBebe(@PathVariable Long id, @RequestBody Bebe bebe, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.updateBebe(id, bebe);
    }

    @DeleteMapping("/bebe/{id}")
    public String deleteBebe(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        adminService.deleteBebe(id);
        return "Bébé supprimé avec succès";
    }
    @GetMapping("/bebe/{id}/activites/aujourdhui")
    public List<Activitebebe> getActivitesAujourdhui(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        checkAdmin(authHeader);

        return adminService.ActivitesAujourdhuiAdmin(id);
    }
    @PostMapping("/bebe/{id}/activites")
    public Activitebebe ajouterActiviteAuBebe(
            @PathVariable Long id,
            @RequestBody Activitebebe activiteBebe,
            @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.ajouterActivite(id, activiteBebe);
    }
    @PutMapping("/activites/{id}")
    public Activitebebe modifierActiviteAuBebe(
            @PathVariable Long id,
            @RequestBody Activitebebe activiteDetails,
            @RequestHeader("Authorization") String authHeader) {

        checkAdmin(authHeader);

        return adminService.modifierActivite(id, activiteDetails);
    }
    @DeleteMapping("/activites/{id}")
    public String supprimerActiviteAuBebe(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        checkAdmin(authHeader);

        adminService.supprimerActivite(id);
        return "Activité supprimée avec succès";
    }

    @GetMapping("/parent/{id}")
    public Parent getParentById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getParentById(id);
    }
    @GetMapping("/parent/{id}/bebes")
    public List<Bebe> getBebesByParent(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getBebesByParentId(id);
    }
    @GetMapping("/modif/bebe/{id}")
    public Bebe getBebeById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        return adminService.getBebeById(id);
    }
    @GetMapping("/activites/{id}")
    public Activitebebe getActiviteById(@PathVariable Long id) {
        return adminService.getActiviteById(id);
    }
    @GetMapping("/profil")
    public Parent getProfilAdmin(@RequestHeader("Authorization") String authHeader) {
        checkAdmin(authHeader);
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        return adminService.getProfilAdmin(email);
    }
}
