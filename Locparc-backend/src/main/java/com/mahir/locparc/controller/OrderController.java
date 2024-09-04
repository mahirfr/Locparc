package com.mahir.locparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mahir.locparc.dao.*;
import com.mahir.locparc.model.Order;
import com.mahir.locparc.model.User;
import com.mahir.locparc.security.config.JwtService;
import com.mahir.locparc.service.EmailService;
import com.mahir.locparc.service.OrderService;
import com.mahir.locparc.service.UserService;
import com.mahir.locparc.view.OrderView;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    private final OrderDao orderDao;
    private final EmailService emailService;
    private final OrderService orderService;
    private final JwtService jwtService;
    private final UserService userService;

    public OrderController(OrderDao orderDao,
                           EmailService emailService,
                           OrderService orderService,
                           JwtService jwtService,
                           UserService userService) {
        this.orderDao = orderDao;
        this.emailService = emailService;
        this.orderService = orderService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

//    TODO: add admin before paths where necessary
    @GetMapping("/")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderDao.findAll();
        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/pending")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> getPendingOrders() {

        List<Order> orders = orderDao.getPendingOrders();

        if (!orders.isEmpty())
            return new ResponseEntity<>(orders, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @JsonView({OrderView.class})
    public ResponseEntity<Order> findById(@PathVariable long id) {

        Optional<Order> optionalOrder = orderDao.findById(id);
        return optionalOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-my-pending")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> getMyPending(@RequestHeader("Authorization") String jwt) {
        String token = jwt.substring(7);
        Optional<User> optionalUser = userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Order> myPendingOrders = orderDao.getPendingOrdersByEmail(optionalUser.get().getEmail());

        if (myPendingOrders.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(myPendingOrders, HttpStatus.OK);
    }

    @GetMapping("/get-my-history")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> getMyHistory(@RequestHeader("Authorization") String jwt) {
        String token = jwt.substring(7);
        Optional<User> optionalUser =
                userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Order> myOrderHistory =
                orderDao.getApprovedOrderHistoryByEmail(optionalUser.get().getEmail());

        if (myOrderHistory.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(myOrderHistory, HttpStatus.OK);
    }

    @GetMapping("/admin/get-user-history")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> getUserOrderHistory(
            @RequestHeader("Authorization") String jwt,
            @Param("name") String name) {

        try {
            String token = jwt.substring(7);
            Optional<User> optionalAdmin = userService.getUserByEmail(jwtService.extractEmail(token));

            if (optionalAdmin.isEmpty() || !userService.isAdmin(optionalAdmin.get()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            List<Order> userOrders = orderDao.getOrderHistoryByFirstOrLastName(name.toLowerCase());

            if (userOrders.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(userOrders, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/persist")
    @JsonView({OrderView.class})
//    ONLY USED FOR GETTING THE ORDER ID BY PERSISTING A SIMPLE ORDER
    ResponseEntity<Order> persistOrder(@RequestBody Order order) {
        if (order.getId() != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        order.setAddress(null);
        order.setRequest(null);
        order.setOrderedItems(null);
        order.setUser(null);
        return new ResponseEntity<>(orderDao.save(order), HttpStatus.CREATED);
    }

    @PostMapping("/save-order")
    @JsonView({OrderView.class})
    public ResponseEntity<Order> sendOrder(@RequestBody Order order) {

        if (!orderService.isOrderValid(order)) return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);

        Order newOrder = orderService.saveOrder(order);
        if (newOrder == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        emailService.sendEmailToAdmins(
                "New order request",
                newOrder.getUser().getFirstName()              + " " +
                newOrder.getUser().getLastName()               + "\n" +
                "With user id : " + newOrder.getUser().getId() + " ");

        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }


    @JsonView({OrderView.class})
    @PostMapping("/save-orders")
    // A method for future use (not needed now)
    public ResponseEntity<List<Order>> sendOrders(@RequestBody List<Order> orders) {
        if (orders.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Order> savedOrders = new ArrayList<>();
        Order newOrder;

        for (Order order: orders) {
            newOrder = orderService.saveOrder(order);
            if (newOrder == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            savedOrders.add(newOrder);
        }

        return new ResponseEntity<>(savedOrders, HttpStatus.OK);
    }

    // TODO refactor below code
    @PutMapping("/approve-order")
    @JsonView({OrderView.class}) // Lack of this jsonview caused a stack overflow error
    public ResponseEntity<Order> approveOrder(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Order order
    ) {
        if (!orderService.isOrderValid(order)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String token = jwt.substring(7);
        Optional<User> optionalUser = userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (!userService.isAdminOrLender(optionalUser.get()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Order approvedOrder = orderService.approveOrder(order, optionalUser.get(), "");
        if (approvedOrder != null)
            return new ResponseEntity<>(approvedOrder, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/approve-orders")
    @JsonView({OrderView.class})
    public ResponseEntity<List<Order>> approveOrders(
            @RequestHeader("Authorization") String jwt,
            @RequestBody List<Order> orders
    ) {
        if (orders.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String token = jwt.substring(7);
        Optional<User> optionalUser = userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (!userService.isAdminOrLender(optionalUser.get()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Order> approvedOrders = new ArrayList<>();

        for (Order order: orders) {
            if (!orderService.isOrderValid(order))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            FIXME before continuing
            approvedOrders.add(orderService.approveOrder(order, optionalUser.get(), ""));
        }

        return new ResponseEntity<>(approvedOrders, HttpStatus.OK);
    }

    @PutMapping("/refuse-order")
    public ResponseEntity<?> refuseOrder(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Order order,
            @RequestParam(required = false) String motive
    ) {
        String token = jwt.substring(7);
        Optional<User> optionalUser = userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (!userService.isAdminOrLender(optionalUser.get()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!orderService.isOrderValid(order)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Order refusedOrder = orderService.refuseOrder(order, optionalUser.get(), motive);
        if (refusedOrder != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/refuse-orders")
    public ResponseEntity<?> refuseOrders(
            @RequestHeader("Authorization") String jwt,
            @RequestBody List<Order> orders,
            @RequestParam(required = false) String motive
    ) {
        if (orders.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String token = jwt.substring(7);
        Optional<User> optionalUser = userService.getUserByEmail(jwtService.extractEmail(token));

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (!userService.isAdminOrLender(optionalUser.get()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        for (Order order: orders) {
            if (!orderService.isOrderValid(order))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            orderService.approveOrder(order, optionalUser.get(), motive);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/admin/approve-return/{returnDate}")
    @JsonView({OrderView.class})
    public ResponseEntity<Order> approveReturn(
            @RequestBody Order order,
            @PathVariable("returnDate") String returnDate) {

        if (!orderService.isOrderValid(order))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!orderService.isOrderApproved(order))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Order terminatedOrder = orderService.approveReturn(order, returnDate);
        if (terminatedOrder != null)
            return new ResponseEntity<>(terminatedOrder, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/modify")
    @JsonView({OrderView.class})
    public ResponseEntity<Order> modifyOrder(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Order order) {

        if (!orderService.isOrderValid(order))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (orderService.isOrderApproved(order))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String token = jwt.substring(7);
        String email = jwtService.extractEmail(token);

        if (orderService.orderComesFromOwner(order, email) != null) {
            Order modifiedOrder = orderService.saveOrder(order);

            if (modifiedOrder != null)
                return new ResponseEntity<>(modifiedOrder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-non-approved/{id}")
    public ResponseEntity<?> deleteNonApprovedOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) {
        String token = jwt.substring(7);
        String email = jwtService.extractEmail(token);
        if (id != null) {
            Optional<Order> optionalOrder = orderDao.findById(id);
            if (optionalOrder.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            if (orderService.isOrderApproved(optionalOrder.get()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            if (orderService.orderComesFromOwner(optionalOrder.get(), email) != null) {
                orderDao.delete(optionalOrder.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-approved/{id}")
    public ResponseEntity<?> deleteApprovedOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) {
        String token = jwt.substring(7);
        String email = jwtService.extractEmail(token);

        Optional<Order> optionalOrder = orderDao.findById(id);
        if (optionalOrder.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!orderService.isOrderApproved(optionalOrder.get()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (orderService.orderComesFromOwner(optionalOrder.get(), email) != null) {
            orderDao.delete(optionalOrder.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            Optional<Order> optionalOrder = orderDao.findById(id);
            if (optionalOrder.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            orderDao.delete(optionalOrder.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
