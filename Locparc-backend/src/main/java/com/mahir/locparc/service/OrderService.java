package com.mahir.locparc.service;

import com.mahir.locparc.dao.*;
import com.mahir.locparc.model.*;
import com.mahir.locparc.model.address.Address;
import com.mahir.locparc.security.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Value("${time.zone}")
    private String timezone;
    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final RequestDao requestDao;
    private final OrderDao orderDao;
    private final ItemDao itemDao;
    private final OrderItemDao orderItemDao;
    private final AddressDao addressDao;
    private final AddressService addressService;
    private final UserDao userDao;


    public OrderService(RequestDao requestDao,
                        OrderDao orderDao,
                        ItemDao itemDao,
                        OrderItemDao orderItemDao,
                        AddressDao addressDao,
                        AddressService addressService,
                        UserDao userDao) {
        this.requestDao = requestDao;
        this.orderDao = orderDao;
        this.itemDao = itemDao;
        this.orderItemDao = orderItemDao;
        this.addressDao = addressDao;
        this.addressService = addressService;
        this.userDao = userDao;
    }

    public boolean isOrderValid(Order order) {
        if (
                order == null                          ||
                order.getOrderedItems().isEmpty()      ||
                order.getUser() == null                ||
                order.getStartDate() == null           ||
                order.getEndDate() == null             ||
                !isOrderStartDateBeforeOrEqualsEndDate(
                                        order.getStartDate(),
                                        order.getEndDate())
        ) return false;

        return true;
    }
    // TODO: add payment eventually
    @Transactional
    public Order saveOrder(Order order) {
        // Get order in order (lol!) to update it
        Optional<Order> optionalOrder = orderDao.findById(order.getId());
        if (optionalOrder.isEmpty()) return null;

        Order existingOrder = optionalOrder.get();

        // User
        Optional<User> user = userDao.findByEmail(order.getUser().getEmail());
        if (user.isEmpty()) return null;
        existingOrder.setUser(user.get());
        // Event
        if (order.getEvent() != null && order.getEvent().equals(""))
            existingOrder.setEvent(null);
        else existingOrder.setEvent(order.getEvent());
        // Dates
        existingOrder.setStartDate(order.getStartDate());
        existingOrder.setEndDate(order.getEndDate());


        try {
            // TODO: modify this block in the future so to avoid data duplication
            if (order.getAddress() != null) {
                if (addressService.isAddressValid(order.getAddress())) {
                    Address newAddress =
                            addressService.constructAddress(order.getAddress());
                    if (newAddress == null) return null;
                    existingOrder.setAddress(addressDao.save(newAddress));
                } else {
                    existingOrder.setAddress(null);
                }
            }
            // save request to avoid transient instance value error
            existingOrder.setRequest(requestDao.save(new Request(false)));
            Optional<Item> item;

            for (OrderItem orderedItem: order.getOrderedItems()) {
                item = itemDao.findAvailableItemById(orderedItem.getItemId());

                if (item.isPresent()) {
                    existingOrder.getOrderedItems().add(orderItemDao.save(orderedItem));
                } else {
                    throw new ResourceNotFoundException(
                            "Le matériel: n'existe pas dans la base de données, ou n'est plus disponible"
                    );
                }
            }
            existingOrder.setOrderedItems(existingOrder.getOrderedItems());
            return orderDao.save(existingOrder);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Order approveOrder(Order order, User admin, String motive) {
        try {
            Optional<Order> optionalOrder = orderDao.findById(order.getId());
            if (optionalOrder.isEmpty()) return null;
            Order existingOrder = optionalOrder.get();

            Request request = existingOrder.getRequest();
            if (motive != null && !motive.equals(""))
                request.setMotive(motive);
            request.setApproved(true);
            request.setAdmin(admin);
            request.setResponseDate(LocalDate.now(ZoneId.of(timezone)));
            existingOrder.setRequest(requestDao.save(request));

            List<OrderItem> orderedItems = existingOrder.getOrderedItems();
            Optional<Item> item;

            for (OrderItem orderedItem: orderedItems) {
                item = itemDao.findAvailableItemById(orderedItem.getItemId());

                if (item.isPresent())
                    orderedItem.setApproved(true);
                else
                    throw new ResourceNotFoundException(
                            "Le matériel: n'existe pas dans la base de données, ou n'est plus disponible"
                    );
            }
            existingOrder.setOrderedItems(orderedItems);

            return orderDao.save(existingOrder);
        } catch (RuntimeException e) {
            // TODO: change this to logger in the future
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Order refuseOrder(Order order, User admin, String motive) {
        Optional<Request> request = requestDao.findById(order.getRequest().getId());
        if (request.isEmpty()) return null;
        Request existingRequest = request.get();
        if (motive != null && !motive.equals(""))
            existingRequest.setMotive(motive);
        existingRequest.setApproved(false);
        existingRequest.setAdmin(admin);
        existingRequest.setResponseDate(LocalDate.now(ZoneId.of(timezone)));
        try {
            order.setRequest(requestDao.save(existingRequest));
            return order;
        } catch (RuntimeException e) {
            // TODO: change this to logger in the future
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public Order approveReturn(Order order, String returnDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date date = sdf.parse(returnDate);
            List<OrderItem> orderedItems = order.getOrderedItems();
            Optional<OrderItem> orderItem;
            for (OrderItem orderedItem: orderedItems) {
                if (orderedItem.getReturnDate() != null || !orderedItem.isApproved())
                    return null;
                orderItem = orderItemDao.findByOrderIdAndAndItemId(orderedItem.getOrderId(), orderedItem.getItemId());
                orderItem.get().setReturnDate(date);
                orderItemDao.save(orderItem.get());
            }
            return order;
        } catch (ResourceNotFoundException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isOrderStartDateBeforeOrEqualsEndDate(
            Date startDate,
            Date endDate) {
        return startDate.before(endDate) || startDate.equals(endDate);
    }


    public Order orderComesFromOwner(Order order, String email) {

        Optional <User> user = userDao.findByEmail(email);
        if (user.isEmpty()) return null;

        if (order.getUser().getEmail().equals(user.get().getEmail()))
            return order;

        return null;
    }


    public boolean isOrderApproved(Order order) {
        return order.getRequest().isApproved() &&
                order.getRequest().getResponseDate() != null;
    }

}
