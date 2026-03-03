package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/index"; // Здесь ссылаемся на страницу login.html
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) { // Создаем "пустого" юзера
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/new"; // Здесь ссылаемся на страницу new.html
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "roleId", required = false) List<Long> roleId) {
        userService.create(user, roleId);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit"; // Здесь ссылаемся на страницу edit.html
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "roleId", required = false) List<Long> roleId) {
        userService.update(user, roleId);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}