package com.example.bookshop.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookshop.Entity.Address;
import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.BookOrder;
import com.example.bookshop.Entity.Order;
import com.example.bookshop.Entity.Payment;
import com.example.bookshop.Entity.Status;
import com.example.bookshop.Entity.StatusOrder;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.OrderReponse;
import com.example.bookshop.EntityDto.Request.BookOrderRequest;
import com.example.bookshop.EntityDto.Request.OrderRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.BookOrderMapper;
import com.example.bookshop.Mapper.OrderMapper;
import com.example.bookshop.Repository.AddressRepository;
import com.example.bookshop.Repository.BookOrderRepository;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.OrderRepository;
import com.example.bookshop.Repository.PaymentRepository;
import com.example.bookshop.Repository.StatusOrderRepository;
import com.example.bookshop.Repository.StatusRepository;

@Service
public class OrderService {

        @Autowired
        OrderMapper orderMapper;
        @Autowired
        BookOrderMapper bookOrderMapper;
        @Autowired
        UserService userService;
        @Autowired
        OrderRepository orderRepository;
        @Autowired
        BookOrderRepository bookOrderRepository;
        @Autowired
        AddressRepository addressRepository;
        @Autowired
        PaymentRepository paymentRepository;
        @Autowired
        BookRepository bookRepository;
        @Autowired
        StatusOrderRepository statusOrderRepository;
        @Autowired
        StatusRepository statusRepository;

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public List<OrderReponse> getAll() {
                List<OrderReponse> orderReponses = new ArrayList<>();
                List<Order> orders = orderRepository.findAll();
                for (Order order : orders) {
                        orderReponses.add(orderMapper.toOrderReponse(order));
                }
                return orderReponses;
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse get(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                return orderMapper.toOrderReponse(order);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public List<OrderReponse> getByStatus(Long status_id) {
                List<Order> orders = orderRepository.findByCurrentStatus(status_id);
                List<OrderReponse> orderReponses = new ArrayList<>();
                for (Order order : orders) {
                        orderReponses.add(orderMapper.toOrderReponse(order));
                }
                return orderReponses;
        }

        public List<OrderReponse> getAllMyOrder() {
                List<OrderReponse> orderReponses = new ArrayList<>();
                List<Order> orders = orderRepository.findByUser(userService.getUserformToKen());
                for (Order order : orders) {
                        orderReponses.add(orderMapper.toOrderReponse(order));
                }
                return orderReponses;
        }

        public List<OrderReponse> getMyOrderByStatus(Long status_id) {
                List<OrderReponse> orderReponses = new ArrayList<>();
                List<Order> orders = orderRepository.findByUserAndCurrentStatus(userService.getUserformToKen(), status_id);
                for (Order order : orders) {
                        orderReponses.add(orderMapper.toOrderReponse(order));
                }
                return orderReponses;
        }

        public OrderReponse getMyOrder(Long id) {
                User user = userService.getUserformToKen();
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                if (user.getId() != order.getUser().getId()) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return orderMapper.toOrderReponse(order);
        }

        @Transactional
        public OrderReponse create(OrderRequest orderRequest) {
                User user = userService.getUserformToKen();
                Order order = orderMapper.toOrder(orderRequest);
                Address address = addressRepository.findById(orderRequest.getAddress_id())
                                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
                Payment payment = paymentRepository.findById(orderRequest.getPayment_id())
                                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
                Status status = statusRepository.findById((long) 1)
                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));

                order.setUser(user);
                order.setAddress(address);
                order.setPayment(payment);
                order.setCurrentStatus(status.getId());
                orderRepository.save(order);
                StatusOrder statusOrder = StatusOrder.builder()
                                .order(order)
                                .status(status)
                                .updateTime(LocalDateTime.now())
                                .description("Created")
                                .build();
                statusOrderRepository.save(statusOrder);
                List<BookOrderRequest> bookOrderRequests = orderRequest.getBookOrderRequests();
                for (BookOrderRequest bookOrderRequest : bookOrderRequests) {
                        BookOrder bookOrder = bookOrderMapper.toBookOrder(bookOrderRequest);
                        Book book = bookRepository.findById(bookOrderRequest.getBook_id())
                                        .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
                        if (book.getQuantity() < bookOrder.getQuantity()) {
                                throw new AppException(ErrorCode.BOOK_ENOUGH_QUANTITY);
                        }
                        book.setQuantity(book.getQuantity() - bookOrderRequest.getQuantity());
                        bookOrder.setBook(book);
                        bookOrder.setOrder(order);
                        bookOrderRepository.save(bookOrder);
                }
                return orderMapper.toOrderReponse(order);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse confirm(Long id) {
                return updateOrder(id, "CONFIRM", "");
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse prepare(Long id) {
                return updateOrder(id, "PREPARE", "");
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse handover(Long id) {
                return updateOrder(id, "HANDOVER", "");
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse deliver(Long id) {
                return updateOrder(id, "DELIVERY", "");
        }

        public OrderReponse complele(Long id, String description) {
                User user = userService.getUserformToKen();
                Order orderfind = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                if (user.getId() != orderfind.getUser().getId()) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return updateOrder(id, "COMPLETE", description);
        }

        public OrderReponse cancel(Long id, String description) {
                User user = userService.getUserformToKen();
                Order orderfind = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                if (user.getId() != orderfind.getUser().getId()) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return updateOrder(id, "CANCEL", description);
        }

        public OrderReponse reject(Long id, String description) {
                User user = userService.getUserformToKen();
                Order orderfind = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                if (user.getId() != orderfind.getUser().getId()) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return updateOrder(id, "REJECT", description);
        }

        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public OrderReponse returnBook(Long id) {
                return updateOrder(id, "RETURN", "");
        }

        private OrderReponse updateOrder(Long id, String target, String description) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                StatusOrder statusOrderUpate = new StatusOrder();
                Status status = new Status();
                List<BookOrder> bookOrders = order.getBook_orders();
                switch (target) {
                        case "CONFIRM":
                                if (order.getCurrentStatus() != 1) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(2L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description("Đơn hàng đã được xác nhận")
                                                .build();
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "PREPARE":
                                if (order.getCurrentStatus() != 2) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(3L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description("Đơn hàng đã được chuẩn bị xong")
                                                .build();
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "HANDOVER":
                                if (order.getCurrentStatus() != 3) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(4L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description("Đơn hàng đã được bàn giao và đang trong quá trình trung chuyển")
                                                .build();
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "DELIVERY":
                                if (order.getCurrentStatus() != 4) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(5L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description("Đơn hàng đã đến kho đích và đang trên đường giao đến bạn")
                                                .build();
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "COMPLETE":
                                if (order.getCurrentStatus() != 5) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(6L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description(description)
                                                .build();
                                for (BookOrder bookOrder : bookOrders) {
                                        Book book = bookOrder.getBook();
                                        book.setPurchaseNumber(book.getPurchaseNumber() + bookOrder.getQuantity());
                                        bookRepository.save(book);
                                }
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "CANCEL":
                                if (order.getCurrentStatus() != 1
                                                && order.getCurrentStatus() != 2) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(7L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description(description)
                                                .build();
                                for (BookOrder bookOrder : bookOrders) {
                                        Book book = bookOrder.getBook();
                                        book.setQuantity(book.getQuantity() + bookOrder.getQuantity());
                                        bookRepository.save(book);
                                }
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "REJECT":
                                if (order.getCurrentStatus() != 5) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(8L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description(description)
                                                .build();
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        case "RETURN":
                                if (order.getCurrentStatus() != 8) {
                                        throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER);
                                }
                                status = statusRepository.findById(9L)
                                                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
                                statusOrderUpate = StatusOrder.builder()
                                                .order(order)
                                                .status(status)
                                                .updateTime(LocalDateTime.now())
                                                .description("Đơn hàng đã được hoàn về kho")
                                                .build();
                                for (BookOrder bookOrder : bookOrders) {
                                        Book book = bookOrder.getBook();
                                        book.setQuantity(book.getQuantity() + bookOrder.getQuantity());
                                        bookRepository.save(book);
                                }
                                statusOrderRepository.save(statusOrderUpate);
                                order.setCurrentStatus(status.getId());
                                orderRepository.save(order);
                                break;
                        default:
                                throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return orderMapper.toOrderReponse(order);
        }
}
