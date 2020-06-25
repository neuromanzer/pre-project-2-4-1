package application.controller;

import application.model.Role;
import application.model.User;
import application.service.RoleService;
import application.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AdminController {

    private static final List<Role> ROLES = Arrays.asList(new Role("ADMIN"), new Role("USER"));

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/init")
    public ModelAndView init() {
        List<Role> roles = roleService.getAll();
        if (roles == null || roles.isEmpty()) {
            ROLES.forEach(roleService::add);
        }
        User user = userService.getUserByName("ADMIN");
        if (user == null) {
            //Role role = roleService.getByName("ADMIN");
            user = new User("ADMIN", "ADMIN", Collections.singletonList(new Role("ADMIN")));
            userService.addUser(user);
        }
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @ModelAttribute("roles")
    public List<Role> roles() {
        return roleService.getAll();
    }

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        List<User> users = userService.getAllUsers();
        ModelAndView mav = new ModelAndView("admin/listOfUsers");
        mav.addObject("users", users);
        return mav;
    }

    @RequestMapping(value = "admin/add", method = RequestMethod.GET)   //ADD FORM
    public String addUserForm(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        return "admin/addUser";
    }

    @RequestMapping(value = "admin/add", method = RequestMethod.POST)  //ADD ACTION
    public String addUserAction(User user) {
        try {
            userService.addUser(user);
            return "redirect:/admin";
        } catch (ConstraintViolationException cve) {
            return "/admin/addUserExists";
        }
    }

    @RequestMapping(value = "admin/edit/{id}", method = RequestMethod.GET) //EDIT FORM
    public ModelAndView editUserForm(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);
        ModelAndView mav = new ModelAndView("admin/editUser");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "admin/edit/save", method = RequestMethod.POST)    //EDIT ACTION
    public String editUserAction(User user) {
        try {
            userService.editUser(user);
            return "redirect:/admin";
        } catch (DataIntegrityViolationException cve) {
            return "/admin/editUserExists";
        }
    }

    @RequestMapping(value = "admin/delete/{id}", method = RequestMethod.GET) //DELETE FORM
    public ModelAndView deleteUserForm(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);
        ModelAndView mav = new ModelAndView("admin/deleteUser");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "admin/delete/save", method = RequestMethod.POST)  //DELETE ACTION
    public String deleteUserAction(User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

    @RequestMapping("/user")
    public ModelAndView showUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(currentUser.getId());
        ModelAndView mav = new ModelAndView("/user/userInfo");
        mav.addObject("user", user);
        return mav;
    }

}
