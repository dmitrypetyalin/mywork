package com.petsoft.employeemanagement.mvc.controller;

import com.petsoft.employeemanagement.mvc.service.RoleService;
import com.petsoft.employeemanagement.mvc.service.UserService;
import com.petsoft.employeemanagement.mvc.form.UserForm;
import com.petsoft.employeemanagement.domain.Role;
import com.petsoft.employeemanagement.domain.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 13.11.2019 23:48
 *
 * @author PetSoft
 */
@Controller
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/users.action", method = RequestMethod.GET)
    public ModelAndView listAllUsers(Map<String, Object> model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        ModelAndView result = new ModelAndView("users", "command", model);
        result.addObject("users", users)
                .addObject("roles", roles);
        return result;
    }

    @ModelAttribute("userForm")
    public UserForm getUserForm() {
        return new UserForm();
    }

    @RequestMapping(value = "/addUser.action", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult result) {
        if (result.hasErrors()) {
            return listAllUsers(result.getModel());
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setAge(userForm.getAge());
        user.setSalary(userForm.getSalary());
        user.setRole(roleService.findById(userForm.getRoleId()));
        userService.save(user);
        return new ModelAndView("redirect:/users.action");
    }

    @RequestMapping(value = "/deleteUser.action", method = RequestMethod.GET)
    public String deleteUser(@RequestParam(name="id") Long id) {
        if (id == null) {
            log.error("id = null");
            return "redirect:/users.action";
        }
        userService.deleteById(id);
        return "redirect:/users.action";
    }
}
