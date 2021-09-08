package ru.ilya.springboot.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ilya.springboot.springboot.entities.Role;
import ru.ilya.springboot.springboot.entities.User;
import ru.ilya.springboot.springboot.service.RoleService;
import ru.ilya.springboot.springboot.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("listUsers", userService.allUser());
        return "admin/listUsers";
    }

    @GetMapping("/{id}")
    public String findUserId(@PathVariable("id")Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/showUserId";
    }

    @GetMapping("/create")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        List<Role> listRoles = roleService.allRole();
        model.addAttribute("listRoles",listRoles);
        return "admin/createUser";
    }

    @PostMapping("/create")
    public String addUser(@RequestParam("idRoles")List<Long> idRoles,
                          User user) {
        Set<Role> roleList = new HashSet<>();
        for(Long id:idRoles) {
            roleList.add(roleService.findRoleById(id));
        }
        user.setRoles(roleList);
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model,
                       @PathVariable("id")long id) {
        model.addAttribute("user",userService.getUserById(id));
        return "admin/editUser";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user")User user,@RequestParam("roles") List<String> roles) {
        Set<Role> listRoles = new HashSet<>();
        for (String nameRole : roles) {
            listRoles.add(roleService.findRoleByName(nameRole));
        }
        user.setRoles(listRoles);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id")long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }
}
